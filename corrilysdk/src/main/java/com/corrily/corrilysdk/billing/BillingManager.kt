package com.corrily.corrilysdk.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.Purchase.PurchaseState
import com.corrily.corrilysdk.misc.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile

class BillingManager(private val context: Context) : PurchasesUpdatedListener,
  BillingClientStateListener {
  private var billingClient = BillingClient
    .newBuilder(context)
    .setListener(this)
    .enablePendingPurchases()
    .build()

  init {
    if (!billingClient.isReady) {
      Logger.info(TAG, "Starting billing client ...")
      startConnection()
    }
  }

  private fun startConnection() {
    billingClient.startConnection(this)
  }

  override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
    when (billingResult.responseCode) {
      BillingResponseCode.OK -> {
        handlePurchases(purchases ?: listOf())
      }

      else -> {
        Logger.error(TAG, billingResult.responseCode.toString())
        Logger.error(TAG, billingResult.debugMessage)
      }
    }
  }

  override fun onBillingServiceDisconnected() {
    startConnection()
  }

  override fun onBillingSetupFinished(billingResult: BillingResult) {
    when (billingResult.responseCode) {
      BillingResponseCode.OK -> {
        Logger.info(TAG, "Billing client started successful")
        CoroutineScope(Dispatchers.IO).launch {
          restorePurchase()
        }
      }

      else -> {
        Logger.error(
          TAG,
          "Can not start billing client with code: ${billingResult.responseCode} & debugMessage: ${billingResult.debugMessage}"
        )
      }
    }
  }

  suspend fun purchase(activity: Activity, productId: String) {
    if (!billingClient.isReady) {
      Logger.error(TAG, "Billing client is not ready for purchase")
      return
    }
    Logger.info(TAG, "Starting purchase")
    val products = getProduct(productId)

    products.let { result ->
      if (result.billingResult.responseCode == BillingResponseCode.OK && result.productDetailsList != null) {
        Logger.info(TAG, "Ready to purchase")
        val productDetails = result.productDetailsList!!.first()
        val offerToken = productDetails.subscriptionOfferDetails?.first()?.offerToken ?: String()
        val productDetailsParamsList = listOf(
          BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
          .setProductDetailsParamsList(productDetailsParamsList)
          .build()

        billingClient.launchBillingFlow(activity, billingFlowParams)
      }
    }
  }

  suspend fun restorePurchase() {
    if (!billingClient.isReady) {
      Logger.error(TAG, "Billing client is not ready for restore purchase")
      return
    }

    Logger.info(TAG, "Start restoring purchases")
    val subsParams = QueryPurchasesParams.newBuilder().setProductType(ProductType.SUBS).build()
    val subsResult = billingClient.queryPurchasesAsync(subsParams)

    if (subsResult.billingResult.responseCode == BillingResponseCode.OK) {
      handlePurchases(subsResult.purchasesList)
      Logger.info(TAG, "End restoring purchases")
    }
  }

  private fun handlePurchases(purchases: List<Purchase>) {
    Logger.info("Manager Purchases", purchases.toString())
    for (purchase in purchases) {
      when (purchase.purchaseState) {
        PurchaseState.PURCHASED -> {
          Logger.info("Manager Products", purchase.products.toString())
          for (product in purchase.products) {
            EntitlementsManager.addProduct(productId = product)
          }
        }

        else -> {
          Logger.info(TAG, "Purchase state ${purchase.purchaseState}")
        }
      }
    }
  }

  private suspend fun getProduct(productId: String): ProductDetailsResult {
    val result = withContext(Dispatchers.IO) {
      val queryProductsParams = QueryProductDetailsParams
        .newBuilder()
        .setProductList(
          listOf(
            QueryProductDetailsParams.Product.newBuilder()
              .setProductId(productId)
              .setProductType(ProductType.SUBS)
              .build()
          )
        ).build()

      billingClient.queryProductDetails(queryProductsParams)
    }
    return result
  }

  companion object {

    private const val TAG = "Billing Manager"


    @Volatile
    private var instance: BillingManager? = null

    fun getInstance(context: Context): BillingManager {
      return instance ?: synchronized(this) {
        instance ?: BillingManager(context).also { instance = it }
      }
    }
  }

}
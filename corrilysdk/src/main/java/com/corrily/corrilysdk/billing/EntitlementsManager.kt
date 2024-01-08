package com.corrily.corrilysdk.billing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

object EntitlementsManager {
  private val purchasedProducts = MutableLiveData<MutableSet<String>>(mutableSetOf())

  val productIds: LiveData<List<String>> = purchasedProducts.map { it.toList() }
  val hasSubscription: LiveData<Boolean> = productIds.map { it.isNotEmpty() }


  fun addProduct(productId: String) {
    val products = purchasedProducts.value ?: mutableSetOf()
    products.add(productId)
    purchasedProducts.postValue(products)
  }

  fun removeProduct(productId: String) {
    val products = purchasedProducts.value ?: mutableSetOf()
    products.remove(productId)
    purchasedProducts.postValue(products)
  }

  private const val TAG = "Entitlements Manager"
}
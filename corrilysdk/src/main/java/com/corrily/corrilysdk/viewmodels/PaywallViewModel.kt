package com.corrily.corrilysdk.viewmodels

import androidx.lifecycle.ViewModel
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.PaywallProduct
import com.corrily.corrilysdk.models.PaywallResponse

class PaywallViewModel(private val factory: DependencyProtocol): ViewModel() {
  var isLoading = true
  var isError = false
  var errorMessage: String? = null

  var paywall: PaywallResponse? = null
  var yearlyProducts: List<PaywallProduct> = listOf()
  var monthlyProduct: List<PaywallProduct> = listOf()

  fun purchase(product: PaywallProduct) {}

  fun restorePurchase() {}

  fun getPaywall(paywallId: Int? = null) {}
}
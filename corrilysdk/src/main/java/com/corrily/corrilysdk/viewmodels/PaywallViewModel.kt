package com.corrily.corrilysdk.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallProduct
import com.corrily.corrilysdk.models.PaywallResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaywallViewModel(private val factory: DependencyProtocol): ViewModel() {
  private val _state: MutableState<String> = mutableStateOf("pending")
  private val _error: MutableState<String?> = mutableStateOf(null)
  private val _data: MutableState<PaywallResponse?> = mutableStateOf(null)

  var yearlyProducts: List<PaywallProduct> = listOf()
  var monthlyProduct: List<PaywallProduct> = listOf()

  val paywall: State<PaywallResponse?> = _data
  val isLoading = _state.value == "pending"
  val isError = _state.value == "error"
  val error = _error.value

  init {
//    getPaywall()
  }

  fun purchase(product: PaywallProduct) {}

  fun restorePurchase() {}

  private fun getPaywall(paywallId: Int? = null) {
    _state.value = "pending"
    _error.value = null

    println("test")

    val dto = PaywallDto(country = factory.user.country, userId = factory.user.userId, ip = factory.user.deviceId, paywallId = paywallId)

    CoroutineScope(Dispatchers.IO).launch {
      try {
        val response = factory.api.getPaywall(dto)
        _data.value = response
//        yearlyProducts = response.products.filter { it.interval == "year" }
//        monthlyProduct = response.products.filter { it.interval == "month" }
      } catch(error: Exception) {
        // TODO: Handle fallback paywall
        _error.value = error.message
        _state.value = "error"
      } finally {
        _state.value = "idle"
      }
    }

  }
}
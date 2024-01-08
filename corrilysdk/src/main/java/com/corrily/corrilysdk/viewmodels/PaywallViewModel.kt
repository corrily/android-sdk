package com.corrily.corrilysdk.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.PaywallDto
import com.corrily.corrilysdk.models.PaywallProduct
import com.corrily.corrilysdk.models.PaywallResponse
import com.corrily.corrilysdk.models.ProductInterval
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class Status {
  Pending,
  Success,
  Error
}

class PaywallViewModel(private val factory: DependencyProtocol, private val paywallId: Int? = null) : ViewModel() {
  private val _status: MutableState<Status> = mutableStateOf(Status.Pending)
  val status: Status
    get() = _status.value
  val isLoading: Boolean
    get() = _status.value == Status.Pending
  val isError: Boolean
    get() = _status.value == Status.Error
  val isSuccess: Boolean
    get() = _status.value == Status.Success

  private val _error: MutableState<String?> = mutableStateOf(null)
  private val _data: MutableState<PaywallResponse?> = mutableStateOf(null)

  val error: String?
    get() = _error.value

  val paywall: PaywallResponse?
    get() = _data.value
  val monthlyProducts: List<PaywallProduct>
    get() = _data.value?.products?.filter { it.interval == ProductInterval.Month } ?: listOf()
  val yearlyProducts: List<PaywallProduct>
    get() = _data.value?.products?.filter { it.interval == ProductInterval.Year } ?: listOf()

  init {
    getPaywall(paywallId = paywallId)
  }

  fun purchase(product: PaywallProduct) {
    CoroutineScope(Dispatchers.IO).launch {

    }
  }

  fun restorePurchase() {}

  private fun getPaywall(paywallId: Int? = null) {
    _status.value = Status.Pending
    _error.value = null

    val dto = PaywallDto(
      country = factory.user.country,
      userId = factory.user.userId,
      ip = factory.user.deviceId,
      paywallId = paywallId
    )

    CoroutineScope(Dispatchers.IO).launch {
      try {
        val response = factory.api.getPaywall(dto)
        _data.value = response
        _status.value = Status.Success
      } catch (error: Exception) {
        if (factory.paywall.fallbackPaywall != null) {
          _data.value = factory.paywall.fallbackPaywall
        } else {
          _error.value = error.message
          _status.value = Status.Error
        }
      }
    }

  }
}
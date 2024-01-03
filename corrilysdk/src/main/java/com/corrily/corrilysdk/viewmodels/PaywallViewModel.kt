package com.corrily.corrilysdk.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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

class PaywallViewModel(private val factory: DependencyProtocol) : ViewModel() {
  private val status: MutableState<Status> = mutableStateOf(Status.Pending)
  val State<Status>.isLoading: Boolean
    get() = value == Status.Pending
  val State<Status>.isError: Boolean
    get() = value == Status.Error
  val State<Status>.isSuccess: Boolean
    get() = value == Status.Success

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
    getPaywall()
  }

  fun purchase(product: PaywallProduct) {}

  fun restorePurchase() {}

  private fun getPaywall(paywallId: Int? = null) {
    status.value = Status.Pending
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
        status.value = Status.Success
      } catch (error: Exception) {
        // TODO: Handle fallback paywall
        _error.value = error.message
        status.value = Status.Error
      }
    }

  }
}
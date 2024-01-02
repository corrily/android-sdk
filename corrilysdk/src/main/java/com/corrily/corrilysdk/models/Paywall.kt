package com.corrily.corrilysdk.models

import kotlinx.serialization.Serializable

@Serializable
data class PaywallProduct(
  val id: Int,
  val name: String,
  val interval: String,
  val intervalCount: Int,
  val apiId: String,
  val price: String,
  val priceUsd: String,
  val cents: Boolean,
  val features: List<PaywallProductFeature>,
  val overrides: PaywallProductOverride?,
  val trial: PaywallProductTrial?
)

@Serializable
data class PaywallProductOverride(
  val buttonCaption: String,
  val buttonColor: String,
  val description: String,
  val badge: String,
  val cardColor: String,
  val buttonUrl: String,
  val priceIsHidden: Boolean,
  val priceOverrideText: String,
)

@Serializable
data class PaywallProductTrial(
  val trialId: Int,
  val trialDays: Int,
  val trialType: String
)

@Serializable
data class PaywallProductFeature(
  val id: Int,
  val name: String,
  val valueId: Int,
  val description: String,
  val apiId: String,
  val status: String,
  val type: String
)

@Serializable
data class PricingPage(
  val id: Int,
  val buttonsCaption: String,
  val buttonsColor: String,
  val backgroundColor: String,
  val collapseFeatures: Boolean,
  val showProductDescription: Boolean,
  val header: String,
  val description: String,
  val showHeader: Boolean,
  val showAnnualDiscountPercentage: Boolean,
  val annualDiscountPercentage: String,
  val type: String,
  val channel: String,
  val showFeatureComparisonTable: Boolean,
  val featureComparisonTableText: String,
  val isDefault: Boolean = false,
  val headerImage: String? = null,
  val footerDescription: String? = null
)

@Serializable
data class PaywallResponse(
  val success: Boolean,
  val pricingPage: PricingPage,
  val products: List<PaywallProduct>
)

@Serializable
data class PaywallDto(
  val country: String,
  val userId: String?,
  val ip: String?,
  val paywallId: Int?
)
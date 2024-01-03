package com.corrily.corrilysdk.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.PaywallProduct
import com.corrily.corrilysdk.models.ProductInterval
import com.corrily.corrilysdk.viewmodels.PaywallViewModel

@Composable
fun PaywallView(factory: DependencyProtocol) {
  var billingType: ProductInterval by remember {
    mutableStateOf(ProductInterval.Month)
  }
  var selectedProduct: PaywallProduct? by remember {
    mutableStateOf(null)
  }

  val paywallVM: PaywallViewModel = viewModel(factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return PaywallViewModel(factory = factory) as T
    }
  })

  val headerImage = paywallVM.paywall?.pricingPage?.headerImage
  val headerText = paywallVM.paywall?.pricingPage?.header
  val headerDescription = paywallVM.paywall?.pricingPage?.description
  val footerDescription = paywallVM.paywall?.pricingPage?.footerDescription

  val purchaseLabel =
    if (selectedProduct?.trial?.trialDays != null) "Start your ${selectedProduct!!.trial!!.trialDays}-day free trial" else "Continue"

  val products = when (billingType) {
    ProductInterval.Year -> paywallVM.yearlyProducts
    else -> paywallVM.monthlyProducts
  }

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .fillMaxWidth()
      .padding(16.dp)
  ) {

    // image
    if (!headerImage.isNullOrBlank()) {
      Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        AsyncImage(model = headerImage, contentDescription = null)
      }
    }

    // header and description
    if (!headerText.isNullOrBlank() || !headerDescription.isNullOrBlank()) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (!headerText.isNullOrBlank()) {
          Text(
            headerText,
            style = Typography().displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        }
        if (!headerDescription.isNullOrBlank()) {
          Text(headerDescription, style = Typography().titleLarge, textAlign = TextAlign.Center)
        }
      }
    }

    // toggle billing type
    BillingTypeToggle(
      leftLabel = "Billed Monthly",
      rightLabel = "Billed Yearly",
      value = billingType,
      onValueChange = {
        billingType = it
      })

    // products
    products.map {
      Box(
        modifier = Modifier
          .clickable(onClick = {
            selectedProduct = it
          })
          .fillMaxWidth()
          .border(
            width = 2.dp,
            color = if (selectedProduct?.id == it.id) Color.Blue else Color.Gray.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
          )
          .padding(16.dp)
      ) {
        if (!it.overrides?.badge.isNullOrBlank()) {
          Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
              .fillMaxWidth()
              .offset(x = 16.dp, y = (-10).dp)
          ) {
            Box(
              modifier = Modifier
                .padding(
                  horizontal = 8.dp
                )
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color.Blue)
            ) {
              Text(
                it.overrides!!.badge,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(it.name, style = Typography().titleMedium, fontWeight = FontWeight.Bold)
          if (!it.overrides?.description.isNullOrBlank()) {
            Text(it.overrides!!.description, style = Typography().titleMedium)
          }
          Row {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
              it.features.map { feat ->
                Text("- ${feat.name}", style = Typography().bodyMedium)
              }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(it.price, style = Typography().titleLarge, fontWeight = FontWeight.Bold)
              Text(
                "Billed ${if (billingType == ProductInterval.Month) "monthly" else "yearly"}",
                style = Typography().bodyMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    if (!paywallVM.error.isNullOrBlank()) {
      Text(paywallVM.error!!, color = Color.Red, style = Typography().bodyMedium)
    }

    // purchase button
    Column {
      Button(
        onClick = {
          if (selectedProduct != null) {
            paywallVM.purchase(selectedProduct!!)
          }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
          .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
      ) {
        Text(
          text = purchaseLabel,
          style = Typography().bodyLarge,
          color = Color.White,
          modifier = Modifier.padding(8.dp)
        )
      }

      // restore purchase, t&c
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
      ) {
        TextButton(onClick = {
          paywallVM.restorePurchase()
        }) {
          Text("Restore purchase", color = Color.Black)
        }
        Text("|")
        TextButton(onClick = { /*TODO*/ }) {
          Text("Terms and Conditions", color = Color.Black)
        }
      }
    }
  }
}
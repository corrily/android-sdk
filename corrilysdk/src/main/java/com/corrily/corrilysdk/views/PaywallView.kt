package com.corrily.corrilysdk.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.corrily.corrilysdk.dependencymanager.DependencyProtocol
import com.corrily.corrilysdk.models.PaywallProduct
import com.corrily.corrilysdk.viewmodels.PaywallViewModel

@Composable
fun PaywallView(factory: DependencyProtocol) {
  var billingType: String by remember {
    mutableStateOf("month")
  }
  var isChecked by remember {
    mutableStateOf(false)
  }
  var selectedProduct: PaywallProduct? by remember {
    mutableStateOf(null)
  }
  var selectedItem: Int by remember {
    mutableStateOf(1)
  }

//  val paywallVM: PaywallViewModel = viewModel(factory = object : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//      return PaywallViewModel(factory = factory) as T
//    }
//  })

//  val headerImage = paywallVM.paywall.value?.pricingPage?.headerImage

  Column(
    verticalArrangement = Arrangement.spacedBy(16.dp),
    modifier = Modifier
      .verticalScroll(rememberScrollState())
      .fillMaxWidth()
      .padding(16.dp)
  ) {

    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
      AsyncImage(model = "https://placehold.co/2000x400/png", contentDescription = null)
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Text("Choose a plan that meets your needs", style = Typography().displaySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
      Text("Free trial for new users only", style = Typography().titleLarge, textAlign = TextAlign.Center)
    }
//    Switch(checked = , onCheckedChange = )
    BillingTypeToggle(leftLabel = "Billed Monthly", rightLabel = "Billed Yearly", value = billingType, onValueChange = {
      if (billingType == "month") billingType = "year" else billingType = "month"
    })

    (1..3).map {
      Box(
        modifier = Modifier
          .clickable(onClick = {
            println("Click")
            selectedItem = it
          })
          .fillMaxWidth()
          .border(
            width = 2.dp,
            color = if (selectedItem == it) Color.Blue else Color.Gray.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp)
          )
          .padding(16.dp)
      ) {
        if (it == 1) {
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
                "Recommended",
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("Product Name", style = Typography().titleMedium, fontWeight = FontWeight.Bold)
          Text("Product Description", style = Typography().titleMedium)
          Row {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
              Text("- Product features", style = Typography().bodyMedium)
              Text("- Product features", style = Typography().bodyMedium)
              Text("- Product features", style = Typography().bodyMedium)
              Text("- Product features", style = Typography().bodyMedium)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("$1000", style = Typography().titleLarge, fontWeight = FontWeight.Bold)
              Text("Billed monthly", style = Typography().bodyMedium, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
    Column {
      Button(onClick = {
        println("Click")
      },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
        .fillMaxWidth(),
        colors= ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
        Text(text = "Continue",style = Typography().bodyLarge, color = Color.White, modifier = Modifier.padding(8.dp))
      }

      Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        TextButton(onClick = { /*TODO*/ }) {
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
package com.corrily.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.corrily.corrilysdk.CorrilySDK
import com.corrily.corrilysdk.billing.EntitlementsManager
import com.corrily.example.ui.theme.CorrilyTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    CorrilySDK.start(context = this, apiKey = "YOUR_API_KEY")

    setContent {
      CorrilyTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val hasSub: Boolean by EntitlementsManager.hasSubscription.observeAsState(initial = false)

          if (hasSub) {
            PremiumContent()
          } else {
            Paywall(activity = this@MainActivity)
          }
        }
      }
    }
  }
}


@Composable
fun Paywall(activity: MainActivity) {
  CorrilySDK.RenderPaywall(activity = activity)
}

@Composable
fun PremiumContent() {
  Text("Thank you for subscribing!")
}
package com.corrily.corrilysdk.views

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun PaywallView() {
  var isChecked by remember {
    mutableStateOf(false)
  }
  Text("hihi ${isChecked.toString()}", Modifier.clickable{
    isChecked = !isChecked
  })
}
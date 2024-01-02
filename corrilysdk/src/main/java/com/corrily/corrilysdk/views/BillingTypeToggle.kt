package com.corrily.corrilysdk.views

import androidx.compose.animation.core.Animation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Switch

@Composable
fun BillingTypeToggle(
  leftLabel: String,
  rightLabel: String,
  color: Color = Color.Blue,
  value: String,
  onValueChange: (String) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.End
  ) {
    Text(text = leftLabel, style = Typography().bodySmall)

    Switch(
      checked = value == "month",
      onCheckedChange = {
        if (it) {
          onValueChange("year")
        } else {
          onValueChange("month")
        }
      },
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.Blue,
        checkedTrackColor = Color.Blue.copy(alpha = 0.5f),
        uncheckedThumbColor = Color.Blue,
        uncheckedTrackColor = Color.Blue.copy(alpha = 0.5f),
      )
    )
    Text(text = rightLabel, style = Typography().bodySmall)
  }
}

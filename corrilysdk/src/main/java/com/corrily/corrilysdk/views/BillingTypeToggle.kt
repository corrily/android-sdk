package com.corrily.corrilysdk.views

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.corrily.corrilysdk.models.ProductInterval

@Composable
fun BillingTypeToggle(
  leftLabel: String,
  rightLabel: String,
  color: Color = Color.Blue,
  value: ProductInterval,
  onValueChange: (ProductInterval) -> Unit,
) {
  val spacing = 8.dp
  val width = 30.dp
  val height = 18.dp
  val thumb = 14.dp
  val offsetX by animateDpAsState(
    targetValue = if (value == ProductInterval.Year) thumb else 2.dp,
    animationSpec = tween(durationMillis = 200) // Customize the animation duration and easing
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = {
        when (value) {
          ProductInterval.Month -> onValueChange(ProductInterval.Year)
          else -> onValueChange(ProductInterval.Month)
        }
      }),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.End,
  ) {
    Text(
      text = leftLabel,
      style = Typography().bodySmall,
      color = if (value == ProductInterval.Year) Color.Gray else Color.Black
    )
    Spacer(modifier = Modifier.width(spacing))
    Box(
      modifier = Modifier
        .size(width = width, height = height)
        .clip(shape = RoundedCornerShape(height))
        .background(color = color), contentAlignment = Alignment.CenterStart
    ) {
      Box(
        modifier = Modifier
          .size(thumb)
          .offset(x = offsetX)
          .clip(shape = RoundedCornerShape(thumb))
          .shadow(elevation = 2.dp)
          .background(color = Color.White)
      ) {}
    }
    Spacer(modifier = Modifier.width(spacing))
    Text(
      text = rightLabel,
      style = Typography().bodySmall,
      color = if (value == ProductInterval.Month) Color.Gray else Color.Black
    )
  }
}

package tech.pauly.fooder.food

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.pauly.fooder.data.domain.model.Macros
import tech.pauly.fooder.data.network.model.Gram
import tech.pauly.fooder.data.network.model.Kcal
import tech.pauly.fooder.ui.theme.ExtendedTheme

@SuppressLint("ModifierParameter")
@Composable
fun MacrosBar(foodMacros: Macros?,  modifier: Modifier = Modifier.fillMaxWidth()) {
    foodMacros?.let { macros ->
        Row(
            modifier = modifier
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (macros.percentageProtein != 0f) {
                Box(
                    modifier = Modifier
                        .weight(macros.percentageProtein)
                        .fillMaxHeight()
                        .background(ExtendedTheme.colors.protein)
                )
            }
            if (macros.percentageCarbs != 0f) {
                Box(
                    modifier = Modifier
                        .weight(macros.percentageCarbs)
                        .fillMaxHeight()
                        .background(ExtendedTheme.colors.carb)
                )
            }
            if (macros.percentageFat != 0f) {
                Box(
                    modifier = Modifier
                        .weight(macros.percentageFat)
                        .fillMaxHeight()
                        .background(ExtendedTheme.colors.fat)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MacrosBarPreview() {
    MacrosBar(
        Macros(
            calories = Kcal(113f),
            fat = Gram(0.43f),
            carbs = Gram(20.4f),
            protein = Gram(7.53f)
        )
    )
}
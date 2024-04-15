package tech.pauly.fooder.food

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tech.pauly.fooder.R
import tech.pauly.fooder.data.domain.model.Macros
import tech.pauly.fooder.data.network.model.Gram
import tech.pauly.fooder.data.network.model.Kcal
import tech.pauly.fooder.ui.theme.ExtendedTheme
import tech.pauly.fooder.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailsBottomSheet(
    foodDetailsUiState: FoodDetailsUiState,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    when (foodDetailsUiState) {
        is FoodDetailsUiState.Empty -> {} //noop
        is FoodDetailsUiState.Success -> {
            val food = foodDetailsUiState.foodDetails
            ModalBottomSheet(
                onDismissRequest = {
                    onDismiss()
                },
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() },
            ) {
                BottomSheetContent(food = food)
            }
        }

        is FoodDetailsUiState.Loading -> {
            // todo
        }

        is FoodDetailsUiState.Error -> {
            // todo
        }
    }
}


@Composable
private fun BottomSheetContent(food: FoodDetailsUiItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .weight(1f, fill = false)
        ) {
            Text(
                food.name,
                style = Typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Text(
                "(per ${food.weight} g)",
                style = Typography.labelSmall,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                "Calories: ${food.calories} kcal",
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.size(8.dp))
            food.macros?.let { macros ->
                Text(
                    "Protein: ${macros.proteinRounded.gram}g (${macros.percentageProtein}%)",
                    style = Typography.bodyMedium,
                    color = ExtendedTheme.colors.protein,
                )
                Text(
                    "Carbs: ${macros.carbsRounded.gram}g (${macros.percentageCarbs}%)",
                    style = Typography.bodyMedium,
                    color = ExtendedTheme.colors.carb,
                )
                Text(
                    "Fat: ${macros.fatRounded.gram}g (${macros.percentageFat}%)",
                    style = Typography.bodyMedium,
                    color = ExtendedTheme.colors.fat,
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            MacrosBar(food.macros, Modifier.width(150.dp))
        }
        food.image?.let {
            Spacer(modifier = Modifier.size(8.dp))
            FoodImage(food.name, food.image, Modifier.requiredSize(150.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun BottomSheetContentPreview() {
    BottomSheetContent(
        FoodDetailsUiItem(
            "food_id",
            "Beans",
            "https://www.edamam.com/food-img/221/221986526e196ef4b38fd70da8d29fd6.jpg",
            100,
            2440,
            Macros(Kcal(100f), Gram(5f), Gram(29f), Gram(2f))
        )
    )
}
package tech.pauly.fooder.food


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tech.pauly.fooder.R
import tech.pauly.fooder.data.domain.model.Macros
import tech.pauly.fooder.data.network.model.Gram
import tech.pauly.fooder.data.network.model.Kcal
import tech.pauly.fooder.ui.theme.ExtendedTheme
import tech.pauly.fooder.ui.theme.Typography


@Composable
fun FoodCard(food: FoodUiItem, onClickFoodCard: (FoodUiItem) -> Unit) {
    Card(
        colors = CardColors(
            MaterialTheme.colorScheme.background,
            contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
            MaterialTheme.colorScheme.onErrorContainer,
            contentColorFor(backgroundColor = MaterialTheme.colorScheme.onErrorContainer)
        ),
        modifier = Modifier
            .fillMaxSize(),
        onClick = { onClickFoodCard(food) }
    ) {
        Column(Modifier.padding(8.dp)) {
            Text(
                text = food.name,
                modifier = Modifier
                    .fillMaxWidth(),
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            FoodImage(food.name, food.image, Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            MacrosBar(food.macros)
        }
    }
}

@Preview(widthDp = 200, heightDp = 300, showBackground = true)
@Composable
fun FoodCardPreview() {
    FoodCard(
        food = FoodUiItem(
            "food_id",
            "Beans",
            "https://www.edamam.com/food-img/221/221986526e196ef4b38fd70da8d29fd6.jpg",
            Macros(
                calories = Kcal(113f),
                fat = Gram(0.43f),
                carbs = Gram(20.4f),
                protein = Gram(7.53f)
            )
        )
    ) {}
}

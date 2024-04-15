package tech.pauly.fooder.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import tech.pauly.fooder.R

@Composable
fun FoodImage(foodName: String, foodUrl: String?, modifier: Modifier) {
    SubcomposeAsyncImage(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp)),
        model = foodUrl,
        contentDescription = "image of $foodName",
        contentScale = ContentScale.Crop,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onBackground),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        error = {
            Image(
                painter = painterResource(R.drawable.food_placeholder),
                contentDescription = "error"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FoodImagePreview() {
    FoodImage(
        "Beans",
        "https://www.edamam.com/food-img/221/221986526e196ef4b38fd70da8d29fd6.jpg",
        Modifier.requiredSize(150.dp)
    )
}
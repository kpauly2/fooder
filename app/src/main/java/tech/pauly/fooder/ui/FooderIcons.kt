package tech.pauly.fooder.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import arrow.fx.coroutines.continuations.resource
import tech.pauly.fooder.R

object FooderIcons {
    val Menu = Icons.Filled.Menu
    val Search = Icons.Filled.Search
    val SearchOutline = Icons.Outlined.Search
    val Recipes = Icons.Filled.ShoppingCart
    val RecipesOutline = Icons.Outlined.ShoppingCart
    val Back = Icons.AutoMirrored.Outlined.ArrowBack
}

object FooderIconsRes {
    val History =  R.drawable.outline_history_24
}
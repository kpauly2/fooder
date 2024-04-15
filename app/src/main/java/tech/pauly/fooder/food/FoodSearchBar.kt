package tech.pauly.fooder.food

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.pauly.fooder.data.local.model.RecentSearch
import tech.pauly.fooder.data.local.model.RecentSearchId
import tech.pauly.fooder.ui.FooderIcons
import tech.pauly.fooder.ui.FooderIconsRes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodSearchBar(
    active: Boolean,
    recentSearches: List<RecentSearch>,
    onSearch: (String) -> Unit,
) {
    var isActive by rememberSaveable(active) { mutableStateOf(active) }
    var query by rememberSaveable { mutableStateOf("") }
    val padding by animateIntAsState(if (isActive) 0 else 16, label = "search padding")

    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            isActive = false
            onSearch(it)
        },
        active = isActive,
        onActiveChange = { isActive = it },
        placeholder = { Text("Enter ingredient to search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = padding.dp)
            .wrapContentHeight(),
        shadowElevation = 6.dp,
        leadingIcon = {
            when (isActive) {
                true -> {
                    Icon(
                        imageVector = FooderIcons.Back,
                        contentDescription = "back",
                        modifier = Modifier.clickable { isActive = false }
                    )
                }

                false -> {
                    Icon(FooderIcons.Search, contentDescription = "search")
                }
            }
        }
    ) {
        RecentSearches(recentSearches) { query ->
            query.let {
                isActive = false
                onSearch(it)
            }
        }
    }
}

@Composable
private fun RecentSearches(
    recentSearches: List<RecentSearch>,
    onSearch: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(recentSearches) { recentSearch ->
            RecentSearch(recentSearch.searchQuery, onSearch)
        }
    }
}


@Composable
private fun RecentSearch(
    query: String,
    onSearch: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onSearch(query)
            }
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = FooderIconsRes.History),
            contentDescription = "history"
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = query,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

val previewRecentSearches = listOf(
    RecentSearch(RecentSearchId(0), "apples"),
    RecentSearch(RecentSearchId(1), "banana"),
    RecentSearch(RecentSearchId(2), "bread")
)

@Preview(showBackground = true)
@Composable
fun FoodSearchBarInactivePreview() {
    FoodSearchBar(active = false, recentSearches = previewRecentSearches) {}
}

@Preview(showBackground = true)
@Composable
fun FoodSearchBarActivePreview() {
    FoodSearchBar(active = true, recentSearches = previewRecentSearches) {}
}

@Preview(showBackground = true)
@Composable
fun RecentSearchesPreview() {
    RecentSearches(previewRecentSearches) { }
}

@Preview(showBackground = true)
@Composable
fun RecentSearchPreview() {
    RecentSearch(query = "apples") { }
}
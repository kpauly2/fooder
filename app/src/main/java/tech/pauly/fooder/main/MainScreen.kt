package tech.pauly.fooder.main

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import tech.pauly.fooder.navigation.FooderAppState
import tech.pauly.fooder.navigation.FooderNavHost
import tech.pauly.fooder.navigation.TopLevelDestination
import tech.pauly.fooder.ui.FooderIcons
import tech.pauly.fooder.ui.theme.FooderTheme
import tech.pauly.fooder.ui.theme.Typography

@Composable
fun MainScreen(
    appState: FooderAppState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    FooderTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { FooderDrawerSheet() },
        ) {
            Scaffold(modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
                topBar = {
                    FooderTopAppBar(onMenuButtonClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    })
                },
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                bottomBar = {
                    FooderBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                    )
                },
                content = { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    ) {
                        FooderNavHost(appState = appState, onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message, action, duration = SnackbarDuration.Short
                            ) == SnackbarResult.ActionPerformed
                        })
                    }
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FooderTopAppBar(onMenuButtonClick: () -> Unit) {
    TopAppBar(title = {
        Text("Fooder", style = Typography.headlineMedium, fontWeight = FontWeight.Bold)
    }, navigationIcon = {
        IconButton(onClick = {
            onMenuButtonClick()
        }) {
            Icon(FooderIcons.Menu, contentDescription = "Fooder menu")
        }
    })
}

@Composable
fun FooderBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar {
        destinations.forEach { destination ->
            val isSelected = currentDestination.run {
                this?.hierarchy?.any {
                    it.route?.contains(destination.name, true) ?: false
                } ?: false
            }
            NavigationBarItem(selected = isSelected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                },
                label = { Text(stringResource(id = destination.titleTextId)) })
        }
    }
}


@Preview
@Composable
fun FooderTopAppBarPreview() {
    FooderTopAppBar {}
}

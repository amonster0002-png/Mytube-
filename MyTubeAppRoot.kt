package com.mytube.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mytube.app.ui.screens.HomeScreen
import com.mytube.app.ui.screens.ProfileScreen
import com.mytube.app.ui.screens.SearchScreen
import com.mytube.app.ui.screens.SubscriptionsScreen
import com.mytube.app.ui.screens.UploadScreen
import com.mytube.app.ui.screens.WatchScreen
import com.mytube.app.ui.viewmodel.VideoViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Upload : Screen("upload", "Upload", Icons.Filled.AddCircle)
    object Subscriptions : Screen("subscriptions", "Subscriptions", Icons.Filled.Subscriptions)
    object Profile : Screen("profile", "You", Icons.Filled.Person)
    object Watch : Screen("watch/{videoId}", "Watch", Icons.Filled.Home)
}

@Composable
fun MyTubeAppRoot(viewModel: VideoViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Search,
        Screen.Upload,
        Screen.Subscriptions,
        Screen.Profile
    )

    val showBottomBar = currentRoute?.startsWith("watch") != true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFF0F0F0F),
                    contentColor = Color.White
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFFF0000),
                                selectedTextColor = Color.White,
                                indicatorColor = Color(0x33FF0000),
                                unselectedIconColor = Color(0xFFAAAAAA),
                                unselectedTextColor = Color(0xFFAAAAAA)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onVideoClick = { videoId ->
                        navController.navigate("watch/$videoId")
                    },
                    onSearchClick = {
                        navController.navigate(Screen.Search.route)
                    }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    viewModel = viewModel,
                    onVideoClick = { videoId ->
                        navController.navigate("watch/$videoId")
                    }
                )
            }
            composable(Screen.Upload.route) {
                UploadScreen(
                    viewModel = viewModel,
                    onUploadSuccess = { newId ->
                        navController.navigate("watch/$newId")
                    }
                )
            }
            composable(Screen.Subscriptions.route) {
                SubscriptionsScreen(
                    viewModel = viewModel,
                    onVideoClick = { videoId ->
                        navController.navigate("watch/$videoId")
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = viewModel,
                    onVideoClick = { videoId ->
                        navController.navigate("watch/$videoId")
                    }
                )
            }
            composable(Screen.Watch.route) { backStack ->
                val videoId = backStack.arguments?.getString("videoId") ?: ""
                WatchScreen(
                    videoId = videoId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onRelatedVideoClick = { nextId ->
                        navController.navigate("watch/$nextId") {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
        }
    }
}

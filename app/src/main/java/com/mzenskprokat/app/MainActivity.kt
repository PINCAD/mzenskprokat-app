package com.mzenskprokat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mzenskprokat.app.ui.screens.CatalogScreen
import com.mzenskprokat.app.ui.screens.ContactsScreen
import com.mzenskprokat.app.ui.screens.HomeScreen
import com.mzenskprokat.app.ui.screens.OrderScreen
import com.mzenskprokat.app.ui.screens.ProductDetailScreen
import com.mzenskprokat.app.ui.theme.MzenskProkatTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mzenskprokat.app.viewmodels.CartViewModel
import androidx.compose.material.icons.filled.Person
import com.mzenskprokat.app.ui.screens.ProfileScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MzenskProkatTheme {
                MainApp()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object MainTabs : Screen("main_tabs", "Главная", Icons.Default.Home)
    object MainTabsOrder : Screen("main_tabs_order", "Корзина", Icons.Default.ShoppingCart)

    object Home : Screen("home", "Главная", Icons.Default.Home)
    object Catalog : Screen("catalog", "Каталог", Icons.AutoMirrored.Filled.List)
    object Profile : Screen("profile", "Профиль", Icons.Default.Person)
    object Contacts : Screen("contacts", "Контакты", Icons.Default.Phone)
    object Order : Screen("order", "Корзина", Icons.Default.ShoppingCart)

    object ProductDetail : Screen("product/{productId}", "Продукт", Icons.Default.Info) {
        fun createRoute(productId: String) = "product/$productId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val rootNavController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = Screen.MainTabs.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(Screen.MainTabs.route) {
                MainTabsScreen(
                    startRoute = Screen.Home.route,
                    cartViewModel = cartViewModel,
                    onOpenProduct = { productId ->
                        rootNavController.navigate(Screen.ProductDetail.createRoute(productId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.MainTabsOrder.route) {
                MainTabsScreen(
                    startRoute = Screen.Order.route,
                    cartViewModel = cartViewModel,
                    onOpenProduct = { productId ->
                        rootNavController.navigate(Screen.ProductDetail.createRoute(productId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.ProductDetail.route) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                if (productId != null) {
                    ProductDetailScreen(
                        productId = productId,
                        onBackClick = { rootNavController.popBackStack() },
                        onOrderClick = {
                            rootNavController.navigate(Screen.MainTabsOrder.route) {
                                popUpTo(Screen.MainTabs.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabsScreen(
    cartViewModel: CartViewModel,
    startRoute: String = Screen.Home.route,
    onOpenProduct: (String) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Catalog,
        Screen.Profile,
        Screen.Order,
        Screen.Contacts
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = startRoute,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable(Screen.Home.route) {
                    HomeScreen()
                }

                composable(Screen.Catalog.route) {
                    CatalogScreen(
                        onProductClick = onOpenProduct
                    )
                }

                composable(Screen.Profile.route) {
                    ProfileScreen()
                }

                composable(Screen.Order.route) {
                    OrderScreen(cartViewModel = cartViewModel)
                }

                composable(Screen.Contacts.route) {
                    ContactsScreen()
                }
            }
        }
    }
}

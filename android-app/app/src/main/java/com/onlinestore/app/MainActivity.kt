package com.onlinestore.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.onlinestore.app.ui.screens.AddressesScreen
import com.onlinestore.app.ui.screens.CartScreen
import com.onlinestore.app.ui.screens.CatalogScreen
import com.onlinestore.app.ui.screens.FavoritesScreen
import com.onlinestore.app.ui.screens.LoginScreen
import com.onlinestore.app.ui.screens.MapPickScreen
import com.onlinestore.app.ui.screens.OrdersScreen
import com.onlinestore.app.ui.theme.OnlineStoreTheme
import com.onlinestore.app.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnlineStoreTheme {
                StoreApp()
            }
        }
    }
}

@Composable
fun StoreApp() {
    val authVm: AuthViewModel = hiltViewModel()
    val loggedIn by authVm.loggedIn.collectAsState()
    LaunchedEffect(loggedIn) {
        if (loggedIn) authVm.connectRealtimeIfLoggedIn()
    }
    if (!loggedIn) {
        LoginScreen()
        return
    }
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val dest = backStack?.destination
    val tabs = listOf(
        Triple("catalog", Icons.Default.Home, R.string.nav_catalog),
        Triple("favorites", Icons.Default.Favorite, R.string.nav_favorites),
        Triple("cart", Icons.Default.ShoppingCart, R.string.nav_cart),
        Triple("orders", Icons.Default.List, R.string.nav_orders),
        Triple("addresses", Icons.Default.LocationOn, R.string.nav_addresses)
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { (route, icon, titleRes) ->
                    NavigationBarItem(
                        selected = dest?.route == route,
                        onClick = {
                            nav.navigate(route) {
                                popUpTo(nav.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = null) },
                        label = { Text(stringResource(titleRes)) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "catalog",
            modifier = Modifier.padding(padding)
        ) {
            composable("catalog") { CatalogScreen() }
            composable("favorites") { FavoritesScreen() }
            composable("cart") { CartScreen() }
            composable("orders") { OrdersScreen() }
            composable("addresses") {
                AddressesScreen(onPickOnMap = { nav.navigate("map") })
            }
            composable("map") { MapPickScreen(onBack = { nav.popBackStack() }) }
        }
    }
}

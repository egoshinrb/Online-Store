package com.onlinestore.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onlinestore.app.R
import com.onlinestore.app.ui.viewmodel.AuthViewModel
import com.onlinestore.app.ui.viewmodel.CatalogViewModel
import com.onlinestore.app.ui.viewmodel.ShoppingCartViewModel

@Composable
fun CatalogScreen(
    authVm: AuthViewModel = hiltViewModel(),
    vm: CatalogViewModel = hiltViewModel(),
    cartVm: ShoppingCartViewModel = hiltViewModel()
) {
    val categories by vm.categories.collectAsState()
    val products by vm.products.collectAsState()
    val loading by vm.loading.collectAsState()
    val err by vm.error.collectAsState()
    var selectedCat by remember { mutableStateOf<Long?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(stringResource(R.string.nav_catalog), style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { authVm.logout() }) {
                Text(stringResource(R.string.logout))
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                FilterChip(
                    selected = selectedCat == null,
                    onClick = {
                        selectedCat = null
                        vm.refresh(null)
                    },
                    label = { Text("All") }
                )
            }
            items(categories) { c ->
                FilterChip(
                    selected = selectedCat == c.id,
                    onClick = {
                        selectedCat = c.id
                        vm.refresh(c.id)
                    },
                    label = { Text(c.name) }
                )
            }
        }
        if (loading) Text("…")
        err?.let { Text(it) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products) { p ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(p.name, style = MaterialTheme.typography.titleMedium)
                        Text("${p.price} ₽ / ${p.unit}")
                    }
                    Button(onClick = { cartVm.addToCart(p) }) {
                        Text(stringResource(R.string.add_to_cart))
                    }
                }
            }
        }
    }
}

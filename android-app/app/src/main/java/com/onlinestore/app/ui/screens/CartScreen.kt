package com.onlinestore.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.onlinestore.app.ui.viewmodel.OrderFlowViewModel
import com.onlinestore.app.ui.viewmodel.ShoppingCartViewModel

@Composable
fun CartScreen(
    cartVm: ShoppingCartViewModel = hiltViewModel(),
    orderVm: OrderFlowViewModel = hiltViewModel()
) {
    val lines by cartVm.cartState.lines.collectAsState()
    var address by remember { mutableStateOf("") }
    val err by orderVm.lastError.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.nav_cart), style = MaterialTheme.typography.headlineSmall)
        err?.let { Text(it) }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f, fill = true)) {
            items(lines) { line ->
                Text("${line.product.name} x ${line.quantity} — ${line.product.price}")
            }
        }
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { orderVm.checkoutCashOnDelivery(address) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.checkout))
        }
    }
}

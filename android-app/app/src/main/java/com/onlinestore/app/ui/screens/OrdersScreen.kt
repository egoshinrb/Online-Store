package com.onlinestore.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.onlinestore.app.ui.viewmodel.OrderFlowViewModel

@Composable
fun OrdersScreen(vm: OrderFlowViewModel = hiltViewModel()) {
    val orders by vm.orders.collectAsState()
    val err by vm.lastError.collectAsState()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Orders", style = MaterialTheme.typography.headlineSmall)
        err?.let { Text(it) }
        LazyColumn {
            items(orders) { o ->
                Text("#${o.id} ${o.status} ${o.total} ₽ — ${o.paymentStatus}")
            }
        }
    }
}

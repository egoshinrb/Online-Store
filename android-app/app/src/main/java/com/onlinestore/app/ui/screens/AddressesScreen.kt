package com.onlinestore.app.ui.screens

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
import com.onlinestore.app.ui.viewmodel.AddressViewModel

@Composable
fun AddressesScreen(
    onPickOnMap: () -> Unit,
    vm: AddressViewModel = hiltViewModel()
) {
    val list by vm.addresses.collectAsState()
    var line by remember { mutableStateOf("") }
    var label by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(stringResource(R.string.nav_addresses), style = MaterialTheme.typography.headlineSmall)
        Button(onClick = onPickOnMap) {
            Text(stringResource(R.string.map_pick))
        }
        OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = line, onValueChange = { line = it }, label = { Text("Address") }, modifier = Modifier.fillMaxWidth())
        Button(
            onClick = { vm.add(label.ifBlank { null }, line, null, null, false) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
        LazyColumn {
            items(list) { a ->
                Text("${a.label ?: ""} ${a.addressLine}")
            }
        }
    }
}

package com.onlinestore.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun LoginScreen(
    vm: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var register by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(if (register) stringResource(R.string.register) else stringResource(R.string.login))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth()
        )
        if (register) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        error?.let { Text(it) }
        Button(
            onClick = {
                if (register) {
                    vm.register(email, password, name) { e ->
                        error = e?.message
                    }
                } else {
                    vm.login(email, password) { e ->
                        error = e?.message
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (register) stringResource(R.string.register) else stringResource(R.string.login))
        }
        Button(onClick = { register = !register }) {
            Text(if (register) stringResource(R.string.login) else stringResource(R.string.register))
        }
    }
}

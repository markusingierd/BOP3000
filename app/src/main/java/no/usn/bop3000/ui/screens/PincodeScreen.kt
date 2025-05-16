package no.usn.bop3000.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PincodeScreen(onSuccess: () -> Unit) {
    var inputCode by remember { mutableStateOf("") }
    val correctCode = "1234"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Skriv inn PIN-kode", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = inputCode,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                    inputCode = it
                }
            },
            label = { Text("PIN") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (inputCode == correctCode) onSuccess()
        }) {
            Text("Logg inn")
        }
    }
}

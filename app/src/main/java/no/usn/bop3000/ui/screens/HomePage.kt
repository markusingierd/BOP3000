package no.usn.bop3000.ui.screens

import androidx.compose.foundation.layout.*
import no.usn.bop3000.R
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.welcome_message),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Handtering av knappetrykk */ },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // Bakgrunnsfarge når deaktivert
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f) // Tekstfarge når deaktivert
            )
        ) {
            Text(
                text = stringResource(id = R.string.klikk_her)
            )
        }
    }
}

@Preview
@Composable
fun PreviewHomePage() {
    HomePage()
}

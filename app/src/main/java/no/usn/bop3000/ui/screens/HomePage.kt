package no.usn.bop3000.ui.screens

import androidx.compose.foundation.layout.*
import no.usn.bop3000.R
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.stringResource

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(id = R.string.welcome_message),
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(45.dp))

        Button(
            onClick = { /* Handtering av knappetrykk */ },
            modifier = Modifier.size(150.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = R.string.klikk_her),
                fontSize = 18.sp
            )
        }
    }

}

@Preview
@Composable
fun PreviewHomePage() {
    HomePage()
}

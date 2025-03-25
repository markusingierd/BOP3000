package no.usn.bop3000.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.graphics.Color
import no.usn.bop3000.R

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.backgroundhome),
            contentDescription = "Bakgrunnsbilde",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x00000000),
                            Color(0xCC1A0033)
                        ),
                        startY =Float.POSITIVE_INFINITY,
                        endY = 0f
                    )
                )
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.welcome),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(R.string.intro_text),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = { navController.navigate("trail") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6600),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(end = 25.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.start_tur),
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
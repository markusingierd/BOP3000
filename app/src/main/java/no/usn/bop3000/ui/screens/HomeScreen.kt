package no.usn.bop3000.ui.screens

import androidx.compose.foundation.layout.*
import no.usn.bop3000.R
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_img),
                contentDescription = "Bannerbilde",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 0f,
                            endY = 100f
                        )
                    )
            )


            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = stringResource(id = R.string.headliner),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(R.string.welcome),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 24.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Text(
                text = stringResource(R.string.intro_text),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handtering av knappetrykk */ },
                enabled = true,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.size(150.dp, 50.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.start_tur),
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp)
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            ImageSlider()
        }
    }
}

@Composable
fun ImageSlider() {
    val images = listOf(
        painterResource(id = R.drawable.slider1),
        painterResource(id = R.drawable.slider2),
        painterResource(id = R.drawable.slider3)
    )

    val currentImageIndex = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentImageIndex.intValue = (currentImageIndex.intValue + 1) % images.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        Image(
            painter = images[currentImageIndex.intValue],
            contentDescription = "Slideshow Image",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
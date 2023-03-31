package com.example.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.stopwatch.ui.theme.StopWatchTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.mutableStateOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier.background(color = Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    AppWrapper()
                }
            }
        }
    }
}

@Composable
fun Timer(
    // total time of the timer
    //totalTime: Long,

    // circular handle color
    handleColor: Color,

    // color of inactive bar / progress bar
    inactiveBarColor: Color,

    // color of active bar
    activeBarColor: Color,
    modifier: Modifier = Modifier,

    // set initial value to 1
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var serviceTimeToPlayInput by remember { mutableStateOf("") }
    var totalTime by remember { mutableStateOf(0L) }
    if (serviceTimeToPlayInput != "") {
    totalTime = serviceTimeToPlayInput.toLong()
    }

    // create variable for
    // size of the composable
    var size by remember { mutableStateOf(IntSize.Zero) }
    // create variable for value

    var value by remember { mutableStateOf(initialValue) }

    // create variable for current time
    var currentTime by remember { mutableStateOf(totalTime) }

    // create variable for isTimerRunning
    var isTimerRunning by remember { mutableStateOf(false) }

//    var currentTotalTime by remember { mutableStateOf(cur)}

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
//            println("currentTime::: " + currentTime.toLong())
//            println("value::: " + value)
//            println("currentTime::: " + (currentTime / totalTime / 100000).toLong())
        }
    }
    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .onSizeChanged { size = it }
        ) {
            // draw the timer

            Column(Modifier.fillMaxWidth()) {
//                STILL working on the progress bar
                Spacer(
                    Modifier
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(color = Color.Green)
                )
                LinearProgressIndicator(
                    progress = value,
//                    progress = (currentTime.toFloat() / totalTime.toFloat()),
//                    progress = (currentTime.toFloat() / serviceTimeToPlayInput.toFloat()).toFloat(),
                    modifier = Modifier
                        .height(15.dp)
                        .fillMaxWidth()
                        .background(color = Color.Red),
                )
            }
            // add value of the timer
            Text(
                text = (currentTime / 1000L).toString(),
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            // create button to start or stop the timer
            Button(
                onClick = {
                    if (currentTime <= 0L) {
                        currentTime = totalTime
                        isTimerRunning = true
                    } else {
                        isTimerRunning = !isTimerRunning
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter),
                // change button color
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                        Color.Green
                    } else {
                        Color.Red
                    }
                )
            ) {
                Text(
                    // change the text of button based on values
                    text =
                    if (isTimerRunning && currentTime >= 0L) "Stop"
                    else if (!isTimerRunning && currentTime >= 0L) "Start"
                    else "Restart"
                )
            }
        }
    }

    Column(modifier = Modifier
        .padding(horizontal = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(50.dp))
        EditTimeToPlay(
            value = serviceTimeToPlayInput,
            onValueChange = {
                serviceTimeToPlayInput = it
                currentTime = serviceTimeToPlayInput.toLong() * 1000L
//                value = currentTime.toFloat()
//                totalTime = currentTime
                value = 1f
            },
            editingEnabled = !isTimerRunning
        )

        Spacer(Modifier.height(50.dp))
        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    isTimerRunning = false
                    currentTime = totalTime
                    value = 1f
                    serviceTimeToPlayInput = ""
                },
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun EditTimeToPlay(
    value: String,
    onValueChange: (String) -> Unit,
    editingEnabled: Boolean
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(textColor = Color.White),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = editingEnabled,
        label = { Text("Enter time in seconds")}
    )
}

@Composable
fun AppWrapper() {
    StopWatchTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Timer(
//                totalTime = 45L * 1000L,
                handleColor = Color.Green,
                inactiveBarColor = Color.DarkGray,
                activeBarColor = Color(0xFF37B900),
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppWrapper()
}
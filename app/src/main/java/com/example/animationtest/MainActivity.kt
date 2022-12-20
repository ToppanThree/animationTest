package com.example.animationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.animationtest.ui.theme.AnimationTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimationTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyUI()
                }
            }
        }
    }
}

internal val LocalSwipeTargetInfo = compositionLocalOf { DragTargetInfo() }

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun MyUI() {
    val composableScope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val currentState = LocalSwipeTargetInfo.current
    val padding = 40.dp
    val radius = 32.dp
    val offset = 32.dp
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val start = with(LocalDensity.current) { offset.toPx() }
    val end = with(LocalDensity.current) { (screenWidth - (offset + padding * 2)).toPx() }
    val anchors = mapOf(start to 0, end to 1)
    Box(modifier = Modifier
        .swipeable(
            state = swipeableState,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(1f) },
            orientation = Orientation.Horizontal
        )
        .size(width = screenWidth, height = 64.dp)
        .padding(horizontal = padding)

    ){
        Canvas(
            modifier = Modifier
                .matchParentSize()
        ) {
            drawRoundRect(
                color = Color(0xFFE3AA28),
                size = Size(width = size.width, height = size.height),
                cornerRadius = CornerRadius(x = 32.dp.toPx(), 32.dp.toPx())
            )
        }

        Canvas(
            modifier = Modifier
                .size(32.dp)
        ){
            if(!currentState.swipeReached) {
                drawCircle(
                    color = Color(0xFFF3E4C2),
                    radius = radius.toPx(),
                    center = Offset(swipeableState.offset.value, offset.toPx())
                )
                currentState.swipeReached = swipeableState.offset.value == end
            }else{
                // TODO
                drawCircle(
                    color = Color(0xFFF3E4C2),
                    radius = radius.toPx(),
                    center = Offset(offset.toPx(), offset.toPx())
                )
                composableScope.launch {
                    swipeableState.snapTo(0)
                    currentState.swipeReached = false
                }
            }
        }

        Text(
            text = "Swipe To Do",
            color = Color(0xFFF3E4C2),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .matchParentSize()
                .wrapContentHeight(),
            fontWeight = FontWeight.Bold,
        )
    }
}

internal class DragTargetInfo {
    var swipeReached by mutableStateOf(false)
}
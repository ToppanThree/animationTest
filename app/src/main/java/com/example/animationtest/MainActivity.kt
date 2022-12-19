package com.example.animationtest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.math.MathUtils
import com.example.animationtest.ui.theme.AnimationTestTheme

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

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

@Preview
@Composable
fun MyUI() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val currentState = LocalDragTargetInfo.current
    val padding = 40.dp
    Box(modifier = Modifier
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
                .matchParentSize()
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                    }, onDrag = { change, dragAmount ->
                        change.consumeAllChanges()
                        currentState.dragOffset += Offset(dragAmount.x, dragAmount.y)
                    }, onDragEnd = {

                        currentState.dragOffset = Offset.Zero
                    }, onDragCancel = {
                        currentState.dragOffset = Offset.Zero
                    })
                }
        ){
            val offsetX = 32.dp.toPx()
            val offsetY = 32.dp.toPx()
            val maxX = screenWidth.toPx() - (offsetX + padding.toPx() * 2)
            val x = MathUtils.clamp(
                offsetX + currentState.dragOffset.x,
                offsetX,
                maxX
            )
            drawCircle(
                color = Color(0xFFF3E4C2),
                radius = 24.dp.toPx(),
                center = Offset(x = x, y = offsetY)
            )
            Log.d("x",x.toString())
            Log.d("maxX",maxX.toString())
            if (x == maxX) Log.d("reached","TO DO")
        }

        Text(
            text = "スライドでサインアウト",
            color = Color(0xFFF3E4C2),
            fontSize = 16.sp, textAlign = TextAlign.Center,
            modifier = Modifier
                .matchParentSize()
                .wrapContentHeight(),
            fontWeight = FontWeight.Bold,
        )
    }
}

internal class DragTargetInfo {
    var dragOffset by mutableStateOf(Offset.Zero)
}
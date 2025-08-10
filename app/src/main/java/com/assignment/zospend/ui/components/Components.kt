package com.assignment.zospend.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.assignment.zospend.R
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDialog(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
        content = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                contentDescription = stringResource(id = R.string.dialog_content_description),
                modifier = Modifier.fillMaxWidth()
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> GenericDropDown(
    label: String,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    getItemName: (T) -> String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            readOnly = true,
            value = getItemName(selectedItem),
            onValueChange = {},
            label = { LabelMedium(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { BodyRegular(getItemName(selectionOption)) },
                    onClick = {
                        onItemSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview(widthDp = 300, heightDp = 200)
@Composable
fun FilledLineChart(
    data: List<Pair<String, Int?>> = listOf(
        Pair("Mon", 10),
        Pair("Tue", 8),
        Pair("Wed", 0),
        Pair("Thu", 10),
        Pair("Fri", 4),
        Pair("Sat", 5),
        Pair("Sun", 9),
    )
) {
    val density = LocalDensity.current
    val spacingY = density.run { 32.dp.toPx() }
    val spacingX = density.run { 20.dp.toPx() }
    val graphColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val transparentGraphColor = graphColor.copy(alpha = 0.4f)

    val textPaint = Paint().apply {
        color = textColor.hashCode()
        textAlign = Paint.Align.CENTER
        textSize = density.run { 12.sp.toPx() }
    }

    val (maxY, yInterval) = remember(data) {
        calculateYAxisParameters(data)
    }

    Canvas(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 12.dp)
            .fillMaxWidth()
            .height(186.dp)
    ) {

        // x axis text
        val spacePerDay = (size.width - spacingX) / data.size
        val startX = spacingX + spacePerDay / 2
        (data.indices step 1).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(data[i].first, startX + i * spacePerDay, size.height, textPaint)
            }
        }

        // y axis text
        val availableHeight = size.height - spacingY
        val maxYCount = if (yInterval > 0) maxY / yInterval else 0
        (0..maxYCount).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    i.times(yInterval).toString(),
                    spacingX,
                    availableHeight - i * availableHeight / maxYCount,
                    textPaint
                )
            }
        }

        // graph line path calculation
        val strokePath = Path().apply {
            data.filter { it.second != null }.indices.forEach { i ->
                val info = data[i]
                val x1 = startX + i * spacePerDay
                val y1 =
                    availableHeight - ((info.second?.times(availableHeight))?.div(maxY)
                        ?: 0f)

                if (i == 0) {
                    moveTo(x1, y1)
                }
                lineTo(x1, y1)

                // plot graph points for all points except ends
                if (i > 0 && i < data.size - 1) {
                    drawCircle(center = Offset(x1, y1), color = graphColor, radius = 4.dp.toPx())
                }
            }
        }

        // plot line graph
        drawPath(
            path = strokePath, color = graphColor, style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        // path for area under graph
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(
                startX + (data.filter { it.second != null }.size - 1) * spacePerDay,
                availableHeight
            )
            lineTo(startX, availableHeight)
            close()
        }

        // fill area under graph with gradient
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(transparentGraphColor, Color.Transparent),
                endY = availableHeight
            ),
        )
    }
}

private fun calculateYAxisParameters(data: List<Pair<String, Int?>>): Pair<Int, Int> {
    val maxDataValue = data.mapNotNull { it.second }.maxOrNull() ?: 0
    if (maxDataValue == 0) return 100 to 25

    val range = maxDataValue.toDouble()
    val roughInterval = range / 4
    val exponent = floor(log10(roughInterval))
    val powerOf10 = 10.0.pow(exponent)
    val fraction = roughInterval / powerOf10

    val niceFraction = when {
        fraction <= 1.0 -> 1.0
        fraction <= 2.0 -> 2.0
        fraction <= 5.0 -> 5.0
        else -> 10.0
    }

    val interval = (niceFraction * powerOf10).toInt()
    val niceMaxY = (ceil(maxDataValue.toDouble() / interval) * interval).toInt()

    return niceMaxY to interval
}
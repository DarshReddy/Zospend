package com.assignment.zospend.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.assignment.zospend.R

private val PoppinsFont = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

/** Title **/
@Composable
fun TitleXL(
    // Title XL - Poppins semibold 24/36
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_24_36.fontSize,
            lineHeight = TextSize.SP_24_36.lineHeight,
            letterSpacing = TextSize.SP_24_36.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun TitleLarge(
    // Poppins semibold 20/32
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_20_32.fontSize,
            lineHeight = TextSize.SP_20_32.lineHeight,
            letterSpacing = TextSize.SP_20_32.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun TitleRegular(
    // Poppins semibold 16/24
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_16_24.fontSize,
            lineHeight = TextSize.SP_16_24.lineHeight,
            letterSpacing = TextSize.SP_16_24.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun TitleRegularBold(
    // Poppins semibold 16/24
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.SP_16_24.fontSize,
            lineHeight = TextSize.SP_16_24.lineHeight,
            letterSpacing = TextSize.SP_16_24.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun TitleSmall(
    // Poppins semibold 12/16
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_12_16.fontSize,
            lineHeight = TextSize.SP_12_16.lineHeight,
            letterSpacing = TextSize.SP_12_16.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

/** Body **/
@Composable
fun BodyXL(
    // Poppins regular 20/32
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_24_36.fontSize,
            lineHeight = TextSize.SP_24_36.lineHeight,
            letterSpacing = TextSize.SP_24_36.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun BodyLarge(
    // Poppins regular 16/24
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_16_24.fontSize,
            lineHeight = TextSize.SP_16_24.lineHeight,
            letterSpacing = TextSize.SP_16_24.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun BodyRegular(
    // Poppins regular 14/20
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_14_20.fontSize,
            lineHeight = TextSize.SP_14_20.lineHeight,
            letterSpacing = TextSize.SP_14_20.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun BodySmall(
    // Poppins regular 12/16
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_12_16.fontSize,
            lineHeight = TextSize.SP_12_16.lineHeight,
            letterSpacing = TextSize.SP_12_16.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun BodyXS(
    //Body xs - Poppins medium 10/14
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_10_14.fontSize,
            lineHeight = TextSize.SP_10_14.lineHeight,
            letterSpacing = TextSize.SP_10_14.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

/** Label **/
@Composable
fun LabelLarge(
    // Poppins Semibold 20/32/0
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_20_32_0.fontSize,
            lineHeight = TextSize.SP_20_32_0.lineHeight,
            letterSpacing = TextSize.SP_20_32_0.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun LabelMedium(
    // Poppins Semibold 14/20/0
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.SemiBold,
            fontSize = TextSize.SP_14_20_0.fontSize,
            lineHeight = TextSize.SP_14_20_0.lineHeight,
            letterSpacing = TextSize.SP_14_20_0.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun LabelSmall(
    // Poppins Regular 12/16/0.5
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Normal,
            fontSize = TextSize.SP_12_16_05.fontSize,
            lineHeight = TextSize.SP_12_16_05.lineHeight,
            letterSpacing = TextSize.SP_12_16_05.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

/** Display **/
@Composable
fun DisplayRegular(
    // Poppins bold 32/44
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.SP_32_44.fontSize,
            lineHeight = TextSize.SP_32_44.lineHeight,
            letterSpacing = TextSize.SP_32_44.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

@Composable
fun DisplayLarge(
    // Poppins bold 40/60
    text: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    if (text != null) {
        Text(
            modifier = modifier,
            text = text,
            fontFamily = PoppinsFont,
            fontWeight = FontWeight.Bold,
            fontSize = TextSize.SP_40_60.fontSize,
            lineHeight = TextSize.SP_40_60.lineHeight,
            letterSpacing = TextSize.SP_40_60.letterSpacing,
            color = color,
            textDecoration = textDecoration,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines,
            minLines = minLines,
            onTextLayout = onTextLayout,
            style = textStyle.copy(
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            )
        )
    }
}

enum class TextSize(val fontSize: TextUnit, val lineHeight: TextUnit, val letterSpacing: TextUnit) {
    SP_10_14(10.sp, 14.sp, TextUnit.Unspecified),
    SP_12_16(12.sp, 16.sp, TextUnit.Unspecified),
    SP_12_16_05(12.sp, 16.sp, 0.5.sp),
    SP_14_20(14.sp, 20.sp, TextUnit.Unspecified),
    SP_14_20_0(14.sp, 20.sp, 0.sp),
    SP_16_24(16.sp, 24.sp, TextUnit.Unspecified),
    SP_20_32(20.sp, 32.sp, TextUnit.Unspecified),
    SP_20_32_0(20.sp, 32.sp, 0.sp),
    SP_24_36(24.sp, 36.sp, TextUnit.Unspecified),
    SP_32_44(32.sp, 44.sp, TextUnit.Unspecified),
    SP_40_60(40.sp, 60.sp, TextUnit.Unspecified),
}

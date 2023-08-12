package com.example.shopping_list.utils

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.shopping_list.R
import com.example.shopping_list.entity.Direcions
import com.example.shopping_list.entity.TypeText
import com.example.shopping_list.entity.Wave
import com.example.shopping_list.ui.components.SliderApp
import com.example.shopping_list.ui.theme.styleApp
import org.intellij.lang.annotations.Language
import kotlin.math.pow
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable fun colorPicker(): Long {
    val listColor: List<Wave> = createListSpectrumRGB()
    val spectrum: List<Color> = listColor.map { Color(it.color) }

    var colorIndex by remember { mutableStateOf(0)}
    var colorPosition by remember{mutableStateOf(0f)}
    var alfaPosition by remember{mutableStateOf(255f)}
    var horPosition by remember{mutableStateOf(255f)}
    var verPosition by remember{mutableStateOf(0f)}
    var colorSelected by remember { mutableStateOf(waveToRGB(listColor[0].wave, 255, 255).color)}
    val sizeShader = 280.dp
    val sizeSlider = sizeShader + 30.dp
    val heightSliderHor = 20.dp

    colorSelected = calculateColor(
        Color(
            waveToRGB(listColor[colorIndex].wave, alfaPosition.roundToInt(), 255).color)
        , horPosition, verPosition
    ).toArgb().toLong()
    Text(text = "")
    Column(  horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth())
    {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Box( modifier = Modifier
                .background(color = Color(listColor[colorIndex].color))
                .width(50.dp)
                .height(50.dp)
                .padding(horizontal = 32.dp))
            Spacer(Modifier.width(1.dp))
            Box( modifier = Modifier
                .background(color = Color(colorSelected))
                .width(50.dp)
                .height(50.dp)
                .padding(horizontal = 32.dp))
        }
        Box{
            Box( modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 18.dp)
                .fillMaxWidth()
                .height(20.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = spectrum),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.corner_default))
                ))
            SliderApp(
                modifier = Modifier.padding(top = 4.dp),
                position = colorPosition,
                step = listColor.size,
                onSelected = { colorPosition = it },
                valueRange = 0f..listColor.size.toFloat()-1,
                direction = Direcions.Top)
            colorIndex = colorPosition.roundToInt()
        }
        Box {
            Text(text = "Прозрачность", style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL))
            SliderApp(
                modifier = Modifier.padding(top = 4.dp),
                position = alfaPosition,
                step = listColor.size,
                onSelected = { alfaPosition = it },
                valueRange = 0f..255f,
                direction = Direcions.Top)
        }
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(alignment = Alignment.BottomCenter)
            ) {
                IntensityGradient(
                    color = Color(waveToRGB(listColor[colorIndex].wave, 255, 255).color),
                    ring_x = horPosition / 255,
                    ring_y = verPosition / 255,
                    modifier = Modifier.size(sizeShader)
                )
                Spacer(Modifier.height(5.dp))
                SliderApp(
                    modifier = Modifier.width(sizeSlider).height(heightSliderHor),
                    position = horPosition,
                    step = 254,
                    onSelected = { horPosition = it },
                    valueRange = 0f..255f,
                    direction = Direcions.Top
                )
            }
            SliderApp(
                modifier = Modifier.width(sizeSlider).height(heightSliderHor)
                    .graphicsLayer {
                        rotationZ = 90f
                        this.translationX = 190.dp.toPx()
                        this.translationY = 130.dp.toPx() },
                position = verPosition,
                step = 254,
                onSelected = { verPosition = it },
                valueRange = 0f..255f,
                direction = Direcions.Bottom
            )
        }
    }
    return colorSelected
}
@Composable
fun calculateColor(color: Color, x: Float, y: Float)=
    Color(red = (color.red + (1f - color.red) * (1f - x / 255)) *  (1f - y / 255),
                green = (color.green+ (1f - color.green) * (1f - x / 255)) *  (1f - y / 255),
                blue = (color.blue+ (1f - color.blue) * (1f - x / 255)) *  (1f - y / 255),
                alpha = 1f)
const val LEN_MIN = 380
const val LEN_MAX = 780
const val LEN_STEP = 2

fun createListSpectrumRGB(): List<Wave>{
    val listColor = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB(wave, 255, 255))
    }
    return listColor
}

fun waveToRGB(length: Int, alpha: Int, intensity: Int): Wave{

    val gamma = 0.9

    val factor = if((length >= 380) && (length<420)) 0.3 + 0.7*(length - 380) / (420 - 380)
        else if((length >= 420) && (length<701)) 1.0
        else if((length >= 701) && (length<781)) 0.3 + 0.7*(780 - length) / (780 - 700)
        else 0.0

    val waveObj = Wave(
        wave = length,
        factor = factor,
        alpha = alpha,
        intensity = intensity,
        gamma = gamma)
    if((length >= 380) && (length<440))
        with(waveObj) {
            X = -(length - 440) / (440 - 380.0)
            Y = 0.0
            Z = 1.0
            R = if (X != 0.0) (intensity * (X * factor).pow(gamma)).roundToInt() * 65536 else 0
            G = 0
            B = (intensity * (waveObj.Z * factor).pow(gamma)).roundToInt()
            color = 16777216L * alpha + R + B
        }
    else if((length >= 440) && (length<490))
        with(waveObj) {
            X = 0.0
            Y = (length - 440) / (490 - 440.0)
            Z = 1.0
            R = 0
            G = if (Y != 0.0) (intensity * (Y * factor).pow(gamma)).roundToInt() * 256 else 0
            B = (intensity * (Z * factor).pow(gamma)).roundToInt()
            color = 16777216L * alpha + R + G + B
        }
    else if((length >= 490) && (length<510))
        with(waveObj) {
            X = 0.0
            Y = 1.0
            Z = (-(length - 510) / (510 - 490.0))
            R = 0
            G = (intensity * (Y * factor).pow(gamma)).roundToInt() * 256
            B = if (Z != 0.0) (intensity * (Z * factor).pow(gamma)).roundToInt() else 0
            color = 16777216L * alpha + R + G + B
        }
    else if((length >= 510) && (length<580))
        with(waveObj) {
            X = (length - 510) / (580 - 510.0)
            Y = 1.0
            Z = 0.0
            R = if (X != 0.0) (intensity * (X * factor).pow(gamma)).roundToInt() * 65536 else 0
            G = (intensity * (Y * factor).pow(gamma)).roundToInt() * 256
            B = 0
            color = 16777216L * alpha + R + G
        }
    else if((length >= 580) && (length<645))
        with(waveObj) {
            X = 1.0
            Y = -(length - 645) / (645 - 580.0)
            Z = 0.0
            R = (intensity * (X * factor).pow(gamma)).roundToInt() * 65536
            G = if (Y != 0.0) (intensity * (Y * factor).pow(gamma)).roundToInt() * 256 else 0
            B = 0
            color = 16777216L * alpha + R + G
        }
    else if((length >= 645) && (length<781))
        with(waveObj) {
            X = 1.0
            Y = 0.0
            Z = 0.0
            R = (intensity * (X * factor).pow(gamma)).roundToInt() * 65536
            G = 0
            B = 0
            color = 16777216L * alpha + R
        }
    else with(waveObj) {
            X = 0.0
            Y = 0.0
            Z = 0.0
            R = 0
            G = 0
            B = 0
            color = 16777216L * alpha
        }
    return waveObj
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun IntensityGradient(color: Color, ring_x: Float, ring_y: Float, modifier: Modifier) {
    @Language("AGSL")
    val myShader = """
    uniform float2 resolution;
    uniform float2 ring_xy;

    layout(color) uniform half4 color;
    float ring ( vec2 frag_xy, vec2 center, float radius){
        return 1. - smoothstep(radius - 0.005, radius + 0.005, length (frag_xy - center));
    }
    half4 main(in vec2 fragCoord) {
        vec2 frag_related = fragCoord/resolution.xy;
        vec3 bgrd = vec3(color.r, color.g, color.b);   
        bgrd = bgrd + (1.0 - bgrd) * (1.0-frag_related.x);
        bgrd = bgrd - bgrd * frag_related.y;
        vec3 grey = vec3(0.3);
        vec3 white = vec3(1.);
        float radius = 0.02;
        vec2 center_uv = vec2(ring_xy.xy);
        vec3 res1 = mix(bgrd , white, ring(frag_related, center_uv, radius));
        vec3 res2 = mix(res1 , grey, ring(frag_related, center_uv, radius - 0.004));
        return vec4(res2,1.0);
    }
    """.trimIndent()

    Box(
        modifier = modifier
            .drawWithCache {
                val shader = RuntimeShader(myShader)
                val shaderBrush = ShaderBrush(shader)
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setFloatUniform("ring_xy", ring_x, ring_y)
                shader.setColorUniform("color", android.graphics.Color.valueOf(color.red, color.green,color.blue, color.alpha))
                onDrawBehind { drawRect(shaderBrush) }
            }
    )
}

//##################################################################################################################

//val Xl = arrayListOf(
//    0.000160, 0.000662, 0.002362, 0.007242, 0.019110, 0.043400, 0.084736, 0.140638, 0.204492, 0.264737,
//    0.314679, 0.357719, 0.383734, 0.386726, 0.370702, 0.342957, 0.302273, 0.254085, 0.195618, 0.132349,
//    0.080507, 0.041072, 0.016172, 0.005132, 0.003816, 0.015444, 0.037465, 0.071358, 0.117749, 0.172953,
//    0.236491, 0.304213, 0.376772, 0.451584, 0.529826, 0.616053, 0.705224, 0.793832, 0.878655, 0.951162,
//    1.014160, 1.074300, 1.118520, 1.134300, 1.123990, 1.089100, 1.030480, 0.950740, 0.856297, 0.754930,
//    0.647467, 0.535110, 0.431567, 0.343690, 0.268329, 0.204300, 0.152568, 0.112210, 0.081261, 0.057930,
//    0.040851, 0.028623, 0.019941, 0.013842, 0.009577, 0.006605, 0.004553, 0.003145, 0.002175, 0.001506,
//    0.001045, 0.000727, 0.000508, 0.000356, 0.000251, 0.000178, 0.000126, 0.000090, 0.000065, 0.000046,
//    0.000033
//)
//val Yl = arrayListOf(
//    0.000017, 0.000072, 0.000253, 0.000769, 0.002004, 0.004509, 0.008756, 0.014456, 0.021391, 0.029497,
//    0.038676, 0.049602, 0.062077, 0.074704, 0.089456, 0.106256, 0.128201, 0.152761, 0.185190, 0.219940,
//    0.253589, 0.297665, 0.339133, 0.395379, 0.460777, 0.531360, 0.606741, 0.685660, 0.761757, 0.823330,
//    0.875211, 0.923810, 0.961988, 0.982200, 0.991761, 0.999110, 0.997340, 0.982380, 0.955552, 0.915175,
//    0.868934, 0.825623, 0.777405, 0.720353, 0.658341, 0.593878, 0.527963, 0.461834, 0.398057, 0.339554,
//    0.283493, 0.228254, 0.179828, 0.140211, 0.107633, 0.081187, 0.060281, 0.044096, 0.031800, 0.022602,
//    0.015905, 0.011130, 0.007749, 0.005375, 0.003718, 0.002565, 0.001768, 0.001222, 0.000846, 0.000586,
//    0.000407, 0.000284, 0.000199, 0.000140, 0.000098, 0.000070, 0.000050, 0.000036, 0.000025, 0.000018,
//    0.000013
//)
//val Zl = arrayListOf(
//    0.000705, 0.002928, 0.010482, 0.032344, 0.086011, 0.197120, 0.389366, 0.656760, 0.972542, 1.282500,
//    1.553480, 1.798500, 1.967280, 2.027300, 1.994800, 1.900700, 1.745370, 1.554900, 1.317560, 1.030200,
//    0.772125, 0.570060, 0.415254, 0.302356, 0.218502, 0.159249, 0.112044, 0.082248, 0.060709, 0.043050,
//    0.030451, 0.020584, 0.013676, 0.007918, 0.003988, 0.001091, 0.000000, 0.000000, 0.000000, 0.000000,
//    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
//    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
//    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
//    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
//    0.000000
//)
//val SRGB_D65 = arrayListOf(
//    3.2404542, -1.5371385, -0.4985314,
//    -0.9692660, 1.8760108, 0.0415560,
//    0.0556434, -0.2040259, 1.0572252
//)
//
//fun createListSpectrumRGB1(): List<Wave>{
//    val listColor = mutableListOf<Wave>()
//    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
//        listColor.add(waveToRGB1(wave))
//    }
//    return listColor
//}
//
//fun waveToRGB1(wavelength: Int, alpha: Int = 255, intensity: Int = 255): Wave {
//
//    var index = ((wavelength - LEN_MIN) / LEN_STEP)
//    if (index >= 79) { index = 79 }
//    val offset = wavelength - LEN_STEP * index
//    val wave = Wave(wave = wavelength, alpha = 255, intensity = 255)
//
//    with(wave){
//        X = interpolate(Xl, index, offset)
//        Y = interpolate(Yl, index, offset)
//        Z = interpolate(Zl, index, offset)
//        R = (correctSRGB(SRGB_D65[0] * X + SRGB_D65[1] * Y + SRGB_D65[2] * Z) * intensity * 65536).roundToInt()
//        G = (correctSRGB(SRGB_D65[3] * X + SRGB_D65[4] * Y + SRGB_D65[5] * Z) * intensity * 256).roundToInt()
//        B = (correctSRGB(SRGB_D65[6] * X + SRGB_D65[7] * Y + SRGB_D65[8] * Z) * intensity).roundToInt()
//        color = (16777216 * alpha).toLong() + R + G + B
//    }
//    return wave
//}
//
//fun interpolate(values: List<Double>, index: Int, offset: Int): Double{
//    return if (offset > 0) {
//        values[index] + offset * ((values[1 + index] - values[index])/(index * LEN_STEP + LEN_STEP - index * LEN_STEP))
//    } else values[index]
//}
//
//fun correctSRGB(c: Double): Double {
//    return if (c < 0.0) 0.0
//            else if (c > 1.0) 1.0
//            else if (c <= 0.0031308) 12.92 * c
//            else 1.055 * c.pow(1 / 2.4) - 0.055
//}

//##################################################################################################################
//fun createListSpectrumRGB2(): List<Wave>{
//    val listColor = mutableListOf<Wave>()
//    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
//        listColor.add(waveToRGB2(wave))
//    }
//    return listColor
//}
//@SuppressLint("SuspiciousIndentation")
//fun waveToRGB2(length: Int, alfaPosition: Int = 255, intensityPosition: Int = 255): Wave{
//
//    val waveObj = Wave(wave = length, alpha = alfaPosition, intensity = intensityPosition)
//
//    with(waveObj){
//        X = 0.362 * exp(-0.5 * ((length - 442.0) * (if (length < 442.0) 0.0624 else 0.0374)).pow(2))
//           + 1.056 * exp(-0.5 * ((length - 599.8) * (if (length < 599.8) 0.0264 else 0.0323)).pow(2))
//           - 0.065 * exp(-0.5 * ((length - 501.1) * (if (length < 501.1) 0.0490 else 0.0382)).pow(2))
//        Y = 0.821 * exp(-0.5 * ((length - 568.8) * (if (length < 568.8) 0.0213 else 0.0247)).pow(2))
//           + 0.286 * exp(-0.5 * ((length - 530.9) * (if (length < 530.9) 0.0613 else 0.0322)).pow(2))
//        Z = 1.217 * exp(-0.5 * ((length - 437.0) * (if (length < 437.0) 0.0845 else 0.0278)).pow(2))
//            + 0.681 * exp(-0.5 * ( (length - 459.0) * (if(length < 459.0) 0.0385 else 0.0725)).pow(2))
//        R = (correctSRGB(SRGB_D65[0] * X + SRGB_D65[1] * Y + SRGB_D65[2] * Z) * 255 * 65536).roundToInt()
//        G = (correctSRGB(SRGB_D65[3] * X + SRGB_D65[4] * Y + SRGB_D65[5] * Z) * 255 * 256).roundToInt()
//        B = (correctSRGB(SRGB_D65[6] * X + SRGB_D65[7] * Y + SRGB_D65[8] * Z) * 255).roundToInt()
//        color = (16777216 * alpha).toLong() + R + G + B
//    }
//
//    return waveObj;
//}


/*
@Composable fun ColorPicker1(){

    val colors = mutableListOf<Int>()
    for (i in 0..16777215 step 10000) colors.add(i)

    Box(modifier = Modifier
        .height(320.dp)
        .fillMaxWidth()
        .background(color = Color.LightGray)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 32.dp),
            modifier = Modifier,
            state = rememberLazyGridState(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(0.dp),
        )
        {
            items(colors.size)
            { index ->
                val item = colors[index].toInt()
                Box(modifier = Modifier
                    .background(shape = CircleShape, color = Color(4278190080 + item))
                    .size(32.dp))
            }
        }
    }
}

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
General idea:
Use CEI color matching functions to convert wavelength to XYZ color.
Convert XYZ to RGB
Clip components to [0..1] and multiply by 255 to fit in the unsigned byte range.
Steps 1 and 2 may vary.

There are several color matching functions, available as tables or as analytic approximations
(suggested by @Tarc and @Haochen Xie). Tables are best if you need a smooth preсise result.

There is no single RGB color space. Multiple transformation matrices and different kinds
of gamma correction may be used.

Below is the C# code I came up with recently. It uses linear interpolation over the "CIE 1964
standard observer" table and sRGB matrix + gamma correction.

static class RgbCalculator {

    const int
         LEN_MIN = 380,
         LEN_MAX = 780,
         LEN_STEP = 5;

    static readonly double[]
        X = {
                0.000160, 0.000662, 0.002362, 0.007242, 0.019110, 0.043400, 0.084736, 0.140638, 0.204492, 0.264737,
                0.314679, 0.357719, 0.383734, 0.386726, 0.370702, 0.342957, 0.302273, 0.254085, 0.195618, 0.132349,
                0.080507, 0.041072, 0.016172, 0.005132, 0.003816, 0.015444, 0.037465, 0.071358, 0.117749, 0.172953,
                0.236491, 0.304213, 0.376772, 0.451584, 0.529826, 0.616053, 0.705224, 0.793832, 0.878655, 0.951162,
                1.014160, 1.074300, 1.118520, 1.134300, 1.123990, 1.089100, 1.030480, 0.950740, 0.856297, 0.754930,
                0.647467, 0.535110, 0.431567, 0.343690, 0.268329, 0.204300, 0.152568, 0.112210, 0.081261, 0.057930,
                0.040851, 0.028623, 0.019941, 0.013842, 0.009577, 0.006605, 0.004553, 0.003145, 0.002175, 0.001506,
                0.001045, 0.000727, 0.000508, 0.000356, 0.000251, 0.000178, 0.000126, 0.000090, 0.000065, 0.000046,
                0.000033
            },

        Y = {
                0.000017, 0.000072, 0.000253, 0.000769, 0.002004, 0.004509, 0.008756, 0.014456, 0.021391, 0.029497,
                0.038676, 0.049602, 0.062077, 0.074704, 0.089456, 0.106256, 0.128201, 0.152761, 0.185190, 0.219940,
                0.253589, 0.297665, 0.339133, 0.395379, 0.460777, 0.531360, 0.606741, 0.685660, 0.761757, 0.823330,
                0.875211, 0.923810, 0.961988, 0.982200, 0.991761, 0.999110, 0.997340, 0.982380, 0.955552, 0.915175,
                0.868934, 0.825623, 0.777405, 0.720353, 0.658341, 0.593878, 0.527963, 0.461834, 0.398057, 0.339554,
                0.283493, 0.228254, 0.179828, 0.140211, 0.107633, 0.081187, 0.060281, 0.044096, 0.031800, 0.022602,
                0.015905, 0.011130, 0.007749, 0.005375, 0.003718, 0.002565, 0.001768, 0.001222, 0.000846, 0.000586,
                0.000407, 0.000284, 0.000199, 0.000140, 0.000098, 0.000070, 0.000050, 0.000036, 0.000025, 0.000018,
                0.000013
            },

        Z = {
                0.000705, 0.002928, 0.010482, 0.032344, 0.086011, 0.197120, 0.389366, 0.656760, 0.972542, 1.282500,
                1.553480, 1.798500, 1.967280, 2.027300, 1.994800, 1.900700, 1.745370, 1.554900, 1.317560, 1.030200,
                0.772125, 0.570060, 0.415254, 0.302356, 0.218502, 0.159249, 0.112044, 0.082248, 0.060709, 0.043050,
                0.030451, 0.020584, 0.013676, 0.007918, 0.003988, 0.001091, 0.000000, 0.000000, 0.000000, 0.000000,
                0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
                0.000000
            };

    static readonly double[]
        MATRIX_SRGB_D65 = {
             3.2404542, -1.5371385, -0.4985314,
            -0.9692660,  1.8760108,  0.0415560,
             0.0556434, -0.2040259,  1.0572252
        };

    public static byte[] Calc(double len) {
        if(len < LEN_MIN || len > LEN_MAX)
            return new byte[3];

        len -= LEN_MIN;
        var index = (int)Math.Floor(len / LEN_STEP);
        var offset = len - LEN_STEP * index;

        var x = Interpolate(X, index, offset);
        var y = Interpolate(Y, index, offset);
        var z = Interpolate(Z, index, offset);

        var m = MATRIX_SRGB_D65;

        var r = m[0] * x + m[1] * y + m[2] * z;
        var g = m[3] * x + m[4] * y + m[5] * z;
        var b = m[6] * x + m[7] * y + m[8] * z;

        r = Clip(GammaCorrect_sRGB(r));
        g = Clip(GammaCorrect_sRGB(g));
        b = Clip(GammaCorrect_sRGB(b));

        return new[] {
            (byte)(255 * r),
            (byte)(255 * g),
            (byte)(255 * b)
        };
    }

    static double Interpolate(double[] values, int index, double offset) {
        if(offset == 0)
            return values[index];

        var x0 = index * LEN_STEP;
        var x1 = x0 + LEN_STEP;
        var y0 = values[index];
        var y1 = values[1 + index];

        return y0 + offset * (y1 - y0) / (x1 - x0);
    }

    static double GammaCorrect_sRGB(double c) {
        if(c <= 0.0031308)
            return 12.92 * c;

        var a = 0.055;
        return (1 + a) * Math.Pow(c, 1 / 2.4) - a;
    }

    static double Clip(double c) {
        if(c < 0)
            return 0;
        if(c > 1)
            return 1;
        return c;
    }
}
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
/**
 * Convert a wavelength in the visible light spectrum to a RGB color value that is suitable to be displayed on a
 * monitor
 *
 * @param wavelength wavelength in nm
 * @return RGB color encoded in int. each color is represented with 8 bits and has a layout of
 * 00000000RRRRRRRRGGGGGGGGBBBBBBBB where MSB is at the leftmost
 */
public static int wavelengthToRGB(double wavelength){
    double[] xyz = cie1931WavelengthToXYZFit(wavelength);
    double[] rgb = srgbXYZ2RGB(xyz);

    int c = 0;
    c |= (((int) (rgb[0] * 0xFF)) & 0xFF) << 16;
    c |= (((int) (rgb[1] * 0xFF)) & 0xFF) << 8;
    c |= (((int) (rgb[2] * 0xFF)) & 0xFF) << 0;

    return c;
}

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
*/

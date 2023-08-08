package com.example.shopping_list.utils

import android.annotation.SuppressLint
import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopping_list.entity.TypeText
import com.example.shopping_list.entity.Wave
import com.example.shopping_list.ui.theme.styleApp
import org.intellij.lang.annotations.Language
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable fun colorPicker(): Long {
    val listColor: List<Wave> = createListSpectrumRGB()
    val spectrum: List<Color> = listColor.map { Color(it.color) }

    var colorIndex by remember { mutableStateOf(0)}
    var colorPosition by remember{mutableStateOf(0f)}
    var alfaPosition by remember{mutableStateOf(255f)}
    var intensityPosition by remember{mutableStateOf(255f)}
    var colorSelected by remember { mutableStateOf(waveToRGB(listColor[0].wave, 255, 255).color)}

    Text(text = "")
    Column( modifier = Modifier.fillMaxWidth()) {
        ShaderBrushExample(Color(waveToRGB(listColor[colorIndex].wave,255,255).color))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Box( modifier = Modifier
                .background(color = Color(listColor[colorIndex].color))
                .width(50.dp)
                .height(50.dp)
                .padding(horizontal = 32.dp))
            colorSelected = waveToRGB(listColor[colorIndex].wave, alfaPosition.roundToInt(), intensityPosition.roundToInt()).color
            Box( modifier = Modifier
                .background(color = Color(colorSelected))
                .width(50.dp)
                .height(50.dp)
                .padding(horizontal = 32.dp))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box {
            Text(text = "Цвет", style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL))
            Box( modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 18.dp)
                .fillMaxWidth()
                .height(20.dp)
                .background(brush = Brush.horizontalGradient(colors = spectrum)))
            Slider(
                modifier = Modifier.padding(top = 4.dp),
                value = colorPosition,
                valueRange = 0f..listColor.size.toFloat()-1,
                steps = listColor.size,
                onValueChange = {
                    colorPosition = it
                    colorIndex = it.roundToInt() },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFA2A2A2),
                    activeTrackColor = Color(0xFFA2A2A2),
                    inactiveTrackColor = Color(0xFFA2A2A2),
                    inactiveTickColor = Color(0xFFA2A2A2),
                    activeTickColor = Color(0xFFA2A2A2)
                ))
        }



        Box{
            Text(text = "Интенсивность", style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL))
            Slider(
                value = intensityPosition,
                valueRange = 0f..255f,
                steps = listColor.size,
                onValueChange = {
                    intensityPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF575757),
                    activeTrackColor = Color(0xFFA2A2A2),
                    inactiveTrackColor = Color(0xFFFFFFFF),
                    inactiveTickColor = Color(0xFFA2A2A2),
                    activeTickColor = Color(0xFF575757)
                ))
        }
        Box {
            Text(text = "Прозрачность", style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL))
            Slider(
                value = alfaPosition,
                valueRange = 0f..255f,
                steps = listColor.size,
                onValueChange = {
                    alfaPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF575757),
                    activeTrackColor = Color(0xFFA2A2A2),
                    inactiveTrackColor = Color(0xFFA2A2A2),
                    inactiveTickColor = Color(0xFFA2A2A2),
                    activeTickColor = Color(0xFF575757)
                ))
        }
    }
    return colorSelected
}
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
fun ShaderBrushExample(color: Color) {
    @Language("AGSL")
    val CUSTOM_SHADER = """
    uniform float2 resolution;
    layout(color) uniform half4 color;

    half4 main(in vec2 fragCoord) {
        vec2 xy = fragCoord/resolution.xy;
        vec3 cl = vec3(color.r, color.g, color.b);   
        cl = cl + (1.0 - cl) * (1.0-xy.x);
        cl = cl - cl * xy.y;
        return vec4(cl,1.0);
    }
    """.trimIndent()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .drawWithCache {
                val shader = RuntimeShader(CUSTOM_SHADER)
                val shaderBrush = ShaderBrush(shader)
                shader.setFloatUniform("resolution", size.width, size.height)
                shader.setColorUniform("color", android.graphics.Color.valueOf(color.red, color.green,color.blue, color.alpha))
                onDrawBehind { drawRect(shaderBrush) }
            }
    )
}
/*

#ifdef GL_ES
precision highp float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform sampler2D backbuffer;

void main( void ) {
	vec2 position = ( gl_FragCoord.xy / resolution.xy );
	vec2 pixel = 1./resolution;
	vec4 me = texture2D(backbuffer, position);

	vec2 rnd = vec2(mod(fract(sin(dot(position + time * 0.001, vec2(14.9898,78.233))) * 43758.5453), 1.0),
	                mod(fract(sin(dot(position + time * 0.001, vec2(24.9898,44.233))) * 27458.5453), 1.0));
	vec2 nudge = vec2(12.0 + 10.0 * cos(time * 0.03775),
	                  12.0 + 10.0 * cos(time * 0.02246));
	vec2 rate = -0.005 + 0.02 * (0.5 + 0.5 * cos(nudge * (position.yx - 0.5) + 0.5 + time * vec2(0.137, 0.262)));

	float mradius = 0.007;//0.07 * (-0.03 + length(zoomcenter - mouse));
	if (length(position-mouse) < mradius) {
		me.r = 0.5+0.5*sin(time * 1.234542);
		me.g = 0.5+0.5*sin(3.0 + time * 1.64242);
		me.b = 0.5+0.5*sin(4.0 + time * 1.444242);
	} else {
		rate *= 6.0 * abs(vec2(0.5, 0.5) - mouse);
		rate += 0.5 * rate.yx;
		vec2 mult = 1.0 - rate;
		vec2 jitter = vec2(1.1 / resolution.x,
		                   1.1 / resolution.y);
		vec2 offset = (rate * mouse) - (jitter * 0.5);
		vec4 source = texture2D(backbuffer, position * mult + offset + jitter * rnd);

		me = me * 0.05 + source * 0.95;
	}
	gl_FragColor = me;
}

 */





//##################################################################################################################

val Xl = arrayListOf(
    0.000160, 0.000662, 0.002362, 0.007242, 0.019110, 0.043400, 0.084736, 0.140638, 0.204492, 0.264737,
    0.314679, 0.357719, 0.383734, 0.386726, 0.370702, 0.342957, 0.302273, 0.254085, 0.195618, 0.132349,
    0.080507, 0.041072, 0.016172, 0.005132, 0.003816, 0.015444, 0.037465, 0.071358, 0.117749, 0.172953,
    0.236491, 0.304213, 0.376772, 0.451584, 0.529826, 0.616053, 0.705224, 0.793832, 0.878655, 0.951162,
    1.014160, 1.074300, 1.118520, 1.134300, 1.123990, 1.089100, 1.030480, 0.950740, 0.856297, 0.754930,
    0.647467, 0.535110, 0.431567, 0.343690, 0.268329, 0.204300, 0.152568, 0.112210, 0.081261, 0.057930,
    0.040851, 0.028623, 0.019941, 0.013842, 0.009577, 0.006605, 0.004553, 0.003145, 0.002175, 0.001506,
    0.001045, 0.000727, 0.000508, 0.000356, 0.000251, 0.000178, 0.000126, 0.000090, 0.000065, 0.000046,
    0.000033
)
val Yl = arrayListOf(
    0.000017, 0.000072, 0.000253, 0.000769, 0.002004, 0.004509, 0.008756, 0.014456, 0.021391, 0.029497,
    0.038676, 0.049602, 0.062077, 0.074704, 0.089456, 0.106256, 0.128201, 0.152761, 0.185190, 0.219940,
    0.253589, 0.297665, 0.339133, 0.395379, 0.460777, 0.531360, 0.606741, 0.685660, 0.761757, 0.823330,
    0.875211, 0.923810, 0.961988, 0.982200, 0.991761, 0.999110, 0.997340, 0.982380, 0.955552, 0.915175,
    0.868934, 0.825623, 0.777405, 0.720353, 0.658341, 0.593878, 0.527963, 0.461834, 0.398057, 0.339554,
    0.283493, 0.228254, 0.179828, 0.140211, 0.107633, 0.081187, 0.060281, 0.044096, 0.031800, 0.022602,
    0.015905, 0.011130, 0.007749, 0.005375, 0.003718, 0.002565, 0.001768, 0.001222, 0.000846, 0.000586,
    0.000407, 0.000284, 0.000199, 0.000140, 0.000098, 0.000070, 0.000050, 0.000036, 0.000025, 0.000018,
    0.000013
)
val Zl = arrayListOf(
    0.000705, 0.002928, 0.010482, 0.032344, 0.086011, 0.197120, 0.389366, 0.656760, 0.972542, 1.282500,
    1.553480, 1.798500, 1.967280, 2.027300, 1.994800, 1.900700, 1.745370, 1.554900, 1.317560, 1.030200,
    0.772125, 0.570060, 0.415254, 0.302356, 0.218502, 0.159249, 0.112044, 0.082248, 0.060709, 0.043050,
    0.030451, 0.020584, 0.013676, 0.007918, 0.003988, 0.001091, 0.000000, 0.000000, 0.000000, 0.000000,
    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
    0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000, 0.000000,
    0.000000
)
val SRGB_D65 = arrayListOf(
    3.2404542, -1.5371385, -0.4985314,
    -0.9692660, 1.8760108, 0.0415560,
    0.0556434, -0.2040259, 1.0572252
)

fun createListSpectrumRGB1(): List<Wave>{
    val listColor = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB1(wave))
    }
    return listColor
}

fun waveToRGB1(wavelength: Int, alpha: Int = 255, intensity: Int = 255): Wave {

    var index = ((wavelength - LEN_MIN) / LEN_STEP)
    if (index >= 79) { index = 79 }
    val offset = wavelength - LEN_STEP * index
    val wave = Wave(wave = wavelength, alpha = 255, intensity = 255)

    with(wave){
        X = interpolate(Xl, index, offset)
        Y = interpolate(Yl, index, offset)
        Z = interpolate(Zl, index, offset)
        R = (correctSRGB(SRGB_D65[0] * X + SRGB_D65[1] * Y + SRGB_D65[2] * Z) * intensity * 65536).roundToInt()
        G = (correctSRGB(SRGB_D65[3] * X + SRGB_D65[4] * Y + SRGB_D65[5] * Z) * intensity * 256).roundToInt()
        B = (correctSRGB(SRGB_D65[6] * X + SRGB_D65[7] * Y + SRGB_D65[8] * Z) * intensity).roundToInt()
        color = (16777216 * alpha).toLong() + R + G + B
    }
    return wave
}

fun interpolate(values: List<Double>, index: Int, offset: Int): Double{
    return if (offset > 0) {
        values[index] + offset * ((values[1 + index] - values[index])/(index * LEN_STEP + LEN_STEP - index * LEN_STEP))
    } else values[index]
}

fun correctSRGB(c: Double): Double {
    return if (c < 0.0) 0.0
            else if (c > 1.0) 1.0
            else if (c <= 0.0031308) 12.92 * c
            else 1.055 * c.pow(1 / 2.4) - 0.055
//    return if (c.absoluteValue <= 0.0031308) 12.92 * c else 1.055 * c.pow(1 / 2.4) - 0.055
}

//##################################################################################################################
fun createListSpectrumRGB2(): List<Wave>{
    val listColor = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB2(wave))
    }
    return listColor
}
@SuppressLint("SuspiciousIndentation")
fun waveToRGB2(length: Int, alfaPosition: Int = 255, intensityPosition: Int = 255): Wave{
    
    val waveObj = Wave(wave = length, alpha = 255, intensity = 255)

    with(waveObj){
        X = 0.362 * exp(-0.5 * ((length - 442.0) * (if (length < 442.0) 0.0624 else 0.0374)).pow(2))
           + 1.056 * exp(-0.5 * ((length - 599.8) * (if (length < 599.8) 0.0264 else 0.0323)).pow(2))
           - 0.065 * exp(-0.5 * ((length - 501.1) * (if (length < 501.1) 0.0490 else 0.0382)).pow(2))
        Y = 0.821 * exp(-0.5 * ((length - 568.8) * (if (length < 568.8) 0.0213 else 0.0247)).pow(2))
           + 0.286 * exp(-0.5 * ((length - 530.9) * (if (length < 530.9) 0.0613 else 0.0322)).pow(2))
        Z = 1.217 * exp(-0.5 * ((length - 437.0) * (if (length < 437.0) 0.0845 else 0.0278)).pow(2))
            + 0.681 * exp(-0.5 * ( (length - 459.0) * (if(length < 459.0) 0.0385 else 0.0725)).pow(2))
        R = (correctSRGB(SRGB_D65[0] * X + SRGB_D65[1] * Y + SRGB_D65[2] * Z) * 255 * 65536).roundToInt()
        G = (correctSRGB(SRGB_D65[3] * X + SRGB_D65[4] * Y + SRGB_D65[5] * Z) * 255 * 256).roundToInt()
        B = (correctSRGB(SRGB_D65[6] * X + SRGB_D65[7] * Y + SRGB_D65[8] * Z) * 255).roundToInt()
        color = (16777216 * alpha).toLong() + R + G + B
    }
    
    return waveObj;
}


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

Если интересует именно "как бы максимум" - перейти от RGB (sRGB?) к xy (не путать с XYZ!)
и найти на спектральной кривой точку, лежащую на одной прямой с заданной и белой точками.
Далее - тривиально (частоты точек на спектральной кривой известны).
Напомню, что переход на плоскость (x,y) путём избавления от яркости делается так:
x=X/(X+Y+Z);
y=Y/(X+Y+Z);

Вот, выкопал в загашнике достаточно точную аппроксимацию спектральной кривой (в пространстве XYZ):
Дл.волны (нм)        X      Y      Z
    380          0.1741 0.0050 0.8209
    385          0.1740 0.0050 0.8210
    390          0.1738 0.0049 0.8213
    395          0.1736 0.0049 0.8215
    400          0.1733 0.0048 0.8219
    405          0.1730 0.0048 0.8222
    410          0.1726 0.0048 0.8226
    415          0.1721 0.0048 0.8231
    420          0.1714 0.0051 0.8235
    425          0.1703 0.0058 0.8239
    430          0.1689 0.0069 0.8242
    435          0.1669 0.0086 0.8245
    440          0.1644 0.0109 0.8247
    445          0.1611 0.0138 0.8251
    450          0.1566 0.0177 0.8257
    455          0.1510 0.0227 0.8263
    460          0.1440 0.0297 0.8263
    465          0.1355 0.0399 0.8246
    470          0.1241 0.0578 0.8181
    475          0.1096 0.0868 0.8036
    480          0.0913 0.1327 0.7760
    485          0.0687 0.2007 0.7306
    490          0.0454 0.2950 0.6596
    495          0.0235 0.4127 0.5638
    500          0.0082 0.5384 0.4534
    505          0.0039 0.6548 0.3413
    510          0.0139 0.7502 0.2359
    515          0.0389 0.8120 0.1491
    520          0.0743 0.8338 0.0919
    525          0.1142 0.8262 0.0596
    530          0.1547 0.8059 0.0394
    535          0.1929 0.7816 0.0255
    540          0.2296 0.7543 0.0161
    545          0.2658 0.7243 0.0099
    550          0.3016 0.6923 0.0061
    555          0.3373 0.6589 0.0038
    560          0.3731 0.6245 0.0024
    565          0.4087 0.5896 0.0017
    570          0.4441 0.5547 0.0012
    575          0.4788 0.5202 0.0010
    580          0.5125 0.4866 0.0009
    585          0.5448 0.4544 0.0008
    590          0.5752 0.4242 0.0006
    595          0.6029 0.3965 0.0006
    600          0.6270 0.3725 0.0005
    605          0.6482 0.3514 0.0004
    610          0.6658 0.3340 0.0002
    615          0.6801 0.3197 0.0002
    620          0.6915 0.3083 0.0002
    625          0.7006 0.2993 0.0001
    630          0.7079 0.2920 0.0001
    635          0.7140 0.2859 0.0001
    640          0.7190 0.2809 0.0001
    645          0.7230 0.2770 0.0000
    650          0.7260 0.2740 0.0000
    655          0.7283 0.2717 0.0000
    660          0.7300 0.2700 0.0000
    665          0.7311 0.2689 0.0000
    670          0.7320 0.2680 0.0000
    675          0.7327 0.2673 0.0000
    680          0.7334 0.2666 0.0000
    685          0.7340 0.2660 0.0000
    690          0.7344 0.2656 0.0000
    695          0.7346 0.2654 0.0000
    700          0.7347 0.2653 0.0000
    705          0.7347 0.2653 0.0000
    710          0.7347 0.2653 0.0000
    715          0.7347 0.2653 0.0000
    720          0.7347 0.2653 0.0000
    725          0.7347 0.2653 0.0000
    730          0.7347 0.2653 0.0000
    735          0.7347 0.2653 0.0000
    740          0.7347 0.2653 0.0000
    745          0.7347 0.2653 0.0000
    750          0.7347 0.2653 0.0000
    755          0.7347 0.2653 0.0000
    760          0.7347 0.2653 0.0000
    765          0.7347 0.2653 0.0000
    770          0.7347 0.2653 0.0000
    775          0.7347 0.2653 0.0000
    780          0.7347 0.2653 0.0000
Напомню, что переход на плоскость (x,y) путём избавления от яркости делается так:
x=X/(X+Y+Z);
y=Y/(X+Y+Z);
Дл.волны (нм) 	X 	Y 	Z	  x	       y
380	0,1741	0,0050	0,8209	0,1741	0,0050
385 0,1740	0,0050	0,8210	0,1740	0,0050
390	0,1738	0,0049	0,8213	0,1738	0,0049
395	0,1736	0,0049	0,8215	0,1736	0,0049
400	0,1733	0,0048	0,8219	0,1733	0,0048
405	0,1730	0,0048	0,8222	0,1730	0,0048
410	0,1726	0,0048	0,8226	0,1726	0,0048
415	0,1721	0,0048	0,8231	0,1721	0,0048
420	0,1714	0,0051	0,8235	0,1714	0,0051
425	0,1703	0,0058	0,8239	0,1703	0,0058
430	0,1689	0,0069	0,8242	0,1689	0,0069
435	0,1669	0,0086	0,8245	0,1669	0,0086
440	0,1644	0,0109	0,8247	0,1644	0,0109
445	0,1611	0,0138	0,8251	0,1611	0,0138
450	0,1566	0,0177	0,8257	0,1566	0,0177
455	0,1510	0,0227	0,8263	0,1510	0,0227
460	0,1440	0,0297	0,8263	0,1440	0,0297
465	0,1355	0,0399	0,8246	0,1355	0,0399
470	0,1241	0,0578	0,8181	0,1241	0,0578
475	0,1096	0,0868	0,8036	0,1096	0,0868
480	0,0913	0,1327	0,7760	0,0913	0,1327
485	0,0687	0,2007	0,7306	0,0687	0,2007
490	0,0454	0,2950	0,6596	0,0454	0,2950
495	0,0235	0,4127	0,5638	0,0235	0,4127
500	0,0082	0,5384	0,4534	0,0082	0,5384
505	0,0039	0,6548	0,3413	0,0039	0,6548
510	0,0139	0,7502	0,2359	0,0139	0,7502
515	0,0389	0,8120	0,1491	0,0389	0,8120
520	0,0743	0,8338	0,0919	0,0743	0,8338
525	0,1142	0,8262	0,0596	0,1142	0,8262
530	0,1547	0,8059	0,0394	0,1547	0,8059
535	0,1929	0,7816	0,0255	0,1929	0,7816
540	0,2296	0,7543	0,0161	0,2296	0,7543
545	0,2658	0,7243	0,0099	0,2658	0,7243
550	0,3016	0,6923	0,0061	0,3016	0,6923
555	0,3373	0,6589	0,0038	0,3373	0,6589
560	0,3731	0,6245	0,0024	0,3731	0,6245
565	0,4087	0,5896	0,0017	0,4087	0,5896
570	0,4441	0,5547	0,0012	0,4441	0,5547
575	0,4788	0,5202	0,0010	0,4788	0,5202
580	0,5125	0,4866	0,0009	0,5125	0,4866
585	0,5448	0,4544	0,0008	0,5448	0,4544
590	0,5752	0,4242	0,0006	0,5752	0,4242
595	0,6029	0,3965	0,0006	0,6029	0,3965
600	0,6270	0,3725	0,0005	0,6270	0,3725
605	0,6482	0,3514	0,0004	0,6482	0,3514
610	0,6658	0,3340	0,0002	0,6658	0,3340
615	0,6801	0,3197	0,0002	0,6801	0,3197
620	0,6915	0,3083	0,0002	0,6915	0,3083
625	0,7006	0,2993	0,0001	0,7006	0,2993
630	0,7079	0,2920	0,0001	0,7079	0,2920
635	0,7140	0,2859	0,0001	0,7140	0,2859
640	0,7190	0,2809	0,0001	0,7190	0,2809
645	0,7230	0,2770	0,0000	0,7230	0,2770
650	0,7260	0,2740	0,0000	0,7260	0,2740
655	0,7283	0,2717	0,0000	0,7283	0,2717
660	0,7300	0,2700	0,0000	0,7300	0,2700
665	0,7311	0,2689	0,0000	0,7311	0,2689
670	0,7320	0,2680	0,0000	0,7320	0,2680
675	0,7327	0,2673	0,0000	0,7327	0,2673
680	0,7334	0,2666	0,0000	0,7334	0,2666
685	0,7340	0,2660	0,0000	0,7340	0,2660
690	0,7344	0,2656	0,0000	0,7344	0,2656
695	0,7346	0,2654	0,0000	0,7346	0,2654
700	0,7347	0,2653	0,0000	0,7347	0,2653
705	0,7347	0,2653	0,0000	0,7347	0,2653
710	0,7347	0,2653	0,0000	0,7347	0,2653
715	0,7347	0,2653	0,0000	0,7347	0,2653
720	0,7347	0,2653	0,0000	0,7347	0,2653
725	0,7347	0,2653	0,0000	0,7347	0,2653
730	0,7347	0,2653	0,0000	0,7347	0,2653
735	0,7347	0,2653	0,0000	0,7347	0,2653
740	0,7347	0,2653	0,0000	0,7347	0,2653
745	0,7347	0,2653	0,0000	0,7347	0,2653
750	0,7347	0,2653	0,0000	0,7347	0,2653
755	0,7347	0,2653	0,0000	0,7347	0,2653
760	0,7347	0,2653	0,0000	0,7347	0,2653
765	0,7347	0,2653	0,0000	0,7347	0,2653
770	0,7347	0,2653	0,0000	0,7347	0,2653
775	0,7347	0,2653	0,0000	0,7347	0,2653
780	0,7347	0,2653	0,0000	0,7347	0,2653

R =  3.2404542*X - 1.5371385*Y - 0.4985314*Z
G = -0.9692660*X + 1.8760108*Y + 0.0415560*Z
B =  0.0556434*X - 0.2040259*Y + 1.0572252*Z
Однако, если вы имели в виду пространство sRGB, то к каждому компоненту необходимо применить
дополнительное нелинейное преобразование: R=adj(R), G=adj(G) и B=adj(B). adj
Функция определяется следующим образом:
function adj(C) {
  if (Abs(C) < 0.0031308) {
    return 12.92 * C;
  }
  return 1.055 * Math.pow(C, 0.41666) - 0.055;
}

1.0144665  0.0000000  0.0000000
 0.0000000  1.0000000  0.0000000
 0.0000000  0.0000000  0.7578869

Colour matching functions
nm	X	Y	Z
390	3.769647E-03	4.146161E-04	1.847260E-02
395	9.382967E-03	1.059646E-03	4.609784E-02
400	2.214302E-02	2.452194E-03	1.096090E-01
405	4.742986E-02	4.971717E-03	2.369246E-01
410	8.953803E-02	9.079860E-03	4.508369E-01
415	1.446214E-01	1.429377E-02	7.378822E-01
420	2.035729E-01	2.027369E-02	1.051821E+00
425	2.488523E-01	2.612106E-02	1.305008E+00
430	2.918246E-01	3.319038E-02	1.552826E+00
435	3.227087E-01	4.157940E-02	1.748280E+00
440	3.482554E-01	5.033657E-02	1.917479E+00
445	3.418483E-01	5.743393E-02	1.918437E+00
450	3.224637E-01	6.472352E-02	1.848545E+00
455	2.826646E-01	7.238339E-02	1.664439E+00
460	2.485254E-01	8.514816E-02	1.522157E+00
465	2.219781E-01	1.060145E-01	1.428440E+00
470	1.806905E-01	1.298957E-01	1.250610E+00
475	1.291920E-01	1.535066E-01	9.991789E-01
480	8.182895E-02	1.788048E-01	7.552379E-01
485	4.600865E-02	2.064828E-01	5.617313E-01
490	2.083981E-02	2.379160E-01	4.099313E-01
495	7.097731E-03	2.850680E-01	3.105939E-01
500	2.461588E-03	3.483536E-01	2.376753E-01
505	3.649178E-03	4.277595E-01	1.720018E-01
510	1.556989E-02	5.204972E-01	1.176796E-01
515	4.315171E-02	6.206256E-01	8.283548E-02
520	7.962917E-02	7.180890E-01	5.650407E-02
525	1.268468E-01	7.946448E-01	3.751912E-02
530	1.818026E-01	8.575799E-01	2.438164E-02
535	2.405015E-01	9.071347E-01	1.566174E-02
540	3.098117E-01	9.544675E-01	9.846470E-03
545	3.804244E-01	9.814106E-01	6.131421E-03
550	4.494206E-01	9.890228E-01	3.790291E-03
555	5.280233E-01	9.994608E-01	2.327186E-03
560	6.133784E-01	9.967737E-01	1.432128E-03
565	7.016774E-01	9.902549E-01	8.822531E-04
570	7.967750E-01	9.732611E-01	5.452416E-04
575	8.853376E-01	9.424569E-01	3.386739E-04
580	9.638388E-01	8.963613E-01	2.117772E-04
585	1.051011E+00	8.587203E-01	1.335031E-04
590	1.109767E+00	8.115868E-01	8.494468E-05
595	1.143620E+00	7.544785E-01	5.460706E-05
600	1.151033E+00	6.918553E-01	3.549661E-05
605	1.134757E+00	6.270066E-01	2.334738E-05
610	1.083928E+00	5.583746E-01	1.554631E-05
615	1.007344E+00	4.895950E-01	1.048387E-05
620	9.142877E-01	4.229897E-01	0.000000E+00
625	8.135565E-01	3.609245E-01	0.000000E+00
630	6.924717E-01	2.980865E-01	0.000000E+00
635	5.755410E-01	2.416902E-01	0.000000E+00
640	4.731224E-01	1.943124E-01	0.000000E+00
645	3.844986E-01	1.547397E-01	0.000000E+00
650	2.997374E-01	1.193120E-01	0.000000E+00
655	2.277792E-01	8.979594E-02	0.000000E+00
660	1.707914E-01	6.671045E-02	0.000000E+00
665	1.263808E-01	4.899699E-02	0.000000E+00
670	9.224597E-02	3.559982E-02	0.000000E+00
675	6.639960E-02	2.554223E-02	0.000000E+00
680	4.710606E-02	1.807939E-02	0.000000E+00
685	3.292138E-02	1.261573E-02	0.000000E+00
690	2.262306E-02	8.661284E-03	0.000000E+00
695	1.575417E-02	6.027677E-03	0.000000E+00
700	1.096778E-02	4.195941E-03	0.000000E+00
705	7.608750E-03	2.910864E-03	0.000000E+00
710	5.214608E-03	1.995557E-03	0.000000E+00
715	3.569452E-03	1.367022E-03	0.000000E+00
720	2.464821E-03	9.447269E-04	0.000000E+00
725	1.703876E-03	6.537050E-04	0.000000E+00
730	1.186238E-03	4.555970E-04	0.000000E+00
735	8.269535E-04	3.179738E-04	0.000000E+00
740	5.758303E-04	2.217445E-04	0.000000E+00
745	4.058303E-04	1.565566E-04	0.000000E+00
750	2.856577E-04	1.103928E-04	0.000000E+00
755	2.021853E-04	7.827442E-05	0.000000E+00
760	1.438270E-04	5.578862E-05	0.000000E+00
765	1.024685E-04	3.981884E-05	0.000000E+00
770	7.347551E-05	2.860175E-05	0.000000E+00
775	5.259870E-05	2.051259E-05	0.000000E+00
780	3.806114E-05	1.487243E-05	0.000000E+00
785	2.758222E-05	1.080001E-05	0.000000E+00
790	2.004122E-05	7.863920E-06	0.000000E+00
795	1.458792E-05	5.736935E-06	0.000000E+00
800	1.068141E-05	4.211597E-06	0.000000E+00
805	7.857521E-06	3.106561E-06	0.000000E+00
810	5.768284E-06	2.286786E-06	0.000000E+00
815	4.259166E-06	1.693147E-06	0.000000E+00
820	3.167765E-06	1.262556E-06	0.000000E+00
825	2.358723E-06	9.422514E-07	0.000000E+00
830	1.762465E-06	7.053860E-07	0.000000E+00

 */

/*
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

/**
 * Convert XYZ to RGB in the sRGB color space
 * <p>
 * The conversion matrix and color component transfer function is taken from http://www.color.org/srgb.pdf, which
 * follows the International Electrotechnical Commission standard IEC 61966-2-1 "Multimedia systems and equipment -
 * Colour measurement and management - Part 2-1: Colour management - Default RGB colour space - sRGB"
 *
 * @param xyz XYZ values in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
 * @return RGB values in a double array, in the order of R, G, B. each value in the range of [0.0, 1.0]
 */
public static double[] srgbXYZ2RGB(double[] xyz) {
    double x = xyz[0];
    double y = xyz[1];
    double z = xyz[2];

    double rl =  3.2406255 * x + -1.537208  * y + -0.4986286 * z;
    double gl = -0.9689307 * x +  1.8757561 * y +  0.0415175 * z;
    double bl =  0.0557101 * x + -0.2040211 * y +  1.0569959 * z;

    return new double[] {
            srgbXYZ2RGBPostprocess(rl),
            srgbXYZ2RGBPostprocess(gl),
            srgbXYZ2RGBPostprocess(bl)
    };
}

/**
 * helper function for {@link #srgbXYZ2RGB(double[])}
 */
private static double srgbXYZ2RGBPostprocess(double c) {
    // clip if c is out of range
    c = c > 1 ? 1 : (c < 0 ? 0 : c);

    // apply the color component transfer function
    c = c <= 0.0031308 ? c * 12.92 : 1.055 * Math.pow(c, 1. / 2.4) - 0.055;

    return c;
}

/**
 * A multi-lobe, piecewise Gaussian fit of CIE 1931 XYZ Color Matching Functions by Wyman el al. from Nvidia. The
 * code here is adopted from the Listing 1 of the paper authored by Wyman et al.
 * <p>
 * Reference: Chris Wyman, Peter-Pike Sloan, and Peter Shirley, Simple Analytic Approximations to the CIE XYZ Color
 * Matching Functions, Journal of Computer Graphics Techniques (JCGT), vol. 2, no. 2, 1-11, 2013.
 *
 * @param wavelength wavelength in nm
 * @return XYZ in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
 */
public static double[] cie1931WavelengthToXYZFit(double wavelength) {
    double wave = wavelength;

    double x;
    {
        double t1 = (wave - 442.0) * ((wave < 442.0) ? 0.0624 : 0.0374);
        double t2 = (wave - 599.8) * ((wave < 599.8) ? 0.0264 : 0.0323);
        double t3 = (wave - 501.1) * ((wave < 501.1) ? 0.0490 : 0.0382);

        x =   0.362 * Math.exp(-0.5 * t1 * t1)
            + 1.056 * Math.exp(-0.5 * t2 * t2)
            - 0.065 * Math.exp(-0.5 * t3 * t3);
    }

    double y;
    {
        double t1 = (wave - 568.8) * ((wave < 568.8) ? 0.0213 : 0.0247);
        double t2 = (wave - 530.9) * ((wave < 530.9) ? 0.0613 : 0.0322);

        y =   0.821 * Math.exp(-0.5 * t1 * t1)
            + 0.286 * Math.exp(-0.5 * t2 * t2);
    }

    double z;
    {
        double t1 = (wave - 437.0) * ((wave < 437.0) ? 0.0845 : 0.0278);
        double t2 = (wave - 459.0) * ((wave < 459.0) ? 0.0385 : 0.0725);

        z =   1.217 * Math.exp(-0.5 * t1 * t1)
            + 0.681 * Math.exp(-0.5 * t2 * t2);
    }

    return new double[] { x, y, z };
}


$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
This is bit cleaned up and tested C++11 version of @haochen-xie. I also added a function that
converts value 0 to 1 to a wavelength in visible spectrum that is usable with this method.
You can just put below in one header file and use it without any dependencies.
This version will be maintained here.
#ifndef common_utils_OnlineStats_hpp
#define common_utils_OnlineStats_hpp

namespace common_utils {

class ColorUtils {
public:

    static void valToRGB(double val0To1, unsigned char& r, unsigned char& g, unsigned char& b)
    {
        //actual visible spectrum is 375 to 725 but outside of 400-700 things become too dark
        wavelengthToRGB(val0To1 * (700 - 400) + 400, r, g, b);
    }

    /**
    * Convert a wavelength in the visible light spectrum to a RGB color value that is suitable to be displayed on a
    * monitor
    *
    * @param wavelength wavelength in nm
    * @return RGB color encoded in int. each color is represented with 8 bits and has a layout of
    * 00000000RRRRRRRRGGGGGGGGBBBBBBBB where MSB is at the leftmost
    */
    static void wavelengthToRGB(double wavelength, unsigned char& r, unsigned char& g, unsigned char& b) {
        double x, y, z;
        cie1931WavelengthToXYZFit(wavelength, x, y, z);
        double dr, dg, db;
        srgbXYZ2RGB(x, y, z, dr, dg, db);

        r = static_cast<unsigned char>(static_cast<int>(dr * 0xFF) & 0xFF);
        g = static_cast<unsigned char>(static_cast<int>(dg * 0xFF) & 0xFF);
        b = static_cast<unsigned char>(static_cast<int>(db * 0xFF) & 0xFF);
    }

    /**
    * Convert XYZ to RGB in the sRGB color space
    * <p>
    * The conversion matrix and color component transfer function is taken from http://www.color.org/srgb.pdf, which
    * follows the International Electrotechnical Commission standard IEC 61966-2-1 "Multimedia systems and equipment -
    * Colour measurement and management - Part 2-1: Colour management - Default RGB colour space - sRGB"
    *
    * @param xyz XYZ values in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
    * @return RGB values in a double array, in the order of R, G, B. each value in the range of [0.0, 1.0]
    */
    static void srgbXYZ2RGB(double x, double y, double z, double& r, double& g, double& b) {
        double rl = 3.2406255 * x + -1.537208  * y + -0.4986286 * z;
        double gl = -0.9689307 * x + 1.8757561 * y + 0.0415175 * z;
        double bl = 0.0557101 * x + -0.2040211 * y + 1.0569959 * z;

        r = srgbXYZ2RGBPostprocess(rl);
        g = srgbXYZ2RGBPostprocess(gl);
        b = srgbXYZ2RGBPostprocess(bl);
    }

    /**
    * helper function for {@link #srgbXYZ2RGB(double[])}
    */
    static double srgbXYZ2RGBPostprocess(double c) {
        // clip if c is out of range
        c = c > 1 ? 1 : (c < 0 ? 0 : c);

        // apply the color component transfer function
        c = c <= 0.0031308 ? c * 12.92 : 1.055 * std::pow(c, 1. / 2.4) - 0.055;

        return c;
    }

    /**
    * A multi-lobe, piecewise Gaussian fit of CIE 1931 XYZ Color Matching Functions by Wyman el al. from Nvidia. The
    * code here is adopted from the Listing 1 of the paper authored by Wyman et al.
    * <p>
    * Reference: Chris Wyman, Peter-Pike Sloan, and Peter Shirley, Simple Analytic Approximations to the CIE XYZ Color
    * Matching Functions, Journal of Computer Graphics Techniques (JCGT), vol. 2, no. 2, 1-11, 2013.
    *
    * @param wavelength wavelength in nm
    * @return XYZ in a double array in the order of X, Y, Z. each value in the range of [0.0, 1.0]
    */
    static void cie1931WavelengthToXYZFit(double wavelength, double& x, double& y, double& z) {
        double wave = wavelength;

        {
            double t1 = (wave - 442.0) * ((wave < 442.0) ? 0.0624 : 0.0374);
            double t2 = (wave - 599.8) * ((wave < 599.8) ? 0.0264 : 0.0323);
            double t3 = (wave - 501.1) * ((wave < 501.1) ? 0.0490 : 0.0382);

            x = 0.362 * std::exp(-0.5 * t1 * t1)
                + 1.056 * std::exp(-0.5 * t2 * t2)
                - 0.065 * std::exp(-0.5 * t3 * t3);
        }

        {
            double t1 = (wave - 568.8) * ((wave < 568.8) ? 0.0213 : 0.0247);
            double t2 = (wave - 530.9) * ((wave < 530.9) ? 0.0613 : 0.0322);

            y = 0.821 * std::exp(-0.5 * t1 * t1)
                + 0.286 * std::exp(-0.5 * t2 * t2);
        }

        {
            double t1 = (wave - 437.0) * ((wave < 437.0) ? 0.0845 : 0.0278);
            double t2 = (wave - 459.0) * ((wave < 459.0) ? 0.0385 : 0.0725);

            z = 1.217 * std::exp(-0.5 * t1 * t1)
                + 0.681 * std::exp(-0.5 * t2 * t2);
        }
    }

};

} //namespace

#endif

$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
This is implemented as part of bitmap_image single file header-only library by Aeash Partow:
inline rgb_t convert_wave_length_nm_to_rgb(const double wave_length_nm)
{
   // Credits: Dan Bruton http://www.physics.sfasu.edu/astro/color.html
   double red   = 0.0;
   double green = 0.0;
   double blue  = 0.0;

   if ((380.0 <= wave_length_nm) && (wave_length_nm <= 439.0))
   {
      red   = -(wave_length_nm - 440.0) / (440.0 - 380.0);
      green = 0.0;
      blue  = 1.0;
   }
   else if ((440.0 <= wave_length_nm) && (wave_length_nm <= 489.0))
   {
      red   = 0.0;
      green = (wave_length_nm - 440.0) / (490.0 - 440.0);
      blue  = 1.0;
   }
   else if ((490.0 <= wave_length_nm) && (wave_length_nm <= 509.0))
   {
      red   = 0.0;
      green = 1.0;
      blue  = -(wave_length_nm - 510.0) / (510.0 - 490.0);
   }
   else if ((510.0 <= wave_length_nm) && (wave_length_nm <= 579.0))
   {
      red   = (wave_length_nm - 510.0) / (580.0 - 510.0);
      green = 1.0;
      blue  = 0.0;
   }
   else if ((580.0 <= wave_length_nm) && (wave_length_nm <= 644.0))
   {
      red   = 1.0;
      green = -(wave_length_nm - 645.0) / (645.0 - 580.0);
      blue  = 0.0;
   }
   else if ((645.0 <= wave_length_nm) && (wave_length_nm <= 780.0))
   {
      red   = 1.0;
      green = 0.0;
      blue  = 0.0;
   }

   double factor = 0.0;

   if ((380.0 <= wave_length_nm) && (wave_length_nm <= 419.0))
      factor = 0.3 + 0.7 * (wave_length_nm - 380.0) / (420.0 - 380.0);
   else if ((420.0 <= wave_length_nm) && (wave_length_nm <= 700.0))
      factor = 1.0;
   else if ((701.0 <= wave_length_nm) && (wave_length_nm <= 780.0))
      factor = 0.3 + 0.7 * (780.0 - wave_length_nm) / (780.0 - 700.0);
   else
      factor = 0.0;

   rgb_t result;

   const double gamma         =   0.8;
   const double intensity_max = 255.0;

   #define round(d) std::floor(d + 0.5)

   result.red   = static_cast<unsigned char>((red   == 0.0) ? red   : round(intensity_max * std::pow(red   * factor, gamma)));
   result.green = static_cast<unsigned char>((green == 0.0) ? green : round(intensity_max * std::pow(green * factor, gamma)));
   result.blue  = static_cast<unsigned char>((blue  == 0.0) ? blue  : round(intensity_max * std::pow(blue  * factor, gamma)));

   #undef round

   return result;
}
$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
Project the wavelength's CIExy towards the D65 white onto the sRGB gamut
#!/usr/bin/ghci
ångstrømsfromTHz terahertz = 2997924.58 / terahertz
tristimulusXYZfromÅngstrøms å=map(sum.map(stimulus))[
 [[1056,5998,379,310],[362,4420,160,267],[-65,5011,204,262]],
 [[821,5688,469,405],[286,5309,163,311]],
 [[1217,4370,118,360],[681,4590,260,138]]]
 where stimulus[ω,μ,ς,σ]=ω/1000*exp(-((å-μ)/if å<μ then ς else σ)^2/2)

standardRGBfromTristimulusXYZ xyz=
 map(gamma.sum.zipWith(*)(gamutConfine xyz))[
 [3.2406,-1.5372,-0.4986],[-0.9689,1.8758,0.0415],[0.0557,-0.2040,1.057]]
gamma u=if u<=0.0031308 then 12.92*u else (u**(5/12)*211-11)/200
[red,green,blue,black]=
 [[0.64,0.33],[0.3,0.6],[0.15,0.06],[0.3127,0.3290,0]]
ciexyYfromXYZ xyz=if xyz!!1==0 then black else map(/sum xyz)xyz
cieXYZfromxyY[x,y,l]=if y==0 then black else[x*l/y,l,(1-x-y)*l/y]
gamutConfine xyz=last$xyz:[cieXYZfromxyY[x0+t*(x1-x0),y0+t*(y1-y0),xyz!!1]|
 x0:y0:_<-[black],x1:y1:_<-[ciexyYfromXYZ xyz],i<-[0..2],
 [x2,y2]:[x3,y3]:_<-[drop i[red,green,blue,red]],
 det<-[(x0-x1)*(y2-y3)-(y0-y1)*(x2-x3)],
 t <-[((x0-x2)*(y2-y3)-(y0-y2)*(x2-x3))/det|det/=0],0<=t,t<=1]

sRGBfromÅ=standardRGBfromTristimulusXYZ.tristimulusXYZfromÅngstrøms
x s rgb=concat["\ESC[48;2;",
               intercalate";"$map(show.(17*).round.(15*).max 0.min 1)rgb,
               "m",s,"\ESC[49m"]
spectrum=concatMap(x" ".sRGBfromÅ)$takeWhile(<7000)$iterate(+60)4000
main=putStrLn spectrum
 */


/*

    CC0 1.0

	@vrtree
	who@tree.is
	http://tree.is


	I dont know if this is going to work, or be interesting,
	or even understandable, But hey! Why not try!

	To start, get inspired by some MAGICAL creations made by raytracing:

	Volcanic by IQ
	https://www.shadertoy.com/view/XsX3RB

	Remnant X by Dave_Hoskins ( Audio Autoplay warnings )
	https://www.shadertoy.com/view/4sjSW1

	Cloud Ten by Nimitz
	https://www.shadertoy.com/view/XtS3DD

	Spectacles by MEEEEEE
    https://www.shadertoy.com/view/4lBXWt

	[2TC 15] Mystery Mountains by Dave_Hoskins
	https://www.shadertoy.com/view/llsGW7

	Raytracing graphics is kinda like baking cakes.

	I want yall to first see how magical
	the cake can be before trying to learn how to make it, because the thing we
	make at first isn't going to be one of those crazy 10 story wedding cakes. its just
	going to be some burnt sugar bread.

	Making art using code can be so fufilling, and so infinite, but to get there you
	need to learn some techniques that might not seem that inspiring. To bake a cake,
	you first need to turn on an oven, and need to know what an oven even is. In this
	tutorial we are going to be learning how to make the oven, how to turn it on,
	and how to mix ingredients. as you can see on our left, our cake isn't very pretty
	but it is a cake. and thats pretty crazy for just one tutorial!

	Once you have gone through this tutorial, you can see a 'minimized' version
	here: https://www.shadertoy.com/view/Xt2XDt

	where I've rewritten it using the varibles and functions that
	are used alot throughout shadertoy. The inspiration examples above
	probably seem completely insane, because of all the single letter variable
	names, but keep in mind, that they all start with most of the same ingredients
	and overn that we will learn about right now!


	I've tried to break up the code into 'sections'
	which have the 'SECTION 'BLAH'' label above them. Not sure
	if thats gonna help or not, but please leave comments
	if you think something works or doesn't work, slash you
	have any questions!!!

	or contact me at @vrtree || @cabbibo


	Cheat sheet for vectors:

    x = left / right
	y = up / down
	z = forwards / backwards

	also, for vectors labeled 'color'

	x = red
	y = green
	z = blue



	//---------------------------------------------------
    // SECTION 'A' : ONE PROGRAM FOR EVERY PIXEL!
    //---------------------------------------------------

	The best metaphor that I can think of for raytracing is
	that the rectangle to our left is actually just a small window
	into a fantastic world. We need to describe that world,
	so that we can see it. BUT HOW ?!?!?!

	What we are doing below is describing what color each pixel
	of the window is, however because of the way that shader
	programs work, we need to give the same instruction to every
	single PIXEL ( or in shadertoy terms, FRAGMENT )
	in the window. This is where the term SIMD comes
	from : Same Instruction Multiple Data

	In this case, the same instruction is the program below,
	and the multiple data is the marvelous little piece of magic
	called 'fragCoord' which is just the position of the pixel in
	window. lets rename some things to look prettier.


	//---------------------------------------------------
    // SECTION 'B' : BUILDING THE WINDOW
    //---------------------------------------------------

	If you think about what happens with an actual window, you
	can begin to get an idea of how the magic of raytracing works
	basically a bunch of rays come from the sun ( and or other
	light sources ) , bounce around a bunch ( or a little ), and
	eventually make it through the window, and into our eyes.

	Now the number of rays are masssiveeee that come from the sun
	and alot of them that are bouncing around, will end up going
	directions that aren't even close to the window, or maybe
	will hit the wall instead of the window.

	We only care about the rays that go through the window
	and make it to our eyeballs!

	This means that we can be a bit intelligent. Instead of
	figuring out the rays that come from the sun and bounce around
	lets start with out eyes, and work backwards!!!!


	//---------------------------------------------------
    // SECTION 'C' : NAVIGATING THE WORLD
    //---------------------------------------------------

	After setting up all the neccesary ray information,
	we FINALLY get to start building the scene. Up to this point,
	we've only built up the window, and the rays that go from our
	eyes through the window, but now we need to describe to the rays
    if they hit anything and what they hit!


	Now this part has some pretty scary code in it ( whenever I look
	at it at least, my eyes glaze over ), so feel free to skip over
	the checkRayHit function. I tried to explain it as best as I could
	down below, and you might want to come back to it after going
	throught the rest of the tutorial, but the important thing to
	remember is the following:


	These 'rays' that we've been talking about will move through the
	scene along their direction. They do this iteratively, and at each
	step will basically ask the question :

	'HOW CLOSE AM I TO THINGS IN THE WORLD???'

	because well, rays are lonely, and want to be closer to things in
	the world. We provide them an answer to that question using our
	description of the world, and they use this information to tell
	them how much further along their path they should move. If the
	answer to the question is:

	'Lovely little ray, you are actually touching a thing in the world!'

	We know what that the ray hit something, and can begin with our next
	step!

	The tricky part about this is that we have to as accuratly as
	possible provide them an answer to their question 'how close??!!'



	//--------------------------------------------------------------
    // SECTION 'D' : MAPPING THE WORLD , AKA 'SDFS ARE AWESOME!!!!'
    //--------------------------------------------------------------

	To answer the above concept, we are going to use this magical
	concept called:

	'Signed Distance Fields'
	-----------------------

	These things are the best, and very basically can be describe as
	a function that takes in a position, and feeds back a value of
	how close you are to a thing. If the value of this distance is negative
	you are inside the thing, if it is positive, you are outside the thing
	and if its 0 you are at the surface of the thing! This positive or negative
	gives us the 'Signed' in 'Signed Distance Field'

	For a super intensive description of many of the SDFs out there
	check out Inigo Quilez's site:

	https://iquilezles.org/articles/distfunctions

	Also, if you want a deep dive into why these functions are the
	ultimate magic, check out this crazy paper by the geniouses
	over at Media Molecule about their new game: 'DREAMS'

    http://media.lolrus.mediamolecule.com/AlexEvans_SIGGRAPH-2015.pdf

	Needless to say, these lil puppies are super amazing, and are
	here to free us from the tyranny of polygons.


	---------

	We are going to put all of our SDFs into a single function called

	'mapTheWorld'

	which will take in a position, and feed back two values.
	The first value is the Distance of Signed Distance Field, and the
	second value will tell us what we are closest too, so that if
	we actually hit something, we can tell what it is. We will denote this
	by an 'ID' value.

	The hardest part for me to wrap my head around for this was the fact that
	these fields do not just describe where the surface of an object is,
	they actually describe how far you are from the object from ANYWHERE
	in the world.

	For example, if I was hitting a round ballon ( AKA a sphere :) )
	I wouldn't just know if I was on the surface of the ballon, I would have
	to know how close I was to the balloon from anywhere in space.

	Check out the 'TAG : BALLOON' in the mapTheWorld function for more detail :)

	I've also made a function for a box, that is slightly more complex, and to be
	honest, I don't exactly understand the math of it, but the beauty of programming
	is that someone else ( AKA Inigo ) does, and I can steal his knowledge, just by
	looking at the functions from his website!

	---------

	One of the magical properties of SDFs is how easily they can be combined
	contorted, and manipulated. They are just these lil functions that take
	in a position and give back a distance value, so we can do things like play with the
	input position, play with the output distance value, or just about anything
	else.

	We'll start by combining two SDFs by asking the simple question

	'Which thing am I closer to?'

	which is as simple as a '>' because we already know exactly how close we are
	to each thing!

	check out 'TAG : WHICH AM I CLOSER TO?'  for more enough

	We use these function to create a map of the world for the rays to navigate,
	and than pass that map to the checkRayHit, which propates the rays throughout
	the world and tells us what they hit.

	Once they know that, we can FINALLY do our last step:


	//--------------------------------------------------------------
    // SECTION 'E' : COLORING THE WORLD!
    //--------------------------------------------------------------

	At the end of our checkRayHit function we return a vec2 with two values:
	.x is the distance that our ray traveled before hitting
	.y is the ID of the thing that we hit.

	if .y is less that 0.0 that means that our ray went as far as we allowed it
	to go without hitting anything. thats one lonely ray :(

	however, that doesn't mean that the ray didn't hit anything. It just meant
	that it is part of the background.

	Thanks little ray!
	You told us important information about our scene,
	and your hard work is helping to create the world!

	We can get reallly crazy with how we color the background of the scene,
	but for this tutorial lets just keep it black, because who doesn't love
	the void.

	we will use the function 'doBackgroundColor' to accomplish this task!

	That tells us about the background, but what if .y is greater than 0.0?
	then we get to make some stuff in the scene!

	if the ID is equal to balloon id, then we 'doBalloonColor'
	and if the ID is equal to the box , then we 'doBoxColor'

	This is all that we need if we want to color simple solid objects,
	but what if we want to add some shading, by doing what we originally
	talked about, that is, following the ray to the sun?

	For this first tutorial, we will keep it to a very naive approach,
	but once you get the basics of sections A - D, we can get SUPER crazy
	with this 'color' the world section.

	For example, we could reflect the
	ray off the surface, and than repeat the checkRayHit with this new information
	continuing to follow this ray through more and more of the world. we could
	repeat this process again and again, and even though our gpu would hate us
	we could continue bouncing around until we got to a light source!

	In a later tutorial we will do exactly this, but for now,
	we are going to do 1 simple task:


	See how much the surface that we hit, faces the sun.


	to do that we need to do 2 things.

	First, determine which way the surface faces
	Second, determine which way rays go from the surface to get to the sun

	1) To determine the way that the surface faces, we will use a function called
	'getNormalOfSurface' This function will either make 100% sense, or 0% sense
	depending on how often you have played with fields, but it made 0% sense to me
	for many years, so don't worry if you don't get it! Whats important is that
	it gives us the direction that the surface faces, which we call its 'Normal'
	You can think of it as a vector that is perpendicular to the surface at a specific point

	So that it is easier to understand what this value is, we are actually going to color our
	box based on this value. We will map the X value of the normal to red, the Y value of the
	normal to green and the Z value of the normal to blue. You can see this more in the
	'doBoxColor' function


	2) To get the direction the rays go to get to the sun, we just need to subtract the sun
	position from the position of where we hit. This will provide us a direction from the sun
	to the position. Look inside the doBalloonColor to see this calculation happen.
	this will give us the direction of the rays from the sun to the surface!


	Now that we have these 2 pieces of information, the last thing we need to do is see
	how much the two vectors ( the normal and the light direction ) 'Face' each other.

	that word 'Face', might not make much sense in this context, but think about it this way.

	If you have a table, and a light above the table, the top of the table will 'Face',
	the light, and the bottom of the table will 'Face' away from the light. The surface
	that 'Faces' the light will get hit by the rays from the light, while the surface
	that 'Faces' away from the light will be totally dark!

	so how do we get this 'Face' value ( pun intended :p ) ?

	There is a magical function called a 'dot product' which does exactly this. you
	can read more here:

	https://en.wikipedia.org/wiki/Dot_product

	basically this function takes in 2 vectors, and feeds back a value from -1 -> 1.

	if the value is -1 , the two vectors face in exact opposite directions, and if
	the value is 1 , the two vectors face in exactly the same direction. if the value is
	0, than they are perpendicular!

	By using the dot product, we take get the ballon's 'Face' value and color it depending
	on this value!

	check out the doBallonColor to see all this craziness in action


	//--------------------------------------------------------------
    // SECTION 'F' : Wrapping up
    //--------------------------------------------------------------

	What a journey it has been. Remember back when we were talking about
	sending rays through the window? Remember them moving all through the
	world trying to be closer to things?

	So much has happened, and at the end of that journey, we got a color for each ray!

	now all we need to do is output that color onto the screen , which is a single call,
	and we've made our world.


	I know this stuff might seem too dry or too complex at times, too confusing,
	too frustrating, but I promise, if you stick with it, you'll soon be making some of the
	other magical structures you see throughout the rest of this site.

	I'll be trying to do some more of these tutorials, and you'll see that VERY
	quickly, you get from this hideous monstrosity to our left, to marvelous worlds
	filled with lights, colors, and love.

	Thanks for staying around, and please contact me:

	@vrtree , @cabbibo with questions, concerns , and improvments. Or just comment!



*/



//---------------------------------------------------
// SECTION 'B' : BUILDING THE WINDOW
//---------------------------------------------------

// Most of this is taken from many of the shaders
// that @iq have worked on. Make sure to check out
// more of his magic!!!


// This calculation basically gets a way for us to
// transform the rays coming out of our eyes and going through the window.
// If it doesn't make sense, thats ok. It doesn't make sense to me either :)
// Whats important to remember is that this basically gives us a way to position
// our window. We could you it to make the window look north, south, east, west, up, down
// or ANYWHERE in between!
//mat3 calculateEyeRayTransformationMatrix( in vec3 ro, in vec3 ta, in float roll )
//{
//    vec3 ww = normalize( ta - ro );
//    vec3 uu = normalize( cross(ww,vec3(sin(roll),cos(roll),0.0) ) );
//    vec3 vv = normalize( cross(uu,ww));
//    return mat3( uu, vv, ww );
//}







//--------------------------------------------------------------
// SECTION 'D' : MAPPING THE WORLD , AKA 'SDFS ARE AWESOME!!!!'
//--------------------------------------------------------------


//'TAG: BALLOON'
//vec2 sdfBalloon( vec3 currentRayPosition ){
//
//    // First we define our balloon position
//    vec3 balloonPosition = vec3( 0. , 0. , -.4);
//
//    // than we define our balloon radius
//    float balloonRadius = .9;
//
//    // Here we get the distance to the center of the balloon
//    float distanceToBalloon = length( currentRayPosition - balloonPosition );
//
//    // finally we get the distance to the balloon surface
//    // by substacting the balloon radius. This means that if
//    // the distance to the balloon is less than the balloon radius
//    // the value we get will be negative! giving us the 'Signed' in
//    // Signed Distance Field!
//    float distanceToBalloonSurface = distanceToBalloon - balloonRadius;
//
//
//    // Finally we build the full balloon information, by giving it an ID
//    float balloonID = 1.;
//
//    // And there we have it! A fully described balloon!
//    vec2 balloon = vec2( distanceToBalloonSurface,  balloonID );
//
//    return balloon;
//
//}


//vec2 sdfBox( vec3 currentRayPosition ){
//
//    // First we define our box position
//    vec3 boxPosition = vec3( -.8 , -.4 , 0.2 );
//
//    // than we define our box dimensions using x , y and z
//    vec3 boxSize = vec3( .4 , .3 , .2 );
//
//    // Here we get the 'adjusted ray position' which is just
//    // writing the point of the ray as if the origin of the
//    // space was where the box was positioned, instead of
//    // at 0,0,0 . AKA the difference between the vectors in
//    // vector format.
//    vec3 adjustedRayPosition = currentRayPosition - boxPosition;
//
//    // finally we get the distance to the box surface.
//    // I don't get this part very much, but I bet Inigo does!
//    // Thanks for making code for us IQ !
//    vec3 distanceVec = abs( adjustedRayPosition ) - boxSize;
//    float maxDistance = max( distanceVec.x , max( distanceVec.y , distanceVec.z ) );
//    float distanceToBoxSurface = min( maxDistance , 0.0 ) + length( max( distanceVec , 0.0 ) );
//
//    // Finally we build the full box information, by giving it an ID
//    float boxID = 2.;
//
//    // And there we have it! A fully described box!
//    vec2 box = vec2( distanceToBoxSurface,  boxID );
//
//    return box;
//
//}


// 'TAG : WHICH AM I CLOSER TO?'
// This function takes in two things
// and says which is closer by using the
// distance to each thing, comparing them
// and returning the one that is closer!
//vec2 whichThingAmICloserTo( vec2 thing1 , vec2 thing2 ){
//
//    vec2 closestThing;
//
//    // Check out the balloon function
//    // and remember how the x of the returned
//    // information is the distance, and the y
//    // is the id of the thing!
//    if( thing1.x <= thing2.x ){
//
//        closestThing = thing1;
//
//    }else if( thing2.x < thing1.x ){
//
//        closestThing = thing2;
//
//    }
//
//    return closestThing;
//
//}



// Takes in the position of the ray, and feeds back
// 2 values of how close it is to things in the world
// what thing it is closest two in the world.
//vec2 mapTheWorld( vec3 currentRayPosition ){
//
//
//    vec2 result;
//
//    // extra credit: uncomment to repeat
//    // currentRayPosition.x = mod(currentRayPosition.x , 4.) - 2.;
//    // currentRayPosition.y = mod(currentRayPosition.y , 4.) - 2.;
//    //currentRayPosition.z = mod(currentRayPosition.z , 4.) - 2.;
//
//
//    vec2 balloon = sdfBalloon( currentRayPosition );
//    vec2 box     = sdfBox( currentRayPosition );
//
//    result = whichThingAmICloserTo( balloon , box );
//
//    return result;
//
//
//}



//---------------------------------------------------
// SECTION 'C' : NAVIGATING THE WORLD
//---------------------------------------------------

// We want to know when the closeness to things in the world is
// 0.0 , but if we wanted to get exactly to 0 it would take us
// alot of time to be that precise. Here we define the laziness
// our navigation function. try chaning the value to see what it does!
// if you are getting too low of framerates, this value will help alot,
// but can also make your scene look very different
// from how it should
//const float HOW_CLOSE_IS_CLOSE_ENOUGH = 0.001;

// This is basically how big our scene is. each ray will be shot forward
// until it reaches this distance. the smaller it is, the quicker the
// ray will reach the edge, which should help speed up this function
//const float FURTHEST_OUR_RAY_CAN_REACH = 10.;

// This is how may steps our ray can take. Hopefully for this
// simple of a world, it will very quickly get to the 'close enough' value
// and stop the iteration, but for more complex scenes, this value
// will dramatically change not only how good the scene looks
// but how fast teh scene can render.

// remember that for each pixel we are displaying, the 'mapTheWorld' function
// could be called this many times! Thats ALOT of calculations!!!
//const int HOW_MANY_STEPS_CAN_OUR_RAY_TAKE = 100;


//vec2 checkRayHit( in vec3 eyePosition , in vec3 rayDirection ){

    //First we set some default values


    // our distance to surface will get overwritten every step,
    // so all that is important is that it is greater than our
    // 'how close is close enough' value
//    float distanceToSurface 			= HOW_CLOSE_IS_CLOSE_ENOUGH * 2.;

//     The total distance traveled by the ray obviously should start at 0
//    float totalDistanceTraveledByRay 	= 0.;

    // if we hit something, this value will be overwritten by the
    // totalDistance traveled, and if we don't hit something it will
    // be overwritten by the furthest our ray can reach,
    // so it can be whatever!
//    float finalDistanceTraveledByRay 	= -1.;

    // if our id is less that 0. , it means we haven't hit anything
    // so lets start by saying we haven't hit anything!
//    float finalID = -1.;



    //here is the loop where the magic happens
//    for( int i = 0; i < HOW_MANY_STEPS_CAN_OUR_RAY_TAKE; i++ ){

        // First off, stop the iteration, if we are close enough to the surface!
//        if( distanceToSurface < HOW_CLOSE_IS_CLOSE_ENOUGH ) break;

        // Second off, stop the iteration, if we have reached the end of our scene!
//        if( totalDistanceTraveledByRay > FURTHEST_OUR_RAY_CAN_REACH ) break;

        // To check how close we are to things in the world,
        // we need to get a position in the scene. to do this,
        // we start at the rays origin, AKA the eye
        // and move along the ray direction, the amount we have already traveled.
//        vec3 currentPositionOfRay = eyePosition + rayDirection * totalDistanceTraveledByRay;

        // Distance to and ID of things in the world
        //--------------------------------------------------------------
        // SECTION 'D' : MAPPING THE WORLD , AKA 'SDFS ARE AWESOME!!!!'
        //--------------------------------------------------------------
//        vec2 distanceAndIDOfThingsInTheWorld = mapTheWorld( currentPositionOfRay );


        // we get out the results from our mapping of the world
        // I am reassigning them for clarity
//        float distanceToThingsInTheWorld = distanceAndIDOfThingsInTheWorld.x;
//        float idOfClosestThingInTheWorld = distanceAndIDOfThingsInTheWorld.y;

        // We save out the distance to the surface, so that
        // next iteration we can check to see if we are close enough
        // to stop all this silly iteration
//        distanceToSurface           = distanceToThingsInTheWorld;

        // We are also finalID to the current closest id,
        // because if we hit something, we will have the proper
        // id, and we can skip reassigning it later!
//        finalID = idOfClosestThingInTheWorld;

        // ATTENTION: THIS THING IS AWESOME!
        // This last little calculation is probably the coolest hack
        // of this entire tutorial. If we wanted too, we could basically
        // step through the field at a constant amount, and at every step
        // say 'am i there yet', than move forward a little bit, and
        // say 'am i there yet', than move forward a little bit, and
        // say 'am i there yet', than move forward a little bit, and
        // say 'am i there yet', than move forward a little bit, and
        // say 'am i there yet', than move forward a little bit, and
        // that would take FOREVER, and get really annoying.

        // Instead what we say is 'How far until we are there?'
        // and move forward by that amount. This means that if
        // we are really far away from everything, we can make large
        // movements towards the surface, and if we are closer
        // we can make more precise movements. making our marching functino
        // faster, and ideally more precise!!

        // WOW!

//        totalDistanceTraveledByRay += distanceToThingsInTheWorld;


//    }

    // if we hit something set the finalDirastnce traveled by
    // ray to that distance!
//    if( totalDistanceTraveledByRay < FURTHEST_OUR_RAY_CAN_REACH ){
//        finalDistanceTraveledByRay = totalDistanceTraveledByRay;
//    }


    // If the total distance traveled by the ray is further than
    // the ray can reach, that means that we've hit the edge of the scene
    // Set the final distance to be the edge of the scene
    // and the id to -1 to make sure we know we haven't hit anything
//    if( totalDistanceTraveledByRay > FURTHEST_OUR_RAY_CAN_REACH ){
//        finalDistanceTraveledByRay = FURTHEST_OUR_RAY_CAN_REACH;
//        finalID = -1.;
//    }
//
//    return vec2( finalDistanceTraveledByRay , finalID );
//
//}







//--------------------------------------------------------------
// SECTION 'E' : COLORING THE WORLD
//--------------------------------------------------------------



// Here we are calcuting the normal of the surface
// Although it looks like alot of code, it actually
// is just trying to do something very simple, which
// is to figure out in what direction the SDF is increasing.
// What is amazing, is that this value is the same thing
// as telling you what direction the surface faces, AKA the
// normal of the surface.
//vec3 getNormalOfSurface( in vec3 positionOfHit ){
//
//    vec3 tinyChangeX = vec3( 0.001, 0.0, 0.0 );
//    vec3 tinyChangeY = vec3( 0.0 , 0.001 , 0.0 );
//    vec3 tinyChangeZ = vec3( 0.0 , 0.0 , 0.001 );
//
//    float upTinyChangeInX   = mapTheWorld( positionOfHit + tinyChangeX ).x;
//    float downTinyChangeInX = mapTheWorld( positionOfHit - tinyChangeX ).x;
//
//    float tinyChangeInX = upTinyChangeInX - downTinyChangeInX;
//
//
//    float upTinyChangeInY   = mapTheWorld( positionOfHit + tinyChangeY ).x;
//    float downTinyChangeInY = mapTheWorld( positionOfHit - tinyChangeY ).x;
//
//    float tinyChangeInY = upTinyChangeInY - downTinyChangeInY;
//
//
//    float upTinyChangeInZ   = mapTheWorld( positionOfHit + tinyChangeZ ).x;
//    float downTinyChangeInZ = mapTheWorld( positionOfHit - tinyChangeZ ).x;
//
//    float tinyChangeInZ = upTinyChangeInZ - downTinyChangeInZ;
//
//
//    vec3 normal = vec3(
//            tinyChangeInX,
//    tinyChangeInY,
//    tinyChangeInZ
//    );
//
//    return normalize(normal);
//}





// doing our background color is easy enough,
// just make it pure black. like my soul.
//vec3 doBackgroundColor(){
//    return vec3( 0. );
//}




//vec3 doBalloonColor(vec3 positionOfHit , vec3 normalOfSurface ){
//
//    vec3 sunPosition = vec3( 1. , 4. , 3. );
//
//    // the direction of the light goes from the sun
//    // to the position of the hit
//    vec3 lightDirection = sunPosition - positionOfHit;
//
//
//    // Here we are 'normalizing' the light direction
//    // because we don't care how long it is, we
//    // only care what direction it is!
//    lightDirection = normalize( lightDirection );
//
//
//    // getting the value of how much the surface
//    // faces the light direction
//    float faceValue = dot( lightDirection , normalOfSurface );
//
//    // if the face value is negative, just make it 0.
//    // so it doesn't give back negative light values
//    // cuz that doesn't really make sense...
//    faceValue = max( 0. , faceValue );
//
//    vec3 balloonColor = vec3( 1. , 0. , 0. );
//
//    // our final color is the balloon color multiplied
//    // by how much the surface faces the light
//    vec3 color = balloonColor * faceValue;
//
//    // add in a bit of ambient color
//    // just so we don't get any pure black
//    color += vec3( .3 , .1, .2 );
//
//
//    return color;
//}



// Here we are using the normal of the surface,
// and mapping it to color, to show you just how cool
// normals can be!
//vec3 doBoxColor(vec3 positionOfHit , vec3 normalOfSurface ){
//
//    vec3 color = vec3( normalOfSurface.x , normalOfSurface.y , normalOfSurface.z );
//
//    //could also just write color = normalOfSurce
//    //but trying to be explicit.
//
//    return color;
//}




// This is where we decide
// what color the world will be!
// and what marvelous colors it will be!
//vec3 colorTheWorld( vec2 rayHitInfo , vec3 eyePosition , vec3 rayDirection ){
//
//    // remember for color
//    // x = red , y = green , z = blue
//    vec3 color;
//
//    // THE LIL RAY WENT ALL THE WAY
//    // TO THE EDGE OF THE WORLD,
//    // AND DIDN'T HIT ANYTHING
//    if( rayHitInfo.y < 0.0 ){
//
//        color = doBackgroundColor();
//
//
//        // THE LIL RAY HIT SOMETHING!!!!
//    }else{

        // If we hit something,
        // we also know how far the ray has to travel to hit it
        // and because we know the direction of the ray, we can
        // get the exact position of where we hit the surface
        // by following the ray from the eye, along its direction
        // for the however far it had to travel to hit something
//        vec3 positionOfHit = eyePosition + rayHitInfo.x * rayDirection;

        // We can then use this information to tell what direction
        // the surface faces in
//        vec3 normalOfSurface = getNormalOfSurface( positionOfHit );


        // 1.0 is the Balloon ID
//        if( rayHitInfo.y == 1.0 ){

//            color = doBalloonColor( positionOfHit , normalOfSurface );


            // 2.0 is the Box ID
//        }else if( rayHitInfo.y == 2.0 ){

//            color = doBoxColor( positionOfHit , normalOfSurface );

//        }


//    }


//    return color;


//}



//void mainImage( out vec4 fragColor, in vec2 fragCoord )
//{

    //---------------------------------------------------
    // SECTION 'A' : ONE PROGRAM FOR EVERY PIXEL!
    //---------------------------------------------------

    // Here we are getting our 'Position' of each pixel
    // This section is important, because if we didn't
    // divied by the resolution, our values would be masssive
    // as fragCoord returns the value of how many pixels over we
    // are. which is alot :)
//    vec2 p = ( -iResolution.xy + 2.0 * fragCoord.xy ) / iResolution.y;

    // thats a super long name, so maybe we will
    // keep on using uv, but im explicitly defining it
    // so you can see exactly what those two letters mean
//    vec2 xyPositionOfPixelInWindow = p;



    //---------------------------------------------------
    // SECTION 'B' : BUILDING THE WINDOW
    //---------------------------------------------------

    // We use the eye position to tell use where the viewer is
//    vec3 eyePosition = vec3( 0., 0., 2.);

    //extra credit: move camera
    //vec3 eyePosition = vec3( sin(iTime), 0., 2.);


    // This is the point the view is looking at.
    // The window will be placed between the eye, and the
    // position the eye is looking at!
//    vec3 pointWeAreLookingAt = vec3( 0. , 0. , 0. );

    // This is where the magic of actual mathematics
    // gives a way to actually place the window.
    // the 0. at the end there gives the 'roll' of the transformation
    // AKA we would be standing so up is up, but up could be changing
    // like if we were one of those creepy dolls whos rotate their head
    // all the way around along the z axis
//    mat3 eyeTransformationMatrix = calculateEyeRayTransformationMatrix( eyePosition , pointWeAreLookingAt , 0. );


    // Here we get the actual ray that goes out of the eye
    // and through the individual pixel! This basically the only thing
    // that is different between the pixels, but is also the bread and butter
    // of ray tracing. It should be since it has the word 'ray' in its variable name...
    // the 2. at the end is the 'lens length' . I don't know how to best
    // describe this, but once the full scene is built, tryin playing with it
    // to understand inherently how it works
//    vec3 rayComingOutOfEyeDirection = normalize( eyeTransformationMatrix * vec3( p.xy , 2. ) );



    //---------------------------------------------------
    // SECTION 'C' : NAVIGATING THE WORLD
    //---------------------------------------------------
//    vec2 rayHitInfo = checkRayHit( eyePosition , rayComingOutOfEyeDirection );


    //--------------------------------------------------------------
    // SECTION 'E' : COLORING THE WORLD
    //--------------------------------------------------------------
//    vec3 color = colorTheWorld( rayHitInfo , eyePosition , rayComingOutOfEyeDirection );


    //--------------------------------------------------------------
    // SECTION 'F' : Wrapping up
    //--------------------------------------------------------------
//    fragColor = vec4(color,1.0);


    // WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW!
    // WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW!
    // WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW!
    // WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW!
    // WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW! WOW!


//}
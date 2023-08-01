package com.example.shopping_list.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shopping_list.entity.TypeText
import com.example.shopping_list.entity.Wave
import com.example.shopping_list.ui.theme.styleApp
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt


@Composable fun colorPicker(): Long {
    val listColor: List<Wave> = createListSpectrumRGB()
    val spectrum: List<Color> = listColor.map { Color(it.color) }

    var colorIndex by remember { mutableStateOf(0)}
    var colorPosition by remember{mutableStateOf(0f)}
    var alfaPosition by remember{mutableStateOf(255f)}
    var intensityPosition by remember{mutableStateOf(255f)}
    var colorSelected by remember { mutableStateOf(0L)}

    Text(text = "")
    Column( modifier = Modifier.fillMaxWidth()) {
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
        Box( modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(brush = Brush.horizontalGradient(colors = spectrum)))
        Box() {
            Text(text = "Цвет", style = styleApp(nameStyle = TypeText.TEXT_IN_LIST_SMALL))
            Slider(
                value = colorPosition,
                valueRange = 0f..listColor.size.toFloat()-1,
                steps = listColor.size,
                onValueChange = {
                    colorPosition = it
                    colorIndex = it.roundToInt() },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF575757),
                    activeTrackColor = Color(0xFFA2A2A2),
                    inactiveTrackColor = Color(0xFFA2A2A2),
                    inactiveTickColor = Color(0xFFA2A2A2),
                    activeTickColor = Color(0xFF575757)
                ))
        }
        Box() {
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
        Box() {
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


fun createListSpectrumRGB(): List<Wave>{
    val listColor = mutableListOf<Wave>()
//    val listColor1 = mutableListOf<Wave>()
//    val listColor2 = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB(wave, 255, 255))
//        listColor1.add(waveToRGB1(wave))
//        listColor2.add(waveToRGB2(wave))
    }
    return listColor
}
fun createListSpectrumRGB1(): List<Wave>{
    val listColor = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB1(wave))
    }
    return listColor
}
fun createListSpectrumRGB2(): List<Wave>{
    val listColor = mutableListOf<Wave>()
    for (wave in LEN_MIN..LEN_MAX step LEN_STEP) {
        listColor.add(waveToRGB2(wave))
    }
    return listColor
}
fun waveToRGB(wavelength: Int, alpha: Int, intensity: Int): Wave{
    val gamma = 0.08

    val factor = if((wavelength >= 380) && (wavelength<420)) 0.3 + 0.7*(wavelength - 380) / (420 - 380)
        else if((wavelength >= 420) && (wavelength<701)) 1.0
        else if((wavelength >= 701) && (wavelength<781)) 0.3 + 0.7*(780 - wavelength) / (780 - 700)
        else 0.0

    return if((wavelength >= 380) && (wavelength<440)){
        calculateColor(wavelength,-(wavelength - 440) / (440 - 380.0), 0.0, 1.0, factor, alpha, intensity, gamma)
    } else if((wavelength >= 440) && (wavelength<490)){
        calculateColor(wavelength,0.0, (wavelength - 440) / (490 - 440.0), 1.0, factor, alpha, intensity, gamma)
    }else if((wavelength >= 490) && (wavelength<510)){
        calculateColor(wavelength,0.0, 1.0, (-(wavelength - 510) / (510 - 490.0)), factor, alpha, intensity, gamma)
    }else if((wavelength >= 510) && (wavelength<580)){
        calculateColor(wavelength,(wavelength - 510) / (580 - 510.0), 1.0, 0.0, factor, alpha, intensity, gamma)
    }else if((wavelength >= 580) && (wavelength<645)){
        calculateColor(wavelength,1.0, (-(wavelength - 645) / (645 - 580.0)), 0.0, factor, alpha, intensity, gamma)
    }else if((wavelength >= 645) && (wavelength<781)){
        calculateColor(wavelength,1.0, 0.0, 0.0, factor, alpha, intensity, gamma)
    } else calculateColor(wavelength,0.0, 0.0, 0.0, factor, alpha, intensity, gamma)
}

fun calculateColor(
    wavelength: Int,
    red: Double,
    green: Double,
    blue: Double,
    factor: Double,
    alpha: Int,
    intensity: Int,
    gamma: Double): Wave {

    val rgbAlpha: Long = (16777216 * alpha).toLong()
    val rgbR = if (red != 0.0) (intensity * (red * factor).pow(gamma)).roundToInt() * 65536 else 0
    val rgbG = if (green != 0.0) (intensity * (green * factor).pow(gamma)).roundToInt() * 256 else 0
    val rgbB = if (blue != 0.0) (intensity * (blue * factor).pow(gamma)).roundToInt() else 0

    return Wave(wavelength, rgbR.toDouble(), rgbG.toDouble(), rgbB.toDouble(), rgbAlpha + rgbR + rgbG + rgbB)
}

const val LEN_MIN = 380
const val LEN_MAX = 780
const val LEN_STEP = 5

val X = arrayListOf(
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
val Y = arrayListOf(
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
val Z = arrayListOf(
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
val MATRIX_SRGB_D65 = arrayListOf<Double>(
    3.2404542, -1.5371385, -0.4985314,
    -0.9692660, 1.8760108, 0.0415560,
    0.0556434, -0.2040259, 1.0572252
)
fun waveToRGB1(wavelength: Int): Wave {

    var index = ((wavelength - LEN_MIN) / LEN_STEP)
    if (index >= 79) {
        index = 79
    }
    val offset = wavelength - LEN_STEP * index

    val waveObj = Wave(
        wave = wavelength,
        X = interpolate(X, index, offset),
        Y = interpolate(Y, index, offset),
        Z = interpolate(Z, index, offset),
        color = 0L,
        alpha = 255,
        intensity = 255
    )

    val r = MATRIX_SRGB_D65[0] * waveObj.X + MATRIX_SRGB_D65[1] * waveObj.Y + MATRIX_SRGB_D65[2] * waveObj.Z
    val g = MATRIX_SRGB_D65[3] * waveObj.X + MATRIX_SRGB_D65[4] * waveObj.Y + MATRIX_SRGB_D65[5] * waveObj.Z
    val b = MATRIX_SRGB_D65[6] * waveObj.X + MATRIX_SRGB_D65[7] * waveObj.Y + MATRIX_SRGB_D65[8] * waveObj.Z

    val rgbAlpha: Long = (16777216 * 255).toLong()
    val rgbR = gammaCorrect_sRGB(r) * 255 * 65536
    val rgbG = gammaCorrect_sRGB(g) * 255 * 256
    val rgbB = gammaCorrect_sRGB(b) * 255
    waveObj.color = rgbAlpha + rgbR + rgbG + rgbB
    return waveObj
}

fun interpolate(values: List<Double>, index: Int, offset: Int): Double{
    return if (offset > 0) {
        values[index] + offset * ((values[1 + index] - values[index])/(index * LEN_STEP + LEN_STEP - index * LEN_STEP))
    } else values[index]
}

fun gammaCorrect_sRGB(c:Double): Int {
    val cc = if( c <= 0.0031308 ) 12.92 * c else 1.055 * c.pow(1/2.4) - 0.055
    return if(cc < 0) 0 else if(cc > 1) 1 else cc.toInt()
}

//##################################################################################################################

fun waveToRGB2(wavelength: Int): Wave{
    val waveObj = cie1931WavelengthToXYZFit(wavelength);
    val rgb = srgbXYZ2RGB(waveObj);

    waveObj.color = 0xFF000000 or
            ((rgb[0] * 0xFF).toInt() and 0xFF).shl(16).toLong() or
            ((rgb[1] * 0xFF).toInt() and 0xFF).shl(8).toLong() or
            (((rgb[2] * 0xFF).toInt() and 0xFF).toLong())
    return waveObj;
}
fun srgbXYZ2RGB(xyz: Wave): List<Double> {

    val rl =  3.2406255 * xyz.X + -1.537208  * xyz.Y + -0.4986286 * xyz.Z
    val gl = -0.9689307 * xyz.X +  1.8757561 * xyz.Y +  0.0415175 * xyz.Z
    val bl =  0.0557101 * xyz.X + -0.2040211 * xyz.Y +  1.0569959 * xyz.Z

    return listOf(
        srgbXYZ2RGBPostprocess(rl),
        srgbXYZ2RGBPostprocess(gl),
        srgbXYZ2RGBPostprocess(bl)
    )
}
fun srgbXYZ2RGBPostprocess(c: Double): Double {
    val cc = if (c > 1) 1.0 else if (c < 0) 0.0 else c
    return if(cc <= 0.0031308) cc * 12.92 else 1.055 * cc.pow(1/2.4) - 0.055
}
fun cie1931WavelengthToXYZFit(wave:Int): Wave {

    var t1 = (wave - 442.0) * (if (wave < 442.0) 0.0624 else 0.0374)
    var t2 = (wave - 599.8) * (if (wave < 599.8) 0.0264 else 0.0323)
    val t3 = (wave - 501.1) * (if (wave < 501.1) 0.0490 else 0.0382)
    val x = 0.362 * exp(-0.5 * t1 * t1)+ 1.056 * exp(-0.5 * t2 * t2)- 0.065 * exp(-0.5 * t3 * t3)

    t1 = (wave - 568.8) * (if (wave < 568.8) 0.0213 else 0.0247)
    t2 = (wave - 530.9) * (if (wave < 530.9) 0.0613 else 0.0322)
    val y = 0.821 * exp(-0.5 * t1 * t1) + 0.286 * exp(-0.5 * t2 * t2)

    t1 = (wave - 437.0) * (if (wave < 437.0) 0.0845 else 0.0278)
    t2 = (wave - 459.0) * (if(wave < 459.0) 0.0385 else 0.0725)
    val z = 1.217 * exp(-0.5 * t1 * t1) + 0.681 * exp(-0.5 * t2 * t2)
    return Wave(wave = wave, X = x, Y = y, Z = z, color = 0, alpha = 255, intensity = 255)
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
object ColorList {
    val list: List<Wave> = listOf(
        Wave (wave = 380, X = 0.1741, Y = 0.0050, Z = 0.8209),
        Wave (wave = 385, X = 0.1740, Y = 0.0050, Z = 0.8210),
        Wave (wave = 390, X = 0.1738, Y = 0.0049, Z = 0.8213),
        Wave (wave = 395, X = 0.1736, Y = 0.0049, Z = 0.8215),
        Wave (wave = 400, X = 0.1733, Y = 0.0048, Z = 0.8219),
        Wave (wave = 405, X = 0.1730, Y = 0.0048, Z = 0.8222),
        Wave (wave = 410, X = 0.1726, Y = 0.0048, Z = 0.8226),
        Wave (wave = 415, X = 0.1721, Y = 0.0048, Z = 0.8231),
        Wave (wave = 420, X = 0.1714, Y = 0.0051, Z = 0.8235),
        Wave (wave = 425, X = 0.1703, Y = 0.0058, Z = 0.8239),
        Wave (wave = 430, X = 0.1689, Y = 0.0069, Z = 0.8242),
        Wave (wave = 435, X = 0.1669, Y = 0.0086, Z = 0.8245),
        Wave (wave = 440, X = 0.1644, Y = 0.0109, Z = 0.8247),
        Wave (wave = 445, X = 0.1611, Y = 0.0138, Z = 0.8251),
        Wave (wave = 450, X = 0.1566, Y = 0.0177, Z = 0.8257),
        Wave (wave = 455, X = 0.1510, Y = 0.0227, Z = 0.8263),
        Wave (wave = 460, X = 0.1440, Y = 0.0297, Z = 0.8263),
        Wave (wave = 465, X = 0.1355, Y = 0.0399, Z = 0.8246),
        Wave (wave = 470, X = 0.1241, Y = 0.0578, Z = 0.8181),
        Wave (wave = 475, X = 0.1096, Y = 0.0868, Z = 0.8036),
        Wave (wave = 480, X = 0.0913, Y = 0.1327, Z = 0.7760),
        Wave (wave = 485, X = 0.0687, Y = 0.2007, Z = 0.7306),
        Wave (wave = 490, X = 0.0454, Y = 0.2950, Z = 0.6596),
        Wave (wave = 495, X = 0.0235, Y = 0.4127, Z = 0.5638),
        Wave (wave = 500, X = 0.0082, Y = 0.5384, Z = 0.4534),
        Wave (wave = 505, X = 0.0039, Y = 0.6548, Z = 0.3413),
        Wave (wave = 510, X = 0.0139, Y = 0.7502, Z = 0.2359),
        Wave (wave = 515, X = 0.0389, Y = 0.8120, Z = 0.1491),
        Wave (wave = 520, X = 0.0743, Y = 0.8338, Z = 0.0919),
        Wave (wave = 525, X = 0.1142, Y = 0.8262, Z = 0.0596),
        Wave (wave = 530, X = 0.1547, Y = 0.8059, Z = 0.0394),
        Wave (wave = 535, X = 0.1929, Y = 0.7816, Z = 0.0255),
        Wave (wave = 540, X = 0.2296, Y = 0.7543, Z = 0.0161),
        Wave (wave = 545, X = 0.2658, Y = 0.7243, Z = 0.0099),
        Wave (wave = 550, X = 0.3016, Y = 0.6923, Z = 0.0061),
        Wave (wave = 555, X = 0.3373, Y = 0.6589, Z = 0.0038),
        Wave (wave = 560, X = 0.3731, Y = 0.6245, Z = 0.0024),
        Wave (wave = 565, X = 0.4087, Y = 0.5896, Z = 0.0017),
        Wave (wave = 570, X = 0.4441, Y = 0.5547, Z = 0.0012),
        Wave (wave = 575, X = 0.4788, Y = 0.5202, Z = 0.0010),
        Wave (wave = 580, X = 0.5125, Y = 0.4866, Z = 0.0009),
        Wave (wave = 585, X = 0.5448, Y = 0.4544, Z = 0.0008),
        Wave (wave = 590, X = 0.5752, Y = 0.4242, Z = 0.0006),
        Wave (wave = 595, X = 0.6029, Y = 0.3965, Z = 0.0006),
        Wave (wave = 600, X = 0.6270, Y = 0.3725, Z = 0.0005),
        Wave (wave = 605, X = 0.6482, Y = 0.3514, Z = 0.0004),
        Wave (wave = 610, X = 0.6658, Y = 0.3340, Z = 0.0002),
        Wave (wave = 615, X = 0.6801, Y = 0.3197, Z = 0.0002),
        Wave (wave = 620, X = 0.6915, Y = 0.3083, Z = 0.0002),
        Wave (wave = 625, X = 0.7006, Y = 0.2993, Z = 0.0001),
        Wave (wave = 630, X = 0.7079, Y = 0.2920, Z = 0.0001),
        Wave (wave = 635, X = 0.7140, Y = 0.2859, Z = 0.0001),
        Wave (wave = 640, X = 0.7190, Y = 0.2809, Z = 0.0001),
        Wave (wave = 645, X = 0.7230, Y = 0.2770, Z = 0.0000),
        Wave (wave = 650, X = 0.7260, Y = 0.2740, Z = 0.0000),
        Wave (wave = 655, X = 0.7283, Y = 0.2717, Z = 0.0000),
        Wave (wave = 660, X = 0.7300, Y = 0.2700, Z = 0.0000),
        Wave (wave = 665, X = 0.7311, Y = 0.2689, Z = 0.0000),
        Wave (wave = 670, X = 0.7320, Y = 0.2680, Z = 0.0000),
        Wave (wave = 675, X = 0.7327, Y = 0.2673, Z = 0.0000),
        Wave (wave = 680, X = 0.7334, Y = 0.2666, Z = 0.0000),
        Wave (wave = 685, X = 0.7340, Y = 0.2660, Z = 0.0000),
        Wave (wave = 690, X = 0.7344, Y = 0.2656, Z = 0.0000),
        Wave (wave = 695, X = 0.7346, Y = 0.2654, Z = 0.0000),
        Wave (wave = 700, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 705, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 710, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 715, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 720, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 725, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 730, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 735, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 740, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 745, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 750, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 755, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 760, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 765, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 770, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 775, X = 0.7347, Y = 0.2653, Z = 0.0000),
        Wave (wave = 780, X = 0.7347, Y = 0.2653, Z = 0.0000),
    )
    val list1: List<Wave> = listOf(
        Wave (wave = 390, X = 0.003769647, Y = 0.0004146161, Z = 0.0184726),
        Wave (wave = 395, X = 0.009382967, Y = 0.001059646, Z = 0.04609784),
        Wave (wave = 400, X = 0.02214302, Y = 0.002452194, Z = 0.109609),
        Wave (wave = 405, X = 0.04742986, Y = 0.004971717, Z = 0.2369246),
        Wave (wave = 410, X = 0.08953803, Y = 0.00907986, Z = 0.4508369),
        Wave (wave = 415, X = 0.1446214, Y = 0.01429377, Z = 0.7378822),
        Wave (wave = 420, X = 0.2035729, Y = 0.02027369, Z = 1.051821),
        Wave (wave = 425, X = 0.2488523, Y = 0.02612106, Z = 1.305008),
        Wave (wave = 430, X = 0.2918246, Y = 0.03319038, Z = 1.552826),
        Wave (wave = 435, X = 0.3227087, Y = 0.0415794, Z = 1.74828),
        Wave (wave = 440, X = 0.3482554, Y = 0.05033657, Z = 1.917479),
        Wave (wave = 445, X = 0.3418483, Y = 0.05743393, Z = 1.918437),
        Wave (wave = 450, X = 0.3224637, Y = 0.06472352, Z = 1.848545),
        Wave (wave = 455, X = 0.2826646, Y = 0.07238339, Z = 1.664439),
        Wave (wave = 460, X = 0.2485254, Y = 0.08514816, Z = 1.522157),
        Wave (wave = 465, X = 0.2219781, Y = 0.1060145, Z = 1.42844),
        Wave (wave = 470, X = 0.1806905, Y = 0.1298957, Z = 1.25061),
        Wave (wave = 475, X = 0.129192, Y = 0.1535066, Z = 0.9991789),
        Wave (wave = 480, X = 0.08182895, Y = 0.1788048, Z = 0.7552379),
        Wave (wave = 485, X = 0.04600865, Y = 0.2064828, Z = 0.5617313),
        Wave (wave = 490, X = 0.02083981, Y = 0.237916, Z = 0.4099313),
        Wave (wave = 495, X = 0.007097731, Y = 0.285068, Z = 0.3105939),
        Wave (wave = 500, X = 0.002461588, Y = 0.3483536, Z = 0.2376753),
        Wave (wave = 505, X = 0.003649178, Y = 0.4277595, Z = 0.1720018),
        Wave (wave = 510, X = 0.01556989, Y = 0.5204972, Z = 0.1176796),
        Wave (wave = 515, X = 0.04315171, Y = 0.6206256, Z = 0.08283548),
        Wave (wave = 520, X = 0.07962917, Y = 0.718089, Z = 0.05650407),
        Wave (wave = 525, X = 0.1268468, Y = 0.7946448, Z = 0.03751912),
        Wave (wave = 530, X = 0.1818026, Y = 0.8575799, Z = 0.02438164),
        Wave (wave = 535, X = 0.2405015, Y = 0.9071347, Z = 0.01566174),
        Wave (wave = 540, X = 0.3098117, Y = 0.9544675, Z = 0.00984647),
        Wave (wave = 545, X = 0.3804244, Y = 0.9814106, Z = 0.006131421),
        Wave (wave = 550, X = 0.4494206, Y = 0.9890228, Z = 0.003790291),
        Wave (wave = 555, X = 0.5280233, Y = 0.9994608, Z = 0.002327186),
        Wave (wave = 560, X = 0.6133784, Y = 0.9967737, Z = 0.001432128),
        Wave (wave = 565, X = 0.7016774, Y = 0.9902549, Z = 0.0008822531),
        Wave (wave = 570, X = 0.796775, Y = 0.9732611, Z = 0.0005452416),
        Wave (wave = 575, X = 0.8853376, Y = 0.9424569, Z = 0.0003386739),
        Wave (wave = 580, X = 0.9638388, Y = 0.8963613, Z = 0.0002117772),
        Wave (wave = 585, X = 1.051011, Y = 0.8587203, Z = 0.0001335031),
        Wave (wave = 590, X = 1.109767, Y = 0.8115868, Z = 0.00008494468),
        Wave (wave = 595, X = 1.14362, Y = 0.7544785, Z = 0.00005460706),
        Wave (wave = 600, X = 1.151033, Y = 0.6918553, Z = 0.00003549661),
        Wave (wave = 605, X = 1.134757, Y = 0.6270066, Z = 0.00002334738),
        Wave (wave = 610, X = 1.083928, Y = 0.5583746, Z = 0.00001554631),
        Wave (wave = 615, X = 1.007344, Y = 0.489595, Z = 0.00001048387),
        Wave (wave = 620, X = 0.9142877, Y = 0.4229897, Z = 0.00),
        Wave (wave = 625, X = 0.8135565, Y = 0.3609245, Z = 0.00),
        Wave (wave = 630, X = 0.6924717, Y = 0.2980865, Z = 0.00),
        Wave (wave = 635, X = 0.575541, Y = 0.2416902, Z = 0.00),
        Wave (wave = 640, X = 0.4731224, Y = 0.1943124, Z = 0.00),
        Wave (wave = 645, X = 0.3844986, Y = 0.1547397, Z = 0.00),
        Wave (wave = 650, X = 0.2997374, Y = 0.119312, Z = 0.00),
        Wave (wave = 655, X = 0.2277792, Y = 0.08979594, Z = 0.00),
        Wave (wave = 660, X = 0.1707914, Y = 0.06671045, Z = 0.00),
        Wave (wave = 665, X = 0.1263808, Y = 0.04899699, Z = 0.00),
        Wave (wave = 670, X = 0.09224597, Y = 0.03559982, Z = 0.00),
        Wave (wave = 675, X = 0.0663996, Y = 0.02554223, Z = 0.00),
        Wave (wave = 680, X = 0.04710606, Y = 0.01807939, Z = 0.00),
        Wave (wave = 685, X = 0.03292138, Y = 0.01261573, Z = 0.00),
        Wave (wave = 690, X = 0.02262306, Y = 0.008661284, Z = 0.00),
        Wave (wave = 695, X = 0.01575417, Y = 0.006027677, Z = 0.00),
        Wave (wave = 700, X = 0.01096778, Y = 0.004195941, Z = 0.00),
        Wave (wave = 705, X = 0.00760875, Y = 0.002910864, Z = 0.00),
        Wave (wave = 710, X = 0.005214608, Y = 0.001995557, Z = 0.00),
        Wave (wave = 715, X = 0.003569452, Y = 0.001367022, Z = 0.00),
        Wave (wave = 720, X = 0.002464821, Y = 0.0009447269, Z = 0.00),
        Wave (wave = 725, X = 0.001703876, Y = 0.000653705, Z = 0.00),
        Wave (wave = 730, X = 0.001186238, Y = 0.000455597, Z = 0.00),
        Wave (wave = 735, X = 0.0008269535, Y = 0.0003179738, Z = 0.00),
        Wave (wave = 740, X = 0.0005758303, Y = 0.0002217445, Z = 0.00),
        Wave (wave = 745, X = 0.0004058303, Y = 0.0001565566, Z = 0.00),
        Wave (wave = 750, X = 0.0002856577, Y = 0.0001103928, Z = 0.00),
        Wave (wave = 755, X = 0.0002021853, Y = 0.00007827442, Z = 0.00),
        Wave (wave = 760, X = 0.000143827, Y = 0.00005578862, Z = 0.00),
        Wave (wave = 765, X = 0.0001024685, Y = 0.00003981884, Z = 0.00),
        Wave (wave = 770, X = 0.00007347551, Y = 0.00002860175, Z = 0.00),
        Wave (wave = 775, X = 0.0000525987, Y = 0.00002051259, Z = 0.00),
        Wave (wave = 780, X = 0.00003806114, Y = 0.00001487243, Z = 0.00),
        Wave (wave = 785, X = 0.00002758222, Y = 0.00001080001, Z = 0.00),
        Wave (wave = 790, X = 0.00002004122, Y = 0.00000786392, Z = 0.00),
        Wave (wave = 795, X = 0.00001458792, Y = 0.000005736935, Z = 0.00),
        Wave (wave = 800, X = 0.00001068141, Y = 0.000004211597, Z = 0.00),
        Wave (wave = 805, X = 0.000007857521, Y = 0.000003106561, Z = 0.00),
        Wave (wave = 810, X = 0.000005768284, Y = 0.000002286786, Z = 0.00),
        Wave (wave = 815, X = 0.000004259166, Y = 0.000001693147, Z = 0.00),
        Wave (wave = 820, X = 0.000003167765, Y = 0.000001262556, Z = 0.00),
        Wave (wave = 825, X = 0.000002358723, Y = 0.0000009422514, Z = 0.00),
        Wave (wave = 830, X = 0.000001762465, Y = 0.000000705386, Z = 0.00),
    )
    val list2: List<Wave> = listOf(
        Wave (wave = 360, X = 0.000000122200, Y = 0.000000013398, Z = 0.000000535027),
        Wave (wave = 365, X = 0.000000919270, Y = 0.000000100650, Z = 0.000004028300),
        Wave (wave = 370, X = 0.000005958600, Y = 0.000000651100, Z = 0.000026143700),
        Wave (wave = 375, X = 0.000033266000, Y = 0.000003625000, Z = 0.000146220000),
        Wave (wave = 380, X = 0.000159952000, Y = 0.000017364000, Z = 0.000704776000),
        Wave (wave = 385, X = 0.000662440000, Y = 0.000071560000, Z = 0.002927800000),
        Wave (wave = 390, X = 0.002361600000, Y = 0.000253400000, Z = 0.010482200000),
        Wave (wave = 395, X = 0.007242300000, Y = 0.000768500000, Z = 0.032344000000),
        Wave (wave = 400, X = 0.019109700000, Y = 0.002004400000, Z = 0.086010900000),
        Wave (wave = 405, X = 0.043400000000, Y = 0.004509000000, Z = 0.197120000000),
        Wave (wave = 410, X = 0.084736000000, Y = 0.008756000000, Z = 0.389366000000),
        Wave (wave = 415, X = 0.140638000000, Y = 0.014456000000, Z = 0.656760000000),
        Wave (wave = 420, X = 0.204492000000, Y = 0.021391000000, Z = 0.972542000000),
        Wave (wave = 425, X = 0.264737000000, Y = 0.029497000000, Z = 1.282500000000),
        Wave (wave = 430, X = 0.314679000000, Y = 0.038676000000, Z = 1.553480000000),
        Wave (wave = 435, X = 0.357719000000, Y = 0.049602000000, Z = 1.798500000000),
        Wave (wave = 440, X = 0.383734000000, Y = 0.062077000000, Z = 1.967280000000),
        Wave (wave = 445, X = 0.386726000000, Y = 0.074704000000, Z = 2.027300000000),
        Wave (wave = 450, X = 0.370702000000, Y = 0.089456000000, Z = 1.994800000000),
        Wave (wave = 455, X = 0.342957000000, Y = 0.106256000000, Z = 1.900700000000),
        Wave (wave = 460, X = 0.302273000000, Y = 0.128201000000, Z = 1.745370000000),
        Wave (wave = 465, X = 0.254085000000, Y = 0.152761000000, Z = 1.554900000000),
        Wave (wave = 470, X = 0.195618000000, Y = 0.185190000000, Z = 1.317560000000),
        Wave (wave = 475, X = 0.132349000000, Y = 0.219940000000, Z = 1.030200000000),
        Wave (wave = 480, X = 0.080507000000, Y = 0.253589000000, Z = 0.772125000000),
        Wave (wave = 485, X = 0.041072000000, Y = 0.297665000000, Z = 0.570060000000),
        Wave (wave = 490, X = 0.016172000000, Y = 0.339133000000, Z = 0.415254000000),
        Wave (wave = 495, X = 0.005132000000, Y = 0.395379000000, Z = 0.302356000000),
        Wave (wave = 500, X = 0.003816000000, Y = 0.460777000000, Z = 0.218502000000),
        Wave (wave = 505, X = 0.015444000000, Y = 0.531360000000, Z = 0.159249000000),
        Wave (wave = 510, X = 0.037465000000, Y = 0.606741000000, Z = 0.112044000000),
        Wave (wave = 515, X = 0.071358000000, Y = 0.685660000000, Z = 0.082248000000),
        Wave (wave = 520, X = 0.117749000000, Y = 0.761757000000, Z = 0.060709000000),
        Wave (wave = 525, X = 0.172953000000, Y = 0.823330000000, Z = 0.043050000000),
        Wave (wave = 530, X = 0.236491000000, Y = 0.875211000000, Z = 0.030451000000),
        Wave (wave = 535, X = 0.304213000000, Y = 0.923810000000, Z = 0.020584000000),
        Wave (wave = 540, X = 0.376772000000, Y = 0.961988000000, Z = 0.013676000000),
        Wave (wave = 545, X = 0.451584000000, Y = 0.982200000000, Z = 0.007918000000),
        Wave (wave = 550, X = 0.529826000000, Y = 0.991761000000, Z = 0.003988000000),
        Wave (wave = 555, X = 0.616053000000, Y = 0.999110000000, Z = 0.001091000000),
        Wave (wave = 560, X = 0.705224000000, Y = 0.997340000000, Z = 0.000000000000),
        Wave (wave = 565, X = 0.793832000000, Y = 0.982380000000, Z = 0.000000000000),
        Wave (wave = 570, X = 0.878655000000, Y = 0.955552000000, Z = 0.000000000000),
        Wave (wave = 575, X = 0.951162000000, Y = 0.915175000000, Z = 0.000000000000),
        Wave (wave = 580, X = 1.014160000000, Y = 0.868934000000, Z = 0.000000000000),
        Wave (wave = 585, X = 1.074300000000, Y = 0.825623000000, Z = 0.000000000000),
        Wave (wave = 590, X = 1.118520000000, Y = 0.777405000000, Z = 0.000000000000),
        Wave (wave = 595, X = 1.134300000000, Y = 0.720353000000, Z = 0.000000000000),
        Wave (wave = 600, X = 1.123990000000, Y = 0.658341000000, Z = 0.000000000000),
        Wave (wave = 605, X = 1.089100000000, Y = 0.593878000000, Z = 0.000000000000),
        Wave (wave = 610, X = 1.030480000000, Y = 0.527963000000, Z = 0.000000000000),
        Wave (wave = 615, X = 0.950740000000, Y = 0.461834000000, Z = 0.000000000000),
        Wave (wave = 620, X = 0.856297000000, Y = 0.398057000000, Z = 0.000000000000),
        Wave (wave = 625, X = 0.754930000000, Y = 0.339554000000, Z = 0.000000000000),
        Wave (wave = 630, X = 0.647467000000, Y = 0.283493000000, Z = 0.000000000000),
        Wave (wave = 635, X = 0.535110000000, Y = 0.228254000000, Z = 0.000000000000),
        Wave (wave = 640, X = 0.431567000000, Y = 0.179828000000, Z = 0.000000000000),
        Wave (wave = 645, X = 0.343690000000, Y = 0.140211000000, Z = 0.000000000000),
        Wave (wave = 650, X = 0.268329000000, Y = 0.107633000000, Z = 0.000000000000),
        Wave (wave = 655, X = 0.204300000000, Y = 0.081187000000, Z = 0.000000000000),
        Wave (wave = 660, X = 0.152568000000, Y = 0.060281000000, Z = 0.000000000000),
        Wave (wave = 665, X = 0.112210000000, Y = 0.044096000000, Z = 0.000000000000),
        Wave (wave = 670, X = 0.081260600000, Y = 0.031800400000, Z = 0.000000000000),
        Wave (wave = 675, X = 0.057930000000, Y = 0.022601700000, Z = 0.000000000000),
        Wave (wave = 680, X = 0.040850800000, Y = 0.015905100000, Z = 0.000000000000),
        Wave (wave = 685, X = 0.028623000000, Y = 0.011130300000, Z = 0.000000000000),
        Wave (wave = 690, X = 0.019941300000, Y = 0.007748800000, Z = 0.000000000000),
        Wave (wave = 695, X = 0.013842000000, Y = 0.005375100000, Z = 0.000000000000),
        Wave (wave = 700, X = 0.009576880000, Y = 0.003717740000, Z = 0.000000000000),
        Wave (wave = 705, X = 0.006605200000, Y = 0.002564560000, Z = 0.000000000000),
        Wave (wave = 710, X = 0.004552630000, Y = 0.001768470000, Z = 0.000000000000),
        Wave (wave = 715, X = 0.003144700000, Y = 0.001222390000, Z = 0.000000000000),
        Wave (wave = 720, X = 0.002174960000, Y = 0.000846190000, Z = 0.000000000000),
        Wave (wave = 725, X = 0.001505700000, Y = 0.000586440000, Z = 0.000000000000),
        Wave (wave = 730, X = 0.001044760000, Y = 0.000407410000, Z = 0.000000000000),
        Wave (wave = 735, X = 0.000727450000, Y = 0.000284041000, Z = 0.000000000000),
        Wave (wave = 740, X = 0.000508258000, Y = 0.000198730000, Z = 0.000000000000),
        Wave (wave = 745, X = 0.000356380000, Y = 0.000139550000, Z = 0.000000000000),
        Wave (wave = 750, X = 0.000250969000, Y = 0.000098428000, Z = 0.000000000000),
        Wave (wave = 755, X = 0.000177730000, Y = 0.000069819000, Z = 0.000000000000),
        Wave (wave = 760, X = 0.000126390000, Y = 0.000049737000, Z = 0.000000000000),
        Wave (wave = 765, X = 0.000090151000, Y = 0.000035540500, Z = 0.000000000000),
        Wave (wave = 770, X = 0.000064525800, Y = 0.000025486000, Z = 0.000000000000),
        Wave (wave = 775, X = 0.000046339000, Y = 0.000018338400, Z = 0.000000000000),
        Wave (wave = 780, X = 0.000033411700, Y = 0.000013249000, Z = 0.000000000000),
        Wave (wave = 785, X = 0.000024209000, Y = 0.000009619600, Z = 0.000000000000),
        Wave (wave = 790, X = 0.000017611500, Y = 0.000007012800, Z = 0.000000000000),
        Wave (wave = 795, X = 0.000012855000, Y = 0.000005129800, Z = 0.000000000000),
        Wave (wave = 800, X = 0.000009413630, Y = 0.000003764730, Z = 0.000000000000),
        Wave (wave = 805, X = 0.000006913000, Y = 0.000002770810, Z = 0.000000000000),
        Wave (wave = 810, X = 0.000005093470, Y = 0.000002046130, Z = 0.000000000000),
        Wave (wave = 815, X = 0.000003767100, Y = 0.000001516770, Z = 0.000000000000),
        Wave (wave = 820, X = 0.000002795310, Y = 0.000001128090, Z = 0.000000000000),
        Wave (wave = 825, X = 0.000002082000, Y = 0.000000842160, Z = 0.000000000000),
        Wave (wave = 830, X = 0.000001553140, Y = 0.000000629700, Z = 0.000000000000),
    )
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
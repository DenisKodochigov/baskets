package com.example.shopping_list.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Transparent = Color(0xFFFFFF)
val Green500 = Color(0xFF1EB980)
val DarkBlue900 = Color(0xFF26282F)
val ScaffoldColor = Color(0xCCECECEC)
val SelectedColor = Color(0xFFCAD6D8)
val Secondary = Color(0xFF03A9F4)
val PrimaryVariant = Color(0xBCEE1616)
val OnPrimaryOnSecondaryOnError = Color(0xFF009688)
val BackgroundBottomSheet = Color(0xFFF0F0F0)
val TextIcon = Color(0xFF464646)
val BackgroundBottomBar = Color(0xFFE0E0E0)
val BorderBottomBar = Color(0xFF464646)
val BackgroundFab = BackgroundBottomBar
val ContentFab = BorderBottomBar
/**
 *
primary - Основной цвет - это цвет, который чаще всего отображается на экранах и компонентах вашего приложения.
primaryVariant - цвет основного варианта используется для различения двух элементов приложения,
        использующих основной цвет, таких как верхняя панель приложения и системная панель.
secondary - Дополнительный цвет предоставляет больше возможностей подчеркнуть и отличить ваш продукт.
        Вторичные цвета лучше всего подходят для: Плавающие кнопки действий; Элементы управления
        выбором, такие как флажки и переключатели; Выделение выделенного текста; Ссылки и заголовки.
secondaryVariant - цвет вторичного варианта используется для различения двух элементов приложения
        с помощью вторичного цвета.
background - цвет фона отображается за прокручиваемым содержимым.
surface - цвет поверхности используется на поверхностях компонентов, таких как карточки, листы и меню.
error - цвет ошибки используется для обозначения ошибки в компонентах, таких как текстовые поля.
onPrimary - цвет, используемый для текста и значков, отображаемых поверх основного цвета.
onSecondary - цвет, используемый для текста и значков, отображаемых поверх основного цвета.
onBackground - цвет, используемый для текста и значков, отображаемых поверх цвета фона.
onSurface - цвет, используемый для текста и значков, отображаемых поверх цвета поверхности.
onError - цвет, используемый для текста и значков, отображаемых поверх цвета ошибки.
isLight - считается ли этот цвет "светлым" или "темным" набором цветов. Это влияет на поведение
        некоторых компонентов по умолчанию: например, в светлой теме TopAppBar по умолчанию будет
        использовать основной цвет фона, в то время как в темной теме он будет использовать surface.
 */
val LightColorPalette = lightColors(
    primary = SelectedColor, // Цвет кнопки и элементов управления
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    secondaryVariant = SelectedColor,
    background = ScaffoldColor,
    surface = ScaffoldColor,
    error = SelectedColor,
    onPrimary = SelectedColor,
    onSecondary = SelectedColor,
    onBackground = TextIcon, //Цвет текста и иконок в bottom bar
    onSurface = TextIcon, //Цвет текста и иконок в bottom bar
    onError = SelectedColor,
)
val DarkColorPalette = darkColors(
    primary = SelectedColor, // Цвет кнопки и элементов управления
    primaryVariant = PrimaryVariant, // Не нашел вариантов использования.
    secondary = SelectedColor,
    secondaryVariant = SelectedColor,
    background = ScaffoldColor,
    surface = ScaffoldColor,
    error = SelectedColor,
    onPrimary = OnPrimaryOnSecondaryOnError,
    onSecondary = OnPrimaryOnSecondaryOnError,
    onBackground = TextIcon,
    onSurface = TextIcon,
    onError = OnPrimaryOnSecondaryOnError,
)

// Rally is always dark themed.
val BottomColorPalette = darkColors(
    primary = SelectedColor,
    surface = ScaffoldColor,
    onSurface = Color.Black,
    background = ScaffoldColor,
    onBackground = Color.Black
)
package com.example.shopping_list.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val ScaffoldColor = Color(0xFFECECEC)
val SelectedColor = Color(0xFF595F7E)
val Secondary = Color(0xFF03A9F4)
val PrimaryVariant = Color(0xBCEE1616)
val OnPrimaryOnSecondaryOnError = Color(0xFF009688)
val BackgroundBottomSheet = Color(0xFFF0F0F0)
val TextIcon = Color(0xFF464646)
val TextDate = Color(0xFF9C9C9C)
val BackgroundBottomBar = Color(0xFFCECECE)
val BorderBottomBar = Color(0xFF464646)
val BackgroundFab = BackgroundBottomBar
val ContentFab = BorderBottomBar
val ButtonColorsMy = Color.LightGray
val SwitcherButtonColor = Color(0xFFDFDFDF)
val BackgroundElementList = Color(0x9FFFFFFF)
val SectionColor = Color(0x22909EE7)
val SectionColor1 = Color(0xFFFFA200)

val md_theme_light_primary = Color(0xFF476810)
val md_theme_light_onPrimary = Color(0xBCEE1616)
val md_theme_light_primaryContainer = Color(0xFFC7F089)
val md_theme_light_onPrimaryContainer = Color(0xffff0000)
val md_theme_light_inversePrimary = Color(0xffff0000)
val md_theme_light_secondary = Color(0xffff0000)
val md_theme_light_onSecondary = Color(0xFF009688)
val md_theme_light_secondaryContainer = Color(0xffff0000)
val md_theme_light_onSecondaryContainer = Color(0xffff0000)
val md_theme_light_tertiary = Color(0xFF757577)
val md_theme_light_onTertiary = Color(0xFFFDFCFC)
val md_theme_light_tertiaryContainer = Color(0xffff0000)
val md_theme_light_onTertiaryContainer = Color(0xffff0000)
val md_theme_light_background = Color(0xffff0000)
val md_theme_light_onBackground = Color(0xffff0000)
val md_theme_light_surface = Color(0xffff0000)
val md_theme_light_onSurface = Color(0xffff0000)
val md_theme_light_surfaceVariant = Color(0xffff0000)
val md_theme_light_onSurfaceVariant = Color(0xffff0000)
val md_theme_light_surfaceTint = Color(0xffff0000)
val md_theme_light_inverseSurface = Color(0xffff0000)
val md_theme_light_inverseOnSurface = Color(0xffff0000)
val md_theme_light_error = Color(0xffff0000)
val md_theme_light_onError = Color(0xFF009688)
val md_theme_light_errorContainer = Color(0xffff0000)
val md_theme_light_onErrorContainer = Color(0xffff0000)
val md_theme_light_outline = Color(0xffff0000)
val md_theme_light_outlineVariant = Color(0xffff0000)
val md_theme_light_scrim = Color(0xffff0000)

val md_theme_dark_primary = Color(0xFFACD370)
val md_theme_dark_onPrimary = Color(0xFF213600)
val md_theme_dark_primaryContainer = Color(0xFF324F00)
val md_theme_dark_onPrimaryContainer = Color(0xffff0000)
val md_theme_dark_inversePrimary = Color(0xffff0000)
val md_theme_dark_secondary = Color(0xffff0000)
val md_theme_dark_onSecondary = Color(0xffff0000)
val md_theme_dark_secondaryContainer = Color(0xffff0000)
val md_theme_dark_onSecondaryContainer = Color(0xffff0000)
val md_theme_dark_tertiary = Color(0xFF757577)
val md_theme_dark_onTertiary = Color(0xFFFDFCFC)
val md_theme_dark_tertiaryContainer = Color(0xffff0000)
val md_theme_dark_onTertiaryContainer = Color(0xffff0000)
val md_theme_dark_background = Color(0xffff0000)
val md_theme_dark_onBackground = Color(0xffff0000)
val md_theme_dark_surface = Color(0xffff0000)
val md_theme_dark_onSurface = Color(0xffff0000)
val md_theme_dark_surfaceVariant = Color(0xffff0000)
val md_theme_dark_onSurfaceVariant = Color(0xffff0000)
val md_theme_dark_surfaceTint = Color(0xffff0000)
val md_theme_dark_inverseSurface = Color(0xffff0000)
val md_theme_dark_inverseOnSurface = Color(0xffff0000)
val md_theme_dark_error = Color(0xffff0000)
val md_theme_dark_onError = Color(0xffff0000)
val md_theme_dark_errorContainer = Color(0xffff0000)
val md_theme_dark_onErrorContainer = Color(0xffff0000)
val md_theme_dark_outline = Color(0xffff0000)
val md_theme_dark_outlineVariant = Color(0xffff0000)
val md_theme_dark_scrim = Color(0xffff0000)

val TabHeight = 70.dp
const val InactiveTabOpacity = 0.50f
const val TabFadeInAnimationDuration = 150
const val TabFadeInAnimationDelay = 100
const val TabFadeOutAnimationDuration = 100

val LightColorScheme = lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        onPrimaryContainer = md_theme_light_onPrimaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
// ..
)
//val LightColorPalette = lightColors(
//    primary = SelectedColor, // Цвет кнопки и элементов управления
//    primaryVariant = PrimaryVariant,
//    secondary = Secondary,
//    secondaryVariant = SelectedColor,
//    background = ScaffoldColor,
//    surface = ScaffoldColor,
//    error = SelectedColor,
//    onPrimary = SelectedColor,
//    onSecondary = SelectedColor,
//    onBackground = TextIcon, //Цвет текста и иконок в bottom bar
//    onSurface = TextIcon, //Цвет текста и иконок в bottom bar
//    onError = SelectedColor,
//)
val DarkColorScheme = darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
// ..
)
//val DarkColorPalette = darkColors(
//    primary = SelectedColor, // Цвет кнопки и элементов управления
//    primaryVariant = PrimaryVariant, // Не нашел вариантов использования.
//    secondary = SelectedColor,
//    secondaryVariant = SelectedColor,
//    background = ScaffoldColor,
//    surface = ScaffoldColor,
//    error = SelectedColor,
//    onPrimary = OnPrimaryOnSecondaryOnError,
//    onSecondary = OnPrimaryOnSecondaryOnError,
//    onBackground = TextIcon,
//    onSurface = TextIcon,
//    onError = OnPrimaryOnSecondaryOnError,
//)

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
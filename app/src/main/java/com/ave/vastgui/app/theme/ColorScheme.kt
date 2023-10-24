package com.ave.vastgui.tools.theme

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.findByContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/21
// Description: 
// Documentation:
// Reference: Material3 Compose Color System | version 1.2.0-alpha10

private fun color(@ColorRes id: Int): Int =
    findByContext { getColor(id) }

val lightColorScheme = ColorScheme(
    color(R.color.md_theme_primary),
    color(R.color.md_theme_onPrimary),
    color(R.color.md_theme_primaryContainer),
    color(R.color.md_theme_onPrimaryContainer),
    color(R.color.md_theme_inversePrimary),
    color(R.color.md_theme_secondary),
    color(R.color.md_theme_onSecondary),
    color(R.color.md_theme_secondaryContainer),
    color(R.color.md_theme_onSecondaryContainer),
    color(R.color.md_theme_tertiary),
    color(R.color.md_theme_onTertiary),
    color(R.color.md_theme_tertiaryContainer),
    color(R.color.md_theme_onTertiaryContainer),
    color(R.color.md_theme_background),
    color(R.color.md_theme_onBackground),
    color(R.color.md_theme_surface),
    color(R.color.md_theme_onSurface),
    color(R.color.md_theme_surfaceVariant),
    color(R.color.md_theme_onSurfaceVariant),
    color(R.color.md_theme_surfaceTint),
    color(R.color.md_theme_inverseSurface),
    color(R.color.md_theme_inverseOnSurface),
    color(R.color.md_theme_error),
    color(R.color.md_theme_onError),
    color(R.color.md_theme_errorContainer),
    color(R.color.md_theme_onErrorContainer),
    color(R.color.md_theme_outline),
    color(R.color.md_theme_outlineVariant),
    color(R.color.md_theme_scrim),
    color(R.color.md_theme_surfaceBright),
    color(R.color.md_theme_surfaceContainer),
    color(R.color.md_theme_surfaceContainerHigh),
    color(R.color.md_theme_surfaceContainerHighest),
    color(R.color.md_theme_surfaceContainerLow),
    color(R.color.md_theme_surfaceContainerLowest),
    color(R.color.md_theme_surfaceDim)
)

val darkColorScheme = ColorScheme(
    color(R.color.md_theme_dark_primary),
    color(R.color.md_theme_dark_onPrimary),
    color(R.color.md_theme_dark_primaryContainer),
    color(R.color.md_theme_dark_onPrimaryContainer),
    color(R.color.md_theme_dark_inversePrimary),
    color(R.color.md_theme_dark_secondary),
    color(R.color.md_theme_dark_onSecondary),
    color(R.color.md_theme_dark_secondaryContainer),
    color(R.color.md_theme_dark_onSecondaryContainer),
    color(R.color.md_theme_dark_tertiary),
    color(R.color.md_theme_dark_onTertiary),
    color(R.color.md_theme_dark_tertiaryContainer),
    color(R.color.md_theme_dark_onTertiaryContainer),
    color(R.color.md_theme_dark_background),
    color(R.color.md_theme_dark_onBackground),
    color(R.color.md_theme_dark_surface),
    color(R.color.md_theme_dark_onSurface),
    color(R.color.md_theme_dark_surfaceVariant),
    color(R.color.md_theme_dark_onSurfaceVariant),
    color(R.color.md_theme_dark_surfaceTint),
    color(R.color.md_theme_dark_inverseSurface),
    color(R.color.md_theme_dark_inverseOnSurface),
    color(R.color.md_theme_dark_error),
    color(R.color.md_theme_dark_onError),
    color(R.color.md_theme_dark_errorContainer),
    color(R.color.md_theme_dark_onErrorContainer),
    color(R.color.md_theme_dark_outline),
    color(R.color.md_theme_dark_outlineVariant),
    color(R.color.md_theme_dark_scrim),
    color(R.color.md_theme_dark_surfaceBright),
    color(R.color.md_theme_dark_surfaceContainer),
    color(R.color.md_theme_dark_surfaceContainerHigh),
    color(R.color.md_theme_dark_surfaceContainerHighest),
    color(R.color.md_theme_dark_surfaceContainerLow),
    color(R.color.md_theme_dark_surfaceContainerLowest),
    color(R.color.md_theme_dark_surfaceDim)
)

class ColorScheme(
    @ColorInt val primary: Int,
    @ColorInt val onPrimary: Int,
    @ColorInt val primaryContainer: Int,
    @ColorInt val onPrimaryContainer: Int,
    @ColorInt val inversePrimary: Int,
    @ColorInt val secondary: Int,
    @ColorInt val onSecondary: Int,
    @ColorInt val secondaryContainer: Int,
    @ColorInt val onSecondaryContainer: Int,
    @ColorInt val tertiary: Int,
    @ColorInt val onTertiary: Int,
    @ColorInt val tertiaryContainer: Int,
    @ColorInt val onTertiaryContainer: Int,
    @ColorInt val background: Int,
    @ColorInt val onBackground: Int,
    @ColorInt val surface: Int,
    @ColorInt val onSurface: Int,
    @ColorInt val surfaceVariant: Int,
    @ColorInt val onSurfaceVariant: Int,
    @ColorInt val surfaceTint: Int,
    @ColorInt val inverseSurface: Int,
    @ColorInt val inverseOnSurface: Int,
    @ColorInt val error: Int,
    @ColorInt val onError: Int,
    @ColorInt val errorContainer: Int,
    @ColorInt val onErrorContainer: Int,
    @ColorInt val outline: Int,
    @ColorInt val outlineVariant: Int,
    @ColorInt val scrim: Int,
    @ColorInt val surfaceBright: Int,
    @ColorInt val surfaceContainer: Int,
    @ColorInt val surfaceContainerHigh: Int,
    @ColorInt val surfaceContainerHighest: Int,
    @ColorInt val surfaceContainerLow: Int,
    @ColorInt val surfaceContainerLowest: Int,
    @ColorInt val surfaceDim: Int,
)
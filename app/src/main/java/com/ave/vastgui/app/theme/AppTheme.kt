package com.ave.vastgui.tools.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/21
// Description: 
// Documentation:
// Reference:

object AppTheme {
    val primary: LiveData<Int>
        get() = _primary
    val onPrimary: LiveData<Int>
        get() = _onPrimary
    val primaryContainer: LiveData<Int>
        get() = _primaryContainer
    val onPrimaryContainer: LiveData<Int>
        get() = _onPrimaryContainer
    val inversePrimary: LiveData<Int>
        get() = _inversePrimary
    val secondary: LiveData<Int>
        get() = _secondary
    val onSecondary: LiveData<Int>
        get() = _onSecondary
    val secondaryContainer: LiveData<Int>
        get() = _secondaryContainer
    val onSecondaryContainer: LiveData<Int>
        get() = _onSecondaryContainer
    val tertiary: LiveData<Int>
        get() = _tertiary
    val onTertiary: LiveData<Int>
        get() = _onTertiary
    val tertiaryContainer: LiveData<Int>
        get() = _tertiaryContainer
    val onTertiaryContainer: LiveData<Int>
        get() = _onTertiaryContainer
    val background: LiveData<Int>
        get() = _background
    val onBackground: LiveData<Int>
        get() = _onBackground
    val surface: LiveData<Int>
        get() = _surface
    val onSurface: LiveData<Int>
        get() = _onSurface
    val surfaceVariant: LiveData<Int>
        get() = _surfaceVariant
    val onSurfaceVariant: LiveData<Int>
        get() = _onSurfaceVariant
    val surfaceTint: LiveData<Int>
        get() = _surfaceTint
    val inverseSurface: LiveData<Int>
        get() = _inverseSurface
    val inverseOnSurface: LiveData<Int>
        get() = _inverseOnSurface
    val error: LiveData<Int>
        get() = _error
    val onError: LiveData<Int>
        get() = _onError
    val errorContainer: LiveData<Int>
        get() = _errorContainer
    val onErrorContainer: LiveData<Int>
        get() = _onErrorContainer
    val outline: LiveData<Int>
        get() = _outline
    val outlineVariant: LiveData<Int>
        get() = _outlineVariant
    val scrim: LiveData<Int>
        get() = _scrim
    val surfaceBright: LiveData<Int>
        get() = _surfaceBright
    val surfaceContainer: LiveData<Int>
        get() = _surfaceContainer
    val surfaceContainerHigh: LiveData<Int>
        get() = _surfaceContainerHigh
    val surfaceContainerHighest: LiveData<Int>
        get() = _surfaceContainerHighest
    val surfaceContainerLow: LiveData<Int>
        get() = _surfaceContainerLow
    val surfaceContainerLowest: LiveData<Int>
        get() = _surfaceContainerLowest
    val surfaceDim: LiveData<Int>
        get() = _surfaceDim

    private val _primary = MutableLiveData(lightColorScheme.primary)
    private val _onPrimary = MutableLiveData(lightColorScheme.onPrimary)
    private val _primaryContainer = MutableLiveData(lightColorScheme.primaryContainer)
    private val _onPrimaryContainer = MutableLiveData(lightColorScheme.onPrimaryContainer)
    private val _inversePrimary = MutableLiveData(lightColorScheme.inversePrimary)
    private val _secondary = MutableLiveData(lightColorScheme.secondary)
    private val _onSecondary = MutableLiveData(lightColorScheme.onSecondary)
    private val _secondaryContainer = MutableLiveData(lightColorScheme.secondaryContainer)
    private val _onSecondaryContainer = MutableLiveData(lightColorScheme.onSecondaryContainer)
    private val _tertiary = MutableLiveData(lightColorScheme.tertiary)
    private val _onTertiary = MutableLiveData(lightColorScheme.onTertiary)
    private val _tertiaryContainer = MutableLiveData(lightColorScheme.tertiaryContainer)
    private val _onTertiaryContainer = MutableLiveData(lightColorScheme.onTertiaryContainer)
    private val _background = MutableLiveData(lightColorScheme.background)
    private val _onBackground = MutableLiveData(lightColorScheme.onBackground)
    private val _surface = MutableLiveData(lightColorScheme.surface)
    private val _onSurface = MutableLiveData(lightColorScheme.onSurface)
    private val _surfaceVariant = MutableLiveData(lightColorScheme.surfaceVariant)
    private val _onSurfaceVariant = MutableLiveData(lightColorScheme.onSurfaceVariant)
    private val _surfaceTint = MutableLiveData(lightColorScheme.surfaceTint)
    private val _inverseSurface = MutableLiveData(lightColorScheme.inverseSurface)
    private val _inverseOnSurface = MutableLiveData(lightColorScheme.inverseOnSurface)
    private val _error = MutableLiveData(lightColorScheme.error)
    private val _onError = MutableLiveData(lightColorScheme.onError)
    private val _errorContainer = MutableLiveData(lightColorScheme.errorContainer)
    private val _onErrorContainer = MutableLiveData(lightColorScheme.onErrorContainer)
    private val _outline = MutableLiveData(lightColorScheme.outline)
    private val _outlineVariant = MutableLiveData(lightColorScheme.outlineVariant)
    private val _scrim = MutableLiveData(lightColorScheme.scrim)
    private val _surfaceBright = MutableLiveData(lightColorScheme.surfaceBright)
    private val _surfaceContainer = MutableLiveData(lightColorScheme.surfaceContainer)
    private val _surfaceContainerHigh = MutableLiveData(lightColorScheme.surfaceContainerHigh)
    private val _surfaceContainerHighest = MutableLiveData(lightColorScheme.surfaceContainerHighest)
    private val _surfaceContainerLow = MutableLiveData(lightColorScheme.surfaceContainerLow)
    private val _surfaceContainerLowest = MutableLiveData(lightColorScheme.surfaceContainerLowest)
    private val _surfaceDim = MutableLiveData(lightColorScheme.surfaceDim)

    fun update(colorScheme: ColorScheme) {
        _primary.postValue(colorScheme.primary)
        _onPrimary.postValue(colorScheme.onPrimary)
        _primaryContainer.postValue(colorScheme.primaryContainer)
        _onPrimaryContainer.postValue(colorScheme.onPrimaryContainer)
        _inversePrimary.postValue(colorScheme.inversePrimary)
        _secondary.postValue(colorScheme.secondary)
        _onSecondary.postValue(colorScheme.onSecondary)
        _secondaryContainer.postValue(colorScheme.secondaryContainer)
        _onSecondaryContainer.postValue(colorScheme.onSecondaryContainer)
        _tertiary.postValue(colorScheme.tertiary)
        _onTertiary.postValue(colorScheme.onTertiary)
        _tertiaryContainer.postValue(colorScheme.tertiaryContainer)
        _onTertiaryContainer.postValue(colorScheme.onTertiaryContainer)
        _background.postValue(colorScheme.background)
        _onBackground.postValue(colorScheme.onBackground)
        _surface.postValue(colorScheme.surface)
        _onSurface.postValue(colorScheme.onSurface)
        _surfaceVariant.postValue(colorScheme.surfaceVariant)
        _onSurfaceVariant.postValue(colorScheme.onSurfaceVariant)
        _surfaceTint.postValue(colorScheme.surfaceTint)
        _inverseSurface.postValue(colorScheme.inverseSurface)
        _inverseOnSurface.postValue(colorScheme.inverseOnSurface)
        _error.postValue(colorScheme.error)
        _onError.postValue(colorScheme.onError)
        _errorContainer.postValue(colorScheme.errorContainer)
        _onErrorContainer.postValue(colorScheme.onErrorContainer)
        _outline.postValue(colorScheme.outline)
        _outlineVariant.postValue(colorScheme.outlineVariant)
        _scrim.postValue(colorScheme.scrim)
        _surfaceBright.postValue(colorScheme.surfaceBright)
        _surfaceContainer.postValue(colorScheme.surfaceContainer)
        _surfaceContainerHigh.postValue(colorScheme.surfaceContainerHigh)
        _surfaceContainerHighest.postValue(colorScheme.surfaceContainerHighest)
        _surfaceContainerLow.postValue(colorScheme.surfaceContainerLow)
        _surfaceContainerLowest.postValue(colorScheme.surfaceContainerLowest)
        _surfaceDim.postValue(colorScheme.surfaceDim)
    }
}
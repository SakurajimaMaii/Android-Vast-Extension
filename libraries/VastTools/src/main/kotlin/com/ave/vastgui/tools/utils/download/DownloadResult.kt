package com.ave.vastgui.tools.utils.download

import com.ave.vastgui.core.extension.cast

/**
 * Download result.
 *
 * @param T
 * @property value
 * @since 0.2.0
 */
class DownloadResult<out T> constructor(val value: Any?) {

    val isSuccess: Boolean get() = value !is Failure && value !is Progress

    val isFailure: Boolean get() = value is Failure

    val isProgress: Boolean get() = value is Progress

    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    companion object {
        fun <T> success(value: T): DownloadResult<T> =
            DownloadResult(value)

        fun <T> failure(exception: Throwable): DownloadResult<T> =
            DownloadResult(createFailure(exception))

        fun <T> progress(currentLength: Long, length: Long, process: Float): DownloadResult<T> =
            DownloadResult(createLoading(currentLength, length, process))
    }

    data class Failure(val exception: Throwable)

    data class Progress(val currentLength: Long, val length: Long, val process: Float)
}


private fun createFailure(exception: Throwable): DownloadResult.Failure =
    DownloadResult.Failure(exception)


private fun createLoading(currentLength: Long, length: Long, process: Float) =
    DownloadResult.Progress(currentLength, length, process)


inline fun <R, T> DownloadResult<T>.fold(
    onSuccess: (value: T) -> R,
    onLoading: (loading: DownloadResult.Progress) -> R,
    onFailure: (exception: Throwable?) -> R
): R {
    return when {
        isFailure -> {
            onFailure(exceptionOrNull())
        }

        isProgress -> {
            onLoading(value as DownloadResult.Progress)
        }

        else -> {
            onSuccess(cast(value))
        }
    }
}

/*
 * Copyright 2022 VastGui guihy2019@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.govast.vastnetstatelayout.view

import android.content.Context
import android.view.ViewStub
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import cn.govast.vastnetstatelayout.interfaces.BaseNetStateListener
import com.gcode.vastnetstatelayout.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/16
// Description:
// Documentation:

// show customized network error view
const val CONTENT_STATE_SHOW_LOADING = 0X01

// show customized network loading view
const val CONTENT_STATE_SHOW_NET_ERROR = 0X02

// show customized network loading error view
const val CONTENT_STATE_SHOW_LOADING_ERROR = 0X03

// show customized network empty view
const val CONTENT_STATE_SHOW_EMPTY_DATA = 0X04

// show customized success view
const val CONTENT_STATE_SHOW_SUCCESS = 0X05

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    CONTENT_STATE_SHOW_LOADING,
    CONTENT_STATE_SHOW_NET_ERROR,
    CONTENT_STATE_SHOW_LOADING_ERROR,
    CONTENT_STATE_SHOW_EMPTY_DATA,
    CONTENT_STATE_SHOW_SUCCESS
)
annotation class NetStateView

/**
 * [VastNetStateMgr] used to manage the view of different network status.
 *
 * Currently mainly supports four view: EMPTY DATA,NET ERROR,LOADING
 * ERROR,LOADING.
 *
 * If you want to customize the view, you can use
 * [setEmptyDataView],[setNetErrorView], [setLoadingView],[setLoadingView],
 * otherwise the default view will be used.
 */
class VastNetStateMgr(private val context: Context) : BaseNetStateListener {

    internal var loadingVs: ViewStub
        private set
    var loadingViewId = 0
        private set

    internal var netErrorRetryVs: ViewStub
        private set
    var netErrorRetryViewId = 0
        private set

    internal var emptyDataVs: ViewStub
        private set
    var emptyDataRetryViewId = 0
        private set

    internal var loadingErrorVs: ViewStub
        private set
    var loadingErrorRetryViewId = 0
        private set

    /** It will be called when the layout state is [CONTENT_STATE_SHOW_LOADING]. */
    private var loadingListener: (() -> Unit)? = null

    /**
     * It will be called when the layout state is
     * [CONTENT_STATE_SHOW_NET_ERROR].
     */
    private var netErrorListener: (() -> Unit)? = null

    /**
     * It will be called when the layout state is
     * [CONTENT_STATE_SHOW_EMPTY_DATA].
     */
    private var emptyDataListener: (() -> Unit)? = null

    /**
     * It will be called when the layout state is
     * [CONTENT_STATE_SHOW_LOADING_ERROR].
     */
    private var loadingErrorListener: (() -> Unit)? = null

    /**
     * Set [loadingViewId] and [loadingVs]
     *
     * @param loadingViewId Your custom net loading view id.
     */
    fun setLoadingView(@LayoutRes loadingViewId: Int) = apply {
        this.loadingViewId = loadingViewId
        loadingVs = ViewStub(context).apply {
            layoutResource = loadingViewId
        }
    }

    /**
     * Set [netErrorRetryViewId] and [netErrorRetryVs]
     *
     * @param netErrorRetryViewId Your custom net error view id.
     */
    fun setNetErrorView(@LayoutRes netErrorRetryViewId: Int) = apply {
        this.netErrorRetryViewId = netErrorRetryViewId
        netErrorRetryVs = ViewStub(context).apply {
            layoutResource = netErrorRetryViewId
        }
    }

    /**
     * Set [emptyDataRetryViewId] and [emptyDataVs]
     *
     * @param emptyDataRetryViewId Your custom empty data view id.
     */
    fun setEmptyDataView(@LayoutRes emptyDataRetryViewId: Int) = apply {
        this.emptyDataRetryViewId = emptyDataRetryViewId
        emptyDataVs = ViewStub(context).apply {
            layoutResource = emptyDataRetryViewId
        }
    }

    /**
     * Set [loadingErrorRetryViewId] and [loadingErrorVs]
     *
     * @param loadingErrorRetryViewId Your custom loading error view id.
     */
    fun setLoadingErrorView(@LayoutRes loadingErrorRetryViewId: Int) = apply {
        this.loadingErrorRetryViewId = loadingErrorRetryViewId
        loadingErrorVs = ViewStub(context).apply {
            layoutResource = loadingErrorRetryViewId
        }
    }

    /**
     * You can set the view click event including the following status:**NET
     * ERROR**.
     *
     * @since 0.0.4
     */
    fun setNetErrorListener(netErrorListener: () -> Unit) = apply {
        this.netErrorListener = netErrorListener
    }

    /**
     * You can set the view click event including the following
     * status:**LOADING**.
     *
     * @since 0.0.4
     */
    fun setLoadingListener(loadingListener: () -> Unit) = apply {
        this.loadingListener = loadingListener
    }

    /**
     * You can set the view click event including the following status:**EMPTY
     * DATA**.
     *
     * @since 0.0.4
     */
    fun setEmptyDataListener(emptyDataListener: () -> Unit) = apply {
        this.emptyDataListener = emptyDataListener
    }

    /**
     * You can set the view click event including the following
     * status:**LOADING ERROR**.
     *
     * @since 0.0.4
     */
    fun setLoadingErrorListener(loadingErrorListener: () -> Unit) = apply {
        this.loadingErrorListener = loadingErrorListener
    }

    init {
        // Load default loading view.
        loadingVs = ViewStub(context).apply {
            layoutResource = R.layout.simple_loading_view
        }
        loadingViewId = R.layout.simple_loading_view
        // Load net error view.
        netErrorRetryVs = ViewStub(context).apply {
            layoutResource = R.layout.simple_net_error_view
        }
        netErrorRetryViewId = R.layout.simple_net_error_view
        // Load default empty data view.
        emptyDataVs = ViewStub(context).apply {
            layoutResource = R.layout.simple_empty_data_view
        }
        emptyDataRetryViewId = R.layout.simple_empty_data_view
        // Load default loading error view.
        loadingErrorVs = ViewStub(context).apply {
            layoutResource = R.layout.simple_loading_error_view
        }
        loadingErrorRetryViewId = R.layout.simple_loading_error_view
    }

    override fun onEmptyData() {
        emptyDataListener?.invoke()
    }

    override fun onLoadingError() {
        loadingErrorListener?.invoke()
    }

    override fun onLoading() {
        loadingListener?.invoke()
    }

    override fun onNetWorkError() {
        netErrorListener?.invoke()
    }
}
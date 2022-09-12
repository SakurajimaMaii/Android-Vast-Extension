/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastnetstatelayout.view

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.widget.FrameLayout
import com.gcode.vastnetstatelayout.interfaces.BaseNetStateLayout

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/5
// Description:
// Documentation:

/**
 * [VastNetStateLayout] is a layout to set customized network state ui.
 *
 * You can use [showLoading]、[showLoadingError]、[showNetError]、[showEmptyData]
 * to show the net state view.
 *
 * If you want to customize the net state view or click events,please set
 * [vastNetStateMgr],like this:
 *
 * ```kotlin
 * val vastNetStateMgr = VastNetStateMgr(this)
 * vastNetStateMgr.setLoadingView(R.layout.simple_net_error_view)
 * vastNetStateMgr.setLoadingErrorListener(object :VastLoadingErrorListener{
 *          override fun onLoadingError() {
 *                  // Something to do when loading error
 *          }
 * })
 * mNetStateLayout.setVastNetStateMgr(vastNetStateMgr)
 * ```
 *
 * If you don't set the [vastNetStateMgr],it will take the default value
 * when you call a method like [showLoading].
 *
 * @since 0.0.1
 */
class VastNetStateLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,defStyleAttr:Int = 0
) : FrameLayout(context, attrs, defStyleAttr),BaseNetStateLayout {
    /**
     * Set default vastNetStateMgr.
     */
    lateinit var vastNetStateMgr:VastNetStateMgr
        private set

    /**
     * Layout sparse array.
     */
    private val layoutSparseArray: SparseArray<View> = SparseArray<View>()

    override fun setVastNetStateMgr(mgr: VastNetStateMgr){
        vastNetStateMgr = mgr
        addNetStateView()
    }

    /**
     * Add all different state layouts to the frame layout
     */
    private fun addNetStateView(){
        vastNetStateMgr.apply {
            addView(loadingVs,LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            addView(netErrorRetryVs,LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            addView(emptyDataVs,LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            addView(loadingErrorVs,LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }
    }

    override fun showLoading(){
        if(!::vastNetStateMgr.isInitialized){
            vastNetStateMgr = VastNetStateMgr(context)
            addNetStateView()
        }
        if(inflateSvLayout(CONTENT_STATE_SHOW_LOADING)){
            showHideViewById(CONTENT_STATE_SHOW_LOADING)
        }
    }

    override fun showNetError(){
        if(!::vastNetStateMgr.isInitialized){
            vastNetStateMgr = VastNetStateMgr(context)
            addNetStateView()
        }
        if(inflateSvLayout(CONTENT_STATE_SHOW_NET_ERROR)){
            showHideViewById(CONTENT_STATE_SHOW_NET_ERROR)
        }
    }

    override fun showLoadingError(){
        if(!::vastNetStateMgr.isInitialized){
            vastNetStateMgr = VastNetStateMgr(context)
            addNetStateView()
        }
        if(inflateSvLayout(CONTENT_STATE_SHOW_LOADING_ERROR)){
            showHideViewById(CONTENT_STATE_SHOW_LOADING_ERROR)
        }
    }

    override fun showEmptyData(){
        if(!::vastNetStateMgr.isInitialized){
            vastNetStateMgr = VastNetStateMgr(context)
            addNetStateView()
        }
        if(inflateSvLayout(CONTENT_STATE_SHOW_EMPTY_DATA)){
            showHideViewById(CONTENT_STATE_SHOW_EMPTY_DATA)
        }
    }

    override fun showSuccess(){
        if(!::vastNetStateMgr.isInitialized){
            vastNetStateMgr = VastNetStateMgr(context)
            addNetStateView()
        }
        showHideViewById(CONTENT_STATE_SHOW_SUCCESS)
    }

    /**
     * Show or hide view according to the [layoutId]
     */
    private fun showHideViewById(@NetStateView layoutId: Int) {
        for (i in 0 until layoutSparseArray.size()) {
            val key = layoutSparseArray.keyAt(i)
            val valueView = layoutSparseArray.valueAt(i)
            if (key == layoutId) {
                valueView.visibility = VISIBLE
            } else {
                if (valueView.visibility != GONE) {
                    valueView.visibility = GONE
                }
            }
        }
    }

    /**
     * Mainly to inflate the ViewStub layout,
     * such as network view, loading view, and
     * empty data view
     *
     * @return Whether the layout corresponding to layoutId is shown or not.
     */
    private fun inflateSvLayout(@NetStateView layoutId:Int):Boolean{
        var isShow = true
        when(layoutId){
            CONTENT_STATE_SHOW_LOADING->{
                isShow = run {
                    val view = vastNetStateMgr.loadingVs.inflate()
                    view.setOnClickListener {
                        vastNetStateMgr.loadingListener?.onLoading()
                    }
                    layoutSparseArray.put(layoutId, view)
                    true
                }
            }
            CONTENT_STATE_SHOW_NET_ERROR->{
                isShow = run {
                    val view = vastNetStateMgr.netErrorRetryVs.inflate()
                    view.setOnClickListener {
                        vastNetStateMgr.netErrorListener?.onNetWorkError()
                    }
                    layoutSparseArray.put(layoutId,view)
                    true
                }
            }
            CONTENT_STATE_SHOW_LOADING_ERROR->{
                isShow = run {
                    val view = vastNetStateMgr.loadingErrorVs.inflate()
                    view.setOnClickListener {
                        vastNetStateMgr.loadingErrorListener?.onLoadingError()
                    }
                    layoutSparseArray.put(layoutId,view)
                    true
                }
            }
            CONTENT_STATE_SHOW_EMPTY_DATA ->{
                isShow = run {
                    val view = vastNetStateMgr.emptyDataVs.inflate()
                    view.setOnClickListener {
                        vastNetStateMgr.emptyDataListener?.onEmptyData()
                    }
                    layoutSparseArray.put(layoutId,view)
                    true
                }
            }
            CONTENT_STATE_SHOW_SUCCESS -> {
                TODO()
            }
        }
        return isShow
    }
}
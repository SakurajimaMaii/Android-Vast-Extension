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

package com.ave.vastgui.app.activity.adpexample.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import androidx.annotation.IntRange
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import com.ave.vastgui.adapter.VastBindAdapter
import com.ave.vastgui.adapter.base.BaseBindHolder
import com.ave.vastgui.adapter.widget.AdapterItem
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.app.BR

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/28
// Description:
// Documentation:

class BindAdapter2(
    dataSource: MutableList<AdapterItemWrapper<*>>,
    mContext: Context
) : VastBindAdapter(dataSource, mContext) {

    companion object {
        @JvmStatic
        @BindingAdapter("android:src")
        fun setImageResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }

    /**
     * Returns `true` if the collection is empty (contains no elements),
     * `false` otherwise.
     *
     * @return Boolean
     */
    fun isItemEmpty() = mDataSource.isEmpty()

    /**
     * Return item from list by position.
     *
     * @param position Int
     * @return BaseItem?
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun getItemByPos(@IntRange(from = 0) position: Int): AdapterItem {
        if (position >= itemCount || position < 0) {
            throw ArrayIndexOutOfBoundsException("The parameter pos should be less than ${mDataSource.size}")
        }
        return mDataSource[position]
    }

    /**
     * Adds the specified item to the end of this list. If you want to add in
     * other position,please refer [addItemByPos]
     *
     * @param item Item you add
     * @return The result `false` means adding failed or item is `null`
     */
    fun addItem(item: AdapterItemWrapper<*>?): Boolean {
        return if (item == null) {
            false
        } else {
            val flag = mDataSource.add(item)
            if (flag) {
                notifyItemInserted(this.itemCount - 1)
            }
            flag
        }
    }

    /** Inserts an element into the list at the specified [pos]. */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun addItemByPos(item: AdapterItemWrapper<*>, @IntRange(from = 0) pos: Int) {
        if (pos > mDataSource.size) {
            throw ArrayIndexOutOfBoundsException("The range of the parameter pos in the addItemByPos() method is wrong")
        }
        mDataSource.add(pos, item)
        notifyItemInserted(pos)
    }

    /**
     * Inserts all of the elements of the specified collection [addItems] into
     * this list at the specified [pos].
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun addItemsByPos(addItems: MutableList<AdapterItemWrapper<Any>>, @IntRange(from = 0) pos: Int) {
        if (pos > mDataSource.size) {
            throw ArrayIndexOutOfBoundsException("The parameter pos cannot be greater than ${mDataSource.size}")
        }
        mDataSource.addAll(pos, addItems)
        notifyItemRangeChanged(pos, addItems.size)
    }

    /** Removes an element from the list by [item]. */
    fun removeItemByObj(item: AdapterItem?): Boolean {
        val pos: Int = mDataSource.indexOf(item)
        if (pos >= 0 && pos < mDataSource.size) {
            removeItemByPos(pos)
        }
        return pos >= 0 && pos < mDataSource.size
    }

    /** Removes an element at the specified [pos] from the list. */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun removeItemByPos(@IntRange(from = 0) pos: Int): AdapterItem? {
        return if (mDataSource.isEmpty())
            null
        else {
            if (pos >= mDataSource.size || pos < 0) {
                throw ArrayIndexOutOfBoundsException("The range of the parameter pos should be between 0 and ${mDataSource.size - 1}.")
            }
            val item: AdapterItem = mDataSource.removeAt(pos)
            notifyItemRemoved(pos)
            item
        }
    }

    /** Delete the elements in the range from [startPos] to [endPos] */
    @SuppressLint("NotifyDataSetChanged")
    fun removeItemsByPos(
        @IntRange(from = 0) startPos: Int,
        @IntRange(from = 0) endPos: Int,
        includeEndPos: Boolean = false
    ) {
        if (endPos - startPos >= mDataSource.size) {
            throw ArrayIndexOutOfBoundsException("The deleted range is larger than the size of the array itself")
        } else if (startPos > endPos) {
            throw Exception("startPos should be smaller than endPos")
        } else if (startPos >= mDataSource.size || endPos >= mDataSource.size) {
            throw ArrayIndexOutOfBoundsException("The parameter startPos or endPos should be less than ${mDataSource.size - 1}.")
        } else {
            // See https://www.kaelli.com/23.html
            if (includeEndPos) {
                mDataSource.subList(startPos, endPos + 1).clear()
            } else {
                mDataSource.subList(startPos, endPos).clear()
            }
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItem() {
        if (mDataSource.isNotEmpty()) {
            mDataSource.clear()
            notifyDataSetChanged()
        }
    }

    class MyVH(binding: ViewDataBinding) : BaseBindHolder(binding) {

    }

    override fun setViewHolder(binding: ViewDataBinding): MyVH {
        return MyVH(binding)
    }

    override fun setVariableId(): Int {
        return BR.item
    }
}
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

package cn.govast.vastutils.activity.baseadpexample

import android.annotation.SuppressLint
import androidx.annotation.IntRange
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.base.BaseViewHolder
import cn.govast.vastadapter.recycleradpter.VastAdapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/19
// Description:
// Documentation:

class BaseAdapter(
    private val items: MutableList<AdapterItem>,
    factories: MutableList<BaseViewHolder.BVAdpVHFactory>
) : VastAdapter(items, factories) {

    /**
     * Returns `true` if the collection is empty (contains no elements), `false` otherwise.
     * @return Boolean
     */
    fun isItemEmpty() = items.isEmpty()

    /**
     * Return item by position
     * @param pos
     * @return item you get
     */
    fun getItemByPos(@IntRange(from = 0) pos: Int): AdapterItem {
        if (pos >= items.size) {
            throw ArrayIndexOutOfBoundsException("The parameter pos should be less than ${items.size}")
        }
        return items[pos]
    }

    /**
     * Adds the specified element to the end of this list.
     * If you want to add in other specified `pos`, please refer to [addItemByPos]
     * @param item Item you want to add.
     * @return The result `false` means adding failed or item is `null`
     */
    fun addItem(item: AdapterItem?): Boolean {
        return if (item != null) {
            val flag = items.add(item)
            if (flag) {
                notifyItemInserted(this.itemCount - 1)
            }
            flag
        } else {
            false
        }
    }

    /**
     * Inserts an element into the list at the specified [pos].
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun addItemByPos(item: AdapterItem, @IntRange(from = 0) pos: Int) {
        if (pos > items.size) {
            throw ArrayIndexOutOfBoundsException("The range of the parameter pos in the addItemByPos() method is wrong")
        }
        items.add(pos, item)
        notifyItemInserted(pos)
    }

    /**
     * Inserts all of the elements of the specified collection [addItems] into this list at the specified [pos].
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun addItemsByPos(addItems: MutableList<AdapterItem>, @IntRange(from = 0) pos: Int) {
        if (pos > items.size) {
            throw ArrayIndexOutOfBoundsException("The parameter pos cannot be greater than ${items.size}")
        }
        items.addAll(pos, addItems)
        notifyItemRangeChanged(pos, addItems.size)
    }

    /**
     * Delete Item by object
     * @param item The object to be deleted
     * @return Return the result of the operation
     */
    fun removeItemByObj(item: AdapterItem?): Boolean {
        val pos: Int = items.indexOf(item)
        if (pos >= 0 && pos < items.size) {
            removeItemByPos(pos)
        }
        return pos >= 0 && pos < items.size
    }

    /**
     * Removes an element at the specified [pos] from the list.
     *
     * @return the element that has been removed.
     */
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun removeItemByPos(@IntRange(from = 0) pos: Int): AdapterItem? {
        return if (items.isEmpty())
            null
        else {
            if (pos >= items.size || pos < 0) {
                throw ArrayIndexOutOfBoundsException("The range of the parameter pos should be between 0 and ${items.size - 1}.")
            }
            val item: AdapterItem = items.removeAt(pos)
            notifyItemRemoved(pos)
            item
        }
    }

    /**
     * Delete the elements in the range from [startPos] to [endPos]
     * @param startPos
     * @param endPos
     * @param includeEndPos `true` Indicates that the element pointed to by [endPos] is contained
     */
    @SuppressLint("NotifyDataSetChanged")
    fun removeItemsByPos(
        @IntRange(from = 0) startPos: Int,
        @IntRange(from = 0) endPos: Int,
        includeEndPos: Boolean = false
    ) {
        if (endPos - startPos >= items.size) {
            throw ArrayIndexOutOfBoundsException("The deleted range is larger than the size of the array itself")
        } else if (startPos > endPos) {
            throw Exception("startPos should be smaller than endPos")
        } else if (startPos >= items.size || endPos >= items.size) {
            throw ArrayIndexOutOfBoundsException("The parameter startPos or endPos should be less than ${items.size - 1}.")
        } else {
            // See https://www.kaelli.com/23.html
            if (includeEndPos) {
                items.subList(startPos, endPos + 1).clear()
            } else {
                items.subList(startPos, endPos).clear()
            }
            notifyDataSetChanged()
        }
    }

    /**
     * Clear all elements in the list
     */
    @SuppressLint("NotifyDataSetChanged")
    fun clearItem() {
        if (items.isNotEmpty()) {
            items.clear()
            this.notifyDataSetChanged()
        }
    }
}
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

package com.gcode.vasttools.adapter


import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import com.gcode.vasttools.adapter.VastBaseAdapter.VastBaseViewHolder


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/19
// Description: A base adapter for ListView.
// Documentation:

/**
 * A base adapter for [ListView].
 *
 * [VastBaseAdapter] usage example:
 *
 * Firstly,make sure the item implements the [VastBaseAdapter.BaseItem]:
 * ```kt
 * class LVIA(
 *      val string: String
 * ):VastBaseAdapter.BaseItem {
 *      override fun getItemType(): String {
 *              return LVIA::class.java.simpleName
 *      }
 * }
 * ```
 *
 * Secondly,creating vh by extending [VastBaseViewHolder] and implements the [VastBaseViewHolder.BaseFactory]:
 * ```kt
 * class LVIAVH(val item: View):
 *      VastBaseAdapter.VastBaseViewHolder(item) {
 *
 *          private val tv: TextView = itemView.findViewById(R.id.lvia_content)
 *
 *          override fun bindData(item:  VastBaseAdapter.BaseItem) {
 *                  tv.text = (item as LVIA).string
 *          }
 *
 *          class Factory:BaseFactory{
 *                  override fun getViewHolder(
 *                      context: Context,convertView: View?,parent: ViewGroup?
 *                  ): VastBaseAdapter.VastBaseViewHolder {
 *                      val view = LayoutInflater.from(context).inflate(R.layout.lv_item_lvia, null)
 *                      return LVIAVH(view)
 *                  }
 *
 *                  override fun getItemType(): String {
 *                      return LVIAVH::class.java.simpleName
 *                  }
 *          }
 *
 * }
 * ```
 *
 * Finally,creating adapter by extending [VastBaseAdapter]:
 * ```kt
 * class ListViewAdapter(context: Context,
 *      dataSources: ArrayList<BaseItem>,
 *      factories: MutableList<VastBaseViewHolder.BaseFactory>
 * ) : BaseListViewAdapter(context, dataSources, factories)
 * ```
 *
 * @property context context.
 * @property dataSources the source data.
 * @property factories the set of [VastBaseAdapter.VastBaseViewHolder.BaseFactory] so that [VastBaseAdapter] can adapt to different VH.
 *
 * @since 0.0.9
 */
abstract class VastBaseAdapter(
    protected val context: Context,
    protected val dataSources: ArrayList<BaseItem>,
    protected val factories: MutableList<VastBaseViewHolder.BaseFactory>
) : BaseAdapter() {

    private val layoutId2FactoriesIndex: MutableMap<String, Int> = HashMap()

    init {
        for (i in factories.indices) {
            val factory = factories[i]
            val layoutId = factory.getItemType()
            if (null != layoutId2FactoriesIndex[layoutId]) {
                throw RuntimeException("Same type found in factories.Please check whether the item and VH's getLayoutId method correspond one-to-one.")
            }
            layoutId2FactoriesIndex[layoutId] = i
        }
    }

    override fun getCount() = dataSources.size

    override fun getItem(position: Int) = dataSources[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val index = layoutId2FactoriesIndex[dataSources[position].getItemType()]
            ?: throw RuntimeException("Can't found the index in layoutId2FactoriesIndex.")
        val factory = factories[index]
        val viewHolder =
            factory.getViewHolder(context, convertView, parent)
        viewHolder.bindData(dataSources[position])
        return viewHolder.getConvertView()
    }

    /**
     * Please make sure that the item implements [BaseItem].
     *
     * @since 0.0.9
     */
    interface BaseItem{
        /**
         * @return the type string of the item.This value should
         *         is the same as the return value of the corresponding
         *         ViewHolder's getItemType method.
         * @since 0.0.9
         */
        fun getItemType():String
    }

    /**
     * The base ViewHolder of the [VastBaseAdapter].
     *
     * @since 0.0.9
     */
    open class VastBaseViewHolder (
        protected val itemView: View,
    ) {

        interface BaseFactory {

            /**
             * Used to let [VastBaseAdapter] get the created VH object.
             *
             * @since 0.0.9
             */
            fun getViewHolder(
                context: Context,
                convertView: View?,
                parent: ViewGroup?
            ): VastBaseViewHolder

            /**
             * @return the type string of the item.This value should be the
             *         same as the return value of the item's get method.
             * @since 0.0.9
             */
            fun getItemType():String

        }

        fun getConvertView(): View {
            return itemView
        }

        open fun bindData(item: BaseItem) {

        }

    }

}
/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.adapter.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.ave.vastgui.adapter.base.ItemHolder
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Images
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/5

class DefaultImageHolder(itemView: View) : ItemHolder<Images.Image>(itemView) {
    private val image: ShapeableImageView = itemView.findViewById(R.id.iidImage)
    private val title: MaterialTextView = itemView.findViewById(R.id.iidTitle)

    override fun onBindData(item: Images.Image) {
        image.load(item.url) {
            crossfade(true)
            placeholder(R.drawable.background)
        }
        title.text = item.title
    }

    companion object : HolderFactory<Images.Image> {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): DefaultImageHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return DefaultImageHolder(view)
        }

        override val layoutId: Int
            get() = R.layout.item_image_default
    }
}

class ComicImageHolder(itemView: View) : ItemHolder<Images.Image>(itemView) {
    private val image: ShapeableImageView = itemView.findViewById(R.id.iicImage)
    private val title: MaterialTextView = itemView.findViewById(R.id.iicTitle)

    override fun onBindData(item: Images.Image) {
        image.load(item.url) {
            crossfade(true)
            placeholder(R.drawable.background)
        }
        title.text = item.title
    }

    companion object : HolderFactory<Images.Image> {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): ComicImageHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
            return ComicImageHolder(view)
        }

        override val layoutId: Int
            get() = R.layout.item_image_comic
    }
}
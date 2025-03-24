/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.vbdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ItemArticleBinding
import com.ave.vastgui.app.net.model.Articles
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/26
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#viewholder

class ArticleAdpt(private val data: MutableList<Articles.Article> = ArrayList()) :
    RecyclerView.Adapter<ArticleAdpt.ArticleVH>() {

    class ArticleVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 使用方式 1
        private val binding by viewBinding(ItemArticleBinding::bind, R.id.article_root)

        // 使用方式 2
        // private val binding by viewBinding(ItemArticleBinding::bind)
        val title = binding.articleTitle
        val shareUser = binding.articleShareUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleVH(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        val article = data[position]
        holder.title.text = article.title
        holder.shareUser.text = article.shareUser
    }

    /**
     * 添加文章
     */
    fun addArticles(articles: List<Articles.Article>) {
        val length = articles.size
        data.addAll(itemCount, articles)
        notifyItemRangeInserted(itemCount, itemCount + length)
    }

    /**
     * 清空所有文章
     */
    fun clearAllArticles() {
        val length = itemCount
        data.clear()
        notifyItemRangeChanged(0, length)
    }

}
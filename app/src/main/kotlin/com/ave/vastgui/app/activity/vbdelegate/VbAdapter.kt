package com.ave.vastgui.app.activity.vbdelegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Person
import com.ave.vastgui.app.databinding.ItemPersonBinding
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/26
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/view-bind/vb-delegate/#viewholder

val persons = listOf(Person("张", "三"), Person("李", "四"), Person("王", "五"))

class VbAdapter(private val data: List<Person>) : RecyclerView.Adapter<VbAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding by viewBinding(ItemPersonBinding::bind)
        val name = binding.name
        val sentence = binding.sentence
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_default, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val person = data[position]
        holder.name.text = person.name
        holder.sentence.text = person.sentence
    }

}

class VbAdapter2(private val data: List<Person>) : RecyclerView.Adapter<VbAdapter2.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding by viewBinding(ItemPersonBinding::bind, R.id.item_textview_root)
        val name = binding.name
        val sentence = binding.sentence
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_default, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val person = data[position]
        holder.name.text = person.name
        holder.sentence.text = person.sentence
    }

}
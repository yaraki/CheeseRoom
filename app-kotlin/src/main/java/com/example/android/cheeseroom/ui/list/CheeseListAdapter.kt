/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.cheeseroom.ui.list

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.widget.TextViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.cheeseroom.R
import com.example.android.cheeseroom.common.Cheeses
import com.example.android.cheeseroom.vo.Cheese
import java.util.*


internal class CheeseListAdapter(
        context: Context,
        private val onCheeseSelected: (Long) -> Unit)
    : RecyclerView.Adapter<CheeseListAdapter.CheeseViewHolder>() {

    private val cheeses = ArrayList<Cheese>()

    private val favoriteDrawable = VectorDrawableCompat.create(context.resources,
            R.drawable.ic_favorite, context.theme)?.apply {
        DrawableCompat.setTint(this, Color.WHITE)
    }

    private val onClickListener = View.OnClickListener {
        onCheeseSelected(it.getTag(R.id.cheese_id) as Long)
    }

    fun setCheeses(cheeses: List<Cheese>?) {
        this.cheeses.clear()
        if (cheeses == null) {
            return
        }
        this.cheeses.addAll(cheeses)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CheeseViewHolder(parent, onClickListener)

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) =
            holder.bind(cheeses[position], favoriteDrawable)

    override fun getItemCount() = cheeses.size

    class CheeseViewHolder(parent: ViewGroup, onClickListener: View.OnClickListener)
        : RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.cheese_list_item, parent, false)) {

        private var image = itemView.findViewById<ImageView>(R.id.image)
        private var name = itemView.findViewById<TextView>(R.id.name)

        init {
            itemView.setOnClickListener(onClickListener)
            ViewGroupCompat.setTransitionGroup(itemView as ViewGroup, true)
        }

        fun bind(cheese: Cheese, favoriteDrawable: Drawable?) {
            itemView.setTag(R.id.cheese_id, cheese.id)
            image.setImageResource(Cheeses.getDrawableForCheese(cheese.name))
            name.text = cheese.name
            TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(name,
                    null, null, if (cheese.favorite) favoriteDrawable else null, null)
        }

    }

}

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

package com.example.android.cheeseroom.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Fade
import com.example.android.cheeseroom.R
import com.example.android.cheeseroom.common.Cheeses
import com.google.android.material.floatingactionbutton.FloatingActionButton


class CheeseDetailFragment : Fragment() {

    companion object {
        private const val ARG_CHEESE_ID = "cheese_id"

        fun newInstance(cheeseId: Long) = CheeseDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_CHEESE_ID, cheeseId)
            }
        }
    }

    private lateinit var viewModel: CheeseDetailViewModel

    private lateinit var image: ImageView
    private lateinit var name: TextView
    private lateinit var favorite: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = Fade()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val cheeseId = arguments?.getLong(ARG_CHEESE_ID) ?: 0

        viewModel = ViewModelProviders.of(this).get(CheeseDetailViewModel::class.java)
        viewModel.setCheeseId(cheeseId)
        viewModel.cheese.observe(this, Observer {
            it?.let { cheese ->
                // The name
                name.text = cheese.name
                // Show the image
                image.setImageResource(Cheeses.getDrawableForCheese(cheese.name))
                //
                favorite.isEnabled = true
                if (cheese.favorite) {
                    favorite.setImageResource(R.drawable.ic_favorite)
                    favorite.contentDescription = getString(R.string.cheese_favorite_true)
                } else {
                    favorite.setImageResource(R.drawable.ic_favorite_border)
                    favorite.contentDescription = getString(R.string.cheese_favorite_mark)
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cheese_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        image = view.findViewById(R.id.image)
        name = view.findViewById(R.id.name)
        favorite = view.findViewById(R.id.favorite)
        favorite.setOnClickListener {
            favorite.isEnabled = false
            viewModel.toggleFavorite()
        }
    }

}

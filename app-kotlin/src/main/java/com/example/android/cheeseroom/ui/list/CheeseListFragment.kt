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

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.transition.Slide
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.cheeseroom.R


class CheeseListFragment : Fragment() {

    private lateinit var adapter: CheeseListAdapter

    interface Listener {
        fun onCheeseSelected(cheeseId: Long)
    }

    companion object {
        fun newInstance() = CheeseListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Slide(GravityCompat.START).apply { duration = 150 }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(CheeseListViewModel::class.java)
        viewModel.cheeses.observe(this, Observer { adapter.setCheeses(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cheese_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<RecyclerView>(R.id.list).run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = CheeseListAdapter(view.context, { cheeseId ->
                activity?.let {
                    if (!it.isFinishing) {
                        (it as Listener).onCheeseSelected(cheeseId)
                    }
                }
            }).also {
                this@CheeseListFragment.adapter = it
            }
        }
    }

}

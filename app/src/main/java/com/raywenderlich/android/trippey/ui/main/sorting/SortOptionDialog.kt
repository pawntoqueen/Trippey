/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.raywenderlich.android.trippey.ui.main.sorting

import android.content.Context
import android.os.Bundle
import android.os.ConditionVariable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.raywenderlich.android.trippey.App
import com.raywenderlich.android.trippey.R
import com.raywenderlich.android.trippey.model.*
import com.raywenderlich.android.trippey.repository.TrippeyRepositoryImpl.Companion.KEY_SORT_OPTION
import kotlinx.android.synthetic.main.dialog_sorting.*

class SortOptionDialog(
  private val onSortOptionsSelected: (SortOption) -> Unit
) : DialogFragment() {

  private val repository by lazy { App.repository }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_sorting, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initUi()
  }

  private fun initUi() {
    confirmButton.setOnClickListener {
      onSortOptionSelected()
    }

    val currentSort = getSortOption()

    sortOptions.check(when (currentSort) {
      ByName -> R.id.sortByTitle
      ByNumberOfLocations -> R.id.sortByNumberOfLocations
      else -> R.id.noSort
    })
  }

  private fun getSortOption(): SortOption {
    val localPreferences = activity?.getPreferences(Context.MODE_PRIVATE)

    return getSortOptionFromName(
      localPreferences?.getString(KEY_SORT_OPTION, "") ?: ""
    )
  }

  private fun onSortOptionSelected() {
    val selectedOption = sortOptions.checkedRadioButtonId

    onSortOptionsSelected(when (selectedOption) {
      R.id.sortByNumberOfLocations -> ByNumberOfLocations
      R.id.sortByTitle -> ByName
      else -> None
    })

    dismissAllowingStateLoss()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NO_TITLE, R.style.FragmentDialogTheme)
  }

  override fun onStart() {
    super.onStart()
    dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.WRAP_CONTENT)
  }
}
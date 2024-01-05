package com.eva.pexelsapp.presentation.feature_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eva.pexelsapp.databinding.HomeSearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeSearchFragment : Fragment() {

	private val viewModel by activityViewModels<SearchPhotoViewModel>()

	private var _binding: HomeSearchFragmentBinding? = null

	private val binding: HomeSearchFragmentBinding
		get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		_binding = HomeSearchFragmentBinding.inflate(inflater)

		val context = requireContext()

		val adapter = PhotoResultsViewAdapter(context)

		binding.searchResults.apply {
			this.adapter = adapter
			layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)

			lifecycleScope.launch {
				viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
					viewModel.searchResults.collect(adapter::submitData)
				}
			}
		}

		val searchBar = binding.searchView
		searchBar.editText.setOnEditorActionListener { view, action, _ ->
			val searchQuery = view.text.toString()
			if (action == EditorInfo.IME_ACTION_SEARCH && searchQuery.isNotEmpty()) {
				viewModel.onSearch(searchQuery)
				true
			} else false
		}

		return binding.root
	}

}
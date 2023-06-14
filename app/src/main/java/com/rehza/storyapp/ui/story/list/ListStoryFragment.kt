package com.rehza.storyapp.ui.story.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.rehza.storyapp.R
import com.rehza.storyapp.databinding.FragmentListStoryBinding
import com.rehza.storyapp.util.Resource
import com.rehza.storyapp.util.ViewModelFactory


class ListStoryFragment : Fragment() {

    private lateinit var binding: FragmentListStoryBinding
    private lateinit var viewModel: ListStoryViewModel
    private lateinit var adapter: ListStoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListStoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[ListStoryViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupLayoutManager()
        setupViewModelObserver()

        binding.fabCreateStory.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_listStoryFragment_to_createStoryFragment))
    }

    private fun setupAdapter() {
        adapter = ListStoryAdapter {
        }
        binding.rvListStory.adapter = adapter
    }

    private fun setupLayoutManager() {
        binding.rvListStory.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupViewModelObserver() {
        viewModel.getAllStories().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.list = resource.data
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                else ->{}
            }
        }
    }
}
package com.example.shopping_list.ui.list_box

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.example.shopping_list.R
import com.example.shopping_list.databinding.FragmentListBoxBinding
import com.example.shopping_list.entity.Box
import com.example.shopping_list.entity.ModeViewer
import com.example.shopping_list.ui.recycler.ListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ListBoxFragment: Fragment() {

    private var _binding: FragmentListBoxBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListBoxViewModel by viewModels()
    private val adapter = ListAdapter(ModeViewer.BOX) { box -> onItemClick(box as Box) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBoxBinding.inflate(inflater, container, false)
        viewModel.getListBox()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boxList.adapter = adapter
        binding.boxList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        viewModel.items.onEach {
            adapter.setList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onItemClick(box: Box) {
        viewModel.putBox(box)
        findNavController().navigate(R.id.action_nav_box_to_nav_product)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
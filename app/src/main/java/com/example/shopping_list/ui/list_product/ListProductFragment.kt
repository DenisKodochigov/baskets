package com.example.shopping_list.ui.list_product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.example.shopping_list.R
import com.example.shopping_list.databinding.FragmentListProductBinding
import com.example.shopping_list.entity.ModeViewer
import com.example.shopping_list.entity.Product
import com.example.shopping_list.ui.recycler.ListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ListProductFragment : Fragment() {

    private var _binding: FragmentListProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListProductViewModel by viewModels()
    private val adapter = ListAdapter(ModeViewer.PRODUCTS) {product -> onItemClick(product as Product) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
        exitTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListProductBinding.inflate(inflater, container, false)
        viewModel.getListProduct()
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productList .adapter = adapter
        binding.productList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        viewModel.items.onEach {
            adapter.setList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun onItemClick(product: Product) {
        viewModel.putProduct(product)
//        findNavController().navigate(R.id.action_nav_box_to_nav_product)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
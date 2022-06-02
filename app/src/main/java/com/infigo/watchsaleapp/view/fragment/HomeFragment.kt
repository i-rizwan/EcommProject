package com.infigo.watchsaleapp.view.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.watchstoreapp.model.CategoryItem
import com.infigo.watchsaleapp.IBadgeUpdater
import com.infigo.watchsaleapp.INavListener
import com.infigo.watchsaleapp.adapter.CategoryAdapter
import com.infigo.watchsaleapp.adapter.ICategoryListener
import com.infigo.watchsaleapp.adapter.IProductListener
import com.infigo.watchsaleapp.adapter.ProductAdapter
import com.infigo.watchsaleapp.databinding.FragmentHomeBinding
import com.infigo.watchsaleapp.model.ProductItem
import com.infigo.watchsaleapp.utils.Constant
import com.infigo.watchsaleapp.utils.Status
import com.infigo.watchsaleapp.viewModel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), ICategoryListener, IProductListener {
    lateinit var listener: INavListener
    private val loginViewModel: LoginViewModel by activityViewModels()
    lateinit var catAdapter: CategoryAdapter
    lateinit var productAdapter: ProductAdapter
    lateinit var binding: FragmentHomeBinding
    lateinit var rv: View
    lateinit var iBadgeUpdater: IBadgeUpdater
    lateinit var offerInstance: ProductItem
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            listener = activity as INavListener
            iBadgeUpdater = activity as IBadgeUpdater
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "error implementing")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        listener.showHideNavigations(true)
        rv = binding.root
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        try {
            val params = rv.layoutParams
            params.height = Constant.getUsableHeight(requireActivity())
            rv.layoutParams = params
        } catch (e: Exception) {
            Log.d("ListView1", "2 GroupInfoFragment Height Exception: $e")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryRV()
        setupProductRV()
        loginViewModel.getAllCategories()
        loginViewModel.getProductByCategory("1")

        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            loginViewModel.allCategory.observe(requireActivity(), Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        catAdapter.updateList(it as ArrayList<CategoryItem>)
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                    }
                }
            })


            loginViewModel.productItemByCategory.observe(requireActivity(), Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        productAdapter.updateList(it.data as ArrayList<ProductItem>)
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            })
        }

        iBadgeUpdater.updateBadge()
        createOfferInstance()
        binding.offerButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToDetailsFragment(
                    offerInstance
                )
            )
        }
    }


    private fun setupCategoryRV() {
        catAdapter = CategoryAdapter(this)
        binding.categoryRv.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            hasFixedSize()
            adapter = catAdapter
        }
    }

    private fun setupProductRV() {
        productAdapter = ProductAdapter(this)
        binding.productListRv.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            hasFixedSize()
            adapter = productAdapter
        }
    }

    override fun onCategoryClick(category: CategoryItem) {
        loginViewModel.getProductByCategory(category.id!!)
    }

    override fun onProductItemClicked(product: ProductItem) {
        findNavController().navigate(HomeFragmentDirections.actionHomeToDetailsFragment(product))


    }

    private fun createOfferInstance() {
        offerInstance = ProductItem(
            "1",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry.Lorem Ipsum has been the industry's standard dummy text.",
            "7",
            "https://firebasestorage.googleapis.com/v0/b/watchstore-9974c.appspot.com/o/watchImages%2Foffer.png?alt=media&token=9dd83b1c-d0d4-463a-ac18-ca5739fc3506",
            "Fossil Men's Golden Watch with Great Discount",
            "70",
            "200",
            6,
            false,

            )
    }

}
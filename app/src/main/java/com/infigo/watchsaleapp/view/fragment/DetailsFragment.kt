package com.infigo.watchsaleapp.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.infigo.watchsaleapp.view.IBadgeUpdater
import com.infigo.watchsaleapp.view.INavListener
import com.infigo.watchsaleapp.R
import com.infigo.watchsaleapp.databinding.FragmentDetailsBinding
import com.infigo.watchsaleapp.model.ProductItem
import com.infigo.watchsaleapp.utils.Constant
import com.infigo.watchsaleapp.utils.Status
import com.infigo.watchsaleapp.view.activity.FullScreenActivity
import com.infigo.watchsaleapp.viewModel.LoginViewModel

class DetailsFragment : Fragment() {
    lateinit var listener: INavListener
    private val args: DetailsFragmentArgs by navArgs()
    lateinit var binding: FragmentDetailsBinding
    private var productCount: Int? = 1
    private var totalQuantity: Int? = 1
    lateinit var rv: View
    private val loginViewModel: LoginViewModel by activityViewModels()
    lateinit var iBadgeUpdater: IBadgeUpdater
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
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        listener.showHideNavigations(false)
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
        onBackPress()
        var product: ProductItem = args.productDetails
        totalQuantity = product.quantity
        binding.apply {
            title.text = product.name
            discountedPrice.text = "$" + product.offer
            mrp.text = "$" + product.price
            quantity.text = "1"
            desc.text = product.description
            Glide.with(watchImage.context).load(product.img).into(watchImage)
            if (product.isFavorite == true)
                binding.fav.setImageResource(R.drawable.fav_selected)
            else
                binding.fav.setImageResource(R.drawable.fav)

        }
        binding.watchImage.setOnClickListener {
            val intent = Intent(requireActivity(), FullScreenActivity::class.java)
            intent.putExtra("imgPath", product.img)
            requireActivity().startActivity(intent)
        }
        binding.back.setOnClickListener {
            callHomeFragment()

        }
        binding.fav.setOnClickListener {
            if (product.isFavorite == true) {
                binding.fav.setImageResource(R.drawable.fav)
                product.isFavorite = false
            } else {
                binding.fav.setImageResource(R.drawable.fav_selected)
                product.isFavorite = true
            }
            loginViewModel.updateFavProduct(product)

        }
        binding.plus.setOnClickListener {
            if (productCount!! < totalQuantity!!) {
                productCount = productCount!! + 1
                binding.quantity.text = productCount.toString()
            }
            setTextColor()
        }
        binding.minus.setOnClickListener {
            if (productCount!! > 1) {
                productCount = productCount!! - 1
                binding.quantity.text = productCount.toString()
            }
            setTextColor()
        }

        binding.addToCart.setOnClickListener {
            product.quantity = productCount
            loginViewModel.addToCart(product)
            loginViewModel.getAddToCartResponse.observe(requireActivity(), Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        Toast.makeText(
                            requireActivity(),
                            "Product added successfully in Cart",
                            Toast.LENGTH_SHORT
                        ).show()
                        iBadgeUpdater.updateBadge()
                    }
                    Status.LOADING -> {
                    }
                    Status.ERROR -> {
                    }
                }
            })

        }
    }

    private fun setTextColor() {
        binding.apply {
            when (productCount) {
                1 -> {
                    minus.setTextColor(Color.GRAY)
                    plus.setTextColor(Color.WHITE)
                }
                totalQuantity -> {
                    minus.setTextColor(Color.WHITE)
                    plus.setTextColor(Color.GRAY)
                }
                else -> {
                    plus.setTextColor(Color.WHITE)
                    minus.setTextColor(Color.WHITE)
                }
            }
        }
    }

    private fun callHomeFragment() {
        findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToHome())
    }

    private fun onBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(
            requireActivity(),
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.i("back pressed12", "back pressed")
                    callHomeFragment()
                }
            })
    }
}
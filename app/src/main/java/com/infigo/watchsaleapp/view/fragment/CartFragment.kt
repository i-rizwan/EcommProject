package com.infigo.watchsaleapp.view.fragment

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.infigo.watchsaleapp.model.CartItem
import com.infigo.watchsaleapp.view.IBadgeUpdater
import com.infigo.watchsaleapp.adapter.CartAdapter
import com.infigo.watchsaleapp.adapter.IcartListener
import com.infigo.watchsaleapp.databinding.FragmentCartBinding
import com.infigo.watchsaleapp.utils.Constant
import com.infigo.watchsaleapp.utils.Status
import com.infigo.watchsaleapp.viewModel.LoginViewModel
import kotlinx.coroutines.*

class CartFragment : Fragment(), IcartListener {
    private val loginViewModel: LoginViewModel by activityViewModels()
    lateinit var binding: FragmentCartBinding
    lateinit var rv: View
    lateinit var cartAdapter: CartAdapter
    private var cartItemList: ArrayList<CartItem> = ArrayList()
    lateinit var iBadgeUpdater: IBadgeUpdater

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            iBadgeUpdater = activity as IBadgeUpdater
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "error implementing")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)

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
        setUpLayout()
        setupCartRV()
        loadData()
        iBadgeUpdater.updateBadge()
        binding.placeOrder.setOnClickListener {
            MainScope().launch {
                loginViewModel.deleteAllCartItems(cartItemList)

                loginViewModel.delCartItemResponse.observe(requireActivity(), Observer {

                    when (it.status) {
                        Status.SUCCESS -> {
                            iBadgeUpdater.updateBadge()
                            //   findNavController().navigate(CartFragmentDirections.actionCartToSuccessFragment())
                        }
                        Status.ERROR -> {
                        }
                        Status.LOADING -> {
                        }
                    }
                })
            }

        }

    }

    private fun loadData() {
        loginViewModel.requestForCartList()
        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            loginViewModel.cartList.observe(requireActivity(), Observer {

                when (it.status) {
                    Status.SUCCESS -> {

                        var data = it
                        cartItemList = data as ArrayList<CartItem>
                        cartAdapter.updateList(cartItemList)
                        setUpLayout()
                    }
                    Status.ERROR -> {
                    }
                    Status.LOADING -> {
                    }
                }
            })

        }
    }

    private fun setUpLayout() {

        binding.apply {
            if (cartItemList.size > 0) {
                rl.visibility = View.VISIBLE
                placeHolder.visibility = View.GONE
                myCart.visibility = View.VISIBLE
            } else {
                placeHolder.visibility = View.VISIBLE
                rl.visibility = View.GONE
                myCart.visibility = View.GONE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPrice(priceData: Array<Int>) {
        val mCharges: String = String.format("%.2f", (priceData[1] * 0.05))
        val mNetPayable: String = (priceData[1] + (priceData[1] * 0.05)).toString()

        binding.apply {
            price.text = "$priceData[0].toString()"
            discount.text = "-$(priceData[0] - priceData[1]).toString()"
            charges.text = "$$mCharges"
            totalPrice.text = "$$mNetPayable"
        }
    }

    private fun setupCartRV() {
        cartAdapter = CartAdapter(this)
        binding.cartRv.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            hasFixedSize()
            adapter = cartAdapter
        }
    }

    override fun onCarttItemClicked(product: CartItem) {
        val cList: MutableList<CartItem> = mutableListOf()
        cList.add(product)
        val data = cList as ArrayList<CartItem>

        loginViewModel.deleteAllCartItems(data)

        GlobalScope.launch(Dispatchers.Main) {
            delay(800)
            val id = findNavController().currentDestination?.id
            findNavController().popBackStack(id!!, true)
            findNavController().navigate(id)
        }
    }

}
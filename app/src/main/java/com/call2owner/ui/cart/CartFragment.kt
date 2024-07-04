package com.call2owner.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.call2owner.databinding.FragmentCartBinding
import com.call2owner.ui.BaseFragment

class CartFragment : BaseFragment() {
    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(layoutInflater)

        return binding.root
    }

}
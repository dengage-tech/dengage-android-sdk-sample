package com.dengage.android.kotlin.sample.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dengage.android.kotlin.sample.extensions.getDengageEvent
import java.util.*

/**
 * Created by Batuhan Coskun on 14 March 2021
 */
abstract class BaseDataBindingFragment<ViewBinding : ViewDataBinding> : Fragment() {

    protected lateinit var binding: ViewBinding

    @LayoutRes
    abstract fun getLayoutRes(): Int
    abstract fun init()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    /**
     * Send page view event to Dengage
     * @param pageType page name
     */
    protected fun sendPageView(pageType: String) {
        val data = HashMap<String, Any>()
        data["page_type"] = pageType
        activity?.getDengageEvent()?.pageView(data)
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}
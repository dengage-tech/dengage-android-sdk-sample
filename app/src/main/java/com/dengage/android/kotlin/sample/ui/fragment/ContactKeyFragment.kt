package com.dengage.android.kotlin.sample.ui.fragment

import com.dengage.android.kotlin.sample.R
import com.dengage.android.kotlin.sample.databinding.FragmentContactKeyBinding
import com.dengage.android.kotlin.sample.extensions.getDengageManager
import com.dengage.android.kotlin.sample.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 02 December 2020
 */
class ContactKeyFragment : BaseDataBindingFragment<FragmentContactKeyBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_contact_key
    }

    override fun init() {
        sendPageView("contact-key")

        binding.etContactKey.setText(activity?.getDengageManager()?.subscription?.contactKey)

        binding.btnSave.setOnClickListener {
            val contactKey = binding.etContactKey.text.toString().trim()
            if (contactKey.isNotEmpty()) {
                activity?.getDengageManager()?.setContactKey(contactKey)
            }
        }
    }

}
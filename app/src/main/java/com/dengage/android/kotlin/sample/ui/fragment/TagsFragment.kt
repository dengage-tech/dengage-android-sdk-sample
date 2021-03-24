package com.dengage.android.kotlin.sample.ui.fragment

import com.dengage.android.kotlin.sample.R
import com.dengage.android.kotlin.sample.databinding.FragmentTagsBinding
import com.dengage.android.kotlin.sample.extensions.getDengageManager
import com.dengage.android.kotlin.sample.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 24 March 2021
 */
class TagsFragment : BaseDataBindingFragment<FragmentTagsBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_tags
    }

    override fun init() {
        sendPageView("tags")

        binding.btnSend.setOnClickListener {
            val tag = binding.etTag.text.toString().trim()
            val value = binding.etValue.text.toString().trim()
            val changeTime = binding.etChangeTime.text.toString().trim()
            val changeValue = binding.etChangeValue.text.toString().trim()
            val removeTime = binding.etRemoveTime.text.toString().trim()

            val tags = mutableListOf<HashMap<String, String>>()
            val tagItem = hashMapOf<String, String>()
            tagItem["tag"] = tag
            tagItem["value"] = value
            if (changeTime.isNotEmpty()) {
                tagItem["changeTime"] = changeTime
            }
            if (changeValue.isNotEmpty()) {
                tagItem["changeValue"] = changeValue
            }
            if (removeTime.isNotEmpty()) {
                tagItem["removeTime"] = removeTime
            }
            tags.add(tagItem)
            activity?.getDengageManager()?.setTags(tags)
        }
    }
}
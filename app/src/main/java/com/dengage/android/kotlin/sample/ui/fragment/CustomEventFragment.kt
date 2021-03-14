package com.dengage.android.kotlin.sample.ui.fragment

import com.dengage.android.kotlin.sample.R
import com.dengage.android.kotlin.sample.databinding.FragmentCustomEventBinding
import com.dengage.android.kotlin.sample.extensions.getDengageManager
import com.dengage.android.kotlin.sample.model.EventParameter
import com.dengage.android.kotlin.sample.ui.adapter.EventParametersAdapter
import com.dengage.android.kotlin.sample.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 03 December 2020
 */
class CustomEventFragment : BaseDataBindingFragment<FragmentCustomEventBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_custom_event
    }

    override fun init() {
        sendPageView("custom-events")

        val eventParameters = mutableListOf<EventParameter>()
        for (i in 0 until 2) {
            eventParameters.add(EventParameter("param${i + 1}", null))
        }
        binding.rvEventParameters.adapter = EventParametersAdapter(eventParameters)

        binding.etTableName.setText("mobile_event_test")

        binding.btnSend.setOnClickListener {
            val customEventData = mutableMapOf<String?, Any?>()

            for (eventParameter in eventParameters) {
                customEventData[eventParameter.key] = eventParameter.value
            }
            activity?.getDengageManager()?.sendCustomEvent(
                binding.etTableName.text.toString().trim(),
                activity?.getDengageManager()?.subscription?.contactKey, customEventData
            )
        }
    }

}
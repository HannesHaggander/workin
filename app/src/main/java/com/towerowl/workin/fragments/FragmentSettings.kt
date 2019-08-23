package com.towerowl.workin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.towerowl.workin.R
import com.towerowl.workin.viewmodels.SettingsModel

class FragmentSettings : Fragment() {

    lateinit var autoWifiSwitch : SwitchCompat

    private val settingsModel : SettingsModel by lazy {
        ViewModelProvider(this)
            .get(SettingsModel::class.java)
            .apply { activity = requireActivity() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
    }

    private fun setupLayout(){
        autoWifiSwitch = requireView().findViewById<SwitchCompat>(R.id.fragment_setting_wifi)
            .also { switch ->
                settingsModel.autoWifiActive.observe(this, Observer {
                    switch.isChecked = it
                })
                settingsModel.fetchWifiAutoStatus()
            }
        autoWifiSwitch.setOnCheckedChangeListener { _, value ->

        }
    }

}
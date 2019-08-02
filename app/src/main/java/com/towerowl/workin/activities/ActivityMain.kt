package com.towerowl.workin.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.towerowl.workin.R
import com.towerowl.workin.fragments.FragmentOverview

class ActivityMain : AppCompatActivity() {

    private val mFragmentContainer : FrameLayout by lazy { findViewById<FrameLayout>(R.id.a_main_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateFragment()
    }

    private fun initiateFragment(){
        supportFragmentManager.beginTransaction()
            .add(mFragmentContainer.id, FragmentOverview())
            .commitNow()
    }

    override fun onStart() {
        super.onStart()
    }
}

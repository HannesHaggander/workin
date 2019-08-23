package com.towerowl.workin.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.towerowl.workin.App
import com.towerowl.workin.R
import com.towerowl.workin.data.Settings
import com.towerowl.workin.fragments.FragmentOverview

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swapFragment(FragmentOverview())
    }

    fun swapFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.a_main_container, fragment)
            .addToBackStack(fragment::class.java.simpleName)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 1){
            supportFragmentManager.popBackStack()
            return
        }

        super.onBackPressed()
    }
}

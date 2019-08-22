package com.towerowl.workin.activities

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.towerowl.workin.R
import com.towerowl.workin.fragments.FragmentOverview

class ActivityMain : AppCompatActivity() {

    private val fragmentContainer : FrameLayout by lazy { findViewById<FrameLayout>(R.id.a_main_container) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swapFragment(FragmentOverview())
    }

    fun swapFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(fragmentContainer.id, fragment)
            .addToBackStack(fragment::class.java.simpleName)
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

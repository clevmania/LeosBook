package com.clevmania.leosbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.clevmania.leosbook.extension.makeGone
import com.clevmania.leosbook.extension.makeVisible
import com.clevmania.leosbook.ui.ToolbarFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val navController by lazy { findNavController(R.id.nav_host_fragment)}

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(

            ), null
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        (this as FragmentActivity).supportFragmentManager
            .registerFragmentLifecycleCallbacks(fragmentLifeCycleCallbacks,true)
    }


    private val fragmentLifeCycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)

            (f as? ToolbarFragment)?.apply {
                if(f.showToolbar) toolbar.makeVisible()
                else toolbar.makeGone()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        (this as FragmentActivity).supportFragmentManager
            .unregisterFragmentLifecycleCallbacks(fragmentLifeCycleCallbacks)
    }
}
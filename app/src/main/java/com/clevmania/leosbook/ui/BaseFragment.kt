package com.clevmania.leosbook.ui

import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * @author by Lawrence on 8/1/20.
 * for LeosBook
 */

interface ToolbarFragment{
    val showToolbar : Boolean
}

abstract class BaseFragment : Fragment() {
    fun showLongToast(message : String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_LONG).show()
    }

    fun showShortToast(message : String){
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    fun longSnackBar(msg : String){
        Snackbar.make(requireView(),msg, Snackbar.LENGTH_LONG).show()
    }
}

abstract class TopLevelFragment : BaseFragment(), ToolbarFragment{
    override val showToolbar = true
}

abstract class GroundFragment : BaseFragment(), ToolbarFragment{
    override val showToolbar = false
}

abstract class AuthFragment : BaseFragment(), ToolbarFragment{
    override val showToolbar = false

    override fun onStart() {
        super.onStart()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}
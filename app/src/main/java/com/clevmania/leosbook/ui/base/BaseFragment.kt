package com.clevmania.leosbook.ui.base

import android.app.AlertDialog
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.clevmania.leosbook.R
import com.clevmania.leosbook.utils.ProgressDialogUtils
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

    private fun showBlockingProgress(message: String = "Please wait") {
        ProgressDialogUtils.getInstance()
            .showProgressDialog(requireContext(), message, cancelable = false)
    }

    private fun hideBlockingProgress() {
        ProgressDialogUtils.getInstance().dismissProgressDialog()
    }

    fun toggleBlockingProgress(value: Boolean) {
        if (value) {
            showBlockingProgress()
        } else {
            hideBlockingProgress()
        }
    }

    fun showTransactionErrorMessageDialog(message: String){
        if(message.toLowerCase() != "authorization failed for this request!") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setCancelable(false)
            builder.setTitle("Oops...." )
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
                (requireContext() as FragmentActivity).finish()
            }
            builder.create().show()
        }
    }

    fun showSuccessDialog(message: String, title: String = getString(R.string.title_api_success)){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.create().show()
    }

    fun showAlertDialog(message: String, title: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.create().show()
    }

    fun showErrorDialog(message: String, title: String = getString(R.string.title_api_error)){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.create().show()
    }
}

abstract class TopLevelFragment : BaseFragment(),
    ToolbarFragment {
    override val showToolbar = true
}

abstract class GroundFragment : BaseFragment(),
    ToolbarFragment {
    override val showToolbar = false
}

abstract class AuthFragment : BaseFragment(),
    ToolbarFragment {
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
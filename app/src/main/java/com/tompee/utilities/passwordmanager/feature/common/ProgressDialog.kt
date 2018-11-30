package com.tompee.utilities.passwordmanager.feature.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.base.BaseDialogFragment
import com.tompee.utilities.passwordmanager.databinding.DialogProgressBinding

class ProgressDialog : BaseDialogFragment() {

    override fun setupDependencies() {
    }

    companion object {
        private const val TAG_COLOR = "color"
        private const val TAG_TEXT = "text"

        fun newInstance(background: Int, textRes: Int): ProgressDialog {
            val dialog = ProgressDialog()
            dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FragmentDialog)
            val bundle = Bundle()
            bundle.putInt(TAG_COLOR, background)
            bundle.putInt(TAG_TEXT, textRes)
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding =
            DataBindingUtil.inflate<DialogProgressBinding>(
                LayoutInflater.from(context),
                R.layout.dialog_progress,
                null,
                false
            )
        binding.progress.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
        binding.root.setBackgroundColor(
            ContextCompat.getColor(
                context!!,
                arguments?.getInt(TAG_COLOR) ?: R.color.colorLoginButton
            )
        )

        binding.progressText.setText(arguments?.getInt(TAG_TEXT) ?: R.string.app_name)
        return AlertDialog.Builder(activity!!)
            .setView(binding.root)
            .setCancelable(false)
            .create()
    }
}
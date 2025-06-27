package com.mw.churchattendance.ui.dialog

import android.content.Context
import android.text.Spannable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.mw.churchattendance.R
import com.mw.churchattendance.base.BaseDialogHelper
import com.mw.churchattendance.databinding.DialogMessageBinding
import com.mw.churchattendance.util.DialogType

class AppDialogHelper(context: Context) :
    BaseDialogHelper() {

    lateinit var binding: DialogMessageBinding

    @RawRes
    var lottieIcon: Int? = null

    @DrawableRes
    var drawableIcon: Int? = null

    override fun create(): AlertDialog {
        setupDialog()
        return super.create()
    }

    //  dialog view
    override val dialogView: View by lazy {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_message, null, false)
        binding.root
    }

    override val builder: AlertDialog.Builder = AlertDialog.Builder(context).setView(dialogView)

    //  Dialog image
    @Deprecated("set resource using lottieIcon or drawableIcon")
    val dialogIcon: LottieAnimationView by lazy {
        binding.dialogImage
    }

    //  dialog title
    val title: TextView by lazy {
        binding.dialogTitle
    }

    //  dialog message
    val message: TextView by lazy {
        binding.dialogMessage
    }

    val divider: View by lazy {
        binding.verticalDivider
    }

    //  dialog negative button
    val negativeButton: Button by lazy {
        binding.negativeButton
    }

    //  dialog positive button
    val positiveButton: Button by lazy {
        if (DialogType.dialog_type == DialogType.Companion.Type.PRIMARY) binding.positiveButton else binding.positiveButtonRemove
    }

    /**
     * Negative button listener
     * */
    fun negativeButtonClickListener(func: (() -> Unit)? = null) =
        with(negativeButton) {
            setClickListenerToDialogButton(func)
        }

    /**
     * Positive button listener
     *
     * */
    fun positiveButtonClickListener(func: (() -> Unit)? = null) =
        with(positiveButton) {
            setClickListenerToDialogButton(func)
        }

    /**
     * view click listener as extension function
     * */
    private fun View.setClickListenerToDialogButton(func: (() -> Unit)?) =
        setOnClickListener {
            func?.invoke()
            dialog?.dismiss()
        }

    /**
     * Setup dialog components
     * */
    private fun setupDialog() {
        if (lottieIcon == null && drawableIcon == null) {
            binding.dialogImage.visibility = View.GONE
        } else {
            message.gravity = Gravity.CENTER
            when {
                lottieIcon != null -> {
                    binding.dialogImage.setAnimation(lottieIcon!!)
                    binding.dialogImage.playAnimation()
                }
                drawableIcon != null -> binding.dialogImage.setImageResource(drawableIcon!!)
            }
        }
        if (title.text.isNullOrEmpty()) {
            title.visibility = View.GONE
            divider.visibility = View.GONE
        }

        if (title.gravity == Gravity.CENTER) divider.visibility = View.GONE

        if (negativeButton.text.isNullOrEmpty()) {
            negativeButton.visibility = View.GONE
        }
        if (DialogType.dialog_type == DialogType.Companion.Type.PRIMARY) {
            binding.positiveButtonRemove.visibility = View.GONE
            binding.positiveButton.visibility = View.VISIBLE
        } else {
            binding.positiveButtonRemove.visibility = View.VISIBLE
            binding.positiveButton.visibility = View.INVISIBLE
        }
    }

    fun updateDialog(
        titleText: String,
        messageText: Spannable,
        positiveText: String,
        negativeText: String
    ) {
        title.text = titleText
        message.text = messageText
        positiveButton.text = positiveText
        negativeButton.text = negativeText
    }
}
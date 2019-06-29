package com.mnemonic.play

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_level.*

class LevelDialog : BottomSheetDialogFragment() {

    lateinit var mListener: LevelListener

    companion object {
        fun getInstance(): LevelDialog = LevelDialog()
    }

    fun setListener(levelListener: LevelListener) {
        mListener = levelListener
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        isCancelable = false
        return inflater.inflate(R.layout.fragment_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnEasy.setOnClickListener {  dismiss()
            mListener.onClick("easy") }
        btnMedium.setOnClickListener { dismiss()
            mListener.onClick("medium") }
        btnHard.setOnClickListener {dismiss()
            mListener.onClick("hard") }

    }

    interface LevelListener {
        fun onClick(level: String)
    }
}
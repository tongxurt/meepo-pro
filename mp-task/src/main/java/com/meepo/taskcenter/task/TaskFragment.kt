package com.meepo.taskcenter.task

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.meepo.sdk.framework.fragment.StaticFragment
import com.meepo.taskcenter.R
import kotlinx.android.synthetic.main.task_fragment.*

class TaskFragment private constructor(): StaticFragment() {
    override fun initView(view: View, savedInstanceState: Bundle?) {
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {


        task_wv.setBackgroundColor(Color.TRANSPARENT)

        val settings = task_wv.settings
        settings.javaScriptEnabled = true

        val nightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val indexUrl = "http://h5.staging.meepod.cn/"
        task_wv.loadUrl(indexUrl)
    }

    override fun setUpContentLayout(): Int = R.layout.task_fragment

    companion object {
        private var taskFragment: TaskFragment? = null

        fun instance(): TaskFragment {

            if (taskFragment == null) {
                taskFragment = TaskFragment()
            }

            return taskFragment!!
        }
    }
}
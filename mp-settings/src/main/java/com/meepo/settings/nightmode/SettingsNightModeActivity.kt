package com.meepo.settings.nightmode

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.meepo.design.helper.StatusBarHelper
import com.meepo.design.selector.OptionGroup
import com.meepo.design.selector.listener.OnSelectListener
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.settings.R
import com.meepo.settings.Store
import kotlinx.android.synthetic.main.settings_night_mode_activity.*

/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/11/1 4:00 PM
 * @version 1.0
 */
internal class SettingsNightModeActivity : BaseActivity<Presenter>(), IContract.IView {

    override fun initData() {
        nightmode_selector.setOptionGroups(Store.State.NIGHT_MODE_OPTION_GROUPS)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        StatusBarHelper.immerse(this)
        StatusBarHelper.setMargin(this, header_ll)


        nightmode_selector.setOptionGroups(Store.State.NIGHT_MODE_OPTION_GROUPS)
        nightmode_selector.setOnSelectListener(object : OnSelectListener {
            override fun onSelect(p0: OptionGroup.Option) {
                Store.Func.saveNightMode(p0.key)

                // 调用会使 滑动返回异常
                NightModeHelper.switchMode(this@SettingsNightModeActivity.delegate, p0.key)


            }

            override fun onUnSelect(p0: OptionGroup.Option) {
            }
        })

        back_iv.setOnClickListener { finish() }

    }

    override fun setUpContentLayout(): Int = R.layout.settings_night_mode_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()

}
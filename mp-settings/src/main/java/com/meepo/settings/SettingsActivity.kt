package com.meepo.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.meepo.basic.AppBasicModule
import com.meepo.design.dialog.SweetAlertDialog
import com.meepo.design.helper.StatusBarHelper
import com.meepo.design.upgrade.AppUpgrader
import com.meepo.design.upgrade.listener.IUpgradeListener
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.sdk.helper.ActivityHelper
import com.meepo.sdk.manager.CacheManager
import com.meepo.settings.Store.State.FONT_SIZE_OPTIONS
import com.meepo.settings.nightmode.SettingsNightModeActivity
import com.meepo.settings.schema.AppUpgrade
import kotlinx.android.synthetic.main.settings_activity.*


/**
 * @author  佟旭
 * @wechat tongxury
 * @date  2020/10/25 3:08 PM
 * @version 1.0
 */
class SettingsActivity : BaseActivity<Presenter>(), IContract.IView {

    override fun setUpContentLayout(): Int = R.layout.settings_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()
    override fun applyUserSettings() {
        app_settings.setSettingsItemGroup(Store.State.SETTINGS_GROUPS)
    }

    override fun applyAppUpdate(appUpgrade: AppUpgrade) {

        if (appUpgrade.update) {
            Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show()
            return
        }

        val apkFileUrl = "https://ec-apps.oss-cn-beijing.aliyuncs.com/miaozhaozhifou-release.apk"
        val updateLog = "fsadfasd\nfasdfs"

        AppUpgrader.Builder(this)
            .setApkFileUrl(apkFileUrl)
            .setConstraint(false)
            .setUpdate(true)
            .setIgnorable(false)
            .setUpgradeLog(updateLog)
            .setTargetSize("11.121M")
            .setNewVersion("2.0.7")
            .setTitle("确认升级吗?")
            .setUpgradeListener(object : IUpgradeListener {
                override fun onBefore(updateApp: AppUpgrader) {
                    Log.e("AppUpgrader", "onBefore")
                }

                override fun onCancel(updateApp: AppUpgrader) {
                    Log.e("AppUpgrader", "onCancel")

                }

                override fun onIgnore(updateApp: AppUpgrader) {
                    Log.e("AppUpgrader", "onIgnore")

                }

                override fun onConfirm(appUpgrader: AppUpgrader) {
                    Log.e("AppUpgrader", "onConfirm")

                }

                override fun onShowDialog(appUpgrader: AppUpgrader) {
                    Log.e("AppUpgrader", "onShowDialog")
                    appUpgrader.showDialogFragment()

                }

                override fun onError(throwable: Throwable) {
                    Log.e("AppUpgrader", "onError")
                }
            })
            .build()
            .upgrade()
    }

    override fun initData() {
        mPresenter?.findUserSettings()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.findUserSettings()
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

//        ImmersionBar.with(this)
//            .statusBarColor(R.color.sweet_dialog_bg_color)     //状态栏颜色，不写默认透明色
//            .autoStatusBarDarkModeEnable(true,0.2f)
//            .init();

        StatusBarHelper.immerse(this, header_ll)
//        StatusBarHelper.setDarkMode(this)
//        StatusBarHelper.setMargin(this, header_ll)


        app_settings.setOnItemClickListener { item, _ ->
            when (item.key) {
                Store.SETTING_ITEM_NIGHT_MODE -> setUpNightMode()
                Store.SETTING_ITEM_FONT_SIZE -> setUpFontSize()
                Store.SETTING_ITEM_CLEAR_CACHE -> setUpClearCache()
                Store.SETTING_ITEM_CHECK_UPDATE -> setUpCheckUpdate()
            }
        }

        back_iv.setOnClickListener { finish() }

        logout_btn.setOnClickListener { AppBasicModule.logout();finish() }
    }

    private fun setUpCheckUpdate() {
        mPresenter?.fetchForAppUpgrade()
    }

    private fun setUpClearCache() {

        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("")
            .setContentText("确定要清除所有缓存吗?")
            .setConfirmText("是的")
            .setCancelText("取消")
            .setConfirmClickListener {
                CacheManager.clearAllCache(this)
                mPresenter?.findUserSettings()

                it.cancel()
            }
            .show()
    }

    private fun setUpFontSize() {
        val singleChoiceDialog = AlertDialog.Builder(this)
        singleChoiceDialog.setTitle("字体大小")
        // 第二个参数是默认选项，此处设置为0
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(
            FONT_SIZE_OPTIONS, Store.State.userSettings.fontSize
        ) { dialog, which ->
            Store.Func.saveFontSize(which)
            mPresenter?.findUserSettings()
        }
        singleChoiceDialog.setCancelable(true)
        singleChoiceDialog.setNegativeButton("取消") { dialog, which -> }
//        singleChoiceDialog.setPositiveButton("确定") { dialog, which -> }

        singleChoiceDialog.show()
    }

    private fun setUpNightMode() {
        ActivityHelper.launch(
            this@SettingsActivity, SettingsNightModeActivity::class.java
        )
    }
}
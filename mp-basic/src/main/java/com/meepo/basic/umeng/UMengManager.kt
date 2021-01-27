package com.meepo.basic.umeng

import android.app.Activity
import android.content.Context
import com.meepo.config.Config.UMENG_APP_KEY
import com.meepo.config.Config.WECHAT_APP_KEY
import com.meepo.config.Config.WECHAT_SECRET
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb

object UMengManager {


    internal fun init(context: Context) {
        // 友盟
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true)
        UMConfigure.init(context, UMENG_APP_KEY, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
        PlatformConfig.setWeixin(WECHAT_APP_KEY, WECHAT_SECRET)
        PlatformConfig.setWXFileProvider("com.tencent.sample2.fileprovider")
    }

    fun shareWebToWxSession(activity: Activity, shareContent: ShareContent, listener: IShareListener) {
        val web = UMWeb(shareContent.url)
        web.title = shareContent.title
        web.description = shareContent.desc
        web.setThumb(UMImage(activity, shareContent.iconResId))

        ShareAction(activity).withMedia(web)
            .setPlatform(SHARE_MEDIA.WEIXIN)
            .setCallback(mapTo(listener))
            .share()
    }


    fun shareWebByBoard(activity: Activity, shareContent: ShareContent, listener: IShareListener) {

        val mShareAction = ShareAction(activity).setDisplayList(
            SHARE_MEDIA.WEIXIN,
            SHARE_MEDIA.WEIXIN_CIRCLE,
            SHARE_MEDIA.MORE
        )
//            .addButton("复制文本", "复制文本", "umeng_socialize_copy", "umeng_socialize_copy")
//            .addButton("复制链接", "复制链接", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
            .setShareboardclickCallback { snsPlatform, share_media ->
//                if (snsPlatform.mShowWord == "复制文本") {
//                    Toast.makeText(activity, "复制文本按钮", Toast.LENGTH_LONG).show()
//                } else if (snsPlatform.mShowWord == "复制链接") {
//                    Toast.makeText(activity, "复制链接按钮", Toast.LENGTH_LONG).show()
//                } else

                val web = UMWeb(shareContent.url)
                web.title = shareContent.title
                web.description = shareContent.desc
                web.setThumb(UMImage(activity, shareContent.iconResId))

                ShareAction(activity).withMedia(web)
                    .setPlatform(share_media)
                    .setCallback(mapTo(listener))
                    .share()
            }

        mShareAction.open()
    }

    private fun mapTo(listener: IShareListener): UMShareListener {
        return object : UMShareListener {
            override fun onResult(p0: SHARE_MEDIA?) {
                p0?.let { listener.onResult(it.getName()) }
            }

            override fun onCancel(p0: SHARE_MEDIA?) {
                p0?.let { listener.onCancel(it.getName()) }
            }

            override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                p0?.let { m ->
                    p1?.let {
                        listener.onError(m.getName(), it)
                    }
                }
            }

            override fun onStart(p0: SHARE_MEDIA?) {
                p0?.let { listener.onStart(it.getName()) }
            }
        }
    }

}
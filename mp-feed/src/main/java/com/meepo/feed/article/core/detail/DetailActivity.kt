package com.meepo.feed.article.core.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import com.meepo.basic.umeng.IShareListener
import com.meepo.basic.umeng.ShareContent
import com.meepo.basic.umeng.UMengManager
import com.meepo.design.helper.StatusBarHelper
import com.meepo.design.input.InputDialog
import com.meepo.design.list.comments.OnCommentClickListener
import com.meepo.design.userinfo.UserInfo
import com.meepo.design.xrecyclerview.XRecyclerView
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.Store.Func.saveReadHistory
import com.meepo.feed.article.core.detail.reply.CommentReplyActivity
import com.meepo.feed.schema.Comment
import com.meepo.feed.schema.Item
import com.meepo.sdk.framework.BaseActivity
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.helper.ActivityHelper
import kotlinx.android.synthetic.main.detail_activity.*


/**
 * @author  佟旭
 * @wechat tongxury
 * @date  11/14/20 7:55 PM
 * @version 1.0
 */
class DetailActivity : BaseActivity<Presenter>(), IContract.IFeedView {

    private lateinit var mItem: Item

    @SuppressLint("SetTextI18n")
    override fun appendComments(comments: List<Comment>?, total: Int, hasMore: Boolean) {
        comment_layout.loadMoreComplete()

        comments?.let {
            comment_layout.addComments(comments.map(this::mapComment))
        }

        comment_layout.setNoMore(!hasMore)

        comment_count_tv.text = "" + total
    }

    override fun applyComment(comment: Comment) {

        comment_layout.insertComment(
            mapComment(comment),
            0
        )
    }

    override fun apply(item: Item) {
        item.let {
            mItem = item
            saveReadHistory(item)
            // 加载成功之后再加载评论
            mPresenter?.fetchComments(item.id, Store.COMMENT_TARGET_ITEM)
        }

        item.content?.content?.let {
            content_webView.loadUrl("javascript:showContent(\'$it\')")
        }

        item.source?.let {

            val headerUserInfo = UserInfo(
                username = it.name,
                avatar = it.avatar ?: ""
            )

            val userInfo = UserInfo(
                tag = "原创",
                username = it.name,
                avatar = it.avatar ?: "",
                desc = it.desc ?: ""
            )

            header_userinfo_view.setUserInfo(headerUserInfo)
            userinfo_view.setUserInfo(userInfo)
        }

        star_ib.setImageResource(if (item.isCollected()) R.drawable.ic_star_color else R.drawable.ic_star)
        star_ib.setOnClickListener { mPresenter?.collect(mItem) }


        follow_btn.setOnClickListener { }

        item.h5?.let {
            val shareContent = ShareContent(title = item.title, desc = item.desc ?: "", url = it.content ?: "", iconResId = R.drawable.ic_star)

            more_iv.setOnClickListener { UMengManager.shareWebByBoard(this, shareContent, shareListener) }
            wechat_ib.setOnClickListener { UMengManager.shareWebToWxSession(this, shareContent, shareListener) }
            share_ib.setOnClickListener { UMengManager.shareWebByBoard(this, shareContent, shareListener) }
        }

    }


    override fun loadData() {
        mItem = ActivityHelper.getExtra(intent, "item")!!
        title_tv.text = mItem.title

    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        StatusBarHelper.immerse(this, header_rl, true)

        back_iv.setOnClickListener { finish() }

        content_nsv.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            header_userinfo_view.visibility =
                if (scrollY > title_tv.height + user_rl.height) View.VISIBLE else View.GONE
        }

        search_layout.setOnClickListener {
            InputDialog("", object : InputDialog.InputListener {
                override fun onSubmit(inputText: String) {
                    mPresenter?.comment(mItem, inputText)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            }).show(supportFragmentManager, "aaa")
        }


        comment_ib.setOnClickListener {
            content_nsv.scrollTo(0, comment_layout.y.toInt())
        }

        comment_layout.setOnCommentClickListener(object : OnCommentClickListener {
            override fun onAvatarClick(comment: com.meepo.design.list.comments.Comment, position: Int) {

            }

            override fun onLikeClick(comment: com.meepo.design.list.comments.Comment, like: Boolean, position: Int) {
                mPresenter?.starComment(comment.id)
            }

            override fun onReplyClick(comment: com.meepo.design.list.comments.Comment, position: Int) {
//                val replyDialog = ReplyDialog(comment)
//                replyDialog.show(supportFragmentManager, "")

                ActivityHelper.launch(this@DetailActivity, CommentReplyActivity::class.java, "comment", comment)
            }

            override fun onUsernameClick(comment: com.meepo.design.list.comments.Comment, position: Int) {
            }
        })

        comment_layout.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                mPresenter?.fetchComments(mItem.id, Store.COMMENT_TARGET_ITEM)
            }

            override fun onRefresh() {
            }
        })

        initWebView()
    }

    private val shareListener = object : IShareListener {
        override fun onStart(media: String) {
            Log.e("onStart", media)
        }

        override fun onResult(media: String) {
            Log.e("onResult", media)
            mPresenter?.share(mItem)
        }

        override fun onError(media: String, e: Throwable) {
            Log.e("onError", media)
        }

        override fun onCancel(media: String) {
            Log.e("onCancel", media)
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {


        content_webView.setBackgroundColor(Color.TRANSPARENT)

        val settings = content_webView.settings
        settings.javaScriptEnabled = true

        val nightMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val indexUrl = "file:///android_asset/feed_detail.html?dark=${nightMode}&fontSize=${16}"
        content_webView.loadUrl(indexUrl)

        content_webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mPresenter?.fetchDetail(mItem.id)
            }
        }
    }

    override fun setUpContentLayout(): Int = R.layout.detail_activity

    override fun setUpPresenter(): IPresenter? = Presenter()


    private fun mapComment(comment: Comment): com.meepo.design.list.comments.Comment {


        return com.meepo.design.list.comments.Comment(
            comment.id,
            com.meepo.design.list.comments.Comment.User(
                comment.id,
                comment.user?.nickname ?: "",
                comment.user?.avatar ?: "",
                comment.tags?.map { t -> com.meepo.design.list.comments.Comment.Tag(id = t.id, name = t.name ?: "") } ?: arrayListOf()
            ),
            comment.stat?.liked ?: false,
            comment.stat?.likeCount ?: 0,
            comment.content ?: "",
            comment.stat?.replyCount ?: 0,
            null,
            comment.createdAt ?: "",
            null
        )
    }

}
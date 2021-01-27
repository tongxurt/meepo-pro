package com.meepo.feed.article.core.detail.reply

import android.os.Bundle
import android.view.View
import com.meepo.basic.schema.UserSummary
import com.meepo.design.input.InputDialog
import com.meepo.design.list.comments.OnCommentClickListener
import com.meepo.design.xrecyclerview.XRecyclerView
import com.meepo.feed.R
import com.meepo.feed.schema.Comment
import com.meepo.sdk.framework.BaseActivity
import com.meepo.sdk.framework.IPresenter
import com.meepo.sdk.helper.ActivityHelper
import kotlinx.android.synthetic.main.detail_comment_reply_activity.*
import kotlinx.android.synthetic.main.detail_comment_reply_activity.search_layout

class CommentReplyActivity : BaseActivity<IContract.IFeedPresenter>(), IContract.IFeedView {

    private lateinit var mComment: com.meepo.design.list.comments.Comment

    override fun appendComments(comments: List<Comment>?, total: Int, hasMore: Boolean) {
        comments?.let {
            reply_comments.addComments(comments.map { c -> mapComment(c) })
        }
        reply_comments.loadMoreComplete()
        reply_comments.setNoMore(!hasMore)

    }

    override fun applyComment(comment: Comment) {
        reply_comments.insertComment(mapComment(comment), 0)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        close_iv.setOnClickListener { finish() }

        mComment = ActivityHelper.getExtra<com.meepo.design.list.comments.Comment>(this.intent, "comment")!!
        main_comments.setPullRefreshEnabled(false)
        main_comments.setLoadingMoreEnabled(false)
        main_comments.setItemLayoutId(R.layout.detail_comment_reply_main_comment_layout)
        main_comments.addComments(mutableListOf(mComment))

        reply_comments.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onLoadMore() {
                mPresenter?.fetchComments(mComment.id)
            }

            override fun onRefresh() {
            }
        })

        reply_comments.setOnCommentClickListener(object : OnCommentClickListener {
            override fun onAvatarClick(comment: com.meepo.design.list.comments.Comment, position: Int) {
            }

            override fun onLikeClick(comment: com.meepo.design.list.comments.Comment, like: Boolean, position: Int) {
                mPresenter?.starComment(comment.id)
            }

            override fun onReplyClick(comment: com.meepo.design.list.comments.Comment, position: Int) {
                showInputDialog(comment)
            }

            override fun onUsernameClick(comment: com.meepo.design.list.comments.Comment, position: Int) {
            }
        })

        search_layout.setOnClickListener {
            showInputDialog(null)
        }

    }

    private fun showInputDialog(replyTo: com.meepo.design.list.comments.Comment?) {

        var hintText = ""
        replyTo?.user?.username?.let { hintText = "回复 $it :" }

        InputDialog(hintText, object : InputDialog.InputListener {
            override fun onSubmit(inputText: String) {
                mPresenter?.comment(mComment.id, replyTo, inputText)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }).show(supportFragmentManager, "aaa")
    }

    override fun loadData() {
        mPresenter?.fetchComments(mComment.id)
    }

    // duplicate
    private fun mapComment(comment: Comment): com.meepo.design.list.comments.Comment {

        val rsp = com.meepo.design.list.comments.Comment(
            id = comment.id,
            user = mapCommentUser(comment.user),
            like = comment.stat?.liked ?: false,
            likeNum = comment.stat?.likeCount ?: 0,
            content = comment.content ?: "",
            replyCount = comment.stat?.replyCount ?: 0,
            replyTagText = "回复",
            time = comment.createdAt ?: "",
            at = if (comment.at != null) com.meepo.design.list.comments.Comment.At(
                user = mapCommentUser(comment.user),
                content = comment.at.content
            ) else null
        )

        return rsp
    }

    private fun mapCommentUser(userSummary: UserSummary?): com.meepo.design.list.comments.Comment.User {
        return com.meepo.design.list.comments.Comment.User(
            id = userSummary?.id ?: "",
            username = userSummary?.nickname ?: "",
            avatar = userSummary?.avatar ?: "",
            tags = userSummary?.tags?.map { t -> com.meepo.design.list.comments.Comment.Tag(id = t, name = t) } ?: arrayListOf()
        )

    }


    override fun setUpContentLayout(): Int = R.layout.detail_comment_reply_activity

    override fun setUpPresenter(): IPresenter? = Presenter()

    override fun isSupportSwipeBack(): Boolean = false
}
package com.meepo.feed.article.core.detail.replyimport com.meepo.feed.Serviceimport com.meepo.feed.Storeimport com.meepo.feed.schema.Commentimport com.meepo.feed.schema.CommentResultimport com.meepo.feed.schema.ReqCommentimport com.meepo.sdk.framework.BasePresenterimport com.meepo.sdk.observer.Observerimport com.meepo.sdk.observer.ObserverManager.applySchedulers/** * @author  佟旭 * @wechat tongxury * @date  2020/10/14 10:46 PM * @version 1.0 */class Presenter : BasePresenter<IContract.IFeedView>(), IContract.IFeedPresenter {    private var commentPage = 1;//    private var hasMore = true;    override fun starComment(id: String) {        Service.get().starComment(id)            .applySchedulers()            .map { rsp -> rsp.data }            .compose(mView?.bindToLife<Unit>())            .subscribe(object : Observer<Unit>() {                override fun onSuccess(t: Unit?) {                }                override fun onFailure(e: Throwable) {                }            })    }    override fun fetchComments(targetId: String) {//        if (!hasMore) {//            return//        }        Service.get().fetchComments(targetId, Store.COMMENT_TARGET_COMMENT, commentPage, size = 15)            .applySchedulers()            .map { rsp -> rsp.data }            .compose(mView?.bindToLife<CommentResult>())            .subscribe(object : Observer<CommentResult>() {                override fun onSuccess(t: CommentResult?) {                    t?.let {                        mView?.appendComments(t.items, t.pagination.total, t.pagination.hasMore)//                        hasMore = t.pagination.hasMore                        commentPage += 1                    }                }                override fun onFailure(e: Throwable) {                }            })    }    override fun comment(itemId: String, replayTo: com.meepo.design.list.comments.Comment?, content: String) {        Service.get()            .addComment(                ReqComment(                    targetId = itemId,                    targetType = Store.COMMENT_TARGET_COMMENT,                    content = content,                    atCommentId = replayTo?.id                )            )            .applySchedulers()            .map { rsp -> rsp.data }            .compose(mView?.bindToLife<Comment>())            .subscribe(object : Observer<Comment>() {                override fun onSuccess(t: Comment?) {                    t?.let {                        mView?.applyComment(t)                    }                }                override fun onFailure(e: Throwable) {                }            })    }}
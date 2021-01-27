package com.meepo.feed.article.core.search

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.meepo.design.helper.StatusBarHelper
import com.meepo.feed.R
import com.meepo.feed.Store
import com.meepo.feed.article.core.search.list.SearchListFragment
import com.meepo.feed.schema.SearchMetadata
import com.meepo.sdk.base.mvp.BaseActivity
import com.meepo.sdk.base.mvp.IBaseContract
import com.meepo.sdk.helper.ActivityHelper
import kotlinx.android.synthetic.main.search_activity.*

class SearchActivity : BaseActivity<IContract.IPresenter>(), IContract.IView {

    private var mSearching: Boolean = false
    private var mSearchListFragment: SearchListFragment? = null

    private val onItemClickListener = object : Adapter.OnItemClickListener {
        override fun onItemClick(value: SearchMetadata.SearchKeyword, position: Int) {
            notifySearchingChanged(true, value.name)
        }
    }

    override fun applySearchMetadata(metadata: SearchMetadata?) {
        metadata?.let {

            val guessAdapter = Adapter(this, onItemClickListener)
            guess_rv.adapter = guessAdapter
            guessAdapter.addMore(it.searchKeywords)
        }
    }

    override fun applySearchHistory(words: List<String>?) {

        if (!words.isNullOrEmpty()) {
            history_ll.visibility = View.VISIBLE

            val historyAdapter = Adapter(this, onItemClickListener)
            history_rv.adapter = historyAdapter
            historyAdapter.addMore(words.map { t -> SearchMetadata.SearchKeyword(id = "", name = t) })
        } else {
            history_ll.visibility = View.GONE
        }
    }

    override fun applySearchHistoryChanged() {
        mPresenter?.getSearchHistory()
    }

    override fun initData() {
        mPresenter?.getSearchMetadata()
        mPresenter?.getSearchHistory()
    }

    private fun notifySearchingChanged(searching: Boolean, keyword: String) {

        search_sv.setText(keyword)

        this.mSearching = searching

        frame_layout.visibility = if (searching) View.VISIBLE else View.GONE
        search_keyword_nsv.visibility = if (searching) View.GONE else View.VISIBLE

        // 销毁
        mSearchListFragment?.let {
            ActivityHelper.removeFragment(this.supportFragmentManager, it)
        }

        if (searching) {
            // 搜索结果页
            mSearchListFragment = SearchListFragment(keyword)

            ActivityHelper.showFragment(
                this.supportFragmentManager,
                R.id.frame_layout,
                mSearchListFragment!!
            )
        }

    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        StatusBarHelper.immerse(this, search_rl, true)

        // 默认获取焦点
        search_sv.requestFocus()

        tv_cancel.setOnClickListener {
            if (mSearching) {
                notifySearchingChanged(false, "")
            } else {
                finish()
            }
        }


        // 历史
        history_rv.layoutManager = GridLayoutManager(this, 2)
        // 猜你想搜
        guess_rv.layoutManager = GridLayoutManager(this, 2)

        // 搜索回调
        search_sv.setOnEditorActionListener { textView, actionId, keyEvent ->

            val keyword = textView.text

            if (!TextUtils.isEmpty(keyword)) {
                mPresenter?.putSearchHistory(keyword.toString())

                notifySearchingChanged(true, keyword.toString())

            }

            (textView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                textView.applicationWindowToken,
                0
            )

            true
        }

        // 是否展示推荐
        iv_recommend_toggle.setOnClickListener {
            onRecommendBarToggle()
        }
        show_recommend_ll.setOnClickListener {
            onRecommendBarToggle()
        }

        // 删除历史
        delete_iv.setOnClickListener {
            Log.e("SearchActivity", "clearSearchHistory")
            mPresenter?.clearSearchHistory()
        }
    }

    private fun onRecommendBarToggle() {
        guess_ll.visibility = if (guess_ll.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        show_recommend_ll.visibility =
            if (guess_ll.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }


    override fun setUpContentLayout(): Int = R.layout.search_activity

    override fun setUpPresenter(): IBaseContract.IBasePresenter? = Presenter()
}
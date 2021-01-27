package com.meepo.feed.article.core.history.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.meepo.design.list.ViewHolder
import com.meepo.design.text.tpl.SmartText
import com.meepo.design.text.tpl.TemplateItem
import com.meepo.feed.R
import com.meepo.feed.article.core.detail.DetailActivity
import com.meepo.feed.schema.Item
import com.meepo.sdk.helper.ActivityHelper

class HistoryListAdapter(val mContext: Context) : RecyclerView.Adapter<ViewHolder>() {

    private var mInflater: LayoutInflater? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    private val mItems: ArrayList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = mInflater?.inflate(R.layout.history_list_item_single_image, parent, false)

        return ViewHolder(mContext, view!!)
    }


    override fun getItemCount(): Int {
        return this.mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = this.mItems[position]

        // title
        holder.setText(R.id.title_tv, data.title)

        // 封面图
        data.covers?.let {
            if (it.isNotEmpty()) {
                holder.setImagePath(R.id.iv_1, data.covers[0].content?:"", imageLoader)
            }
        }

        // 补充信息
        holder.getView<SmartText>(R.id.detail_smarttext)?.let {

            val tpls = ArrayList<TemplateItem>()

            data.supplements?.forEachIndexed { index, supplement ->
                tpls.add(
                    TemplateItem(
                        value = supplement.value,
                        size = 10,
                        color = ContextCompat.getColor(mContext, R.color.hint_text_color)
                    )
                )

                if (index != data.supplements.size - 1) {
                    tpls.add(TemplateItem(value = "  "))
                }
            }

            it.setItems(tpls)
        }

        // 点击事件
        holder.setOnItemClickListener(View.OnClickListener {
            ActivityHelper.launch(
                mContext,
                DetailActivity::class.java,
                "item",
                data
            )
        })
    }

    fun addMore(items: List<Item>) {
        this.mItems.addAll(items)

        notifyItemRangeInserted(this.mItems.size - items.size, items.size)
    }


    val imageLoader = object : ViewHolder.ImageLoader {
        override fun loadImage(iv: ImageView, path: String) {
            Glide.with(mContext).load(path).apply(
                RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
            ).transition(DrawableTransitionOptions().crossFade(500))
                .into(iv)
        }

    }
}
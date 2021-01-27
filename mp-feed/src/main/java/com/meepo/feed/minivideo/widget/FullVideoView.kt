package com.meepo.feed.minivideo.widget

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.dueeeke.videoplayer.controller.ControlWrapper
import com.dueeeke.videoplayer.controller.IControlComponent
import com.dueeeke.videoplayer.player.VideoView
import com.dueeeke.videoplayer.util.L
import com.meepo.feed.R
import com.meepo.sdk.helper.Func
import kotlin.math.abs

class FullVideoView : FrameLayout, IControlComponent {
    private var thumb: ImageView? = null
    private var mPlayBtn: ImageView? = null
    private var mControlWrapper: ControlWrapper? = null
    private var mScaledTouchSlop = 0
    private var mStartX = 0
    private var mStartY = 0

    //用来判断是否是连续的点击事件
    private val mHits = LongArray(2)

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * 解决点击和VerticalViewPager滑动冲突问题
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x.toInt()
                mStartY = event.y.toInt()
                // 定长队列
                System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
                mHits[mHits.size - 1] = SystemClock.uptimeMillis()

                return true
            }
            MotionEvent.ACTION_UP -> {
                val endX = event.x.toInt()
                val endY = event.y.toInt()

                if (abs(endX - mStartX) >= mScaledTouchSlop || abs(endY - mStartY) >= mScaledTouchSlop) {
                    return true
                }

                Func.delay(500) {
                    if (SystemClock.uptimeMillis() - mHits[0] > 800) {
                        performClick()
                    }
                }
            }
        }
        return false
    }

    override fun attach(controlWrapper: ControlWrapper) {
        mControlWrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation) {}
    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoView.STATE_IDLE -> {
                L.e("STATE_IDLE " + hashCode())
                thumb!!.visibility = View.VISIBLE
            }
            VideoView.STATE_PLAYING -> {
                L.e("STATE_PLAYING " + hashCode())
                thumb!!.visibility = View.GONE
                mPlayBtn!!.visibility = View.GONE
            }
            VideoView.STATE_PAUSED -> {
                L.e("STATE_PAUSED " + hashCode())
                thumb!!.visibility = View.GONE
                mPlayBtn!!.visibility = View.VISIBLE
            }
            VideoView.STATE_PREPARED -> L.e("STATE_PREPARED " + hashCode())
            VideoView.STATE_ERROR -> {
                L.e("STATE_ERROR " + hashCode())
                Toast.makeText(context, R.string.dkplayer_error_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {}
    override fun setProgress(duration: Int, position: Int) {}
    override fun onLockStateChanged(isLocked: Boolean) {}

    init {
        LayoutInflater.from(context).inflate(R.layout.video_mini_list_item_controller_layout, this, true)
        thumb = findViewById(R.id.thumb_iv)
        mPlayBtn = findViewById(R.id.ctrl_iv)
        setOnClickListener { mControlWrapper!!.togglePlay() }
        mScaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }
}
package com.meepo.feed.minivideo;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dueeeke.videoplayer.controller.BaseVideoController;
import com.meepo.feed.minivideo.widget.DebugInfoView;

/**
 * 抖音
 * Created by dueeeke on 2018/1/6.
 */

public class MiniVideoController extends BaseVideoController {

    public MiniVideoController(@NonNull Context context) {
        super(context);
    }

    public MiniVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniVideoController(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        //显示调试信息
        addControlComponent(new DebugInfoView(getContext()));
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public boolean showNetWarning() {
        //不显示移动网络播放警告
        // todo
        return false;
    }
}

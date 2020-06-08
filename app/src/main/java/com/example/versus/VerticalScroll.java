package com.example.versus;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class VerticalScroll extends ScrollView {
    private VerticalScroll.ScrollViewListener scrollViewListener = null;

    public interface ScrollViewListener {
        void onScrollChanged(VerticalScroll scrollView, int x, int y, int oldx, int oldy);
    }

    public VerticalScroll(Context context) { super(context); }

    public VerticalScroll(Context context, AttributeSet attrs) { super(context, attrs); }

    public VerticalScroll(Context context, AttributeSet attrs, int defStyleAttr) { super(context, attrs, defStyleAttr); }

    public void setScrollViewListener(VerticalScroll.ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }
}

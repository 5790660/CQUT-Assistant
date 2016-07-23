package com.xybst.ui.layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.xybst.ui.view.CourseView;

public class CourseLayout extends ViewGroup {

    private Rect container = new Rect();
    private int divider = dip2px(2);

    public CourseLayout(Context context) {
        super(context);
    }

    public CourseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CourseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        int lessonWidth = getMeasuredWidth() / 7;
        int lessonHeight = getMeasuredHeight() / 11;

        for (int i = 0; i < count; i++) {
            final CourseView child = (CourseView) getChildAt(i);
            if (child.getVisibility() != GONE) {
                container.left =  lessonWidth * (child.getDayOfWeek() - 1) + divider;
                container.right = container.left + lessonWidth - divider ;
                container.top = top + lessonHeight * (child.getStartSection() - 1) + divider;
                container.bottom = container.top + (child.getEndSection() - child.getStartSection() + 1) * lessonHeight - divider;
                // Place the child.
                child.layout(container.left, container.top,
                        container.right, container.bottom);
            }
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}






















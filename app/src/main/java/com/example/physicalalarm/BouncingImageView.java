package com.example.physicalalarm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class BouncingImageView extends androidx.appcompat.widget.AppCompatImageView {
    private View mParent;

    public BouncingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BouncingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BouncingImageView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mParent = (View) getParent();
        getHandler().post(mRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        getHandler().removeCallbacks(mRunnable);
        super.onDetachedFromWindow();
    }

    private final Runnable mRunnable = new Runnable() {
        private static final int DIRECTION_POSITIVE = 1;
        private static final int DIRECTION_NEGATIVE = -1;
        private static final int ANIMATION_STEPS = 4;
        private int mHorizontalDirection = DIRECTION_POSITIVE;
        private int mVerticalDirection = DIRECTION_NEGATIVE;

        public boolean mStarted = false;

        @Override
        public void run() {
            if (mParent == null) {
                return;
            }

            final float width = getMeasuredWidth();
            final float height = getMeasuredHeight();
            final float parentWidth = mParent.getMeasuredWidth();
            final float parentHeight = mParent.getMeasuredHeight();
            float x = getX();
            float y = getY();

            if (!mStarted) {
                /***
                 * Randomize initial position
                 */
                x = (float) Math.random() * (parentWidth - width);
                y = (float) Math.random() * (parentHeight - height);
                mHorizontalDirection = ((int) x % 2 == 0) ? DIRECTION_NEGATIVE : DIRECTION_POSITIVE;
                mVerticalDirection = ((int) y % 2 == 0) ? DIRECTION_NEGATIVE : DIRECTION_POSITIVE;
                mStarted = true;
            } else {
                if (mHorizontalDirection == DIRECTION_NEGATIVE) {
                    x -= ANIMATION_STEPS;
                } else {
                    x += ANIMATION_STEPS;
                }

                if (mVerticalDirection == DIRECTION_NEGATIVE) {
                    y -= ANIMATION_STEPS;
                } else {
                    y += ANIMATION_STEPS;
                }

                if (x - (width / 3) < 0) {
                    mHorizontalDirection = DIRECTION_POSITIVE;
                } else if (x + (width / 3) > (parentWidth - width)) {
                    mHorizontalDirection = DIRECTION_NEGATIVE;
                }

                if (y - (height / 3) < 0) {
                    mVerticalDirection = DIRECTION_POSITIVE;
                } else if (y + (width / 3) > (parentHeight - height)) {
                    mVerticalDirection = DIRECTION_NEGATIVE;
                }
            }

            setX(x);
            setY(y);

            getHandler().post(this);
        }
    };
}

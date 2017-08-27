package com.example.quicksidebar;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.example.quicksidebar.listener.onLetterTouchedListener;

import static android.util.TypedValue.COMPLEX_UNIT_SP;


public class QuickSideTipsView extends View implements onLetterTouchedListener {

    private static final String TAG = "QuickSideTipsView";

    private float mTipTextSize;
    private int mTipTextColor;
    private float mTipViewWidth;
    private float mTipViewHeight;
    private int mTipBgColor;
    private float mTipViewPadding;
    private String mTipLetter;

    public QuickSideTipsView(Context context) {
        this(context, null);
    }

    public QuickSideTipsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideTipsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mTipTextSize = 15;
        mTipViewWidth = 0f;
        mTipViewHeight = 0f;
        mTipLetter = "";
        mTipTextColor = context.getResources().getColor(R.color.tipTextColor);
        mTipBgColor = context.getResources().getColor(R.color.tipBgColor);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QuickSideTipsView);

            for (int i = 0; i < a.length(); i++) {
                int attr = a.getIndex(i);

                if (attr == R.styleable.QuickSideTipsView_tipTextColor) {
                    mTipTextColor = a.getColor(attr, mTipTextColor);
                } else if (attr == R.styleable.QuickSideTipsView_tipTextSize) {
                    mTipTextSize = a.getDimension(attr, TypedValue.applyDimension(COMPLEX_UNIT_SP, mTipTextSize,
                            context.getResources().getDisplayMetrics()));
                } else if (attr == R.styleable.QuickSideTipsView_tipViewBackgroundColor) {
                    mTipBgColor = a.getColor(attr, mTipBgColor);
                } else {
                    Log.d(TAG, "initView: do nothing");
                }
            }

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Paint paintRect = new Paint();
        paintRect.setAntiAlias(true);
        paintRect.setColor(mTipBgColor);
        paintRect.setStyle(Paint.Style.FILL);
        float left = (width - mTipViewWidth) / 2;
        float top = (height - mTipViewHeight) / 2;
        float right = (width + mTipViewWidth) / 2;
        float bottom = (height + mTipViewHeight) / 2;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom, 10, 10, paintRect);
        } else {
            canvas.drawRect(left, top, right, bottom, paintRect);
        }

        Paint paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextSize(mTipTextSize);
        paintText.setColor(mTipTextColor);
        Rect rect = new Rect();
        paintText.getTextBounds(mTipLetter, 0, mTipLetter.length(), rect);
        float x = (width - rect.width()) / 2;
        float y = (height + rect.height()) / 2;
        canvas.drawText(mTipLetter, x, y, paintText);
    }


    @Override
    public void onLetterChanged(String letter) {
        ValueAnimator bgAnimator = ValueAnimator.ofFloat(0, 80, 0);
        bgAnimator.setDuration(1000);
        bgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mTipViewHeight = value;
                mTipViewWidth = value;
                invalidate();
            }
        });

        mTipLetter = letter;
        ValueAnimator textAnimator = ValueAnimator.ofFloat(0, 30, 0);
        textAnimator.setDuration(1000);
        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mTipTextSize = value;
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = animatorSet.play(bgAnimator);
        builder.with(textAnimator);
        animatorSet.start();
    }
}

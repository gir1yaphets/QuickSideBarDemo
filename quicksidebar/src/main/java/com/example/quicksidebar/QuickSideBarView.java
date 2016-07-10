package com.example.quicksidebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class QuickSideBarView extends View {

    private static final String TAG = "QuickSideBarView";

    /**
     * 字体颜色
     */
    private int mTextColor;

    /**
     * 背景颜色
     */
    private int mBackgroundColor;

    /**
     * 字体大小
     */
    private float mTextSize;

    /**
     * 顶部留着间距
     */
    private int mPaddingTop;

    /**
     * sidebar宽度
     */
    private int mWidth;

    /**
     * sidebar高度
     */
    private int mHeight;

    /**
     * 默认26个字母
     */
    private static final int LETTER_SIZE = 26;

    /**
     * 每个字母的高度
     */
    private int mItemHeight;

    /**
     * 画笔对象
     */
    private Paint mPaint;

    /**
     * letters
     */
    private List<String> mLetters;


    private float mTextSizeChoose;
    private int mTextColorChoose;

    public QuickSideBarView(Context context) {
        this(context, null);
    }

    public QuickSideBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickSideBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.quickSideBarLetters));

        mTextColor = context.getResources().getColor(android.R.color.black);
        mTextColorChoose = context.getResources().getColor(android.R.color.black);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar);
        mPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.height_quicksidebartips);
        mTextSizeChoose = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar_choose);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);
            mTextColor = typedArray.getColor(R.styleable.QuickSideBarView_sidebarTextColor, Color.BLACK);
            mTextSize = typedArray.getFloat(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize);

            typedArray.recycle();
        }

        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight() - mPaddingTop;
        mItemHeight = mHeight / mLetters.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mLetters.size(); i++) {
            mPaint.setTextSize(mTextSize);
            mPaint.setColor(mTextColor);
            Rect rect = new Rect();
            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), rect);
            float xPos = mWidth / 2;
            float yPos = mItemHeight * i + rect.height() / 2 + mPaddingTop;
            Log.d(TAG, "onDraw() called with: " + "xPos = [" + xPos + "]");
            Log.d(TAG, "onDraw() called with: " + "yPos = [" + yPos + "]");
            canvas.drawText(mLetters.get(i), xPos, yPos, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }
}

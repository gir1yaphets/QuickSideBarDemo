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

import com.example.quicksidebar.listener.onLetterTouchedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuickSideBarView extends View {

    private static final String TAG = "QuickSideBarView";

    private int mTextColor;
    private int mBackgroundColor;

    private float mTextSize;
    private int mPaddingTop;
    private int mWidth;
    private int mHeight;
    private static final int LETTER_SIZE = 26;
    private int mItemHeight;
    private Paint mPaint;
    private List<String> mLetters;
    private int mChoose = -1;
    private List<onLetterTouchedListener> mListeners;

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

        mListeners = new ArrayList<>();
        mLetters = Arrays.asList(context.getResources().getStringArray(R.array.quickSideBarLetters));

        mTextColor = context.getResources().getColor(android.R.color.black);
        mTextColorChoose = context.getResources().getColor(android.R.color.black);
        mBackgroundColor = context.getResources().getColor(R.color.backgroundColor);
        mTextSize = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar);
        mPaddingTop = context.getResources().getDimensionPixelSize(R.dimen.height_quicksidebartips);
        mTextSizeChoose = context.getResources().getDimensionPixelSize(R.dimen.textSize_quicksidebar_choose);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickSideBarView);
            mTextColor = typedArray.getColor(R.styleable.QuickSideBarView_sidebarTextColor, Color.BLACK);
            mTextSize = typedArray.getDimension(R.styleable.QuickSideBarView_sidebarTextSize, mTextSize);
            mBackgroundColor = typedArray.getColor(R.styleable.QuickSideBarView_sidebarBackgroundColor, mBackgroundColor);

            typedArray.recycle();
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        Rect textBound = new Rect();
        mPaint.setTextSize(mTextSize);
        String letter = mLetters.get(0);
        mPaint.getTextBounds(letter, 0, letter.length(), textBound);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = width;
        } else {
            mWidth = textBound.width() * 2;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = height;
            mItemHeight = mHeight / mLetters.size();
        } else {
            mItemHeight = textBound.height() * 2;
            mHeight = mItemHeight * mLetters.size() + mPaddingTop;
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        canvas.clipRect(0, mPaddingTop, mWidth, mHeight);
        canvas.drawColor(mBackgroundColor);
        canvas.restore();

        for (int i = 0; i < mLetters.size(); i++) {
            mPaint.setTextSize(mTextSize);
            mPaint.setColor(mTextColor);
            Rect rect = new Rect();
            mPaint.getTextBounds(mLetters.get(i), 0, mLetters.get(i).length(), rect);
            float xPos = (mWidth - rect.width()) / 2;
            float yPos = mItemHeight * i + (mItemHeight + rect.height()) / 2 + mPaddingTop;
            canvas.drawText(mLetters.get(i), xPos, yPos, mPaint);
        }
    }

    private float mTouchY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = event.getY();
                if (mTouchY < mPaddingTop) {
                    break;
                }
                return true;
            case MotionEvent.ACTION_UP:
                int newChoose = (int) ((mTouchY - mPaddingTop) / (mHeight - mPaddingTop) * mLetters.size());

                Log.d(TAG, "dispatchTouchEvent() called with: " + "oldChoose = [" + mChoose + "]");
                Log.d(TAG, "dispatchTouchEvent() called with: " + "newChoose = [" + newChoose + "]");

                if (mChoose != newChoose) {
                    mChoose = newChoose;

                    for (onLetterTouchedListener l : mListeners) {
                        l.onLetterChanged(mLetters.get(mChoose));
                    }
                }
                return true;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    public void addOnLetterTouchListener(onLetterTouchedListener l) {
        mListeners.add(l);
    }
}

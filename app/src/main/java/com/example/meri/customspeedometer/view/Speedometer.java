package com.example.meri.customspeedometer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.meri.customspeedometer.R;

public class Speedometer extends View {

    private int mMinSpeed, mMaxSpeed;
    private int mYellow, mBlack, mWhite;

    private int mCurrentSpeed, mSpeed;

    private Paint mPaint;
    private RectF mRect;

    public Speedometer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCurrentSpeed = 0;
        mSpeed = 0;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new RectF();

        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.Speedometer, 0, 0);

        try {
            mMinSpeed = arr.getInt(R.styleable.Speedometer_minSpeed, 0);
            mMaxSpeed = arr.getInt(R.styleable.Speedometer_maxSpeed, 200);
            mYellow = arr.getColor(R.styleable.Speedometer_yellowColor, 0);
            mBlack = arr.getColor(R.styleable.Speedometer_blackColor, 0);
            mWhite = arr.getColor(R.styleable.Speedometer_whiteColor, 0);
        } finally {
            arr.recycle();
        }
    }

    public void increaseSpeed(){
        if(mCurrentSpeed != mMaxSpeed){
            mCurrentSpeed += 2;
        }
    }

    public void decreaseSpeed(){
        if(mCurrentSpeed != mMinSpeed){
            mCurrentSpeed -= 2;
        }
    }

    private float mAngle = 0;
    private int mWidth, mHeight;

    private final int pointsCount = 110;
    private final int pointsMaxHeight = 25;
    private final int pointsMinHeight = 15;
    private int pointHeight;

    @Override
    protected void onDraw(Canvas canvas) {

        mWidth = canvas.getWidth() / 2;
        mHeight = canvas.getHeight() / 2;
        mPaint.setColor(mYellow);
        mPaint.setStyle(Paint.Style.STROKE);
        
        mRect.set(mWidth - 350, mHeight - 350,
                mWidth + 350, mHeight + 350);

        drawArcs(canvas);
        drawTicks(canvas);
        drawCircle(canvas);
        drawNeedle(canvas);

        mSpeed = 0;
    }
    
    private void drawNeedle(Canvas canvas){
        canvas.rotate(-260f, mWidth, mHeight);
        mPaint.setStrokeWidth(4f);

        canvas.drawLine(
                (float) (mWidth + Math.cos((180f - mAngle) / 180f * Math.PI)),
                (float) (mHeight - Math.sin(mAngle / 180f * Math.PI)),
                (float) (mWidth + Math.cos((180f - mAngle) / 180f * Math.PI) * (150)),
                (float) (mHeight - Math.sin(mAngle / 180f * Math.PI) * (150)),
                mPaint
        );
    }

    private void drawCircle(Canvas canvas){
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth, mHeight, 15, mPaint);
    }

    private void drawTicks(Canvas canvas){
        canvas.rotate(-40f, mWidth, mHeight);
        float angle = 0f;
        for (int i = 0; i <= 2*pointsCount; i += 2){
            if(i % 20 == 0){
                pointHeight = pointsMaxHeight;
                mPaint.setStrokeWidth(1f);
                mPaint.setColor(mWhite);

                drawNumbers(canvas, angle);

                mPaint.setStrokeWidth(4f);
            } else {
                pointHeight = pointsMinHeight;
                mPaint.setStrokeWidth(3f);
                mPaint.setColor(mYellow);
            }

            angle += 260 / pointsCount;

            mPaint.setStrokeWidth(5f);
            canvas.drawLine(mWidth - 300, mHeight, mWidth - 300 - pointHeight,
                    mHeight, mPaint);
            canvas.rotate(260f / pointsCount, mWidth, mHeight);
        }
    }

    private void drawNumbers(Canvas canvas, float angle){
        mPaint.setTextSize(20f);
        String speed = Integer.toString(mSpeed);
        canvas.save();
        float numberX = mWidth - 250;
        float numberY = mHeight;
        canvas.rotate(-angle, numberX, numberY);
        canvas.drawText(speed, numberX, numberY, mPaint);
        canvas.restore();
        mSpeed += 20;
    }

    private void drawArcs(Canvas canvas){
        if(mCurrentSpeed < mMaxSpeed){
            mPaint.setStrokeWidth(8f);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawArc(mRect, 140f, mAngle, false, mPaint);
        } else {
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(8f);
            canvas.drawArc(mRect, 140f, 260f, false, mPaint);
        }

        mRect.set(mWidth - 100, mHeight - 100, mWidth + 100, mHeight + 100);
        mPaint.setColor(mWhite);
        mPaint.setStrokeWidth(1f);
        canvas.drawArc(mRect, 160f, 220f, false, mPaint);
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    public void speedUp(final TextView textView){
        mHandler.removeCallbacks(mRunnable);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mAngle != 250) {
                    mAngle += 260 / pointsCount;
                    invalidate();

                    textView.setText(mCurrentSpeed + " km/h");
                    mHandler.postDelayed(this, 100);
                    increaseSpeed();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 100);
    }

    public void speedDown(final TextView textView){
        mHandler.removeCallbacks(mRunnable);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mAngle != 0) {
                    mAngle -= 260 / pointsCount;
                    invalidate();

                    textView.setText(mCurrentSpeed + "km/h");
                    mHandler.postDelayed(this, 100);
                    decreaseSpeed();
                }
            }
        };
        mHandler.postDelayed(mRunnable, 100);
    }
}
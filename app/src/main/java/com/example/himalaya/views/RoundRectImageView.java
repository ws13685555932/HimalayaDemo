package com.example.himalaya.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class RoundRectImageView extends AppCompatImageView {

    private float roundRatio = 0.1f;
    private Path mPath;
    private RectF mRectF;

    public RoundRectImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(0, 0, getWidth(), getHeight());
        mPath.addRoundRect(mRectF, roundRatio * getWidth(), roundRatio * getHeight(), Path.Direction.CW);
        canvas.save();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();
    }
}

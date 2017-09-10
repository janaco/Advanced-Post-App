package com.nandy.vkchanllenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

import java.security.MessageDigest;

/**
 * Created by yana on 10.09.17.
 */

public class RoundedCornersTransformation implements Transformation<Bitmap> {

    public enum CornerType {
        ALL, BORDER
    }

    private BitmapPool mBitmapPool;
    private int mRadius;
    private int mDiameter;
    private int mMargin;
    private CornerType mCornerType;
    private int mColor = Color.BLACK;
    private int mBorder;

    public RoundedCornersTransformation(Context context, int radius, int margin) {
        this(context, radius, margin, CornerType.ALL);
    }

    public RoundedCornersTransformation(Context context, int radius, int margin, int color, int border) {
        this(context, radius, margin, CornerType.BORDER);
        mColor = color;
        mBorder = border;
    }

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin) {
        this(pool, radius, margin, CornerType.ALL);
    }

    public RoundedCornersTransformation(Context context, int radius, int margin,
                                        CornerType cornerType) {
        this(Glide.get(context).getBitmapPool(), radius, margin, cornerType);
    }

    public RoundedCornersTransformation(BitmapPool pool, int radius, int margin,
                                        CornerType cornerType) {
        mBitmapPool = pool;
        mRadius = radius;
        mDiameter = mRadius * 2;
        mMargin = margin;
        mCornerType = cornerType;
    }


    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - mMargin;
        float bottom = height - mMargin;

        switch (mCornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                break;

            case BORDER:
                drawBorder(canvas, paint, width, height);
                break;
            default:
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                break;
        }
    }


//    private void drawBorder(Canvas canvas, Paint paint, float right,
//                            float bottom) {
//
//        // stroke
//        Paint strokePaint = new Paint();
//        strokePaint.setStyle(Paint.Style.STROKE);
//        strokePaint.setColor(mColor);
//        strokePaint.setStrokeWidth(mBorder);
//
//        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom), paint);
//
//        // stroke
//        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, strokePaint);
//    }


    private void drawBorder(Canvas canvas, Paint paint, float width,
                            float height) {

        // stroke
        Paint strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeWidth(mBorder);

        canvas.drawRect(new RectF(mMargin*2, mMargin*2, width-mMargin*2, height-mMargin*2),  paint);

        canvas.drawRoundRect(new RectF(mMargin, mMargin, width-mMargin, height-mMargin), mRadius, mRadius, strokePaint);

        strokePaint.setColor(mColor);
        // stroke
        canvas.drawRoundRect(new RectF(mMargin/2, mMargin/2, width - mMargin/2, height - mMargin/2), mRadius, mRadius, strokePaint);
    }


    public String getId() {
        return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                + mDiameter + ", cornerType=" + mCornerType.name() + ")";
    }
}
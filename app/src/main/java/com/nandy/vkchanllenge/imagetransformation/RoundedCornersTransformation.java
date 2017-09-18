package com.nandy.vkchanllenge.imagetransformation;

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

/**
 * Created by yana on 10.09.17.
 */

public class RoundedCornersTransformation implements Transformation<Bitmap> {

    public enum CornerType {
        ALL, BORDER
    }

    private BitmapPool bitmapPool;
    private int radius;
    private int diameter;
    private int margin;
    private CornerType mCornerType;
    private int color = Color.BLACK;
    private int border;

    public RoundedCornersTransformation(Context context, int radius, int margin) {
        this(context, radius, margin, CornerType.ALL);
    }

    public RoundedCornersTransformation(Context context, int radius, int margin, int color, int border) {
        this(context, radius, margin, CornerType.BORDER);
        this.color = color;
        this.border = border;
    }


    private RoundedCornersTransformation(Context context, int radius, int margin,
                                        CornerType cornerType) {
        this(Glide.get(context).getBitmapPool(), radius, margin, cornerType);
    }

    private RoundedCornersTransformation(BitmapPool pool, int radius, int margin,
                                        CornerType cornerType) {
        bitmapPool = pool;
        this.radius = radius;
        diameter = this.radius * 2;
        this.margin = margin;
        mCornerType = cornerType;
    }


    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - margin;
        float bottom = height - margin;

        switch (mCornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;

            case BORDER:
                drawBorder(canvas, paint, width, height);
                break;
            default:
                canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
                break;
        }
    }

    private void drawBorder(Canvas canvas, Paint paint, float width,
                            float height) {

        Paint strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.WHITE);
        strokePaint.setStrokeWidth(border);

        canvas.drawRect(new RectF(margin * 2, margin * 2, width - margin * 2, height - margin * 2), paint);

        canvas.drawRoundRect(new RectF(margin, margin, width - margin, height - margin), radius, radius, strokePaint);

        strokePaint.setColor(color);
        canvas.drawRoundRect(new RectF(margin / 2, margin / 2, width - margin / 2, height - margin / 2), radius, radius, strokePaint);

    }


    public String getId() {
        return "RoundedTransformation(radius=" + radius + ", margin=" + margin + ", diameter="
                + diameter + ", cornerType=" + mCornerType.name() + ")";
    }

    public static Bitmap transform(Bitmap source, int imageSize, boolean withBorder, int radius, int border, int margin, int color) {


        Bitmap bitmap = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        float right = imageSize - margin;
        float bottom = imageSize - margin;

        if (withBorder) {
            Paint strokePaint = new Paint();
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setColor(Color.WHITE);
            strokePaint.setStrokeWidth(border);

            canvas.drawRect(new RectF(margin * 2, margin * 2, imageSize - margin * 2, imageSize - margin * 2), paint);

            canvas.drawRoundRect(new RectF(margin, margin, imageSize - margin, imageSize - margin), radius, radius, strokePaint);

            strokePaint.setColor(color);
            canvas.drawRoundRect(new RectF(margin / 2, margin / 2, imageSize - margin / 2, imageSize - margin / 2), radius, radius, strokePaint);

        } else {
            canvas.drawRoundRect(new RectF(margin, margin, right, bottom), radius, radius, paint);
        }
        return bitmap;
    }

}
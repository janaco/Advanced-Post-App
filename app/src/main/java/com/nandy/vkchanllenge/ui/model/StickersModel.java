package com.nandy.vkchanllenge.ui.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nandy.vkchanllenge.R;
import com.nandy.vkchanllenge.ui.Background;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 09.09.17.
 */

public class StickersModel {

    private static final int TRASH_SIZE = 48;
    private static final int TRASH_RELEASED_SIZE = 56;

    private Context context;
    private final List<Bitmap> stickers = new ArrayList<>();
    private StickerTouchListener stickerTouchListener;
    private ImageView viewTrash;
    private boolean highlightTrashView;

    private float scaledDensity;
    private int trashPadding = 30;

    public StickersModel(Context context) {
        this.context = context;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        scaledDensity = displayMetrics.scaledDensity;
        trashPadding *= scaledDensity;

        viewTrash = new ImageView(context);
        int size = (int) scaledDensity * 48;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.bottomMargin = (int) scaledDensity * 16;
        viewTrash.setLayoutParams(params);
    }


    public void setStickerTouchListener(StickerTouchListener stickerTouchListener) {
        this.stickerTouchListener = stickerTouchListener;
    }


    public void setHighlightTrashView(boolean highlightTrashView) {
        this.highlightTrashView = highlightTrashView;
    }

    public Single<List<Bitmap>> loadStickers() {

        Single<List<Bitmap>> single = Single.create(e -> {
            AssetManager assetManager = context.getAssets();

            try {
                String[] stickerFiles = assetManager.list("stickers");
                for (String sticker : stickerFiles) {
                    InputStream is = assetManager.open("stickers/" + sticker);
                    stickers.add(BitmapFactory.decodeStream(is));
                }
            } catch (IOException error) {
                error.printStackTrace();
            }

        });
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        return single;
    }

    public List<Bitmap> getStickers() {
        return stickers;
    }

    public ImageView createStickerView(Bitmap bitmap) {
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setOnTouchListener(new OnStickerTouchListener(stickerTouchListener));


        return imageView;
    }


    private class OnStickerTouchListener implements View.OnTouchListener {


        // these matrices will be used to move and zoom image
        private Matrix matrix = new Matrix();
        private Matrix savedMatrix = new Matrix();
        // we can be in one of these 3 states
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        private int mode = NONE;
        // remember some things for zooming
        private PointF start = new PointF();
        private PointF mid = new PointF();
        private float oldDist = 1f;
        private float d = 0f;
        private float newRot = 0f;
        private float[] lastEvent = null;

        private StickerTouchListener stickerTouchListener;

        public OnStickerTouchListener(StickerTouchListener stickerTouchListener) {
            this.stickerTouchListener = stickerTouchListener;
        }

        int trashXLeft = (int) viewTrash.getX();
        int trashXRight = trashXLeft + viewTrash.getWidth();
        int trashYTop = (int) viewTrash.getY();
        int trashYBottom = trashYTop + viewTrash.getHeight();
        private boolean remove;


        private Disposable trashSubscription;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // handle touch events here
            ImageView view = (ImageView) v;
            float x =  event.getX();
            float y =  event.getY();


            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:

                    savedMatrix.set(matrix);
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    lastEvent = null;
                    trashSubscription = Observable.just(true).delay(500, TimeUnit.MILLISECONDS)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(aBoolean -> {
                                viewTrash.setImageResource(highlightTrashView ? R.drawable.ic_trash_highlighted : R.drawable.ic_trash);
                                stickerTouchListener.showTrash(viewTrash);
                                return true;
                            })
                            .subscribe();
                    trashXLeft = (int) viewTrash.getX();
                    trashXRight = trashXLeft + viewTrash.getWidth();
                    trashYTop = (int) viewTrash.getY();
                    trashYBottom = trashYTop + viewTrash.getHeight();


                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = (float) spacing(event);
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    lastEvent = new float[4];
                    lastEvent[0] = event.getX(0);
                    lastEvent[1] = event.getX(1);
                    lastEvent[2] = event.getY(0);
                    lastEvent[3] = event.getY(1);
                    d = rotation(event);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    mode = NONE;
                    lastEvent = null;
                    viewTrash.setImageResource(highlightTrashView ? R.drawable.ic_trash_highlighted : R.drawable.ic_trash);
                    stickerTouchListener.remove(viewTrash);
                    if (remove) {
                        stickerTouchListener.remove(view);
                    }
                    trashSubscription.dispose();


                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == DRAG) {
                        matrix.set(savedMatrix);
                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        matrix.postTranslate(dx, dy);

                    } else if (mode == ZOOM) {
                        float newDist = (float) spacing(event);
                        if (newDist > 10f) {
                            matrix.set(savedMatrix);
                            float scale = (newDist / oldDist);
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                        if (lastEvent != null && event.getPointerCount() == 2) {
                            newRot = rotation(event);
                            float r = newRot - d;
                            float[] values = new float[9];
                            matrix.getValues(values);
                            float xc = (view.getWidth() / 2);
                            float yc = (view.getHeight() / 2);
                            matrix.postRotate(r, xc, yc);
                        }
                    }


                    if (x + trashPadding > trashXLeft && x - trashPadding < trashXRight
                            && y + trashPadding > trashYTop && y - trashPadding < trashYBottom) {
                        viewTrash.setImageResource(highlightTrashView ? R.drawable.ic_trash_released_highlighted : R.drawable.ic_trash_released);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewTrash.getLayoutParams();
                        params.width = (int) (TRASH_RELEASED_SIZE * scaledDensity);
                        params.height = (int) (TRASH_RELEASED_SIZE * scaledDensity);
                        viewTrash.setLayoutParams(params);
                        view.setAlpha(0.48f);
                        remove = true;

                    } else {
                        viewTrash.setImageResource(highlightTrashView ? R.drawable.ic_trash_highlighted : R.drawable.ic_trash);
                        view.setAlpha(1f);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewTrash.getLayoutParams();
                        params.width = (int) (TRASH_SIZE * scaledDensity);
                        params.height = (int) (TRASH_SIZE * scaledDensity);
                        viewTrash.setLayoutParams(params);
                        remove = false;
                    }

                    break;
            }

            view.setImageMatrix(matrix);
            return true;
        }

        /**
         * Determine the space between the first two fingers
         */
        private double spacing(MotionEvent event) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return Math.sqrt(x * x + y * y);
        }

        /**
         * Calculate the mid point of the first two fingers
         */
        private void midPoint(PointF point, MotionEvent event) {
            float x = event.getX(0) + event.getX(1);
            float y = event.getY(0) + event.getY(1);
            point.set(x / 2, y / 2);
        }

        /**
         * Calculate the degree to be rotated by.
         *
         * @param event
         * @return Degrees
         */
        private float rotation(MotionEvent event) {
            double delta_x = (event.getX(0) - event.getX(1));
            double delta_y = (event.getY(0) - event.getY(1));
            double radians = Math.atan2(delta_y, delta_x);
            return (float) Math.toDegrees(radians);
        }
    }

    public interface StickerTouchListener {

        void showTrash(View viewTrash);

        void remove(View view);
    }
}

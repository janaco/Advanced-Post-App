package com.nandy.vkchanllenge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageButton;

/**
 * Created by yana on 10.09.17.
 */

public class CheckableImageButton extends AppCompatImageView implements Checkable {

    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = {R.attr.is_checked};


    public CheckableImageButton(Context context) {
        this(context, null);
    }

    public CheckableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckableImageButton);

        boolean checked = a.getBoolean(R.styleable.CheckableImageButton_is_checked, false);
        setChecked(checked);

        a.recycle();
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    public boolean isChecked() {
        return mChecked;
    }

    /**
     * <p>
     * Changes the checked state of this button.
     * </p>
     *
     * @param checked true to check the button, false to uncheck it
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }


    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {

        if (!isChecked()){
            bm = getRoundedBitmap(bm, 4);
        }

        super.setImageBitmap(bm);
    }

    private Bitmap getRoundedBitmap(Bitmap srcBitmap, int cornerRadius) {
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth(),
                srcBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Rect rect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        srcBitmap.recycle();

        return dstBitmap;
    }


    static class SavedState extends BaseSavedState {
        boolean checked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            checked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(checked);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }
}
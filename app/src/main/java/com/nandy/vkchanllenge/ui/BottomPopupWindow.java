package com.nandy.vkchanllenge.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by yana on 11.09.17.
 */

public abstract  class BottomPopupWindow {
    private int screenHeight = -1;
    private int popupHeight = -1;
    private PopupWindow popupWindow;
    private View parentView;


    public BottomPopupWindow(View parentView) {
            this.parentView = parentView;

            parentView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
                screenHeight = screenHeight == -1 && bottom > oldBottom ? bottom : screenHeight;

                int dHeight = oldBottom - bottom;
                boolean validHeight = popupHeight == -1 && dHeight > 80 && bottom != oldBottom;

                popupHeight = validHeight ? dHeight : popupHeight != (dHeight) && dHeight > 0 ? dHeight : popupHeight;

                if (screenHeight == bottom) {
                    dismiss();
                }

                resize();
            });
    }

    public Context getContext(){
        return parentView.getContext();
    }

    public void showPopup() {
        if (popupWindow == null) {
            createPopupWindowView();
        }

        if (!isShowed()) {
            new Handler().postDelayed(() -> {
                popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                resize();
            }, 10L);

        } else {
            dismiss();
        }
    }

    public abstract View onCreateView(Context context, View parent);

    public abstract void onViewCreated(View view);

    public void createPopupWindowView() {

        View view = onCreateView(parentView.getContext(), parentView);
        parentView.setFocusable(true);
        popupWindow = new PopupWindow(view);
        popupWindow.setHeight(View.MeasureSpec.makeMeasureSpec(setPopupHeight(), View.MeasureSpec.EXACTLY));
        popupWindow.setWidth(View.MeasureSpec.makeMeasureSpec(getDisplayDimensions(getContext()).x, View.MeasureSpec.EXACTLY));
        onViewCreated(view);
    }

    public void dismiss() {
        if (isShowed()) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowed() {
        return popupWindow != null && popupWindow.isShowing();
    }

    private void resize() {
        if (isShowed()) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) popupWindow.getContentView().getRootView().getLayoutParams();
            layoutParams.height = setPopupHeight();
            wm.updateViewLayout(popupWindow.getContentView().getRootView(), layoutParams);
        }
    }

    private int setPopupHeight() {
        return popupHeight == -1 && popupHeight != screenHeight && popupHeight < 80
                ? (getDisplayDimensions(getContext()).y / 2)
                : popupHeight;
    }

    private Point getDisplayDimensions(Context context) {
        Point size = new Point();
        WindowManager w = ((Activity) context).getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
        } else {
            Display d = w.getDefaultDisplay();
            size.x = d.getWidth();
            size.y = d.getHeight();
        }
        return size;
    }
}
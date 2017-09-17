package com.nandy.vkchanllenge.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by yana on 17.09.17.
 */

public class WindowUtils {

    public static float getDensity(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null ){
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);

            return metrics.scaledDensity;
        }

        return 3;
    }
}

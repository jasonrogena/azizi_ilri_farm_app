package org.cgiar.ilri.farm.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;

/**
 * Created by jrogena on 20/01/2016.
 */
public class Display {
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getHeight(Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static int getWidth(Activity activity) {
        android.view.Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }
}

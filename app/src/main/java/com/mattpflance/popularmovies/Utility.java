package com.mattpflance.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by MattPflance on 2016-02-15.
 */
public class Utility {

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}

package com.when0matters.xlist.Helper;

import android.support.v4.content.ContextCompat;

import com.when0matters.xlist.Application.XListApplication;

/**
 * Created by HuiMin on 1/29/2017.
 */

public class ResourceHelper {
    public static String getString(int id) {
        return XListApplication.getContext().getResources().getString(id);
    }

    public static int getColor(int id) {
        return ContextCompat.getColor(XListApplication.getContext(), id);
    }

    public static String[] getStringArray(int id) {
        return XListApplication.getContext().getResources().getStringArray(id);
    }
}

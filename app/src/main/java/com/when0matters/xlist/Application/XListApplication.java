package com.when0matters.xlist.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by HuiMin on 1/29/2017.
 */

public class XListApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){ return mContext; }
}

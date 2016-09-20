/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * MenuUtil.java
 * 
 * Description
 *  
 * Author nongzhanfei
 * 
 * Ver 1.0, 9/19/16, NongZhanfei, Create file
 */
package com.tplink.tpsoundrecorder.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 传入布局文件与菜单文件
 */
public class MenuUtil {
    public static void setMenuRL(Activity activity, int viewId,int layoutId) {
        View view = doView(activity, viewId);
        RelativeLayout layout = (RelativeLayout) view.findViewById(layoutId);
        setcontent(activity,layout);
    }

    public static void setMenuLL(Activity activity, int viewId,int layoutId) {
        View view = doView(activity, viewId);
        LinearLayout layout = (LinearLayout) view.findViewById(layoutId);
        setcontent(activity,layout);
    }

    public static void setMenuCL(Activity activity, int viewId,int layoutId) {
        View view = doView(activity, viewId);
        CoordinatorLayout layout = (CoordinatorLayout) view.findViewById(layoutId);
        setcontent(activity,layout);
    }
    private static View doView(Activity activity, int viewId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        //还要设置偏移，否则状态栏和内容重叠
        View view = View.inflate(activity, viewId, null);
        return view;
    }

    private static void setcontent(Activity activity, RelativeLayout layout) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout content = new LinearLayout(activity);
        content.addView(layout, lp);
        activity.setContentView(content);
    }
    private static void setcontent(Activity activity, LinearLayout layout) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout content = new LinearLayout(activity);
        content.addView(layout, lp);
        activity.setContentView(content);
    }
    private static void setcontent(Activity activity, CoordinatorLayout layout) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        LinearLayout content = new LinearLayout(activity);
        content.addView(layout, lp);
        activity.setContentView(content);
    }
}
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        Window window = activity.getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        //还要设置偏移，否则状态栏和内容重叠
//        View view = View.inflate(activity, viewId, null);
//        CoordinatorLayout layout = (CoordinatorLayout) view.findViewById(layoutId);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.MATCH_PARENT);
//
//        LinearLayout content = new LinearLayout(activity);
//        content.addView(layout, lp);
//        activity.setContentView(content);

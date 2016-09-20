/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * SPUtil.java
 * 
 * Description
 *  
 * Author nongzhanfei
 * 
 * Ver 1.0, 9/20/16, NongZhanfei, Create file
 */
package com.tplink.tpsoundrecorder.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;


import java.io.File;
import java.lang.reflect.Field;

public class SPUtil {

    private void savePreToSDcard(Context context) {
        try {
            Field field;
            // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            // 获取mBase变量
            Object obj = field.get(this);
            // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            // 创建自定义路径
            File file = new File("/sdcard");
            // 修改mPreferencesDir变量的值
            field.set(obj, file);
            SharedPreferences mySharedPreferences = context.getSharedPreferences(
                    "config", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("name", "20130310");
            editor.commit();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

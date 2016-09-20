/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * FileUtil.java
 * 
 * Description
 *  
 * Author nongzhanfei
 * 
 * Ver 1.0, 9/19/16, NongZhanfei, Create file
 */
package com.tplink.tpsoundrecorder.util;

import android.content.Context;
import android.os.Environment;

import com.tplink.tpsoundrecorder.R;
import com.tplink.tpsoundrecorder.service.SoundRecorderService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<File> getFiles(Context context) {
        ArrayList<File> files = new ArrayList<File>();

        String sdPath = null;
        String internalPath;
        files.clear();

        if (AndroidUtil.getSDState(context) != null
                && AndroidUtil.getSDState(context).equals(Environment.MEDIA_MOUNTED)) {
            if (context.getResources().getBoolean(R.bool.config_storage_path)) {
                sdPath = AndroidUtil.getSDPath(context) + File.separator
                        + SoundRecorderService.FOLDER_NAME;
            } else {
                sdPath = AndroidUtil.getSDPath(context) + File.separator
                        + SoundRecorderService.FOLDER_NAME;
            }
        }
        //  /storage/emulated/0     /      SoundRecorder
        internalPath = AndroidUtil.getPhoneLocalStoragePath(context) + File.separator
                + SoundRecorderService.FOLDER_NAME;
        //SD卡路径
        if (sdPath != null) {
            File sdDir = new File(sdPath);
            File[] soundFileList = sdDir.listFiles();
            if (soundFileList != null) {
                for (File file : soundFileList) {
                    if (file.getName().endsWith(".aac") || file.getName().endsWith(".wav")
                            || file.getName().endsWith(".amr")) {
                        files.add(file);
                    }
                }
            }
        }
        //内部路径
        if (internalPath != null) {
            File internalDir = new File(internalPath);
            File[] soundFileList = internalDir.listFiles();
            if (soundFileList != null) {
                for (File file : soundFileList) {
                    if (file.getName().endsWith(".aac") || file.getName().endsWith(".wav")
                            || file.getName().endsWith(".amr")) {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }
}

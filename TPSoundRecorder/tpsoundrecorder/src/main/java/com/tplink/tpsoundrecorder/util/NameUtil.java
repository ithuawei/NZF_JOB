/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * NameUtil.java
 * 
 * Description
 *  
 * Author nongzhanfei
 * 
 * Ver 1.0, 9/20/16, NongZhanfei, Create file
 */
package com.tplink.tpsoundrecorder.util;

import java.io.File;
import java.util.List;

/**
 * 命名的需要，如果是录制存储,首个个就是01
 */
public class NameUtil {
    public static String getNames(List<File> files) {
        String num = files.size() + "";
        if (num.length() == 1) {
            num = "0" + num;
        }
        return "Recording - " + num;
    }
}

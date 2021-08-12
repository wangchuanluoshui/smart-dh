package com.hyn.utils;

import org.springframework.util.ResourceUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/*
 * @Classname Test
 * @Description TODO
 * @Date 2020/8/19 8:31
 * @Created by 62538
 */
public class Test {

    static int applyBatchNum = 10;

    static String test = "qqqq";

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);
    }

    public static void main(String[] args) {
        System.out.println(getStringByLength(55).length());
    }


    public static String getStringByLength(int len) {
        char[] chars = new char[len];
        chars[0] = '\0';
        return new String(chars);
    }
}

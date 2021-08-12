package com.hyn.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/*
 * @Classname MultipartFileToFileUtils
 * @Description TODO
 * @Date 2020-12-26 11:53
 * @Created by 62538
 */
public class MultipartFileToFileUtils {
    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }

    public static String saveFile(MultipartFile file, String path) {
        // 判断文件是否为空
        String savePath = path + "/" + file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                File filepath = new File(path);
                if (!filepath.exists()) {
                    filepath.mkdirs();
                }
                // 文件保存路径
                //复制
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(savePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return savePath;
    }
}

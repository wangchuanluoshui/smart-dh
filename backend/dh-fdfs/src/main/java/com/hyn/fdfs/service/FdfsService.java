package com.hyn.fdfs.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
 * @Classname FdfsService
 * @Description TODO
 * @Date 2021-01-02 21:23
 * @Created by 62538
 */
public interface FdfsService {
    String upload(MultipartFile multipartFile, String fileExtName) throws IOException;
}

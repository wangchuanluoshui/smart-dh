package com.hyn.fdfs.service;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
 * @Classname FdfsServiceImpl
 * @Description TODO
 * @Date 2021-01-02 21:26
 * @Created by 62538
 */
@Service
public class FdfsServiceImpl implements FdfsService {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Override
    public String upload(MultipartFile multipartFile, String fileExtName) throws IOException {
        StorePath storePath = fastFileStorageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), fileExtName, null);
        String path = storePath.getFullPath();
        return path;
    }
}

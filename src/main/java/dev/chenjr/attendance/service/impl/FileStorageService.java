package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static dev.chenjr.attendance.utils.RandomUtil.randomStringWithDate;
import static org.springframework.util.StringUtils.getFilenameExtension;

@Service
public class FileStorageService implements IStorageService {

    @Value("${storage.file.storage-path}")
    String storagePath;


    @Value("${storage.file.url-prefix}")
    String urlPrefix;

    /**
     * 存储上传的文件
     *
     * @param uploaded 上传的文件
     * @return 文件存储的名字，用来存在数据库里
     */
    @Override
    public String storeFile(MultipartFile uploaded) throws HttpStatusException {
        String storeName = randomStringWithDate(20);
        String extension = getFilenameExtension(uploaded.getOriginalFilename());
        storeName = storeName + '.' + extension;
        File saveFile = new File(getFullStoragePath(storeName));
        try {
            uploaded.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw HttpStatusException.badRequest("上传失败！");
        }
        return storeName;
    }

    /**
     * 返回访问静态资源的URL前缀
     *
     * @param storeName 文件存储的文件名
     * @return URL前缀
     */
    @Override
    public String getFullUrl(String storeName) {
        if (storeName == null || storeName.trim().length() == 0) {
            return null;
        }
        return urlPrefix + storeName;
    }

    /**
     * 返回访问静态资源的URL前缀
     *
     * @param storeName   文件存储的文件名
     * @param defaultName 默认文件名
     * @return URL前缀
     */
    @Override
    public String getFullUrl(String storeName, String defaultName) {
        if (storeName == null || storeName.trim().length() == 0) {
            return urlPrefix + defaultName;
        }
        return urlPrefix + storeName;
    }

    /**
     * 返回存储的路径
     *
     * @param storeName 文件存储的文件名
     * @return 存储的路径
     */
    @Override
    public String getFullStoragePath(String storeName) {
        return storagePath + storeName;
    }

    /**
     * @return 存储的路径
     */
    @Override
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * @return url前缀
     */
    @Override
    public String getUrlPrefix() {
        return urlPrefix;
    }


}

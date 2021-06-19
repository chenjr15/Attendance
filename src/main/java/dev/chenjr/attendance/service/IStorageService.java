package dev.chenjr.attendance.service;

import dev.chenjr.attendance.exception.HttpStatusException;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService extends IService {
    /**
     * 存储上传的文件
     *
     * @param uploaded 上传的文件
     * @return 存储的文件名
     */
    String storeFile(MultipartFile uploaded) throws HttpStatusException;

    /**
     * 返回访问静态资源的URL前缀
     *
     * @param storeName 文件存储的文件名
     * @return URL前缀
     */
    String getFullUrl(String storeName);

    /**
     * 返回完整的存储的路径
     *
     * @param storeName 文件存储的文件名
     * @return 完整的存储的路径
     */
    String getFullStoragePath(String storeName);

    /**
     * @return 存储的路径
     */
    String getStoragePath();

    /**
     * @return url前缀
     */
    String getUrlPrefix();
}

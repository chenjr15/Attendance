package dev.chenjr.attendance.service;

/**
 * 缓存支持服务，如Redis
 */
public interface ICacheService extends IService {

    void setValue(String key, String value, long expireSec);

    void setValue(String key, String value);

    String getValue(String key);

    void setHashValue(String setName, String key, String value);

    void setHashValue(String setName, String key, String value, long expireSec);

    String getHashValue(String setName, String key);
}

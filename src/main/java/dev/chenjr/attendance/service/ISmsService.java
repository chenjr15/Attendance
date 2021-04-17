package dev.chenjr.attendance.service;

/**
 * 短信服务接口
 */
public interface ISmsService extends IService {

    /**
     * 发送验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    boolean sendCode(String phone, String type) throws Exception;

    /**
     * 判断验证码是否有效，匹配、过期
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @param code  要匹配的验证码
     * @return 是否有效
     */
    boolean codeValid(String phone, String type, String code);
}

package dev.chenjr.attendance.service;

import dev.chenjr.attendance.exception.SmsException;

/**
 * 短信服务接口
 */
public interface ISmsService extends IService {
    String TYPE_LOGIN = "login";
    String TYPE_REGISTER = "register";
    String TYPE_RESET_PASSWORD = "reset_password";

    String[] ALL_TYPES = {TYPE_LOGIN, TYPE_REGISTER, TYPE_RESET_PASSWORD};

    /**
     * 发送验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    boolean sendCode(String phone, String type) throws SmsException;

    /**
     * 获取验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    String getSmsCodeTes(String phone, String type);

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

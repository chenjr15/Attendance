package dev.chenjr.attendance.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import dev.chenjr.attendance.exception.CodeMismatch;
import dev.chenjr.attendance.exception.SmsException;
import dev.chenjr.attendance.service.ICacheService;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SmsService implements ISmsService {
    @Autowired
    private Client smsClient;
    @Value("${aliyun.sms.templateCode}")
    private String templateCode;
    @Value("${aliyun.sms.signName}")
    private String signName;
    @Value("${aliyun.sms.expireTime}")
    long expireTime = 120;
    @Autowired
    private ICacheService cacheService;


    /**
     * 发送验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    @Override
    public boolean sendCode(String phone, String type) throws SmsException {
        String oldCode = getCode(phone, type);
        if (oldCode != null && !"".equals(oldCode)) {
            return true;
        }
        // 生成随机验证代码
        String smsCode = RandomUtil.randomNumberString(4);
        // 构造短信发送请求
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setTemplateCode(templateCode)
                .setSignName(signName)
                .setTemplateParam(String.format("{\"code\":\"%s\"}", smsCode))
                .setPhoneNumbers(phone);
        SendSmsResponse sendSmsResponse;
        String retCode;
        // 尝试发送请求
        try {
            sendSmsResponse = smsClient.sendSms(sendSmsRequest);
            log.info(sendSmsResponse.toString());
            String msg = sendSmsResponse.getBody().getMessage();
            log.info("SMS message" + msg);
            retCode = sendSmsResponse.getBody().getCode();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SmsException(e.getMessage());
        }
        // 存储随机验证码
        storeCode(phone, type, smsCode);
        return "OK".equals(retCode);
    }

    /**
     * 获取验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    @Override
    public String getSmsCodeTes(String phone, String type) {
        return this.getCode(phone, type);
    }

    private String getCode(String phone, String type) {

        String typeHashName = getKeyNameOfTypePhone(type, phone);
        return cacheService.getValue(typeHashName);
    }

    private String storeCode(String phone, String type, String code) {
        String typeHashName = getKeyNameOfTypePhone(type, phone);
        cacheService.setValue(typeHashName, code, expireTime);
        return code;
    }


    /**
     * 判断验证码是否有效，匹配、过期
     *
     * @param phone 手机号
     * @param type  验证码类型
     * @param code  要匹配的验证码
     * @return 是否有效
     */
    @Override
    public boolean codeValid(String phone, String type, String code) {
        return Objects.equals(getCode(phone, type), code);
    }

    @Override
    public void codeValidAndThrow(String phone, String type, String code) {
        if (!Objects.equals(getCode(phone, type), code)) {
            throw new CodeMismatch();
        }
    }

    @Bean
    public Client createClient(
            @Value("${aliyun.sms.accessKeyId}") String accessKeyId,
            @Value("${aliyun.sms.accessKeySecret}") String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    public String getKeyNameOfTypePhone(String type, String phone) {
        phone = phone.replaceAll("[+-]", "_");
        return String.format("SMS_CODE_%s_%s", type, phone);
    }

//    public String getTypeHashName(String type) {
//        return String.format("SMS_CODE_%s", type);
//    }
}

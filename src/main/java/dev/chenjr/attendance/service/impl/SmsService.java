package dev.chenjr.attendance.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import dev.chenjr.attendance.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class SmsService implements ISmsService {
    @Autowired
    private Client smsClient;
    @Value("aliyun.sms.templateCode")
    private String templateCode;
    @Value("aliyun.sms.signName")
    private String signName;

    private Map<String, Map<String, String>> codeTypeMap = new HashMap<>();

    /**
     * 发送验证码 指定手机号和类型
     *
     * @param phone 手机号
     * @param type  类型
     * @return 发送结果
     */
    @Override
    public boolean sendCode(String phone, String type) throws Exception {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setTemplateCode(templateCode)
                .setSignName(signName)
                .setPhoneNumbers(phone);
        SendSmsResponse sendSmsResponse = smsClient.sendSms(sendSmsRequest);
        String msg = sendSmsResponse.getBody().getMessage();
        log.info("SMS message" + msg);

        return "OK".equals(sendSmsResponse.getBody().getCode());
    }

    private String getCode(String phone, String type) {
        if (!codeTypeMap.containsKey(type)) {
            codeTypeMap.put(type, new HashMap<>());
        }
        Map<String, String> codeMap = codeTypeMap.get(type);
        if (!codeMap.containsKey(phone)) {
            return null;
        }
        return codeMap.get(phone);
    }

    private String storeCode(String phone, String type, String code) {
        if (!codeTypeMap.containsKey(type)) {
            codeTypeMap.put(type, new HashMap<>());
        }
        Map<String, String> codeMap = codeTypeMap.get(type);
        codeMap.put(phone, code);
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
}

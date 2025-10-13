package com.example.aop.aspect;

import com.example.aop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据脱敏切面
 * 演示：@AfterReturning 对返回数据进行脱敏处理
 *
 * @author demo
 */
@Aspect
@Component
@Slf4j
public class DataMaskingAspect {

    /**
     * 定义切入点：拦截Controller层返回用户信息的方法
     */
    @Pointcut("execution(* com.example.aop.controller.*.*(..)) && " +
              "(execution(* *..getUserById(..)) || execution(* *..getAllUsers(..)))")
    public void userDataMethods() {}

    /**
     * 返回通知：对用户敏感数据进行脱敏
     */
    @AfterReturning(pointcut = "userDataMethods()", returning = "result")
    public void maskSensitiveData(Object result) {
        if (result == null) {
            return;
        }

        log.info("🎭 [数据脱敏] 开始处理返回数据");

        // 处理单个用户对象
        if (result instanceof User) {
            maskUserData((User) result);
        }
        // 处理用户列表
        else if (result instanceof List) {
            List<?> list = (List<?>) result;
            for (Object item : list) {
                if (item instanceof User) {
                    maskUserData((User) item);
                }
            }
        }
        // 处理包装在ApiResponse中的数据
        else if (result.getClass().getSimpleName().equals("ApiResponse")) {
            try {
                // 使用反射获取data字段
                java.lang.reflect.Field dataField = result.getClass().getDeclaredField("data");
                dataField.setAccessible(true);
                Object data = dataField.get(result);

                if (data instanceof User) {
                    maskUserData((User) data);
                } else if (data instanceof List) {
                    List<?> list = (List<?>) data;
                    for (Object item : list) {
                        if (item instanceof User) {
                            maskUserData((User) item);
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("⚠️ [数据脱敏] 处理ApiResponse数据时出错: {}", e.getMessage());
            }
        }

        log.info("✅ [数据脱敏] 数据脱敏处理完成");
    }

    /**
     * 对用户数据进行脱敏处理
     */
    private void maskUserData(User user) {
        if (user == null) {
            return;
        }

        // 脱敏手机号：138****1234
        if (user.getPhone() != null) {
            String originalPhone = user.getPhone();
            user.setPhone(maskPhone(originalPhone));
            log.debug("📱 [数据脱敏] 手机号: {} -> {}", originalPhone, user.getPhone());
        }

        // 脱敏身份证：123456********1234
        if (user.getIdCard() != null) {
            String originalIdCard = user.getIdCard();
            user.setIdCard(maskIdCard(originalIdCard));
            log.debug("🆔 [数据脱敏] 身份证: {} -> {}", originalIdCard, user.getIdCard());
        }

        // 脱敏邮箱：zh***@example.com
        if (user.getEmail() != null) {
            String originalEmail = user.getEmail();
            user.setEmail(maskEmail(originalEmail));
            log.debug("📧 [数据脱敏] 邮箱: {} -> {}", originalEmail, user.getEmail());
        }
    }

    /**
     * 脱敏手机号
     * 规则：保留前3位和后4位，中间用****替换
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return phone;
        }

        if (phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(7);
        } else {
            // 其他长度的手机号，保留前后各2位
            return phone.substring(0, 2) + "****" + phone.substring(phone.length() - 2);
        }
    }

    /**
     * 脱敏身份证号
     * 规则：保留前6位和后4位，中间用********替换
     */
    private String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 10) {
            return idCard;
        }

        if (idCard.length() == 18) {
            return idCard.substring(0, 6) + "********" + idCard.substring(14);
        } else if (idCard.length() == 15) {
            return idCard.substring(0, 6) + "*****" + idCard.substring(11);
        } else {
            // 其他长度，保留前后各3位
            return idCard.substring(0, 3) + "****" + idCard.substring(idCard.length() - 3);
        }
    }

    /**
     * 脱敏邮箱
     * 规则：保留前2位和@后的域名，用户名中间用***替换
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }

        String username = parts[0];
        String domain = parts[1];

        if (username.length() <= 2) {
            return email; // 用户名太短，不脱敏
        } else if (username.length() <= 4) {
            return username.charAt(0) + "***@" + domain;
        } else {
            return username.substring(0, 2) + "***@" + domain;
        }
    }
}


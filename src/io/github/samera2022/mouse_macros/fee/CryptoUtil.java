package io.github.samera2022.mouse_macros.fee;

import io.github.samera2022.mouse_macros.Localizer;

import javax.swing.*;
import java.time.LocalDateTime;

public class CryptoUtil {

    protected static DateIntEncoderDecoder.DecodedResult verifyCode(String code) {
        // 验证格式
        if (!code.matches("^[A-Z0-9]{5}-[A-Z0-9]{5}-[A-Z0-9]{5}$")) {
            JOptionPane.showMessageDialog(null,
                    Localizer.get("verification.invalid_format"),
                    Localizer.get("error"),
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }

        try {
            // 解码验证码
            DateIntEncoderDecoder.DecodedResult result = DateIntEncoderDecoder.decode(code);
            // 验证日期
            LocalDateTime now = LocalDateTime.now();
            boolean reasonable = !result.dateTime.isBefore(LocalDateTime.parse("2025.07.10 22:28:00", FeeConsts.DATE_FORMATTER));

            // 移除权限相关提示，只保留核心验证逻辑
            if (result.dateTime.isBefore(now.minusDays(FeeConsts.ACTIVATION_VALID_DAYS))) {
                return null;
            } else if (result.dateTime.isAfter(now) || (!reasonable)) {
                return null;
            }

            // 验证数字范围
            if (result.number < 1 || result.number > 9) {
                return null;
            }

            return result;

        } catch (Exception ex) {
            return null;
        }
    }
}
package site.unimeet.unimeetbackend.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import site.unimeet.unimeetbackend.domain.auth.service.EmailVerificationService;
import site.unimeet.unimeetbackend.global.exception.ErrorCode;
import site.unimeet.unimeetbackend.global.exception.auth.AuthenticationException;

@RequiredArgsConstructor
@Service
public class EmailService {
    private final EmailVerificationService emailVerificationService;
    private final JavaMailSender javaMailSender;
    public void sendEmailVerificationCode(String email) {
        // 코드 생성 후 20분간 기억한다.
        String verificationCode  = emailVerificationService.generateCode(email);

        // 생성된 코드로 이메일을 전송한다.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증받아라");
        message.setText("너의 인증코드는 " + verificationCode);
        try {
            javaMailSender.send(message);
        } catch (MailException e){
            throw new AuthenticationException(ErrorCode.EMAIL_CANNOT_BE_SENT);
        }
    }

    public void sendEmail(String email, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(content);
        try {
            javaMailSender.send(message);
        } catch (MailException e){
            throw new AuthenticationException(ErrorCode.EMAIL_CANNOT_BE_SENT);
        }
    }
}


















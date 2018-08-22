package pl.netia.gateway.emailconfig;

public interface EmailSender {
    void sendEmail(String from, String to, String subject, String content);
}
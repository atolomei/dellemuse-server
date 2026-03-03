package dellemuse.serverapp.service;

import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    public void sendEmail(String to, String subject, String body) {
        if (to == null || to.trim().isEmpty()) {
            logger.warn("EmailService: empty recipient, skipping email");
            return;
        }

        // Simple fallback logger-based email send for environments without JavaMail
        logger.info("[EmailService] To: " + to + " Subject: " + subject + "\n" + body);
    }
}
package com.hansung.vinyl.identification.application;

import com.hansung.vinyl.account.domain.AccountCreatedEvent;
import com.hansung.vinyl.account.domain.AccountRepository;
import com.hansung.vinyl.account.domain.Email;
import com.hansung.vinyl.common.exception.data.DuplicateDataException;
import com.hansung.vinyl.common.exception.data.NoSuchDataException;
import com.hansung.vinyl.common.exception.validate.IllegalException;
import com.hansung.vinyl.common.service.EmailService;
import com.hansung.vinyl.common.service.EmailContentBuilder;
import com.hansung.vinyl.identification.domain.IdentificationToken;
import com.hansung.vinyl.identification.domain.IdentificationTokenRepository;
import com.hansung.vinyl.identification.dto.IdentificationRequest;
import com.hansung.vinyl.identification.dto.IdentificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class IdentificationService {
    private final IdentificationTokenRepository identificationTokenRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final EmailContentBuilder emailContentBuilder;
    private final MessageSource messageSource;

    @Value("${server.url}")
    private String serverUrl;

    public IdentificationResponse certify(IdentificationRequest identificationRequest) {
        validateDuplicateEmail(identificationRequest);
        IdentificationToken identificationToken = IdentificationToken.create(identificationRequest.getEmail());
        identificationTokenRepository.save(identificationToken);
        sendEmail(identificationToken);
        String responseMessage = messageSource.getMessage("identification.email.response", null, null);
        return IdentificationResponse.of(identificationToken, responseMessage);
    }

    public IdentificationResponse validate(String token) {
        IdentificationToken identificationToken = findIdentificationTokenById(token);
        IdentificationToken resultToken = identificationToken.validate();
        String responseMessage = createMessage(resultToken);
        return IdentificationResponse.of(resultToken, responseMessage);
    }

    @Transactional(readOnly = true)
    public IdentificationResponse result(String sEmail) {
        Email email = Email.of(sEmail);
        IdentificationToken resultToken = findIdentificationByEmail(email);
        String message = createMessage(resultToken);
        return IdentificationResponse.of(resultToken, message);
    }

    @EventListener
    public void delete(AccountCreatedEvent event) {
        IdentificationToken identificationToken;
        try {
            identificationToken = findIdentificationByEmail(event.getAccount().getEmail());
        } catch (NoSuchDataException exception) {
            return;
        }
        identificationTokenRepository.delete(identificationToken);
    }

    private void sendEmail(IdentificationToken identificationToken) {
        SimpleMailMessage simpleMailMessage = createSimpleMailMessage(identificationToken);
        emailService.send(simpleMailMessage);
    }

    private void validateDuplicateEmail(IdentificationRequest identificationRequest) {
        if (accountRepository.existsByEmail(Email.of(identificationRequest.getEmail()))) {
            throw new DuplicateDataException("email", identificationRequest.getEmail(), getClass().getName());
        }
    }

    private SimpleMailMessage createSimpleMailMessage(IdentificationToken identificationToken) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(identificationToken.getEmail().value());
        simpleMailMessage.setSubject(messageSource.getMessage("identification.email.subject", null, null));
        String message = emailContentBuilder.buildIdentification(
                serverUrl + "/v1/identifications/" + identificationToken.getId());
        simpleMailMessage.setText(message);
        return simpleMailMessage;
    }

    private IdentificationToken findIdentificationTokenById(String token) {
        return identificationTokenRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new IllegalException("token", token, getClass().getName()));
    }

    private String createMessage(IdentificationToken resultToken) {
        String responseMessage = messageSource.getMessage("identification.email.success", null, null);
        if (!resultToken.isApproved()) {
            responseMessage = messageSource.getMessage("identification.email.fail", null, null);
        }
        return responseMessage;
    }

    private IdentificationToken findIdentificationByEmail(Email email) {
        return identificationTokenRepository.findByEmail(com.hansung.vinyl.identification.domain.Email.of(email.value()))
                .orElseThrow(() -> new NoSuchDataException("email", email, getClass().getName()));
    }

    public UUID token(String sEmail) {
        Email email = Email.of(sEmail);
        IdentificationToken resultToken = findIdentificationByEmail(email);
        return resultToken.getId();
    }
}

package com.java.admin.unit.util;

import com.java.admin.util.SendMailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendMailUtilTest {

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    private SendMailUtil sendMailUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_SUBJECT = "Test Subject";
    private static final String TEST_TEMPLATE = "test-template";
    private static final String TEST_HTML_CONTENT = "<html><body>Test Content</body></html>";

    @BeforeEach
    void setUp() {
        sendMailUtil = new SendMailUtil(emailSender, templateEngine);
    }

    @Test
    @DisplayName("Should send HTML email successfully when valid parameters provided")
    void sendHtmlMessage_shouldSendEmailSuccessfully_whenValidParameters() throws MessagingException {
        // Arrange
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", "John Doe");
        variables.put("token", "abc123");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(TEST_TEMPLATE), any(Context.class))).thenReturn(TEST_HTML_CONTENT);

        // Act
        assertDoesNotThrow(() -> sendMailUtil.sendHtmlMessage(TEST_EMAIL, TEST_SUBJECT, TEST_TEMPLATE, variables));

        // Assert
        verify(emailSender).createMimeMessage();
        verify(templateEngine).process(eq(TEST_TEMPLATE), any(Context.class));
        verify(emailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("Should process template with correct context variables")
    void sendHtmlMessage_shouldProcessTemplateWithCorrectContext_whenVariablesProvided() throws MessagingException {
        // Arrange
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", "testUser");
        variables.put("verificationCode", "123456");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(TEST_TEMPLATE), any(Context.class))).thenReturn(TEST_HTML_CONTENT);

        // Act
        sendMailUtil.sendHtmlMessage(TEST_EMAIL, TEST_SUBJECT, TEST_TEMPLATE, variables);

        // Assert
        verify(templateEngine).process(eq(TEST_TEMPLATE), argThat(context -> {
            Context ctx = (Context) context;
            return ctx.getVariableNames().contains("userName") &&
                    ctx.getVariableNames().contains("verificationCode");
        }));
    }

    @Test
    @DisplayName("Should handle empty variables map")
    void sendHtmlMessage_shouldHandleEmptyVariables_whenNoVariablesProvided() throws MessagingException {
        // Arrange
        Map<String, Object> emptyVariables = new HashMap<>();

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(TEST_TEMPLATE), any(Context.class))).thenReturn(TEST_HTML_CONTENT);

        // Act
        assertDoesNotThrow(() -> sendMailUtil.sendHtmlMessage(TEST_EMAIL, TEST_SUBJECT, TEST_TEMPLATE, emptyVariables));

        // Assert
        verify(templateEngine).process(eq(TEST_TEMPLATE), any(Context.class));
        verify(emailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("Should throw RuntimeException when MessagingException occurs")
    void sendHtmlMessage_shouldThrowRuntimeException_whenMessagingExceptionOccurs() throws MessagingException {
        // Arrange
        Map<String, Object> variables = Map.of("key", "value");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq(TEST_TEMPLATE), any(Context.class))).thenReturn(TEST_HTML_CONTENT);

        // Act & Assert
        try (MockedConstruction<MimeMessageHelper> mockedHelper = mockConstruction(MimeMessageHelper.class,
                (mock, context) -> {
                    doThrow(new MessagingException("Error setting recipient")).when(mock).setTo(anyString());
                })) {

            RuntimeException exception = assertThrows(RuntimeException.class, () ->
                    sendMailUtil.sendHtmlMessage(TEST_EMAIL, TEST_SUBJECT, TEST_TEMPLATE, variables)
            );

            assertEquals("Error sending HTML email", exception.getMessage());
            assertInstanceOf(MessagingException.class, exception.getCause());
        }

        verify(emailSender).createMimeMessage();
    }

    @Test
    @DisplayName("Should call emailSender.send exactly once")
    void sendHtmlMessage_shouldCallSendOnce_whenSuccessful() throws MessagingException {
        // Arrange
        Map<String, Object> variables = Map.of("key", "value");

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any(Context.class))).thenReturn(TEST_HTML_CONTENT);

        // Act
        sendMailUtil.sendHtmlMessage(TEST_EMAIL, TEST_SUBJECT, TEST_TEMPLATE, variables);

        // Assert
        verify(emailSender, times(1)).send(mimeMessage);
    }
}
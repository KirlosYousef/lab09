package messaging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MessagingAppTest {

    private static final String MESSAGE = "Hello, world!";

    private static final String ENCRYPTED_MESSAGE = "fiowjefoijiojwfl";

    private static final String TO = "Alice";

    private static final String FROM = "Bob";

    private static final String ATTACKER = "Hacker";

    private MessagingApp underTest;

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {

        encryptionService = Mockito.mock(EncryptionService.class);
        underTest = new MessagingApp(encryptionService);

    }

    @Test
    void testSendMessageShouldEncryptTextWhenSendingToAuthenticatedUser() {
        // Given
        Mockito.when(encryptionService.auth(TO)).thenReturn(true);
        Mockito.when(encryptionService.encrypt(MESSAGE)).thenReturn(ENCRYPTED_MESSAGE);
        // When
        underTest.sendMessage(TO, MESSAGE);
        // Then
        Mockito.verify(encryptionService).auth(TO);
        Mockito.verify(encryptionService).encrypt(MESSAGE);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void testReceiveMessageShouldDecryptTextWhenReceivingFromAuthenticatedUser() {
        // Given
        Mockito.when(encryptionService.auth(FROM)).thenReturn(true);
        Mockito.when(encryptionService.decrypt(MESSAGE)).thenReturn(MESSAGE);
        // When
        underTest.receiveMessage(FROM, MESSAGE);
        // Then
        Mockito.verify(encryptionService).auth(FROM);
        Mockito.verify(encryptionService).decrypt(MESSAGE);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void testSendMessageShouldThrowIllegalArgumentExceptionWhenSendingToUnAuthenticatedUser(){
        // Given
        Mockito.when(encryptionService.auth(ATTACKER)).thenReturn(false);
        Mockito.when(encryptionService.encrypt(MESSAGE)).thenReturn(ENCRYPTED_MESSAGE);
        // When
        try {
            underTest.sendMessage(ATTACKER, MESSAGE);
        } catch (IllegalArgumentException e){
            assertEquals(ATTACKER, e.getMessage());
        } catch (Exception e){
            fail();
        }
        // Then
        Mockito.verify(encryptionService).auth(ATTACKER);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }

    @Test
    void testReceiveMessageShouldThrowIllegalArgumentExceptionWhenReceivingFromUnAuthenticatedUser(){
        // Given
        Mockito.when(encryptionService.auth(ATTACKER)).thenReturn(false);
        Mockito.when(encryptionService.decrypt(MESSAGE)).thenReturn(MESSAGE);
        // When
        try {
            underTest.receiveMessage(ATTACKER, ENCRYPTED_MESSAGE);
        } catch (IllegalArgumentException e){
            assertEquals(ATTACKER, e.getMessage());
        } catch (Exception e){
            fail();
        }
        // Then
        Mockito.verify(encryptionService).auth(ATTACKER);
        Mockito.verifyNoMoreInteractions(encryptionService);
    }
}
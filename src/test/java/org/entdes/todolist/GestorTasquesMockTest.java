package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.entdes.mail.IEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GestorTasquesMockTest {

    // Mock del servei d'enviament de correu amb Mockito
    @Mock
    private IEmailService emailService;

    private GestorTasques gestor;
    // Recipient definit per a la prova (normalment provindrà del fitxer de configuració)
    private final String recipient = "test@example.com";

    @BeforeEach
    void setUp() {
        // Utilitzem el constructor amb el servei de correu per injectar el mock
        gestor = new GestorTasques(emailService, recipient);
    }

    @Test
    void testAfegirTascaEnviaEmail() throws Exception {
        // Arrange: dades vàlides per afegir una nova tasca
        String desc = "Test tasca email";
        LocalDate dataInici = LocalDate.now().plusDays(1);

        // Act: afegim la tasca
        gestor.afegirTasca(desc, dataInici, null, 2);

        // Assert: verifiquem que el mètode enviarCorreu s'ha cridat amb els paràmetres esperats
        verify(emailService, times(1))
            .enviarCorreu(recipient, "Nova Tasca Creada", "Has creat la tasca: " + desc);
    }

    @Test
    void testAfegirTascaAmbDescripcioBuidaNoEnviaEmail() throws Exception {
        // Act & Assert: comprovem que afegir una tasca amb descripció buida llança una excepció...
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("   ", null, null, null);
        });

        // ...i que no es fa cap crida al servei d'enviament de correu
        verify(emailService, times(0))
                .enviarCorreu(anyString(), anyString(), anyString());
    }
    
    // Test amb stub: implementació manual d'IEmailService per simular l'enviament real de correu.
    @Test
    void testAfegirTascaEnviaEmailAmbStub() throws Exception {
        // Creem un stub del servei de correu per a la prova
        EmailServiceStub stubService = new EmailServiceStub();
        GestorTasques gestorStub = new GestorTasques(stubService, recipient);

        // Dades de la tasca
        String desc = "Tasca amb stub";
        LocalDate dataInici = LocalDate.now().plusDays(1);
        
        // Afegim la tasca amb el stub
        gestorStub.afegirTasca(desc, dataInici, null, 3);

        // Verifiquem que el stub ha estat cridat
        assert stubService.enviarCalled;
        // Verifiquem que els paràmetres passats són correctes
        assert stubService.destinatari.equals(recipient);
        assert stubService.subject.equals("Nova Tasca Creada");
        assert stubService.message.contains(desc);
    }

    // Implementació stub d'IEmailService
    private static class EmailServiceStub implements IEmailService {
        boolean enviarCalled = false;
        String destinatari;
        String subject;
        String message;
        
        @Override
        public void enviarCorreu(String destinatari, String subject, String message) {
            this.enviarCalled = true;
            this.destinatari = destinatari;
            this.subject = subject;
            this.message = message;
        }
    }
}
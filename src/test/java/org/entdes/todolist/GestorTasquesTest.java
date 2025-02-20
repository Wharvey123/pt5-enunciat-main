package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GestorTasquesTest {
    
    private GestorTasques gestor;

    @BeforeEach
    void setUp() {
        // Inicialitza el GestorTasques abans de cada test
        gestor = new GestorTasques();
    }

    @Test
    void testAfegirTascaAmbDadesValides() throws Exception {
        // Prova d'afegir una tasca amb dades vàlides
        String desc = "Tasca 1";
        LocalDate dataInici = LocalDate.now().plusDays(1);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(2);
        int id = gestor.afegirTasca(desc, dataInici, dataFiPrevista, 3);
        assertTrue(id > 0, "L'id de la tasca ha de ser major que 0");
        Tasca tasca = gestor.obtenirTasca(id);
        assertEquals(desc, tasca.getDescripcio(), "La descripció ha de coincidir");
        assertEquals(dataInici, tasca.getDataInici(), "La data d'inici ha de coincidir");
        assertEquals(dataFiPrevista, tasca.getDataFiPrevista(), "La data fi prevista ha de coincidir");
        assertEquals(3, tasca.getPrioritat(), "La prioritat ha de coincidir");
    }
    
    @Test
    void testAfegirTascaAmbDescripcioBuida() {
        // Prova d'afegir una tasca amb descripció buida
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("   ", null, null, null);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }
    
    @Test
    void testAfegirTascaAmbDescripcioNull() {
        // Prova d'afegir una tasca amb descripció nul·la
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(null, LocalDate.now().plusDays(1), null, null);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }
    
    @Test
    void testAfegirTascaAmbTascaDuplicada() throws Exception {
        // Prova d'afegir una tasca duplicada
        String desc = "Tasca Duplicada";
        gestor.afegirTasca(desc, null, null, null);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("tasca duplicada", null, null, null);
        });
        assertEquals("Ja existeix una tasca amb la mateixa descripció", exception.getMessage());
    }
    
    @Test
    void testAfegirTascaAmbDataIniciPosteriorADataFiPrevista() {
        // Prova d'afegir una tasca amb data d'inici posterior a la data fi prevista
        String desc = "Tasca amb dates incorrectes";
        LocalDate dataInici = LocalDate.now().plusDays(5);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(desc, dataInici, dataFiPrevista, 2);
        });
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }
    
    @Test
    void testAfegirTascaAmbDataIniciAnteriorActual() {
        // Prova d'afegir una tasca amb data d'inici anterior a la data actual
        String desc = "Tasca amb data passada";
        LocalDate dataInici = LocalDate.now().minusDays(1);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca(desc, dataInici, null, null);
        });
        assertEquals("La data d'inici no pot ser anterior a la data actual.", exception.getMessage());
    }
    
    @Test
    void testEliminarTasca() throws Exception {
        // Prova d'eliminar una tasca existent
        int id = gestor.afegirTasca("Tasca a eliminar", LocalDate.now().plusDays(1), null, null);
        gestor.eliminarTasca(id);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.obtenirTasca(id);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }
    
    @Test
    void testEliminarTascaMultiples() throws Exception {
        // Prova d'eliminar una tasca en presència de múltiples tasques
        int id1 = gestor.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), null, 1);
        int id2 = gestor.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), null, 1);
        gestor.eliminarTasca(id1);
        List<Tasca> tasques = gestor.llistarTasques();
        assertEquals(1, tasques.size(), "Només hauria de quedar una tasca");
        assertEquals(id2, tasques.get(0).getId(), "La tasca restant ha de tenir l'id correcte");
    }
    
    @Test
    void testMarcarCompletada() throws Exception {
        // Prova de marcar una tasca com a completada
        int id = gestor.afegirTasca("Tasca per completar", LocalDate.now().plusDays(1), null, null);
        gestor.marcarCompletada(id);
        Tasca tasca = gestor.obtenirTasca(id);
        assertTrue(tasca.isCompletada(), "La tasca ha d'estar marcada com completada");
    }
    
    @Test
    void testMarcarCompletadaTascaInexistent() {
        // Prova de marcar com a completada una tasca inexistent
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.marcarCompletada(999);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }
    
    // Test per a modificar una tasca existent
    @Test
    void testModificarTasca() throws Exception {
        int id = gestor.afegirTasca("Tasca per modificar", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), 2);
        // Marquem la tasca com a completada per assignar una data fi real
        gestor.marcarCompletada(id);
        // Ara modifiquem la tasca: canviem la descripció, desmarquem la tasca (completada false) i canviem la prioritat
        gestor.modificarTasca(id, "Tasca modificada", false, LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), 4);
        Tasca tascaModificada = gestor.obtenirTasca(id);
        assertEquals("Tasca modificada", tascaModificada.getDescripcio(), "La descripció ha de ser actualitzada");
        assertFalse(tascaModificada.isCompletada(), "La tasca ha de quedar desmarcada");
        // Si es desmarca, la data fi real s'ha de netejar
        assertNull(tascaModificada.getDataFiReal(), "La data fi real ha de netejar-se en desmarcar la tasca");
        assertEquals(4, tascaModificada.getPrioritat(), "La prioritat ha de ser actualitzada");
    }

    // Test per a modificar una tasca amb descripció buida
    @Test
    void testModificarTascaAmbDescripcioBuida() throws Exception {
        int id = gestor.afegirTasca("Tasca original", LocalDate.now().plusDays(1), null, null);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "  ", true, LocalDate.now().plusDays(1), null, 3);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }

    // Test per a modificar una tasca amb descripció nul·la
    @Test
    void testModificarTascaAmbNovaDescripcioNull() throws Exception {
        int id = gestor.afegirTasca("Tasca original", LocalDate.now().plusDays(1), null, null);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, null, true, LocalDate.now().plusDays(1), null, 3);
        });
        assertEquals("La descripció no pot estar buida.", exception.getMessage());
    }

    // Test per a modificar una tasca amb dates incorrectes
    @Test
    void testModificarTascaAmbDatesIncorrectes() throws Exception {
        int id = gestor.afegirTasca("Tasca amb dates", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3), null);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Tasca amb dates", true, LocalDate.now().plusDays(5), LocalDate.now().plusDays(3), null);
        });
        assertEquals("La data d'inici no pot ser posterior a la data fi prevista.", exception.getMessage());
    }

    // Test per a modificar una tasca amb prioritat incorrecta
    @Test
    void testModificarTascaAmbPrioritatIncorrecta() throws Exception {
        int id = gestor.afegirTasca("Tasca amb prioritat", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 3);
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Tasca amb prioritat", true, LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), 6);
        });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    // Test per a modificar una tasca sense netejar la data fi real quan la tasca no estava completada prèviament
    @Test
    void testModificarTascaSenseNetejarDataFiReal() throws Exception {
        // Afegim una tasca que inicialment no està completada
        int id = gestor.afegirTasca("Tasca no completada", LocalDate.now().plusDays(1), null, 3);
        // Modifiquem la tasca passant completada com a true
        gestor.modificarTasca(id, "Tasca actualitzada", true, LocalDate.now().plusDays(1), null, 3);
        Tasca tasca = gestor.obtenirTasca(id);
        assertTrue(tasca.isCompletada(), "La tasca ha de quedar marcada com completada");
        // Com que la tasca no estava completada anteriorment, no s'hauria de netejar la data fi real
        assertNull(tasca.getDataFiReal(), "La data fi real s'ha mantingut sense definir");
    }

    // Test per a obtenir una tasca existent
    @Test
    void testObtenirTasca() throws Exception {
        int id = gestor.afegirTasca("Tasca per obtenir", LocalDate.now().plusDays(1), null, null);
        Tasca tasca = gestor.obtenirTasca(id);
        assertNotNull(tasca, "La tasca obtinguda no hauria de ser null");
        assertEquals("Tasca per obtenir", tasca.getDescripcio(), "La descripció ha de coincidir");
    }

    // Test per a obtenir una tasca inexistent
    @Test
    void testObtenirTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.obtenirTasca(999);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    // Test per a obtenir el nombre de tasques
    @Test
    void testGetNombreTasques() throws Exception {
        int countInicial = gestor.getNombreTasques();
        gestor.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), null, null);
        gestor.afegirTasca("Tasca 2", LocalDate.now().plusDays(2), null, null);
        assertEquals(countInicial + 2, gestor.getNombreTasques(), "El nombre de tasques ha d'incrementar correctament");
    }

    // Test per a llistar totes les tasques
    @Test
    void testLlistarTasques() throws Exception {
        gestor.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), null, null);
        gestor.afegirTasca("Tasca 2", LocalDate.now().plusDays(2), null, null);
        List<Tasca> tasques = gestor.llistarTasques();
        assertEquals(2, tasques.size(), "El llistat de tasques ha de contenir totes les tasques afegides");
    }

    // Test per a llistar tasques per descripció
    @Test
    void testLlistarTasquesPerDescripcio() throws Exception {
        gestor.afegirTasca("Compra llet", LocalDate.now().plusDays(1), null, null);
        gestor.afegirTasca("Comprar pa", LocalDate.now().plusDays(1), null, null);
        gestor.afegirTasca("Llegir un llibre", LocalDate.now().plusDays(1), null, null);
        List<Tasca> filtrar = gestor.llistarTasquesPerDescripcio("compra");
        assertEquals(2, filtrar.size(), "El filtre per descripció ha de retornar 2 tasques");
    }

    // Test per a llistar tasques per compleció
    @Test
    void testLlistarTasquesPerComplecio() throws Exception {
        gestor.afegirTasca("Tasca incompleta", LocalDate.now().plusDays(1), null, null);
        int id2 = gestor.afegirTasca("Tasca completa", LocalDate.now().plusDays(1), null, null);
        gestor.marcarCompletada(id2);
        List<Tasca> completes = gestor.llistarTasquesPerComplecio(true);
        assertEquals(1, completes.size(), "Només s'haurien de retornar les tasques completades");
        assertTrue(completes.get(0).isCompletada(), "La tasca retornada ha d'estar marcada com completada");
    }

    // Test per a modificar una tasca inexistent
    @Test
    void testModificarTascaInexistent() {
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(999, "Nou nom", true, LocalDate.now().plusDays(1), null, null);
        });
        assertEquals("La tasca no existeix", exception.getMessage());
    }
}
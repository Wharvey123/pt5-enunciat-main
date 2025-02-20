package org.entdes.todolist;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class TascaTest {

    @Test
    void getId() {
        // Crea una nova tasca i comprova que l'identificador és major que 0.
        Tasca tasca = new Tasca("Test");
        assertTrue(tasca.getId() > 0, "El ID de la tasca ha de ser major que 0");
    }

    @Test
    void isCompletada() {
        // Verifica que una nova tasca no estigui marcada com completada per defecte.
        Tasca tasca = new Tasca("Test");
        assertFalse(tasca.isCompletada(), "La tasca ha d'estar pendent per defecte");
    }

    @Test
    void setCompletada() {
        // Marca la tasca com a completada i comprova que el canvi s'ha aplicat.
        Tasca tasca = new Tasca("Test");
        tasca.setCompletada(true);
        assertTrue(tasca.isCompletada(), "La tasca ha d'estar marcada com completada");
    }

    @Test
    void getDescripcio() {
        // Comprova que la descripció retornada és la mateixa que la proporcionada al constructor.
        String desc = "Tasca de prova";
        Tasca tasca = new Tasca(desc);
        assertEquals(desc, tasca.getDescripcio(), "La descripció ha de coincidir amb la proporcionada");
    }

    @Test
    void setDescripcio() {
        // Canvia la descripció i comprova que el mètode getDescripcio retorna la nova descripció.
        Tasca tasca = new Tasca("Original");
        String novaDesc = "Modificada";
        tasca.setDescripcio(novaDesc);
        assertEquals(novaDesc, tasca.getDescripcio(), "La nova descripció ha de ser actualitzada");
    }

    @Test
    void getDataInici() {
        // Comprova que inicialment la data d'inici és null, i després la defineix i la verifica.
        Tasca tasca = new Tasca("Test");
        assertNull(tasca.getDataInici(), "La data d'inici ha de ser null per defecte");
        LocalDate data = LocalDate.now().plusDays(1);
        tasca.setDataInici(data);
        assertEquals(data, tasca.getDataInici(), "La data d'inici ha de ser la assignada");
    }

    @Test
    void setDataInici() {
        // Assigna una data d'inici i comprova que s'ha actualitzat correctament.
        Tasca tasca = new Tasca("Test");
        LocalDate data = LocalDate.now().plusDays(2);
        tasca.setDataInici(data);
        assertEquals(data, tasca.getDataInici(), "La data d'inici ha de ser la assignada");
    }

    @Test
    void getDataFiPrevista() {
        // Comprova que inicialment la data fi prevista és null, després la defineix i la verifica.
        Tasca tasca = new Tasca("Test");
        assertNull(tasca.getDataFiPrevista(), "La data fi prevista ha de ser null per defecte");
        LocalDate data = LocalDate.now().plusDays(3);
        tasca.setDataFiPrevista(data);
        assertEquals(data, tasca.getDataFiPrevista(), "La data fi prevista ha de ser la assignada");
    }

    @Test
    void setDataFiPrevista() {
        // Assigna una data fi prevista i verifica que s'actualitzi.
        Tasca tasca = new Tasca("Test");
        LocalDate data = LocalDate.now().plusDays(4);
        tasca.setDataFiPrevista(data);
        assertEquals(data, tasca.getDataFiPrevista(), "La data fi prevista ha de ser la assignada");
    }

    @Test
    void getPrioritat() {
        // Comprova que inicialment la prioritat és null, la defineix i verifica el seu valor.
        Tasca tasca = new Tasca("Test");
        assertNull(tasca.getPrioritat(), "La prioritat ha de ser null per defecte");
        tasca.setPrioritat(3);
        assertEquals(3, tasca.getPrioritat(), "La prioritat ha de ser el valor assignat");
    }

    @Test
    void setPrioritat() {
        // Canvia la prioritat i comprova que s'actualitzi correctament.
        Tasca tasca = new Tasca("Test");
        tasca.setPrioritat(4);
        assertEquals(4, tasca.getPrioritat(), "La prioritat ha de ser el valor assignat");
    }

    @Test
    void getDataFiReal() {
        // Verifica que inicialment la data fi real és null, la defineix i comprova el seu valor.
        Tasca tasca = new Tasca("Test");
        assertNull(tasca.getDataFiReal(), "La data fi real ha de ser null per defecte");
        LocalDate data = LocalDate.now();
        tasca.setDataFiReal(data);
        assertEquals(data, tasca.getDataFiReal(), "La data fi real ha de ser la assignada");
    }

    @Test
    void setDataFiReal() {
        // Assigna una data fi real i comprova que el mètode getDataFiReal la retorni correctament.
        Tasca tasca = new Tasca("Test");
        LocalDate data = LocalDate.now();
        tasca.setDataFiReal(data);
        assertEquals(data, tasca.getDataFiReal(), "La data fi real ha de ser el valor assignat");
    }

    @Test
    void testToString() {
        // Comprova que el mètode toString retorni el format correcte segons si la tasca està completada o pendent.
        String desc = "Tasca test";
        Tasca tasca = new Tasca(desc);
        // Per defecte, no està completada.
        String expected = desc + ": " + "Pendent";
        assertEquals(expected, tasca.toString(), "El toString ha de mostrar 'Pendent' si no està completada");
        // Marcar la tasca com a completada i comprovar el format.
        tasca.setCompletada(true);
        expected = desc + ": " + "Completada";
        assertEquals(expected, tasca.toString(), "El toString ha de mostrar 'Completada' si està completada");
    }
}

package ba.unsa.etf.rpr.zadaca2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BibliotekaModelTest {

    @Test
    void getTrenutnaKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        assertNull(m.getTrenutnaKnjiga());
    }

    @Test
    void dajKnjige() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        assertTrue(m.dajKnjige().contains("Meša Selimović, Tvrđava"));
        assertTrue(m.dajKnjige().contains("Ivo Andrić, Travnička hronika"));
        assertTrue(m.dajKnjige().contains("J. K. Rowling, Harry Potter"));
    }

    @Test
    void setTrenutnaKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        Knjiga k = m.getKnjige().get(0);
        m.setTrenutnaKnjiga(k);
        assertEquals(k, m.getTrenutnaKnjiga());
    }

    @Test
    void deleteKnjiga() {
        BibliotekaModel m = new BibliotekaModel();
        m.napuni();
        Knjiga k = m.getKnjige().get(0);
        m.setTrenutnaKnjiga(k);
        m.deleteKnjiga();
        String expected = "Meša Selimović";
        assertFalse(m.dajKnjige().contains(expected));
    }

    @Test
    void addKnjiga1() {
        BibliotekaModel m = new BibliotekaModel();
        m.addKnjiga(new Knjiga("a", "a", "a", 1));
        m.addKnjiga(new Knjiga("b", "b", "b", 1));
        m.addKnjiga(new Knjiga("c", "c", "c", 1));
        assertTrue(m.dajKnjige().contains("a, a"));
        assertTrue(m.dajKnjige().contains("b, b"));
        assertTrue(m.dajKnjige().contains("c, c"));
    }
}
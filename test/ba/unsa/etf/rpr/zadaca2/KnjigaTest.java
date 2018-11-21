package ba.unsa.etf.rpr.zadaca2;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class KnjigaTest {
    @Test
    void getIsbn() {
        Knjiga k = new Knjiga("a","b","c",1);
        assertEquals("c", k.getIsbn());
    }

    @Test
    void getDatumIzdanja() {
        Knjiga k = new Knjiga("a","b","c",1);
        assertEquals(LocalDate.now(), k.getDatumIzdanja());
    }

    @Test
    void setDatumIzdanja() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        assertEquals(LocalDate.of(2018, 11, 17), k.getDatumIzdanja());
    }

    @Test
    void toStringTest() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        String result = "" + k;
        String expected = "a, b, c, 1, 17. 11. 2018";
        assertEquals(expected, result);
    }

    @Test
    void toStringTest1() {
        Knjiga k = new Knjiga("a","b","c",1);
        k.setDatumIzdanja(LocalDate.of(2018, 11, 17));
        String result = "" + k;
        String content = "a, b, c";
        assertTrue(result.contains(content));
    }
}
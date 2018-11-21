package ba.unsa.etf.rpr.zadaca2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Knjiga {
    private SimpleStringProperty autor = new SimpleStringProperty("");
    private SimpleStringProperty naslov = new SimpleStringProperty("");
    private SimpleStringProperty isbn = new SimpleStringProperty("");
    private SimpleIntegerProperty brojStranica = new SimpleIntegerProperty(0);

    public Knjiga() {}

    public Knjiga(String a, String n, String i, int b) {
        autor = new SimpleStringProperty(a);
        naslov = new SimpleStringProperty(n);
        isbn = new SimpleStringProperty(i);
        brojStranica = new SimpleIntegerProperty(b);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }


    public String getNaslov() {
        return naslov.get();
    }

    public SimpleStringProperty naslovProperty() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov.set(naslov);
    }


    public String getAutor() {
        return autor.get();
    }

    public SimpleStringProperty autorProperty() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor.set(autor);
    }

    public int getBrojStranica() {
        return brojStranica.get();
    }

    public SimpleIntegerProperty brojStranicaProperty() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica.set(brojStranica);
    }

    @Override
    public String toString() {
        return autor.get() + ", " + naslov.get();
    }

}

package ba.unsa.etf.rpr.zadaca2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class BibliotekaController {

    private BibliotekaModel model;

    public ChoiceBox<Knjiga> izborKnjige;
    public TextField knjigaAutor;
    public TextField knjigaNaslov;
    public TextField knjigaIsbn;
    //public ListView listaKnjiga;

    public BibliotekaController(BibliotekaModel m) {
        model = m;
    }

    @FXML
    public void initialize() {
        izborKnjige.setItems(model.getKnjige());
        //knjigaAutor.textProperty().bindBidirectional(new SimpleStringProperty(model.getTrenutnaKnjiga().getAutor()));

        model.trenutnaKnjigaProperty().addListener((obs, oldKnjiga, newKnjiga) -> {
            System.out.print("Mijenjam data binding");
            if (oldKnjiga != null) {
                System.out.print(" sa "+oldKnjiga);
                knjigaAutor.textProperty().unbindBidirectional(oldKnjiga.autorProperty());
                knjigaNaslov.textProperty().unbindBidirectional(oldKnjiga.naslovProperty());
                knjigaIsbn.textProperty().unbindBidirectional(oldKnjiga.isbnProperty());
            }
            if (newKnjiga == null) {
                System.out.println(" na ništa");
                knjigaAutor.setText("");
                knjigaNaslov.setText("");
                knjigaIsbn.setText("");
            }
            else {
                System.out.println(" na " + newKnjiga);
                knjigaAutor.textProperty().bindBidirectional(newKnjiga.autorProperty());
                knjigaNaslov.textProperty().bindBidirectional(newKnjiga.naslovProperty());
                knjigaIsbn.textProperty().bindBidirectional(newKnjiga.isbnProperty());
            }
        });
    }

    public void izabranaKnjiga(ActionEvent actionEvent) {
        System.out.println("Promijenjena je trenutna knjiga na: " + izborKnjige.getValue());
        model.setTrenutnaKnjiga(izborKnjige.getValue());
    }

    public void ispisiKnjige(ActionEvent actionEvent) {
        model.ispisiKnjige();
    }
}

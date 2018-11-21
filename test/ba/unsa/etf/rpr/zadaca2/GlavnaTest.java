package ba.unsa.etf.rpr.zadaca2;

import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
class GlavnaTest {
    Stage theStage;
    BibliotekaModel model;

    @Start
    public void start (Stage stage) throws Exception {
        model = new BibliotekaModel();
        model.napuni();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("glavna.fxml"));
        loader.setController(new GlavnaController(model));
        Parent root = loader.load();
        stage.setTitle("Biblioteka");
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
        stage.toFront();

        theStage = stage;
    }

    @Test
    public void testStatusMsg (FxRobot robot) {
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertNotNull(statusMsg);
        assertEquals("Program pokrenut.", statusMsg);
    }

    @Test
    public void testTableViewColumns (FxRobot robot) {
        TableView tableView = robot.lookup("#tabelaKnjiga").queryAs(TableView.class);
        assertNotNull(tableView);
        ObservableList<TableColumn> columns = tableView.getColumns();
        assertEquals("Naslov", columns.get(1).getText());
        assertEquals("Autor", columns.get(0).getText());
        assertEquals("Datum izdanja", columns.get(2).getText());
    }

    @Test
    public void testOpen (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");
        robot.press(KeyCode.ALT).press(KeyCode.F).release(KeyCode.F).press(KeyCode.O).release(KeyCode.O).release(KeyCode.ALT);

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        assertEquals("Nije implementirano", dialogPane.getHeaderText());

        // Zatvaramo dijalog zbog ostalih testova
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);
    }

    @Test
    public void testCancelDelete (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Selektujemo Ivu Andrića
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        robot.clickOn("#tabelaKnjiga");

        // Edit > Delete
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.D).release(KeyCode.D).release(KeyCode.ALT);

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();

        // Klik na dugme Cancel
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button cancelButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        robot.clickOn(cancelButton);

        // Da li je knjiga obrisana?
        String expected = "Ivo Andrić";
        assertTrue(model.dajKnjige().contains(expected));

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga nije obrisana.", statusMsg);
    }

    @Test
    public void testDelete (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Selektujemo Ivu Andrića
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        robot.clickOn("#tabelaKnjiga");

        // Edit > Delete
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.D).release(KeyCode.D).release(KeyCode.ALT);

        // Čekamo da dijalog postane vidljiv
        robot.lookup(".dialog-pane").tryQuery().isPresent();
        //DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);

        // Zatvaramo dijalog
        DialogPane dialogPane = robot.lookup(".dialog-pane").queryAs(DialogPane.class);
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        robot.clickOn(okButton);

        // Da li je knjiga obrisana?
        String expected = "Ivo Andrić";
        assertFalse(model.dajKnjige().contains(expected));

        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga obrisana.", statusMsg);
    }

    @Test
    public void testAdd (FxRobot robot) {
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        robot.clickOn("#knjigaAutor");
        robot.write("Testni autor");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("Testni naslov");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("1234");

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        // Da li je knjiga dodana?
        String expected = "Testni autor, Testni naslov";

        assertTrue(model.dajKnjige().contains(expected));
    }

    @Test
    public void testAddSpinner (FxRobot robot) {
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        robot.clickOn("#knjigaAutor");
        robot.write("Testni autor");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("Testni naslov");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("1234");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.press(KeyCode.UP).release(KeyCode.UP); // Biramo 1 strelicom gore

        // Koju vrijednost ima spinner?
        Spinner kbs = robot.lookup("#knjigaBrojStranica").queryAs(Spinner.class);
        assertNotNull(kbs);
        Integer i = (Integer)kbs.getValueFactory().getValue();

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        // Spinner treba imati vrijednost 1
        assertEquals(new Integer(1), i);

        // Da li je knjiga dodana?
        String expected = "Testni autor, Testni naslov, 1234, 1";
        assertTrue(model.dajKnjige().contains(expected));
    }

    @Test
    public void testAddDateFormat (FxRobot robot) {
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        robot.clickOn("#knjigaAutor");
        robot.write("Testni autor");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("Testni naslov");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.write("1234");
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        robot.press(KeyCode.TAB).release(KeyCode.TAB);
        // Selektujemo postojeću vrijednost kako bi ista bila obrisana
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("13. 02. 1920");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Ako datum nije prepoznat, neće se zadržati izmjena
        DatePicker datePicker = robot.lookup("#knjigaDatum").queryAs(DatePicker.class);
        assertNotNull(datePicker);
        String date = datePicker.getEditor().getText();

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        // Da li je datum uspješno promijenjen?
        assertEquals("13. 02. 1920", date);

        // Da li je knjiga dodana?
        String expected = "Testni autor, Testni naslov, 1234, 0, 13. 02. 1920";
        assertTrue(model.dajKnjige().contains(expected));
    }


    @Test
    public void testChange (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Selektujemo Ivu Andrića
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        robot.clickOn("#tabelaKnjiga");
        robot.clickOn("#tbChange");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        // Statusna poruka u ovom trenutku mora biti "Mijenjam knjigu."
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();

        // Travnička hronikaaa
        robot.clickOn("#knjigaNaslov");
        robot.press(KeyCode.END).release(KeyCode.END);
        robot.write("aaa");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        // Da li je stara statusna poruka bila dobra?
        assertEquals("Mijenjam knjigu.", statusMsg);

        // Da li je knjiga izmijenjena?
        String expected = "Travnička hronikaaaa,";
        assertTrue(model.dajKnjige().contains(expected));

        // Nova statusna poruka
        statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga izmijenjena.", statusMsg);
    }

    @Test
    public void testAddValidateAutor (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Otvaramo Add dijalog tastaturom kroz meni
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.A).release(KeyCode.A).release(KeyCode.ALT);

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        robot.clickOn("#knjigaAutor");
        robot.write("abc");

        // Uzmi boju
        TextField autor = robot.lookup("#knjigaAutor").queryAs(TextField.class);
        Background bg = autor.getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        assertTrue(colorFound);
    }

    @Test
    public void testAddValidateAutor1 (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Otvaramo Add dijalog tastaturom kroz meni
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.A).release(KeyCode.A).release(KeyCode.ALT);

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        robot.clickOn("#knjigaAutor");
        robot.write("abc");

        // Autor je sada validan - Brišemo autora
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        // Uzmi sada boju
        TextField autor = robot.lookup("#knjigaAutor").queryAs(TextField.class);
        Background bg = autor.getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        assertTrue(colorFound);

        // Da li je statusna poruka ispravna?
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga nije dodana.", statusMsg);
    }

    @Test
    public void testAddValidateNaslov (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Otvaramo Add dijalog tastaturom kroz meni
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.A).release(KeyCode.A).release(KeyCode.ALT);

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaNaslov").tryQuery().isPresent();

        robot.clickOn("#knjigaNaslov");
        robot.write("abc");

        // Brišemo naslov
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);

        // Sad opet unosimo naslov - sad je validan!
        robot.write("abc");

        // Uzmi boju
        TextField naslov = robot.lookup("#knjigaNaslov").queryAs(TextField.class);
        Background bg = naslov.getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound = true;

        // Unosimo i autora - sada je kompletna forma validna
        robot.clickOn("#knjigaAutor");
        robot.write("abc");

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);
        assertTrue(colorFound);

        // Da li je statusna poruka ispravna?
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga dodana.", statusMsg);

        // Da li je knjiga dodana
        String expected = "abc, abc";
        assertTrue(model.dajKnjige().contains(expected));
    }

    @Test
    public void testAddValidateNull (FxRobot robot) {
        // Prazna knjiga se ne može dodati
        robot.clickOn("#tbAdd");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaAutor").tryQuery().isPresent();

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        String expected = "\n , ,";
        assertFalse(model.dajKnjige().contains(expected));

        // Da li je statusna poruka ispravna?
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga nije dodana.", statusMsg);
    }

    @Test
    public void testChangeValidateNaslov (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Selektujemo Ivu Andrića
        robot.press(KeyCode.DOWN).release(KeyCode.DOWN);
        robot.clickOn("#tabelaKnjiga");
        robot.clickOn("#tbChange");

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaNaslov").tryQuery().isPresent();

        // Brišemo naslov
        robot.clickOn("#knjigaNaslov");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.press(KeyCode.DELETE).release(KeyCode.DELETE);

        // Da li je boja lightpink
        TextField naslov = robot.lookup("#knjigaNaslov").queryAs(TextField.class);
        Background bg = naslov.getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);

        assertTrue(colorFound);

        // Da li je knjiga izmijenjena? trebalo bi da nije
        String expected = "Ivo Andrić, Travnička hronika,";
        assertTrue(model.dajKnjige().contains(expected));

        // Nova statusna poruka
        String statusMsg = robot.lookup("#statusMsg").queryAs(Label.class).getText();
        assertEquals("Knjiga nije izmijenjena.", statusMsg);
    }

    @Test
    public void testAddValidateDatum (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");

        // Otvaramo Add dijalog tastaturom kroz meni
        robot.press(KeyCode.ALT).press(KeyCode.E).release(KeyCode.E).press(KeyCode.A).release(KeyCode.A).release(KeyCode.ALT);

        // Čekamo da prozor postane vidljiv
        robot.lookup("#knjigaDatum").tryQuery().isPresent();

        // Datum u budućnosti
        robot.clickOn("#knjigaDatum");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("19. 10. 2019");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        // Uzmi boju
        DatePicker datePicker = robot.lookup("#knjigaDatum").queryAs(DatePicker.class);
        Background bg = datePicker.getEditor().getBackground();
        Paint lightpink = Paint.valueOf("#ffb6c1");
        boolean colorFound = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(lightpink))
                colorFound = true;

        // Datum u prošlosti
        robot.clickOn("#knjigaDatum");
        robot.press(KeyCode.CONTROL).press(KeyCode.A).release(KeyCode.A).release(KeyCode.CONTROL);
        robot.write("19. 10. 1919");
        robot.press(KeyCode.ENTER).release(KeyCode.ENTER);

        bg = datePicker.getEditor().getBackground();
        Paint yellowgreen = Paint.valueOf("#adff2f");
        boolean colorFound2 = false;
        for (BackgroundFill bf : bg.getFills())
            if (bf.getFill().equals(yellowgreen))
                colorFound2 = true;

        // Zatvaramo prozor
        robot.press(KeyCode.ALT).press(KeyCode.F4).release(KeyCode.F4).release(KeyCode.ALT);
        assertTrue(colorFound);
        assertTrue(colorFound2);
    }

    /*@Test
    public void testQuit (FxRobot robot) {
        robot.clickOn("#tabelaKnjiga");
        robot.press(KeyCode.ALT).press(KeyCode.F).release(KeyCode.F).press(KeyCode.E).release(KeyCode.E).release(KeyCode.ALT);
        assertFalse(theStage.isShowing());
    }*/

}
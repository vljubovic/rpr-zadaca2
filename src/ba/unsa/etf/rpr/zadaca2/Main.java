package ba.unsa.etf.rpr.zadaca2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        BibliotekaModel model = new BibliotekaModel();
        model.napuni();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("biblioteka.fxml"));
        loader.setController(new BibliotekaController(model));
        Parent root = loader.load();
        primaryStage.setTitle("Biblioteka");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

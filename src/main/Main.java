package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage popup = new Stage();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("popupName.fxml")
        );
        popup.initStyle(StageStyle.UTILITY);
        Parent root2 = loader.load();
        popup.setTitle("Leitura de dados");
        popup.setResizable(false);
        popup.setScene(new Scene(root2, 400, 450));
        popup.show();
        popup.setAlwaysOnTop(true);
        popup.toFront();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

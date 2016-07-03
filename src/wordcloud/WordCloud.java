/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcloud;

import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Dylan
 */
public class WordCloud extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader chargeur = new  FXMLLoader (getClass().getResource("/views/MainView.fxml"));        
        chargeur.setController(new MainController());
        MainController c = chargeur.getController();        
        Parent root = chargeur.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

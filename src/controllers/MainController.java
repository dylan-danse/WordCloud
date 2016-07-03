/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import models.WeighedWord;
import models.TextParser;
import models.PrintedWord;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Cloud;


/**
 *
 * @author Dylan
 */
public class MainController implements Initializable {
    
    @FXML private TextArea textArea;    
    @FXML private ListView tempListView;
    @FXML private FlowPane cloudFlowPane;
    
    @FXML
    private void generateButtonClicked(ActionEvent event) {
        tempListView.getItems().clear();
        cloudFlowPane.getChildren().clear();
        
        /*TODO : Remove, just for testing*/
        ObservableList<WeighedWord> obslist = FXCollections.observableArrayList(TextParser.stringToWeighedWords(textArea.getText(), 20, 3, 2));
        tempListView.setItems(obslist);
        /* ----------------------------- */ 
        
        Cloud cloud = new Cloud(TextParser.stringToWeighedWords(textArea.getText(), 20, 3, 2));        
        addLabelsToCloud(cloud);
    }
    
    @FXML
    private void importFileButtonClicked(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        textArea.setText(TextParser.textFileToString(selectedFile.getAbsolutePath()));
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    private void addLabelsToCloud(Cloud words){        
        for (PrintedWord word : words) {
            Label label = new Label(word.getWord());        
            label.setTextFill(word.getColor());
            label.setFont(new Font(word.getSize()));
            label.setPadding(new Insets(2));
            cloudFlowPane.getChildren().add(label);
        }        
    }
    
}

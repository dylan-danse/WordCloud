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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Cloud;


/**
 *
 * @author Dylan
 */
public class MainController implements Initializable {
    
    @FXML private TextArea textArea;    
    @FXML private ListView tempListView;
    @FXML private FlowPane cloudFlowPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*TODO : Remove, just for testing*/
        cloudFlowPane.setStyle("-fx-border-color: black;");
        /* ----------------------------- */ 
    }
    
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
    
    private void addLabelsToCloud(Cloud words){        
        for (PrintedWord word : words) {
            cloudFlowPane.getChildren().add(generateLabelFor(word));
        }        
    }
    
    private void showModifyLabelDialog(Label label){
        Label newLabel = label;
        
        Dialog<PrintedWord> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");
        
        Label label1 = new Label("Font size: ");
        Label label2 = new Label("Color: ");
        TextField fontSize = new TextField();
        ColorPicker picker = new ColorPicker();
        
        GridPane grid = new GridPane();
        grid.add(label1,1,1);
        grid.add(label2,1,2);
        grid.add(fontSize,2,1);
        grid.add(picker,2,2);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        
        dialog.setResultConverter((ButtonType param) -> {
            if (param == buttonTypeOk) {
                return new PrintedWord("test", Integer.parseInt(fontSize.getText()), picker.getValue());
            }else{
                return null;                
            }
        });
        
        Optional<PrintedWord> result = dialog.showAndWait();
        if (result.isPresent()) {                   
            label.setTextFill(result.get().getColor());
            label.setFont(new Font(result.get().getSize()));
        }
    }
    
    private Label generateLabelFor(PrintedWord word){
        Label label = new Label(word.getWord());        
        label.setTextFill(word.getColor());
        label.setFont(new Font(word.getSize()));
        label.setPadding(new Insets(2));            

        label.setOnMouseClicked((MouseEvent e) -> {
            showModifyLabelDialog(label);
        });
        
        return label;
    }
}

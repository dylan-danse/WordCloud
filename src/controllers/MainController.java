/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import models.Cloud;
import models.PrintedWord;
import models.TextParser;
import models.WeighedWord;


/**
 *
 * @author Dylan
 */
public class MainController implements Initializable {
    
    @FXML private TextArea textArea;    
    @FXML private ListView tempListView;
    @FXML private Pane cloudPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*TODO : Remove, just for testing*/
        cloudPane.setStyle("-fx-border-color: black;");
        /* ----------------------------- */ 
        
        textArea.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if(db.hasFiles()){
                event.acceptTransferModes(TransferMode.ANY);
                for(File file:db.getFiles()){
                    if("txt".equals(TextParser.getExtension(file)))
                        textArea.setText(TextParser.textFileToString(file.getAbsolutePath()));
                }
            }else{
                event.setDropCompleted(false);
                textArea.setText("");
            }            
            event.consume();
        });
    }
    
    @FXML
    private void importFileButtonClicked(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        textArea.setText(TextParser.textFileToString(selectedFile.getAbsolutePath()));
    }
    
    @FXML
    private void generateButtonClicked(ActionEvent event) {
        tempListView.getItems().clear();
        cloudPane.getChildren().clear();
        
        /*TODO : Remove, just for testing*/
        ObservableList<WeighedWord> obslist = FXCollections.observableArrayList(TextParser.stringToWeighedWords(textArea.getText(), 20, 3, 1));
        tempListView.setItems(obslist);    
        /* ----------------------------- */ 
        
        Cloud cloud = new Cloud(TextParser.stringToWeighedWords(textArea.getText(), 20, 3, 2));        
        cloudPane.getChildren().addAll(generateTextsFor(cloud));
    }
    
    private void showModifyLabelDialog(Text text){
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
        if(result.isPresent()){
            text.setFill(result.get().getColor());
            text.setFont(new Font(result.get().getSize()));
        }        
    }
    
    private ArrayList<Text> generateTextsFor(Cloud words){        
        ArrayList<Text> texts = new ArrayList<>();        
        words.stream().map((word) -> {
            final Delta dragDelta = new Delta();
            
            Text text = new Text(word.getWord());        
            text.setFill(word.getColor());
            text.setFont(new Font(word.getSize()));
            
            /*Events*/
            text.setOnMouseClicked((MouseEvent e) -> {
                showModifyLabelDialog(text);
            });        
            text.setOnMousePressed((MouseEvent mouseEvent) -> {
                dragDelta.x = text.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = text.getLayoutY() - mouseEvent.getSceneY();
                text.setCursor(Cursor.MOVE);
            });
            text.setOnMouseReleased((MouseEvent mouseEvent) -> {
                text.setCursor(Cursor.HAND);
            });
            text.setOnMouseDragged((MouseEvent mouseEvent) -> {
                text.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                text.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            });
            return text;
        }).map((text) -> {
            text.setOnMouseEntered((MouseEvent mouseEvent) -> {
                text.setCursor(Cursor.HAND);
            });
            return text;
        }).forEach((text) -> {
            texts.add(text);
        });
        return texts;
    }    
}
class Delta { double x, y; }

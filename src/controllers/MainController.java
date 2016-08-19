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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import javafx.util.converter.NumberStringConverter;
import models.Cloud;
import models.PrintedWord;
import models.TextParser;


/**
 *
 * @author Dylan
 */
public class MainController implements Initializable {
    
    @FXML private TextArea textArea;
    @FXML private Pane cloudPane;
    @FXML private TextField minSizeField;
    @FXML private TextField minFreqField;
    @FXML private TextField maxWordsNumberField;
    @FXML private CheckBox isOrdered;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*TODO : Remove, just for testing*/
        cloudPane.setStyle("-fx-border-color: black;");
        /* ----------------------------- */ 
        
        minSizeField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        minFreqField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        maxWordsNumberField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        
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
        cloudPane.getChildren().clear();
        int minSize = 3;
        int minFreq = 2;
        int maxWords = 20;
        
        try {
            minSize = Integer.parseInt(minSizeField.getText());
        } catch (Exception e) {
        }
        try {
            minFreq = Integer.parseInt(minFreqField.getText());
        } catch (Exception e) {
        }
        try {
            maxWords = Integer.parseInt(maxWordsNumberField.getText());
        } catch (Exception e) {
        }
        
        Cloud cloud = new Cloud(TextParser.stringToWeighedWords(textArea.getText(), maxWords, minSize, minFreq, isOrdered.isSelected()));        
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
                if (fontSize.getText().isEmpty())
                    return new PrintedWord(text.getText(), picker.getValue(),text.getFont());
                else
                    return new PrintedWord(text.getText(), picker.getValue(), new Font("System",Integer.parseInt(fontSize.getText())));
            }else{
                return null;                
            }
        });
        
        Optional<PrintedWord> result = dialog.showAndWait();
        if(result.isPresent()){
            text.setFill(result.get().getColor());
            text.setFont(result.get().getFont());
        }        
    }
    
    private ArrayList<Text> generateTextsFor(Cloud words){        
        ArrayList<Text> texts = new ArrayList<>();        
        words.stream().map((word) -> {
            
            Text text = new Text(word.getWord());        
            text.setFill(word.getColor());
            text.setFont(word.getFont());
            
            /*Events*/
            text.setOnMouseClicked((MouseEvent e) -> {                
                System.out.println("CLICKED");
                showModifyLabelDialog(text);
            });        
            text.setOnMousePressed((MouseEvent mouseEvent) -> {                 
                System.out.println("PRESSED");
                text.setCursor(Cursor.MOVE);
            });
            text.setOnMouseReleased((MouseEvent mouseEvent) -> {                
                System.out.println("RELEASED");
                /*TODO : Switch words in cloud*/ 
                text.setCursor(Cursor.HAND);
            });
            text.setOnMouseDragged((MouseEvent mouseEvent) -> {
                System.out.println("DRAGGED");
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
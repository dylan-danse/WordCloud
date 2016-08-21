/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import models.Cloud;
import models.Colors;
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
    @FXML private ComboBox fontChooserComboBox;
    @FXML private ImageView image;
    @FXML private ComboBox themeColorComboBox;
    
    private Text textClicked; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        minSizeField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        minFreqField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        maxWordsNumberField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        fontChooserComboBox.getItems().setAll(Font.getFamilies());
        themeColorComboBox.getItems().setAll(Colors.values());
        themeColorComboBox.getSelectionModel().select(0);
        
        
        
        textArea.setOnDragOver((DragEvent event) -> {
            if(event.getDragboard().hasFiles()){
                event.acceptTransferModes(TransferMode.ANY);
            }          
            event.consume();
        });
        
        textArea.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            StringBuilder sb = new StringBuilder();
            
            if(db.hasFiles()){
                for(File file:db.getFiles()){
                    if("txt".equals(TextParser.getExtension(file)))
                        sb.append(TextParser.textFileToString(file.getAbsolutePath()));
                }
                textArea.setText(sb.toString());
                textArea.positionCaret(sb.length());
            }          
            event.consume();
        });
        
        image.setOnMouseClicked((MouseEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(new Stage());
            try {
                textArea.setText(TextParser.textFileToString(selectedFile.getAbsolutePath()));
            } catch (Exception e) {
            }            
            event.consume();
        });
    }
    
    @FXML
    private void generateButtonClicked(ActionEvent event) {       
        cloudPane.getChildren().clear();
        int minSize = 3;
        int minFreq = 2;
        int maxWords = 40;
        String fontFamily = "System";
        
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
        try {
            fontFamily = fontChooserComboBox.getSelectionModel().getSelectedItem().toString();
        } catch (Exception e) {
        }
        
        Cloud cloud = new Cloud(TextParser.stringToWeighedWords(textArea.getText(), 
                                                                maxWords, 
                                                                minSize, 
                                                                minFreq, 
                                                                isOrdered.isSelected()),
                                                                fontFamily, 
                                                                (Colors)themeColorComboBox.getSelectionModel().getSelectedItem());        
        cloudPane.getChildren().addAll(generateTextsFor(cloud));
    }
        
    private void showModifyLabelDialog(Text text){
        Dialog<PrintedWord> dialog = new Dialog<>();
        dialog.setTitle("Text Custom");
        dialog.setHeaderText("Customize the text : " + text.getText());
        
        Label label1 = new Label("Font size: ");
        Label label2 = new Label("Color: ");
        TextField fontSize = new TextField();
        fontSize.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        fontSize.setText(String.valueOf(text.getFont().getSize()));
        ColorPicker picker = new ColorPicker((Color) text.getFill());
        
        
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

        for(PrintedWord word : words){
            Text text = new Text(word.getWord());
            text.setFill(word.getColor());
            text.setFont(word.getFont());
            FlowPane.setMargin(text, new Insets(5));
            if(Math.random() < 0.2)
                text.setRotate(-90);            
            
            text.setOnMouseClicked((MouseEvent mouseEvent) -> { 
                showModifyLabelDialog(text);
            });   
            text.setOnDragDetected((MouseEvent mouseEvent) -> { 
                text.startFullDrag();
            });   
            text.setOnMousePressed((MouseEvent mouseEvent) -> { 
                textClicked = text;
                text.setCursor(Cursor.MOVE);
            });
            text.setOnMouseReleased((MouseEvent mouseEvent) -> {  
                textClicked = null;
                text.setCursor(Cursor.HAND);
            });
            text.setOnMouseEntered((MouseEvent mouseEvent) -> {
                text.setCursor(Cursor.HAND);
            });
            text.setOnMouseDragOver((MouseDragEvent mouseEvent) -> {
                if(textClicked != null){
                    cloudPane.getChildren().setAll(moveItem(cloudPane.getChildren().indexOf(textClicked), 
                                                            cloudPane.getChildren().indexOf(text), 
                                                            cloudPane.getChildren()));
                }
            });
            
            texts.add(text);
        }
        
        return texts;
    } 
    
    public List<Node> moveItem(int sourceIndex, int targetIndex, List<Node> list) {
        List<Node> newList = new ArrayList<>(list);
        if (sourceIndex <= targetIndex) {
            Collections.rotate(newList.subList(sourceIndex, targetIndex + 1), -1);
        } else {
            Collections.rotate(newList.subList(targetIndex, sourceIndex + 1), 1);
        }
        return newList;
    }
}
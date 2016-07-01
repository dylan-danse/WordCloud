/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcloud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.paint.Color;


/**
 *
 * @author Dylan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private TextArea textArea;    
    @FXML private ListView tempListView;
    @FXML private Pane cloudPane;
    private File selectedFile;
    
    @FXML
    private void generateButtonClicked(ActionEvent event) {
        tempListView.getItems().clear();
        ObservableList<WeighedWord> obslist = FXCollections.observableArrayList(TextParser.getWords(textArea.getText(), 20, 3));
        tempListView.setItems(obslist);
    }
    
    @FXML
    private void importFileButtonClicked(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(new Stage());
        textArea.setText(getContentFromTextFile(selectedFile.getAbsolutePath()));
    }
    
    private String getContentFromTextFile(String path){
        StringBuilder sb = new StringBuilder();
        String line;
        int octetsRead;
        byte[]tampon;
        
        try(InputStream out = Files.newInputStream(Paths.get(path), StandardOpenOption.READ)) {            
            tampon = new byte[256];
            octetsRead = out.read(tampon);
            while (octetsRead > 0) {
                line = new String(tampon, StandardCharsets.ISO_8859_1);
                sb.append(line);
                octetsRead = out.read(tampon);
            }
        } catch (IOException ex) {
            System.out.println("Specified path is not accessible");
            ex.printStackTrace(System.err);
        }        
        return sb.toString();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<PrintedWord> words = new ArrayList<>();
        words.add(new PrintedWord("ceci", 20, Color.RED));
        words.add(new PrintedWord("est", 20, Color.BLUE));
//        words.add(new PrintedWord("un", 55, Color.GREEN));
//        words.add(new PrintedWord("test", 30, Color.RED));
//        words.add(new PrintedWord("pour", 40, Color.BLUE));
//        words.add(new PrintedWord("l'affichage", 15, Color.GREEN));
//        words.add(new PrintedWord("du", 35, Color.RED));
//        words.add(new PrintedWord("nuage", 20, Color.BLUE));
//        words.add(new PrintedWord("de", 40, Color.GREEN));
//        words.add(new PrintedWord("mots", 10, Color.RED));
        
        for(PrintedWord word : words){
            addLabelToCloud(word);
        }
    }
    
    private void addLabelToCloud(PrintedWord word){
        Label label = new Label(word.getWord());        
        label.setTextFill(word.getColor());
        label.setFont(new Font(word.getSize()));
        
        label.
        
        cloudPane.getChildren().add(label);
    }
    
}

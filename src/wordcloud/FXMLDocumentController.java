/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Dylan
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private TextArea textArea;    
    @FXML private ListView tempListView;
    private File selectedFile;
    
    @FXML
    private void generateButtonClicked(ActionEvent event) {
        tempListView.getItems().clear();
        ObservableList<WeighedWord> obslist = FXCollections.observableArrayList(TextParser.getWords(textArea.getText(), 20, 3));
        tempListView.setItems(obslist);
    }
    
    @FXML
    private void importFileButtonClicked(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        selectedFile = fileChooser.showOpenDialog(new Stage());
        textArea.setText(getContentFromTextFile(selectedFile.getAbsolutePath()));
    }
    
    private String getContentFromTextFile(String path) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        String text;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            text = sb.toString();
        } 
        finally {
            br.close();
        }
        return text;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

/*
 * Made by Andrea Mate.
 * For practice.
 * This is the way!
 */
package sorozatok;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Máté Andrea
 */
public class FXMLDocumentController implements Initializable {
    
   @FXML
    private TextField txtCim;

    @FXML
    private Spinner<Integer> spEvad;

    @FXML
    private Spinner<Integer> spResz;

    @FXML
    private TableView<Sorozat> tblSorozatok;

    @FXML
    private TableColumn<Sorozat, String> oCim;

    @FXML
    private TableColumn<Sorozat, Integer> oEvad;
    
    @FXML
    private TableColumn<Sorozat, Integer> oResz;

    @FXML
    void hozzaad() {
        if (!txtCim.getText().isEmpty()) {
            Sorozat s= new Sorozat();
            s.setCim(txtCim.getText());
            s.setEvad(spEvad.getValue());
            s.setResz(spResz.getValue());
            tblSorozatok.getItems().add(s);
            int utolso=tblSorozatok.getItems().size()-1;
            tblSorozatok.getSelectionModel().select(utolso);
        }
        txtCim.requestFocus();
        ment();
    }

    @FXML
    void leptet() {
        int i=tblSorozatok.getSelectionModel().getSelectedIndex();
        if (i>-1) {
            Sorozat s=tblSorozatok.getItems().get(i);
            s.novel();
            tblSorozatok.getItems().set(i, s);
        }
        ment();
    }

    @FXML
    void modosit() {
        int i=tblSorozatok.getSelectionModel().getSelectedIndex();
        if (!txtCim.getText().isEmpty() && i>-1) {
            Sorozat s= new Sorozat();
            s.setCim(txtCim.getText());
            s.setEvad(spEvad.getValue());
            s.setResz(spResz.getValue());
            tblSorozatok.getItems().set(i, s);
            tblSorozatok.getSelectionModel().select(i);
        }
        txtCim.requestFocus();
        ment();
    }

    @FXML
    void torol() {
        int i=tblSorozatok.getSelectionModel().getSelectedIndex();
        if (i>-1) {
            tblSorozatok.getItems().remove(i);
        }
        tblSorozatok.requestFocus();
        ment();
    }

    @FXML
    void ujsorozat() {
        beallit(new Sorozat());
        tblSorozatok.getSelectionModel().select(null);
        txtCim.requestFocus();
    }

    @FXML
    private void web() throws Exception{
        Desktop.getDesktop().browse(new URI("http://www.sorozatbarat.online"));
    }
    
     @FXML
    void bill(KeyEvent event) {
         if (event.getCode() == KeyCode.INSERT) {
             leptet();
         }else if (event.getCode()==KeyCode.DELETE) {
             torol();
         }
    }
    
    public void betolt(ObservableList<Sorozat> lista){
        try (Scanner be= new Scanner(new File("sorozatok.txt"),"utf8")) {
            while (be.hasNextLine()) {
                lista.add(new Sorozat(be.nextLine()));
                
            }
        } catch (Exception e) {
        }
    }
    
    private void beallit(Sorozat s){
        txtCim.setText(s.getCim());
        spEvad.getValueFactory().setValue(s.getEvad());
        spResz.getValueFactory().setValue(s.getResz());
    }
    
    private void ment(){
        try (PrintWriter ki=new PrintWriter("sorozatok.txt", "utf8")){
            for (Sorozat f : tblSorozatok.getItems()) {
                ki.println(f);
            }
        } catch (IOException e) {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Nem tudtam menteni!");
            alert.showAndWait();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spResz.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30,1));
        spEvad.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30,1));
        oCim.setCellValueFactory(
                new PropertyValueFactory<>("cim"));
        oEvad.setCellValueFactory(
                new PropertyValueFactory<>("evad"));
        oResz.setCellValueFactory(
                new PropertyValueFactory<>("resz"));
        betolt(tblSorozatok.getItems());
        tblSorozatok.getSelectionModel().selectedItemProperty().addListener(
            (o,regi,uj)->{
                if(uj!=null)
                    beallit(uj);
            });
        
    }    
    
}

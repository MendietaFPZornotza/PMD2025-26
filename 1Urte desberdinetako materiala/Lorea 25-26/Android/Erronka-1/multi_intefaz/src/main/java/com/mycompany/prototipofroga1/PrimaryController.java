package com.mycompany.prototipofroga1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Liburu;
import model.IdazleData;
import conexionbd.SocketClient;

public class PrimaryController implements Initializable{

    @FXML
    private TableView<Liburu> tblLiburuak;
    @FXML
    private TableColumn  <Liburu, Number> colId;
    @FXML
    private TableColumn <Liburu,String> colTitulo;
    @FXML
    private TableColumn<Liburu,String> colAutor;
    @FXML
    private TableColumn <Liburu,String> colGenero;
    @FXML
    private Menu mnuSwitch;
    @FXML
    private Label lblOpzioa;
    @FXML
    private ComboBox<IdazleData> cmbIdazleak;
    @FXML
    private TableView<Liburu> tblLiburuAuthor;
    @FXML
    private TableColumn<Liburu, Number> colIdLiburuAuthor;
    @FXML
    private TableColumn<Liburu,String> colTituluLiburuAuthor;
    @FXML
    private TableColumn<Liburu,String> colGeneroLiburuAuthor;
    @FXML
    private Menu mnuOptLiburuak;
    @FXML
    private Menu mnuOptionAuthor;

    IdazleData idazleSelek;
    SocketClient socketClient;
    ObservableList<Liburu> listLiburuguztiak;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        hideElements();

        System.out.println("initialize PrimaryController");
        // Configure cells for tablewiew tblLiburuak  
        this.colId.setCellValueFactory(new PropertyValueFactory<Liburu, Number>("id"));
        this.colTitulo.setCellValueFactory(new PropertyValueFactory<Liburu, String>("izenburua"));
        this.colAutor.setCellValueFactory(new PropertyValueFactory<Liburu, String>("idazlea"));
        this.colGenero.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getGeneroakAsString())
        );

        // Initialize socket client and load data
        socketClient = new SocketClient();
        listLiburuguztiak = socketClient.getBooks();
        
    }

    @FXML
    private void switchToSelection(ActionEvent event) throws IOException {
         App.setRoot("selection");
    }

    @FXML
    private void cargarTableview(ActionEvent event) {
        this.idazleSelek =cmbIdazleak.getSelectionModel().getSelectedItem();
        if(idazleSelek !=null){
            // Actualiza el label con el nombre del autor,
             lblOpzioa.setText(idazleSelek.getIzena());
        // Actualiza el TableView con la lista de libros del autor seleccionado
        System.out.println(idazleSelek.getLiburuak());
        ObservableList<Liburu> librosObservable = FXCollections.observableArrayList(idazleSelek.getLiburuak());
        tblLiburuak.setItems(librosObservable);
        
       
         // (Opcional) Mostrar por consola para depuración
         for ( Liburu liburu : idazleSelek.getLiburuak()) {
         // Aquí puedes trabajar con cada 'liburu'
                System.out.println("ID: " + liburu.getId() +
                               " Título: " + liburu.getIzenburua() +
                               " Géneros: " + liburu.getGeneroakAsString());
             
                                        }
               }
              
    }

    @FXML
    private void chargeLiburuak(ActionEvent event) {
        hideElements();
        this.lblOpzioa.setVisible(true);
        this.lblOpzioa.setText("LIBURUAK ");
        
        // Load books from server
        listLiburuguztiak = socketClient.getBooks();
        this.tblLiburuak.setItems(listLiburuguztiak);
    }

    @FXML
    private void chargeAuthors(ActionEvent event) {
        hideElements();
        this.lblOpzioa.setVisible(true);
        this.cmbIdazleak.setVisible(true);
        this.lblOpzioa.setText("AUTHOR/BOOKS ");
        
        // Load author data from server
        ObservableList<IdazleData> authorData = socketClient.getAuthorData();
        this.cmbIdazleak.setItems(authorData);
        System.out.println("Author data loaded: " + authorData.size() + " authors");
    }
    
    public void hideElements(){
    //Hide elements
      
        
        this.lblOpzioa.setVisible(false);
        this.cmbIdazleak.setVisible(false);
    }
    
    
}

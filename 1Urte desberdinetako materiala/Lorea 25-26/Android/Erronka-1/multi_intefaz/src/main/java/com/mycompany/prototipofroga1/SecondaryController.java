package com.mycompany.prototipofroga1;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.IdazleData;
import model.Liburu;

public class SecondaryController implements Initializable{

    @FXML
    private Button secondaryButton;
    @FXML
    private Label lblIdazlea;
    @FXML
    private TableColumn<Liburu, Number> colId;
    @FXML
    private TableColumn<Liburu, String> colGenero;
    @FXML
    private ComboBox<IdazleData> cmbIdazleak;
    @FXML
    private TableView<Liburu> tblLiburuak;
    @FXML
    private TableColumn<Liburu, String> colTitulo;

    IdazleData idazleSelek;
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
       @FXML
    private void cargarTableview(ActionEvent event) {
        
        
       this.idazleSelek =cmbIdazleak.getSelectionModel().getSelectedItem();
        if(idazleSelek !=null){
            // Actualiza el label con el nombre del autor,
             lblIdazlea.setText(idazleSelek.getIzena());
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
    
     @Override
    public void initialize(URL url, ResourceBundle rb) {
            // Configurar las columnas del TableView
         colId.setCellValueFactory(new PropertyValueFactory<>("id"));
         colTitulo.setCellValueFactory(new PropertyValueFactory<>("izenburua"));
         // Para la columna de géneros usamos un lambda para llamar al método que devuelve la cadena
         colGenero.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getGeneroakAsString()));

          //  Cargar el JSON usando Gson
        Gson gson = new Gson();
      //  String userDirectoryPath = System.getProperty("user.dir");
     //   String filePath =  userDirectoryPath + "\\src\\main\\resources\\json\\\\datos.json" ;    
   
       try (
         InputStream inputStream = getClass().getClassLoader().getResourceAsStream("json/idazleak.json");
         InputStreamReader reader = new InputStreamReader(inputStream)) {
         java.lang.reflect.Type tipoListIdazleak = new TypeToken<List<IdazleData>>(){}.getType();
         List<IdazleData> listIdazleak = gson.fromJson(reader, tipoListIdazleak);
         System.out.println(listIdazleak);
         ObservableList<IdazleData> obList = FXCollections.observableList(listIdazleak);
         this.cmbIdazleak.setItems(obList );
         System.out.println("oblist");
         System.out.print(obList);
          } catch (Exception e) {
        System.err.println("Error al leer el JSON desde resources: " + e.getMessage());
         } 
  

    }

}
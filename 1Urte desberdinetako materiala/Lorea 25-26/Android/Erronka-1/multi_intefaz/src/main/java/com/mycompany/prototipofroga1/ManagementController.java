package com.mycompany.prototipofroga1;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Genre;
import model.Writer;
import conexionbd.SocketClient;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;

import javafx.scene.control.SelectionMode;

public class ManagementController implements Initializable {

    @FXML
    private Label lblId;
    @FXML
    private MenuItem mnuNewBook;
    @FXML
    private MenuItem mnuBookDelete;
    @FXML
    private MenuItem mnuBookUpdate;
    @FXML
    private MenuItem mnuWriterNew;
    @FXML
    private MenuItem mnuWriterDelete;
    @FXML
    private MenuItem mnuWriterUpdate;
    @FXML
    private MenuItem mnuGenresNew;
    @FXML
    private MenuItem mnuGenresDelete;
    @FXML
    private MenuItem mnuGenresUpdate;
    @FXML
    private Menu mnuSwitchToSelection;
    @FXML
    private Label lbloption;
    @FXML
    private TextField txtId;
    @FXML
    private Label lblTitulua;
    @FXML
    private TextField txtTitulua;
    @FXML
    private Label lblAutor;
    @FXML
    private ComboBox<Writer> cmbAutor;
    @FXML
    private Label lblGeneros;
    @FXML
    private ListView<Genre> lstGenre;
    @FXML
    private Button btnGorde;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button btnAutoreBerria;
    @FXML
    private Button btnOnartuAutorea;
    @FXML
    private TextField txtAutorea;

       @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize ManagementController");
        this.txtId.setVisible(false);
        this.lblId.setVisible(false);
        this.btnOnartuAutorea.setVisible(false);
        this.txtAutorea.setVisible(false);
        // Configurar la ListView para permitir selección múltiple si se desea
        lstGenre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        loadAuthors();
        loadGenres();
        
    }    

    @FXML
    private void backToSelection(ActionEvent event) throws IOException {
         App.setRoot("selection");
    }
    @FXML
    private void ikusiAutoreBerria(ActionEvent event) {
        this.btnAutoreBerria.setVisible(false);
        this.btnOnartuAutorea.setVisible(true);
        this.txtAutorea.setVisible(true);
    }
    @FXML
    private void btnOnartuAutorea(ActionEvent event) {
        System.out.println("gorde autore berrie");
    }
     public void showAlert(Alert.AlertType alertType,String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        alert.show();
    } 

    @FXML
    private void saveBook(ActionEvent event) {
       String title = txtTitulua.getText().trim();
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validación", "El título es obligatorio.");
            return;
        }
       // Obtener el autor seleccionado o crear uno nuevo
        int authorId = -1;
        // Se puede insertar un autor nuevo si se ha ingresado en txtTitulua1,
        // de lo contrario se utiliza el autor seleccionado en el ComboBox.
        Writer selectedAuthor = cmbAutor.getValue();
        SocketClient conexion = new SocketClient();
        if (selectedAuthor == null) {
        // Si no se seleccionó un autor, verificar si el usuario ha ingresado uno nuevo
        String newAuthorName = txtAutorea.getText().trim();
        if (newAuthorName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validación", "Debes seleccionar o ingresar un autor.");
            return;
        }
        // Insertar el nuevo autor en la base de datos
        authorId = insertNewAuthor(newAuthorName);
        //Actualizar el combo con el nuevo autro
        cmbAutor.setItems(conexion.getAuthors());
         //seleccionar el autor nuevo en el combo automaticamente
          for(Writer writer :cmbAutor.getItems()){
            if(writer.getIzena().equals(newAuthorName)){
                cmbAutor.getSelectionModel().select(writer);
                break;
                }
            }

        }else{
            authorId = selectedAuthor.getId();
        } 
        
        // Obtener los géneros seleccionados
        ObservableList<Genre> selectedGenres = lstGenre.getSelectionModel().getSelectedItems();
        if (selectedGenres.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validación", "Debes seleccionar al menos un género.");
            return;
        }
        
        // Convertir géneros a lista de strings
        List<String> genreNames = new ArrayList<>();
        for (Genre genre : selectedGenres) {
            genreNames.add(genre.getGeneroa());
        }
        
      //Insertar el libro en la bd con géneros
       conexion.insertBook(title, authorId, genreNames);
}

    private int insertNewAuthor(String authorName) {
        int generatedId = -1;
        //String sql = "INSERT INTO idazlea (izena) VALUES (?)";
         SocketClient conexion = new SocketClient();
         generatedId= conexion.insertWriter(authorName);
        // Se obtiene la lista de autores (ObservableList de Strings) de la BD.
      
        ObservableList<Writer> listaAutores = conexion.getAuthors();
        // Se asigna la lista de autores al ComboBox
         return generatedId;
    }

     
    private void loadAuthors() {
        System.out.println("loadAuthors ManagementController");
        // Se instancia la clase de conexión
        SocketClient conexion = new SocketClient();
        // Se obtiene la lista de autores (ObservableList de Strings) de la BD.
        ObservableList<Writer> listaAutores = conexion.getAuthors();
        // Se asigna la lista de autores al ComboBox
        cmbAutor.setItems(listaAutores);
       
        cmbAutor.setCellFactory((ListView<Writer> listView) -> {
          return new ListCell<Writer>() {
        @Override
        protected void updateItem(Writer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getIzena()); // Solo muestra el nombre
            }
        }
    };
});

// También establece la celda del botón (la que se ve cuando se selecciona un elemento)
cmbAutor.setButtonCell(new ListCell<Writer>() {
    @Override
    protected void updateItem(Writer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getIzena());
        }
    }
});

     }
     /**
     * Carga los géneros de la tabla "generoak" en el ListView.
     */
    private void loadGenres() {
        System.out.println("loadGenres ManagementController");
         // Se instancia la clase de conexión
        SocketClient conexion = new SocketClient();
        // Se obtiene la lista de generos(ObservableList de Strings) de la BD.
        ObservableList<Genre> listaAutores = conexion.getGenre();
         ObservableList<Genre> genres = conexion.getGenre();
         System.out.println(genres);
        // Se asigna la lista de genres al ComboBox
           lstGenre.setItems(genres);
       lstGenre.setCellFactory((ListView<Genre> listView) -> {
    return new ListCell<Genre>() {
        @Override
        protected void updateItem(Genre item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.getGeneroa()); // Solo muestra el nombre
            }
        }
    };
});

// También establece la celda del botón (la que se ve cuando se selecciona un elemento)
cmbAutor.setButtonCell(new ListCell<Writer>() {
    @Override
    protected void updateItem(Writer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getIzena());
        }
    }
});
          
    }
}
  
    

    


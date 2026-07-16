package conexionbd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Genre;
import model.Writer;
import model.Liburu;
import model.IdazleData;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final Gson gson = new Gson();
    
    public SocketClient() {
        // Constructor for compatibility
    }
    
    /**
     * Sends a request to the server and returns the response
     */
    private String sendRequest(String request) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            // Send request
            out.println(request);
            
            // Read response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            
            return response.toString();
            
        } catch (IOException e) {
            //Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, "Error communicating with server", e);
            showAlert(Alert.AlertType.ERROR, "Connection Error", "Could not connect to server: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all authors from server
     */
    public ObservableList<Writer> getAuthors() {
        ObservableList<Writer> obs = FXCollections.observableArrayList();
        
        try {
            String request = "GET_AUTHORS";
            String response = sendRequest(request);
            
            if (response != null && !response.isEmpty()) {
                java.lang.reflect.Type type = new TypeToken<List<Writer>>(){}.getType();
                List<Writer> writers = gson.fromJson(response, type);
                obs.addAll(writers);
            }
            
        } catch (Exception ex) {
            //Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al obtener los autores: " + ex.getMessage());
        }
        
        return obs;
    }
    
    /**
     * Get all genres from server
     */
    public ObservableList<Genre> getGenre() {
        ObservableList<Genre> obs = FXCollections.observableArrayList();
        
        try {
            String request = "GET_GENRES";
            String response = sendRequest(request);
            
            if (response != null && !response.isEmpty()) {
                java.lang.reflect.Type type = new TypeToken<List<Genre>>(){}.getType();
                List<Genre> genres = gson.fromJson(response, type);
                obs.addAll(genres);
            }
            
        } catch (Exception ex) {
            //Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error al obtener los generos: " + ex.getMessage());
        }
        
        return obs;
    }
    
    /**
     * Insert a new book
     */
    public void insertBook(String izenburua, int idIdazlea) {
        insertBook(izenburua, idIdazlea, new ArrayList<>());
    }
    
    /**
     * Insert a new book with genres
     */
    public void insertBook(String izenburua, int idIdazlea, List<String> generoak) {
        try {
            // Create the request with genres as a comma-separated string
            String genresString = String.join(",", generoak);
            String request = "INSERT_BOOK:" + izenburua + ":" + idIdazlea + ":" + genresString;
            String response = sendRequest(request);
            
            if (response != null && response.equals("SUCCESS")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book inserted successfully with genres: " + genresString);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert book");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error inserting book: " + e.getMessage());
        }
    }
    
    /**
     * Insert a new writer and return the generated ID
     */
    public int insertWriter(String newWriter) {
        try {
            String request = "INSERT_WRITER:" + newWriter;
            String response = sendRequest(request);
            
            if (response != null && response.startsWith("SUCCESS:")) {
                String[] parts = response.split(":");
                if (parts.length > 1) {
                    return Integer.parseInt(parts[1]);
                }
            }
            
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert writer");
            return -1;
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error inserting writer: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Insert book-genre relationship
     */
    public void insertBookGen(String newBookGen) {
        try {
            String request = "INSERT_BOOK_GENRE:" + newBookGen;
            String response = sendRequest(request);
            
            if (response != null && response.equals("SUCCESS")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Book-genre relationship inserted successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert book-genre relationship");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error inserting book-genre relationship: " + e.getMessage());
        }
    }
    
    /**
     * Insert a new genre and return the generated ID
     */
    public int insertGenre(String newGenre) {
        try {
            String request = "INSERT_GENRE:" + newGenre;
            String response = sendRequest(request);
            
            if (response != null && response.startsWith("SUCCESS:")) {
                String[] parts = response.split(":");
                if (parts.length > 1) {
                    return Integer.parseInt(parts[1]);
                }
            }
            
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to insert genre");
            return -1;
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error inserting genre: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Show alert dialog
     */
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    
    /**
     * Get all books from server
     */
    public ObservableList<Liburu> getBooks() {
        ObservableList<Liburu> obs = FXCollections.observableArrayList();
        
        try {
            String request = "GET_BOOKS";
            String response = sendRequest(request);
            
            if (response != null && !response.isEmpty()) {
                java.lang.reflect.Type type = new TypeToken<List<Liburu>>(){}.getType();
                List<Liburu> books = gson.fromJson(response, type);
                obs.addAll(books);
            }
            
        } catch (Exception ex) {
            System.out.println("Error al obtener los libros: " + ex.getMessage());
        }
        
        return obs;
    }
    
    /**
     * Get all author data (with their books) from server
     */
    public ObservableList<IdazleData> getAuthorData() {
        ObservableList<IdazleData> obs = FXCollections.observableArrayList();
        
        try {
            String request = "GET_AUTHOR_DATA";
            String response = sendRequest(request);
            
            if (response != null && !response.isEmpty()) {
                java.lang.reflect.Type type = new TypeToken<List<IdazleData>>(){}.getType();
                List<IdazleData> authorData = gson.fromJson(response, type);
                obs.addAll(authorData);
            }
            
        } catch (Exception ex) {
            System.out.println("Error al obtener los datos de autores: " + ex.getMessage());
        }
        
        return obs;
    }
    
    /**
     * Close connection (no-op for socket client, but kept for compatibility)
     */
    public void cerrarConexion() {
        // No need to close anything for socket client
        System.out.println("Socket connection closed");
    }
} 
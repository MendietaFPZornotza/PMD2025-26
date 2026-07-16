package conexionbd;

import com.google.gson.Gson;
import model.Genre;
import model.Writer;
import model.Liburu;
import model.IdazleData;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple test server to demonstrate socket communication
 * This server simulates a database server by handling requests and returning JSON responses
 */
public class TestServer {
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();
    
    // Simulate database data - using non-final so they can be modified
    private static final List<Writer> writers = new ArrayList<>();
    private static final List<Genre> genres = new ArrayList<>();
    private static final List<Liburu> books = new ArrayList<>();
    private static final List<IdazleData> authorData = new ArrayList<>();
    private static final AtomicInteger writerIdCounter = new AtomicInteger(4); // Start from 4 since we have 3 initial writers
    private static final AtomicInteger bookIdCounter = new AtomicInteger(7); // Start from 7 since we have 6 initial books
    
    static {
        // Initialize with some sample data
        writers.add(new Writer(1, "Gabriel García Márquez"));
        writers.add(new Writer(2, "Jorge Luis Borges"));
        writers.add(new Writer(3, "Pablo Neruda"));
        
        genres.add(new Genre(1, "Ficción"));
        genres.add(new Genre(2, "Poesía"));
        genres.add(new Genre(3, "Ensayo"));
        genres.add(new Genre(4, "Novela"));
        genres.add(new Genre(5, "Cuento"));
        
        // Sample books
        books.add(new Liburu(1, "Cien años de soledad", "Gabriel García Márquez", Arrays.asList("Ficción", "Novela")));
        books.add(new Liburu(2, "El amor en los tiempos del cólera", "Gabriel García Márquez", Arrays.asList("Ficción", "Novela")));
        books.add(new Liburu(3, "Ficciones", "Jorge Luis Borges", Arrays.asList("Ficción", "Cuento")));
        books.add(new Liburu(4, "El Aleph", "Jorge Luis Borges", Arrays.asList("Ficción", "Cuento")));
        books.add(new Liburu(5, "Veinte poemas de amor", "Pablo Neruda", Arrays.asList("Poesía")));
        books.add(new Liburu(6, "Canto general", "Pablo Neruda", Arrays.asList("Poesía")));
        
        // Author data with their books
        authorData.add(new IdazleData(1, "Gabriel García Márquez", 
            Arrays.asList(books.get(0), books.get(1))));
        authorData.add(new IdazleData(2, "Jorge Luis Borges", 
            Arrays.asList(books.get(2), books.get(3))));
        authorData.add(new IdazleData(3, "Pablo Neruda", 
            Arrays.asList(books.get(4), books.get(5))));
    }
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Test server started on port " + PORT);
            System.out.println("Waiting for client connections...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                // Handle client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
            
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String request = in.readLine();
            System.out.println("Received request: " + request);
            
            String response = processRequest(request);
            System.out.println("Sending response: " + response);
            
            out.println(response);
            
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
    
    private static String processRequest(String request) {
        if (request == null || request.isEmpty()) {
            return "ERROR: Empty request";
        }
        
        String[] parts = request.split(":");
        String command = parts[0];
        
        switch (command) {
            case "GET_AUTHORS":
                return gson.toJson(writers);
                
            case "GET_GENRES":
                return gson.toJson(genres);
                
            case "GET_BOOKS":
                return gson.toJson(books);
                
            case "GET_AUTHOR_DATA":
                return gson.toJson(authorData);
                
            case "INSERT_WRITER":
                if (parts.length > 1) {
                    String writerName = parts[1];
                    int newId = writerIdCounter.getAndIncrement();
                    Writer newWriter = new Writer(newId, writerName);
                    writers.add(newWriter);
                    
                    // Also add to authorData with empty book list
                    IdazleData newAuthorData = new IdazleData(newId, writerName, new ArrayList<>());
                    authorData.add(newAuthorData);
                    
                    System.out.println("Writer inserted: " + writerName + " with ID: " + newId);
                    return "SUCCESS:" + newId;
                }
                return "ERROR: Missing writer name";
                
            case "INSERT_BOOK":
                if (parts.length > 2) {
                    String bookTitle = parts[1];
                    int authorId = Integer.parseInt(parts[2]);
                    
                    // Get genres if provided
                    List<String> bookGenres = new ArrayList<>();
                    if (parts.length > 3 && !parts[3].isEmpty()) {
                        String[] genreArray = parts[3].split(",");
                        for (String genre : genreArray) {
                            if (!genre.trim().isEmpty()) {
                                bookGenres.add(genre.trim());
                            }
                        }
                    }
                    
                    // Find the author
                    Writer author = null;
                    for (Writer w : writers) {
                        if (w.getId() == authorId) {
                            author = w;
                            break;
                        }
                    }
                    
                    if (author != null) {
                        // Create new book with genres
                        int newBookId = bookIdCounter.getAndIncrement();
                        Liburu newBook = new Liburu(newBookId, bookTitle, author.getIzena(), bookGenres);
                        books.add(newBook);
                        
                        // Add book to author's data
                        for (IdazleData authorDataItem : authorData) {
                            if (authorDataItem.getId() == authorId) {
                                authorDataItem.getLiburuak().add(newBook);
                                break;
                            }
                        }
                        
                        System.out.println("Book inserted: " + bookTitle + " by " + author.getIzena() + " (ID: " + newBookId + ") with genres: " + bookGenres);
                        return "SUCCESS";
                    } else {
                        return "ERROR: Author not found with ID: " + authorId;
                    }
                }
                return "ERROR: Missing book information";
                
            case "INSERT_BOOK_GENRE":
                if (parts.length > 1) {
                    String bookGenreInfo = parts[1];
                    // In a real implementation, you would save this to a database
                    System.out.println("Book-genre relationship inserted: " + bookGenreInfo);
                    return "SUCCESS";
                }
                return "ERROR: Missing book-genre information";
                
            case "INSERT_GENRE":
                if (parts.length > 1) {
                    String genreName = parts[1];
                    int newGenreId = genres.size() + 1; // Simple ID generation
                    Genre newGenre = new Genre(newGenreId, genreName);
                    genres.add(newGenre);
                    
                    System.out.println("Genre inserted: " + genreName + " with ID: " + newGenreId);
                    return "SUCCESS:" + newGenreId;
                }
                return "ERROR: Missing genre name";
                
            default:
                return "ERROR: Unknown command: " + command;
        }
    }
} 
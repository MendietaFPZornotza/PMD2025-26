package conexionbd;

import model.Liburu;
import model.IdazleData;
import model.Writer;
import model.Genre;
import java.util.Arrays;
import java.util.List;

/**
 * Simple test class to verify socket communication
 */
public class SocketTest {
    public static void main(String[] args) {
        System.out.println("Testing Socket Communication...");
        
        SocketClient client = new SocketClient();
        
        // Test getting authors
        System.out.println("\n=== Testing Authors ===");
        var authors = client.getAuthors();
        System.out.println("Authors loaded: " + authors.size());
        for (Writer author : authors) {
            System.out.println("- " + author.getIzena());
        }
        
        // Test getting genres
        System.out.println("\n=== Testing Genres ===");
        var genres = client.getGenre();
        System.out.println("Genres loaded: " + genres.size());
        for (Genre genre : genres) {
            System.out.println("- " + genre.getGeneroa());
        }
        
        // Test getting books
        System.out.println("\n=== Testing Books ===");
        var books = client.getBooks();
        System.out.println("Books loaded: " + books.size());
        for (Liburu book : books) {
            System.out.println("- " + book.getIzenburua() + " by " + book.getIdazlea());
        }
        
        // Test getting author data
        System.out.println("\n=== Testing Author Data ===");
        var authorData = client.getAuthorData();
        System.out.println("Author data loaded: " + authorData.size());
        for (IdazleData author : authorData) {
            System.out.println("- " + author.getIzena() + " has " + author.getLiburuak().size() + " books");
        }
        
        // Test inserting new data
        System.out.println("\n=== Testing Data Insertion ===");
        
        // Insert a new writer
        System.out.println("Inserting new writer: Mario Vargas Llosa");
        int newWriterId = client.insertWriter("Mario Vargas Llosa");
        System.out.println("New writer ID: " + newWriterId);
        
        // Insert a new book for the new writer with genres
        System.out.println("Inserting new book: La ciudad y los perros with genres");
        List<String> bookGenres = Arrays.asList("Ficción", "Novela");
        client.insertBook("La ciudad y los perros", newWriterId, bookGenres);
        
        // Insert a new genre
        System.out.println("Inserting new genre: Realismo mágico");
        int newGenreId = client.insertGenre("Realismo mágico");
        System.out.println("New genre ID: " + newGenreId);
        
        // Test getting updated data
        System.out.println("\n=== Testing Updated Data ===");
        
        // Get updated authors
        var updatedAuthors = client.getAuthors();
        System.out.println("Updated authors count: " + updatedAuthors.size());
        for (Writer author : updatedAuthors) {
            System.out.println("- " + author.getIzena());
        }
        
        // Get updated books
        var updatedBooks = client.getBooks();
        System.out.println("Updated books count: " + updatedBooks.size());
        for (Liburu book : updatedBooks) {
            System.out.println("- " + book.getIzenburua() + " by " + book.getIdazlea());
        }
        
        // Get updated genres
        var updatedGenres = client.getGenre();
        System.out.println("Updated genres count: " + updatedGenres.size());
        for (Genre genre : updatedGenres) {
            System.out.println("- " + genre.getGeneroa());
        }
        
        // Get updated author data
        var updatedAuthorData = client.getAuthorData();
        System.out.println("Updated author data count: " + updatedAuthorData.size());
        for (IdazleData author : updatedAuthorData) {
            System.out.println("- " + author.getIzena() + " has " + author.getLiburuak().size() + " books");
        }
        
        System.out.println("\nSocket communication test completed!");
    }
}

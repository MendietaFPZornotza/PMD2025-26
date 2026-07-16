# Multi Interface Library Management System

This project has been updated to use socket-based communication instead of direct database connections. The client application now communicates with a server through TCP sockets, sending requests and receiving JSON responses.

## Architecture Changes

### Before (Database Direct Connection)
- Direct MySQL database connection
- SQL queries executed locally
- Database credentials stored in application

### After (Socket Communication)
- TCP socket communication with server
- JSON-based request/response protocol
- Server handles database operations
- Client only needs network connectivity

## Components

### Client Side (`SocketClient.java`)
- Handles communication with the server
- Sends formatted requests
- Processes JSON responses
- Maintains the same API as the old database connection

### Server Side (`TestServer.java`)
- Simple test server implementation
- Handles client requests
- Returns JSON responses
- Simulates database operations

## Request/Response Protocol

### Request Format
Requests are sent as colon-separated strings:
- `GET_AUTHORS` - Retrieve all authors
- `GET_GENRES` - Retrieve all genres
- `GET_BOOKS` - Retrieve all books
- `GET_AUTHOR_DATA` - Retrieve authors with their books
- `INSERT_WRITER:author_name` - Insert new writer
- `INSERT_BOOK:title:author_id:genres` - Insert new book with genres (genres as comma-separated string)
- `INSERT_GENRE:genre_name` - Insert new genre
- `INSERT_BOOK_GENRE:relationship_data` - Insert book-genre relationship

### Response Format
- **JSON arrays** for data retrieval (authors, genres)
- **SUCCESS** for successful operations
- **SUCCESS:generated_id** for insert operations that return IDs
- **ERROR:message** for failed operations

## How to Run

### 1. Start the Test Server
```bash
# Compile the project
mvn compile

# Run the test server
java -cp target/classes conexionbd.TestServer
```

The server will start on port 8080 and display:
```
Test server started on port 8080
Waiting for client connections...
```

### 2. Run the Client Application
```bash
# Run the JavaFX application
mvn javafx:run
```

## Configuration

### Server Connection Settings
The client connects to the server using these default settings (configurable in `SocketClient.java`):
- **Host**: localhost
- **Port**: 8080

### Sample Data
The test server includes sample data:
- **Authors**: Gabriel García Márquez, Jorge Luis Borges, Pablo Neruda
- **Genres**: Ficción, Poesía, Ensayo, Novela, Cuento
- **Books**: Cien años de soledad, El amor en los tiempos del cólera, Ficciones, El Aleph, Veinte poemas de amor, Canto general
- **Author-Book Relationships**: Each author is associated with their respective books

## State Persistence

The server maintains state across requests:
- **Global Lists**: All data is stored in global lists that persist during the server's lifetime
- **Real-time Updates**: When new data is inserted, it's immediately available in subsequent requests
- **Consistent State**: All related data (authors, books, genres) is kept synchronized
- **ID Generation**: Automatic ID generation ensures unique identifiers for new records

## Genre Management

The system now supports full genre management:
- **Genre Selection**: Users can select multiple genres when creating books
- **Genre Storage**: Selected genres are stored with each book
- **Genre Display**: Books display their associated genres in the interface
- **Genre Validation**: Users must select at least one genre when creating a book
- **Genre Persistence**: Genres are maintained in the server's global state

## Benefits of Socket Communication

1. **Separation of Concerns**: Database logic is isolated on the server
2. **Security**: Database credentials are not exposed to clients
3. **Scalability**: Multiple clients can connect to the same server
4. **Flexibility**: Server can be implemented in any language
5. **Network Distribution**: Client and server can run on different machines
6. **State Persistence**: Server maintains data state across multiple client connections

## Production Considerations

For production use, consider:
- Implementing proper authentication
- Using SSL/TLS for encrypted communication
- Adding connection pooling
- Implementing proper error handling and retry logic
- Using a more robust server framework (Spring, Netty, etc.)
- Adding request/response validation
- Implementing logging and monitoring

## Migration Notes

The application maintains the same user interface and functionality. The only changes are:
- Removed MySQL dependency from `pom.xml`
- Removed `java.sql` module requirement
- Replaced `ConexionMysql` with `SocketClient`
- Updated `ManagementController` to use the new client
- Deleted old database connection files

All existing features continue to work as before, but now through socket communication instead of direct database access. 
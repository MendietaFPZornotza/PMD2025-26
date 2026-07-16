
package model;


public class Book {
     private int id;
     private String izenburua;
     private int  idazlea_id;

    public Book(int id, String izenburua) {
        this.id = id;
        this.izenburua = izenburua;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzenburua() {
        return izenburua;
    }

    public void setIzenburua(String izenburua) {
        this.izenburua = izenburua;
    }

    public int getIdazlea_id() {
        return idazlea_id;
    }

    public void setIdazlea_id(int idazlea_id) {
        this.idazlea_id = idazlea_id;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", izenburua=" + izenburua + ", idazlea_id=" + idazlea_id + '}';
    }
     
     
     
     
}


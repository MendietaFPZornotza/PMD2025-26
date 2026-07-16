
package model;


public class Writer {
     private int id;
     private String izena;

    public Writer(int id, String izena) {
        this.id = id;
        this.izena = izena;
    }

    public Writer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    @Override
    public String toString() {
        return "Writer{" + "id=" + id + ", izena=" + izena + '}';
    }
    

}

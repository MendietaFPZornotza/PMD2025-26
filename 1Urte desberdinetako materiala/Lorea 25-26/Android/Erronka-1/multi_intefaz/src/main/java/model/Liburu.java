
package model;

import java.util.List;


public class Liburu {
    private int id;
    private String izenburua;
    private String idazle_izena;
    private List<String> generoak;

    public Liburu(int id, String izenburua, String idazle_izena, List<String> generoak) {
        this.id = id;
        this.izenburua = izenburua;
        this.idazle_izena = idazle_izena;
        this.generoak = generoak;
    }

    public Liburu(int id, String izenburua,List<String> generoak) {
        this.id = id;
        this.izenburua = izenburua;
        this.generoak = generoak;
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

    public String getIdazlea() {
        return idazle_izena;
    }

    public void setIdazlea(String idazlea) {
        this.idazle_izena = idazlea;
    }

    public List<String> getGeneroak() {
        return generoak;
    }

    public void setGeneroak(List<String> generoak) {
        this.generoak = generoak;
    }
    
    // Devuelve la lista de géneros en formato cadena
    public String getGeneroakAsString() {
        return String.join(", ", generoak);
    }
  
    // Opcional: para mostrar el título en el TableView o ComboBox
    @Override
    public String toString() {
        return "LiburuDatu{" +
           "id=" + id +
           ", izenburua='" + izenburua + '\'' +
           ", generoak=" + generoak +
           ", idazle_izena='" + idazle_izena + '\'' +
           '}';
    }
}

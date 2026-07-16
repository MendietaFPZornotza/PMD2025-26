
package model;

import java.util.List;


public class IdazleData {
    private int id;
    private String izena;
    //private String idazle_izena;
    private List<Liburu> liburuak;

    public IdazleData(int id, String izena,  List<Liburu> liburuak) {
        this.id = id;
        this.izena = izena;
       // this.idazle_izena = idazle_izena;
        this.liburuak = liburuak;
    }

//    public IdazleData() {
//    }

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

//    public String getIdazle_izena() {
//        return idazle_izena;
//    }
//
//    public void setIdazle_izena(String idazle_izena) {
//        this.idazle_izena = idazle_izena;
//    }

    public List<Liburu> getLiburuak() {
        return liburuak;
    }

    public void setLiburuak(List<Liburu> liburuak) {
        this.liburuak = liburuak;
    }

    
    // Opcional: para mostrar el nombre en el ComboBox
    @Override
    public String toString() {
        return izena ;
    }
    
}

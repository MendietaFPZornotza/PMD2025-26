
package model;


public class Genre {
    private int liburua_id;
     private String generoa;

    public Genre(int liburua_id, String generoa) {
        this.liburua_id = liburua_id;
        this.generoa = generoa;
    }

    public Genre() {
    }

    public int getLiburua_id() {
        return liburua_id;
    }

    public void setLiburua_id(int liburua_id) {
        this.liburua_id = liburua_id;
    }

    public String getGeneroa() {
        return generoa;
    }

    public void setGeneroa(String generoa) {
        this.generoa = generoa;
    }

    @Override
    public String toString() {
        return "Genre{" + "liburua_id=" + liburua_id + ", generoa=" + generoa + '}';
    }

     
     
}

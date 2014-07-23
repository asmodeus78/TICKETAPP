package it.ticketclub.ticketapp;

/**
 * Created by Gio on 15/04/2014.
 */
public class Ticket {
    private Integer id;
    private String codice;
    private String categoria;
    private String titolo;
    private String titoloSup;
    private String foto;
    private String lat;
    private String lon;

    private Integer scaricati;
    private Float mediaVoti;


    public Ticket(Integer id, String categoria, String codice, String titolo, String titoloSup, String foto, Integer scaricati, Float mediaVoti, String lat, String lon){
        this.id = id;
        this.categoria = categoria;
        this.codice = codice;
        this.titolo = titolo;
        this.titoloSup = titoloSup;
        this.foto = foto;
        this.scaricati = scaricati;
        this.mediaVoti = mediaVoti;
        this.lat = lat;
        this.lon = lon;
    }

    public Integer getId(){
        return id;
    }
    public String getCategoria(){
        return categoria;
    }
    public String getCodice(){
        return codice;
    }
    public String getTitolo(){
        return titolo;
    }
    public String getTitoloSup(){
        return titoloSup;
    }
    public String getFoto(){
        return foto;
    }
    public Integer getScaricati(){return scaricati; }
    public Float getMediaVoti(){return mediaVoti; }

    public void setId(Integer id){
        this.id = id;
    }
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    public void setCodice(String codice){
        this.codice = codice;
    }
    public void setTitolo(String titolo){
        this.titolo = titolo;
    }
    public void setTitoloSup(String titoloSup) {
        this.titoloSup = titoloSup;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public void setScaricati(Integer scaricati){this.scaricati = scaricati; }
    public void setMediaVoti(Float mediaVoti){this.mediaVoti = mediaVoti; }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}

package it.ticketclub.ticketapp;

/**
 * Created by Gio on 15/04/2014.
 */
public class Ticket {
    private Integer id;
    private String titolo;
    private String titoloSup;
    private String foto;


    public Ticket(Integer id, String titolo, String titoloSup, String foto){
        this.id = id;
        this.titolo = titolo;
        this.titoloSup = titoloSup;
        this.foto = foto;
    }

    public Integer getId(){
        return id;
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

    public void setCodice(Integer id){
        this.id = id;
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

}

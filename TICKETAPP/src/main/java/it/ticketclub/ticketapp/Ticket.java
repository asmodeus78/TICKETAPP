package it.ticketclub.ticketapp;

/**
 * Created by Gio on 15/04/2014.
 */
public class Ticket {
    private String codice;
    private String titolo;
    private String foto;


    public Ticket(String codice, String titolo, String foto){
        this.codice = codice;
        this.titolo = titolo;
        this.foto = foto;
    }

    public String getCodice(){
        return codice;
    }
    public String getTitolo(){
        return titolo;
    }
    public String getFoto(){
        return foto;
    }

    public void setCodice(String codice){
        this.codice = codice;
    }
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

}

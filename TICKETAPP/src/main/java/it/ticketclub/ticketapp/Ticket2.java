package it.ticketclub.ticketapp;

/**
 * Created by Gio on 15/04/2014.
 */
public class Ticket2 {
    private String id;
    private String codice;
    private String cWeb;
    private String qta;
    private String titoloSup;
    private String foto;
    private String usato;
    private String feedback;
    private String dataDownload;

    public Ticket2(String id, String codice, String cWeb, String qta, String titoloSup, String foto, String usato, String feedback,String dataDownload){
        this.id = id;
        this.codice = codice;
        this.cWeb = cWeb;
        this.foto = foto;
        this.titoloSup = titoloSup;
        this.usato = usato;
        this.feedback = feedback;
        this.dataDownload = dataDownload;
        this.qta=qta;

    }

    public String getId(){
        return id;
    }
    public String getCodice(){
        return codice;
    }
    public String getcWeb(){
        return cWeb;
    }
    public String getUsato(){
        return usato;
    }
    public String getTitoloSup(){
        return titoloSup;
    }
    public String getFoto(){
        return foto;
    }
    public String getFeedback(){return feedback; }
    public String getDataDownload(){return dataDownload; }
    public String getQta(){return qta; }

    public void setId(String id){
        this.id = id;
    }
    public void setCweb(String categoria){
        this.cWeb = cWeb;
    }
    public void setCodice(String codice){
        this.codice = codice;
    }
    public void setUsato(String usato){
        this.usato = usato;
    }
    public void setTitoloSup(String titoloSup) {
        this.titoloSup = titoloSup;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }
    public void setFeedback(String feedback){this.feedback = feedback; }
    public void setDataDownload(String dataDownload){this.dataDownload = dataDownload; }
    public void setQta(String qta){this.dataDownload = qta; }


}

package it.ticketclub.ticketapp;

/**
 * Created by Gio on 15/05/2014.
 */
public class Feedback {

    private Integer id;
    private String dataInserimento;
    private String idImg;
    private String nominativo;
    private String voto;
    private String commento;

    public Feedback(Integer id, String dataInserimento, String idImg, String nominativo, String voto, String commento){
        this.id = id;
        this.dataInserimento = dataInserimento;
        this.idImg = idImg;
        this.nominativo = nominativo;
        this.voto = voto;
        this.commento = commento;
    }

    public Integer getId(){
        return id;
    }
    public String getDataInserimento(){
        return dataInserimento;
    }
    public String getIdImg(){
        return idImg;
    }
    public String getNominativo(){
        return nominativo;
    }
    public String getVoto(){
        return voto;
    }
    public String getCommento(){
        return commento;
    }


    public void setId(Integer id){
        this.id = id;
    }
    public void setDataInserimento(String dataInserimento){
        this.dataInserimento = dataInserimento;
    }
    public void setIdImg(String idImg){
        this.idImg = idImg;
    }
    public void setNominativo(String nominativo){
        this.nominativo = nominativo;
    }
    public void setVoto(String voto) {
        this.voto = voto;
    }
    public void setCommento(String commento) {
        this.commento = commento;
    }



}

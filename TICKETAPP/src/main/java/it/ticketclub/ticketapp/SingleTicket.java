package it.ticketclub.ticketapp;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;


public class SingleTicket extends Activity {

    private String idTicket;
    private String descrizione;
    private String codice;
    private String crediti;
    private String titolo;

    public String getSecondoTitolo() {
        return SecondoTitolo;
    }

    public void setSecondoTitolo(String secondoTitolo) {
        SecondoTitolo = secondoTitolo;
    }

    private String SecondoTitolo;

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }


/*
    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }
    */
    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCrediti() {
        return crediti;
    }

    public void setCrediti(String crediti) {
        this.crediti = crediti;
    }

    public SingleTicket(String idTicket,Context ctx) {
        this.idTicket = idTicket;

        MyDatabase db2=new MyDatabase(ctx);
        db2.open();  //apriamo il db
        Cursor c2;
        c2 = db2.fetchSingleTicket(idTicket);

        this.startManagingCursor(c2);

        if (c2.moveToFirst()) {
            do {
                setTitolo(c2.getString(3));
                setSecondoTitolo(c2.getString(2));
                setCodice(c2.getString(1));
                setCrediti(c2.getString(15));
                setDescrizione(c2.getString(7));

            } while (c2.moveToNext());
        }








    }


}

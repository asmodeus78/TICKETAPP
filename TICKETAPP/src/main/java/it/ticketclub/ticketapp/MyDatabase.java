package it.ticketclub.ticketapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gio on 09/05/2014.
 */
public class MyDatabase {

    SQLiteDatabase db;

    DbHelper myDbHelper;
    Context mContext;



    private static final String DB_NAME="ticketclub.db";//nome del db
    private static final int DB_VERSION=1; //numero di versione del nostro db

    public MyDatabase(Context ctx){
        mContext = ctx;
        myDbHelper = new DbHelper(ctx,DB_NAME,null,DB_VERSION); //quando istanziamo questa classe, istanziamo anche l'helper (vedi sotto)
    }

    public void open(){  //il database su cui agiamo è leggibile/scrivibile
        db=myDbHelper.getWritableDatabase();
        //Setup conf = new Setup();
        //db = SQLiteDatabase.openOrCreateDatabase(conf.path + DB_NAME,null);
    }

    public void close(){ //chiudiamo il database su cui agiamo
        db.close();
    }

    public void insertTicket(int id, String categoria, String codice, String titolo, String titoloSup, float mediaVoti, int scaricati){ //metodo per inserire i dati
        ContentValues cv=new ContentValues();
        cv.put(TicketMetaData.ID, id);
        cv.put(TicketMetaData.TICKET_CATEGORIA_KEY , categoria);
        cv.put(TicketMetaData.TICKET_CODICE_KEY , codice);
        cv.put(TicketMetaData.TICKET_TITOLO_KEY , titolo);
        cv.put(TicketMetaData.TICKET_TITOLO_SUP_KEY , titoloSup);
        cv.put(TicketMetaData.TICKET_MEDIA_VOTI_KEY , mediaVoti);
        cv.put(TicketMetaData.TICKET_SCARICATI_KEY , scaricati);

        db.insert(TicketMetaData.TICKET_TABLE, null, cv);
    }

    public Cursor fetchTicket(){ //metodo per fare la query di tutti i dati
        return db.query(TicketMetaData.TICKET_TABLE, null,null,null,null,null,null);
    }

    public Cursor fetchTicketByCateg(String categoria){ //metodo per fare la query di tutti i dati
        return db.query(TicketMetaData.TICKET_TABLE, null,"categoria='" + categoria + "'",null,null,null,null);
    }

    public void deleteTicket(){
        db.delete(TicketMetaData.TICKET_TABLE,null,null);
    }

    static class TicketMetaData {  // i metadati della tabella, accessibili ovunque
        static final String TICKET_TABLE = "TICKET";
        static final String ID = "idTicket";
        static final String TICKET_CODICE_KEY = "codice";
        static final String TICKET_TITOLO_KEY  = "titolo";
        static final String TICKET_TITOLO_SUP_KEY  = "titoloSup";
        static final String TICKET_DESCRIZIONE_KEY  = "descrizione";
        static final String TICKET_INDIRIZZO_KEY  = "indirizzo";
        static final String TICKET_LAT_KEY  = "lat";
        static final String TICKET_LON_KEY  = "lon";
        static final String TICKET_ORDINE_KEY  = "ordine";
        static final String TICKET_PREZZO_KEY  = "prezzo";
        static final String TICKET_PREZZO_CR_KEY  = "prezzoCR";
        static final String TICKET_CATEGORIA_KEY  = "categoria";
        static final String TICKET_SCARICATI_KEY  = "scaricati";
        static final String TICKET_MEDIA_VOTI_KEY  = "mediaVoti";
        static final String TICKET_NOMINATIVO_KEY  = "nominativo";
        static final String TICKET_TELEFONO_KEY  = "telefono";
        static final String TICKET_SITO_KEY  = "sito";
    }

    private static final String TICKET_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TicketMetaData.TICKET_TABLE + " ("
            + TicketMetaData.ID + " integer primary key, "
            + TicketMetaData.TICKET_CODICE_KEY + " text not null, "
            + TicketMetaData.TICKET_TITOLO_KEY + " text not null, "
            + TicketMetaData.TICKET_TITOLO_SUP_KEY + " text not null, "
            //+ TicketMetaData.TICKET_DESCRIZIONE_KEY + " text not null, "
            //+ TicketMetaData.TICKET_INDIRIZZO_KEY + " text null, "
            //+ TicketMetaData.TICKET_LAT_KEY + " text null, "
            //+ TicketMetaData.TICKET_LON_KEY + " text null, "
            //+ TicketMetaData.TICKET_PREZZO_KEY + " money null, "
            //+ TicketMetaData.TICKET_PREZZO_CR_KEY + " integer not null, "
            + TicketMetaData.TICKET_CATEGORIA_KEY + " text not null, "
            + TicketMetaData.TICKET_SCARICATI_KEY + " integer not null, "
            + TicketMetaData.TICKET_MEDIA_VOTI_KEY + " float null "
            //+ TicketMetaData.TICKET_NOMINATIVO_KEY + " text not null, "
            //+ TicketMetaData.TICKET_TELEFONO_KEY + " text null, "
            //+ TicketMetaData.TICKET_SITO_KEY + " text null, )";
            //+ TicketMetaData.TICKET_ORDINE_KEY + " integer not null, "
            + ")";

    private class DbHelper extends SQLiteOpenHelper { //classe che ci aiuta nella creazione del db

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) { //solo quando il db viene creato, creiamo la tabella
            _db.execSQL(TICKET_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            //qui mettiamo eventuali modifiche al db, se nella nostra nuova versione della app, il db cambia numero di versione

        }

    }

}

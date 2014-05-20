package it.ticketclub.ticketapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;


public class MyDatabase {

    SQLiteDatabase db;

    DbHelper myDbHelper;
    Context mContext;



    private static final String DB_NAME="ticketclub.db";//nome del db
    private static final int DB_VERSION=3; //numero di versione del nostro db

    // GESTIONE DATABASE
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
    // GESTIONE DATABASE FINE


    //DESCRITTORI STRUTTURA DATI
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
        static final String TICKET_CATEGORIA_KEY  = "categoria";
        static final String TICKET_SCARICATI_KEY  = "scaricati";
        static final String TICKET_MEDIA_VOTI_KEY  = "mediaVoti";
        static final String TICKET_NOMINATIVO_KEY  = "nominativo";
        //static final String TICKET_ORDINE_KEY  = "ordine";
        //static final String TICKET_PREZZO_KEY  = "prezzo";
        // static final String TICKET_PREZZO_CR_KEY  = "prezzoCR";
        static final String TICKET_TELEFONO_KEY  = "telefono";
        static final String TICKET_SITO_KEY  = "sito";
        static final String TICKET_DATA_SCADENZA_KEY = "dataScadenza";
    }
    static class FeedBackMetaData {  // i metadati della tabella, accessibili ovunque
        static final String FEEDBACK_TABLE = "FEEDBACK";
        static final String ID = "idFeedback";
        static final String FEEDBACK_IDTICKET_KEY  = "idTicket";
        static final String FEEDBACK_DATAINSERIMENTO_KEY  = "dataInserimento";
        static final String FEEDBACK_IDIMG_KEY = "idImg";
        static final String FEEDBACK_NOMINATIVO_KEY = "nominativo";
        static final String FEEDBACK_VOTO_KEY  = "voto";
        static final String FEEDBACK_COMMENTO_KEY  = "commento";
    }
    static class ConfigMetaData {
        static final String CONFIG_TABLE = "CONFIG";
        static final String CONFIG_UPDATE_KEY = "lastUpdate";
        static final String CONFIG_IDUTENTE_KEY = "idUtente";
        static final String CONFIG_NOME_UTENTE_KEY = "nominativo";
        static final String CONFIG_EMAIL_KEY = "email";
        static final String CONFIG_CREDITI_KEY = "crediti";
        static final String CONFIG_ABBONAMENTO_KEY = "abbonamento";
    }

    //COMMAND FOR INSERT DATA
    public void insertTicket(int id, String categoria, String codice, String titolo, String titoloSup, float mediaVoti, int scaricati, String descrizione, String indirizzo, String lat, String lon, String nominativo, String telefono, String sito, String dataScadenza){ //metodo per inserire i dati
        ContentValues cv=new ContentValues();
        cv.put(TicketMetaData.ID, id);
        cv.put(TicketMetaData.TICKET_CATEGORIA_KEY , categoria);
        cv.put(TicketMetaData.TICKET_CODICE_KEY , codice);
        cv.put(TicketMetaData.TICKET_TITOLO_KEY , titolo);
        cv.put(TicketMetaData.TICKET_TITOLO_SUP_KEY , titoloSup);
        cv.put(TicketMetaData.TICKET_MEDIA_VOTI_KEY , mediaVoti);
        cv.put(TicketMetaData.TICKET_SCARICATI_KEY , scaricati);
        cv.put(TicketMetaData.TICKET_DESCRIZIONE_KEY, descrizione);

        cv.put(TicketMetaData.TICKET_INDIRIZZO_KEY, indirizzo);
        cv.put(TicketMetaData.TICKET_LAT_KEY, lat);
        cv.put(TicketMetaData.TICKET_LON_KEY, lon);

        cv.put(TicketMetaData.TICKET_NOMINATIVO_KEY, nominativo);
        cv.put(TicketMetaData.TICKET_TELEFONO_KEY, telefono);
        cv.put(TicketMetaData.TICKET_SITO_KEY, sito);

        cv.put(TicketMetaData.TICKET_DATA_SCADENZA_KEY, dataScadenza);

        db.insert(TicketMetaData.TICKET_TABLE, null, cv);

    }
    public void insertFeedback(int id, String idTicket, String dataInserimento, String idImg, String nominativo, String voto, String commento){
        ContentValues cv=new ContentValues();
        cv.put(FeedBackMetaData.ID, id);
        cv.put(FeedBackMetaData.FEEDBACK_IDTICKET_KEY , idTicket);
        cv.put(FeedBackMetaData.FEEDBACK_DATAINSERIMENTO_KEY , dataInserimento);
        cv.put(FeedBackMetaData.FEEDBACK_IDIMG_KEY , idImg);
        cv.put(FeedBackMetaData.FEEDBACK_NOMINATIVO_KEY , nominativo);
        cv.put(FeedBackMetaData.FEEDBACK_VOTO_KEY , voto);
        cv.put(FeedBackMetaData.FEEDBACK_COMMENTO_KEY, commento);

        db.insert(FeedBackMetaData.FEEDBACK_TABLE, null, cv);
    }

    public void insertConfig(String update, int idUtente, String nominativo, String email,String crediti, String abbonamento){
        ContentValues cv=new ContentValues();

        cv.put(ConfigMetaData.CONFIG_UPDATE_KEY, update);
        cv.put(ConfigMetaData.CONFIG_IDUTENTE_KEY, idUtente);
        cv.put(ConfigMetaData.CONFIG_NOME_UTENTE_KEY, nominativo);
        cv.put(ConfigMetaData.CONFIG_EMAIL_KEY, email);
        cv.put(ConfigMetaData.CONFIG_CREDITI_KEY, crediti);
        cv.put(ConfigMetaData.CONFIG_ABBONAMENTO_KEY, abbonamento);

        db.insert(ConfigMetaData.CONFIG_TABLE, null, cv);
    }

    public void updateLastDownload(String data){
        String sql ="update " + ConfigMetaData.CONFIG_TABLE + " set " + ConfigMetaData.CONFIG_UPDATE_KEY + "='" + data + "'";
        db.execSQL(sql);
    }




    //COMMAND FOR FETCH DATA
    public Cursor fetchTicket(){ //metodo per fare la query di tutti i dati
        return db.query(TicketMetaData.TICKET_TABLE, null,null,null,null,null,null);
    }
    public Cursor fetchSingleTicket(String id){ //metodo per fare la query di tutti i dati
        return db.query(TicketMetaData.TICKET_TABLE, null,"idTicket=" + id,null,null,null,null);
    }
    public Cursor fetchTicketByCateg(String categoria){ //metodo per fare la query di tutti i dati
        return db.query(TicketMetaData.TICKET_TABLE, null,"categoria='" + categoria + "'",null,null,null,null);
    }
    public Cursor fetchFeedback(String id){
        return db.query(FeedBackMetaData.FEEDBACK_TABLE, null,"idTicket=" + id,null,null,null,null);
    }
    public Cursor fetchConfig(){ //metodo per fare la query di tutti i dati
        return db.query(ConfigMetaData.CONFIG_TABLE, null,null,null,null,null,null);
    }

    //COMMAND FOR DELETE DATA
    public void deleteTicket(){
        db.delete(TicketMetaData.TICKET_TABLE,null,null);
    }
    public void deleteTicketByData(String dataScadenza){

        String sql = "delete from " + TicketMetaData.TICKET_TABLE + " where dataScadenza<'" + dataScadenza + "'";
        db.execSQL(sql);
        Log.d("TICKET:",sql);

    }
    public void deleteFeedback(){
        db.delete(FeedBackMetaData.FEEDBACK_TABLE,null,null);
    }
    public void deleteConfig(){
        db.delete(ConfigMetaData.CONFIG_TABLE,null,null);
    }


    //COMMAND FOR CREATE TABLE
    private static final String TICKET_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TicketMetaData.TICKET_TABLE + " ("
            + TicketMetaData.ID + " integer primary key, "
            + TicketMetaData.TICKET_CODICE_KEY + " text not null, "
            + TicketMetaData.TICKET_TITOLO_KEY + " text not null, "
            + TicketMetaData.TICKET_TITOLO_SUP_KEY + " text not null, "
            + TicketMetaData.TICKET_CATEGORIA_KEY + " text not null, "
            + TicketMetaData.TICKET_SCARICATI_KEY + " integer not null, "
            + TicketMetaData.TICKET_MEDIA_VOTI_KEY + " float null, "
            + TicketMetaData.TICKET_DESCRIZIONE_KEY + " text not null, "
            + TicketMetaData.TICKET_INDIRIZZO_KEY + " text null, "
            + TicketMetaData.TICKET_LAT_KEY + " text null, "
            + TicketMetaData.TICKET_LON_KEY + " text null, "
            + TicketMetaData.TICKET_NOMINATIVO_KEY + " text not null, "
            + TicketMetaData.TICKET_TELEFONO_KEY + " text null, "
            + TicketMetaData.TICKET_SITO_KEY + " text null, "
            + TicketMetaData.TICKET_DATA_SCADENZA_KEY + " text null "
            //+ TicketMetaData.TICKET_PREZZO_KEY + " money null, "
            //+ TicketMetaData.TICKET_PREZZO_CR_KEY + " integer not null, "
            //+ TicketMetaData.TICKET_ORDINE_KEY + " integer not null, "
    + ")";




    private static final String FEEDBACK_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + FeedBackMetaData.FEEDBACK_TABLE + " ("
            + FeedBackMetaData.ID + " integer primary key, "
            + FeedBackMetaData.FEEDBACK_IDTICKET_KEY + " text not null, "
            + FeedBackMetaData.FEEDBACK_DATAINSERIMENTO_KEY + " text not null, "
            + FeedBackMetaData.FEEDBACK_IDIMG_KEY + " text null, "
            + FeedBackMetaData.FEEDBACK_NOMINATIVO_KEY + " text not null, "
            + FeedBackMetaData.FEEDBACK_VOTO_KEY + " text not null, "
            + FeedBackMetaData.FEEDBACK_COMMENTO_KEY + " text null "
            + ")";

    private static final String CONFIG_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ConfigMetaData.CONFIG_TABLE + " ("
            + ConfigMetaData.CONFIG_IDUTENTE_KEY + " integer not null default 0, "
            + ConfigMetaData.CONFIG_NOME_UTENTE_KEY + " text null, "
            + ConfigMetaData.CONFIG_EMAIL_KEY + " text null, "
            + ConfigMetaData.CONFIG_CREDITI_KEY + " int not null default 0, "
            + ConfigMetaData.CONFIG_ABBONAMENTO_KEY + " int not null default 0, "
            + ConfigMetaData.CONFIG_UPDATE_KEY + " text not null default 0 "
            + ")";

    private class DbHelper extends SQLiteOpenHelper { //classe che ci aiuta nella creazione del db

        public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) { //solo quando il db viene creato, creiamo la tabella

            Log.d("COLONNA","DB CONFIG CREATED");
            _db.execSQL(CONFIG_TABLE_CREATE);

            Log.d("COLONNA","DB TICKET CREATED");
            _db.execSQL(TICKET_TABLE_CREATE);

            Log.d("COLONNA","DB FEEDBACK CREATED si dovrebbe rimuovere");
            _db.execSQL(FEEDBACK_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            //qui mettiamo eventuali modifiche al db, se nella nostra nuova versione della app, il db cambia numero di versione

            /*if (oldVersion!=newVersion){

                _db.execSQL("DROP " + TicketMetaData.TICKET_TABLE);
                _db.execSQL("DROP " + FeedBackMetaData.FEEDBACK_TABLE);

            }*/

        }

    }

}

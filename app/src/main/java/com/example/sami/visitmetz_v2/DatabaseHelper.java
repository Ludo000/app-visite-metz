package com.example.sami.visitmetz_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // nom de la base de données
    private static final String Database_Name = "sites.db";

    // nom de la table
    private static final String Table_Name = "sites_table";

    DatabaseHelper(Context context) {
        super(context, Database_Name, null, 1);
    }

    // cette méthode sera appellée la première fois à la création de la base de données.
    // Elle permet de créer la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ajouter le nom de la table et les lignes de celle-ci
        db.execSQL("CREATE TABLE " + Table_Name + "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, ID_EXT INTEGER, NOM TEXT, LATITUDE DOUBLE, LONGITUDE DOUBLE, ADRESSE_POSTALE TEXT, CATEGORIE TEXT, RESUME TEXT, IMAGE BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si une version de la base de données existe, elle sera supprimée et remplacée par la nouvelle
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }

    boolean addData(String nom, Double latitude, Double longitude, String adresse, String categorie, String resume, byte[] image)
    {
        //Opens the database that will be used for writing and reading
        SQLiteDatabase db  = this.getWritableDatabase();
        //Permits to add new info in the table
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_ext",0);
        contentValues.put("nom",nom);
        contentValues.put("image",image);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);
        contentValues.put("adresse_postale",adresse);
        contentValues.put("categorie",categorie);
        contentValues.put("resume",resume);
        //inserts a row into database
        long result = db.insert(Table_Name,null,contentValues);
        //Check if value is correctly inserted
        return result != -1;
    }

    void updateData(String nom, Double latitude, Double longitude, String adresse, String categorie, String resume, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nom",nom);
        contentValues.put("image",image);
        contentValues.put("latitude",latitude);
        contentValues.put("longitude",longitude);
        contentValues.put("adresse_postale",adresse);
        contentValues.put("categorie",categorie);
        contentValues.put("resume",resume);
        String query = "Update "+ Table_Name + " Where NOM"  + " = '" +  nom + "'";
        db.execSQL(query);
    }

    Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from " + Table_Name;
        //Runs the provided SQL and returns a Cursor on the added result set
        return db.rawQuery(query,null);
    }

    Cursor getData(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from "+ Table_Name + " Where NOM"  + " = '" +  name + "'";
        return db.rawQuery(query, null);
    }

    void deleteData(String nom_site)
    {
        try {
            SQLiteDatabase db  = this.getWritableDatabase();
            String query = "DELETE FROM "+ Table_Name +
                    " WHERE NOM" + " = '" + nom_site + "';";
            db.execSQL(query);
            db.delete(
                    Table_Name,
                    "NOM" + " = " + nom_site,null);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}

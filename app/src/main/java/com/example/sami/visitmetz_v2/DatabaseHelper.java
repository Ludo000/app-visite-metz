package com.example.sami.visitmetz_v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // nom de la base de données
    private static final String Database_Name = "sites.db";

    // nom de la table site
    private static final String Table_Site = "sites_table";

    // nom de la table site
    public static final String Table_Categorie = "categories_table";

    // nom de la table site
    private static final String Table_SitesFavoris = "SitesFavoris_table";

    public DatabaseHelper(Context context) {
        super(context, Database_Name, null, 1);
    }

    // cette méthode sera appellée la première fois à la création de la base de données.
    // Elle permet de créer la base de données
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Ajouter le nom de la table et les lignes de celle-ci
        db.execSQL("CREATE TABLE " + Table_Categorie + "(_idCategorie INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT)");
        db.execSQL("CREATE TABLE " + Table_Site + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, ID_EXT INTEGER, NOM TEXT, LATITUDE REAL, LONGITUDE REAL, ADRESSE_POSTALE TEXT, _idCategorie INTEGER REFERENCES "+ Table_Categorie +", RESUME TEXT, IMAGE BLOB)");
        db.execSQL("CREATE TABLE " + Table_SitesFavoris + "(_idFavoris INTEGER PRIMARY KEY AUTOINCREMENT, _id INTEGER REFERENCES "+Table_Site+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si une version de la base de données existe, elle sera supprimée et remplacée par la nouvelle
        db.execSQL("DROP TABLE IF EXISTS " + Table_Categorie);
        db.execSQL("DROP TABLE IF EXISTS " + Table_Site);
        db.execSQL("DROP TABLE IF EXISTS " + Table_SitesFavoris);
        onCreate(db);
    }
}

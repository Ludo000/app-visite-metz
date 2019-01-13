package com.example.sami.visitmetz_v2.ContentProvider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.sami.visitmetz_v2.DatabaseHelper;

import java.util.HashMap;

@SuppressLint("Registered")
public class CategoriesProvider extends ContentProvider {

    private SQLiteDatabase db;

    static final String PROVIDER_NAME = "com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/categories_table";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //the code for multiple rows
    static final int CATEGORIES = 1;

    //the code for a single row
    static final int CATEGORIE_ID = 2;

    private static HashMap<String, String> CATEGORIES_PROJECTION_MAP;

    public static void setCategoriesProjectionMap(HashMap<String, String> categoriesProjectionMap) {
        CATEGORIES_PROJECTION_MAP = categoriesProjectionMap;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        // Defines a handle to the Room database
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        /*
          Create a write able database which will trigger its
          creation if it doesn't already exist.
         */

        db = databaseHelper.getWritableDatabase();
        return db != null;
    }
    
    // Creates a UriMatcher object.
    static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for sites_table are shown.
         */

        /*
         * Sets the integer value for multiple rows in sites_table to 1. Notice that no wildcard is used
         * in the path
         */
        uriMatcher.addURI(PROVIDER_NAME, "categories_table", CATEGORIES);


        // Sets the code for a single row to 2. In this case, the "#" wildcard is
        // used. "content://com.example.sami.visitmetz_v2.ContentProvider/categories_table/3" matches, but
        // "content://com.example.sami.visitmetz_v2.ContentProvider/categories_table doesn't.

        uriMatcher.addURI(PROVIDER_NAME, "categories_table/#", CATEGORIE_ID);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DatabaseHelper.Table_Categorie);

        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for categories_table are shown.
         */
        switch (uriMatcher.match(uri)) {

            // If the incoming URI was for all of categories_table
            case CATEGORIES:
                if (TextUtils.isEmpty(sortOrder)) {
                    // A projection map maps from passed column names to database column names
                    qb.setProjectionMap(CATEGORIES_PROJECTION_MAP);
                    sortOrder = "_idCategorie ASC";
                }
                break;

            /*
             * Because this URI was for a single row, the _ID value part is
             * present. Get the last path segment from the URI; this is the _ID value.
             * Then, append the value to the WHERE clause for the query
             */
            case CATEGORIE_ID:
                String id = uri.getPathSegments().get(1);
                qb.appendWhere( "_idCategorie = " + id);
                break;

            default:
                /*
                 * By default sort on student names
                 */
                sortOrder = "nom";
        }

        if (sortOrder == null || sortOrder.equals("")){
            /*
             * By default sort on student names
             */
            sortOrder = "nom";
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /*
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case CATEGORIES:
                count = db.delete(DatabaseHelper.Table_Categorie, selection, selectionArgs);
                break;

            case CATEGORIE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(DatabaseHelper.Table_Categorie, "_idCategorie = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                // If the URI is not recognized, do some error handling.
                throw new IllegalArgumentException("L'URI " + uri + " est inconnue");
        }

        /*
         * register to watch a content URI for changes
         */
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            /*
             * update all site records
             */
            case CATEGORIES:
                count = db.update(DatabaseHelper.Table_Categorie, values, selection, selectionArgs);
                break;
            /*
             * update a particular site
             */
            case CATEGORIE_ID:
                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                String id = uri.getPathSegments().get(1);
                count = db.update(DatabaseHelper.Table_Categorie, values,"_idCategorie = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                // If the URI is not recognized, do some error handling.
                throw new IllegalArgumentException("L'URI " + uri + " est inconnue");
        }

        /*
         * register to watch a content URI for changes
         */
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /*
             * Get all site records
             */
            case CATEGORIES:
                return "vnd.android.cursor.dir/vnd.example.categories_table";
            /*
             * Get a particular site
             */
            case CATEGORIE_ID:
                return "vnd.android.cursor.item/vnd.example.categories_table";
            default:
                // If the URI is not recognized, do some error handling.
                throw new IllegalArgumentException("L'URI " + uri + " est inconnue");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Gets the row id after inserting a map with the keys representing the the column
        // names and their values. The second attribute is used when you try to insert
        // an empty row
        long rowID = db.insert(DatabaseHelper.Table_Categorie,null, values);

        // Verify a row has been added
        if (rowID > 0) {

            // Append the given id to the path and return a Builder used to manipulate URI
            // references
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);

            // getContentResolver provides access to the content model
            // notifyChange notifies all observers that a row was updated
            getContext().getContentResolver().notifyChange(_uri, null);

            // Return the Builder used to manipulate the URI
            return _uri;
        }
        // If the URI is not recognized, do some error handling.
        throw new SQLException("Échec de l'enregistrement de " + uri);
    }
}

package com.example.sami.visitmetz_v2.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.example.sami.visitmetz_v2.DatabaseHelper;

import java.util.HashMap;

public class SitesProvider extends ContentProvider {

    private SQLiteDatabase db;

    static final String PROVIDER_NAME = "com.example.sami.visitmetz_v2.ContentProvider.SitesProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/sites_table";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    // nom de la table site
    static final String Table_Site = "sites_table";

    //the code for multiple rows
    static final int SITES = 1;

    //the code for a single row
    static final int SITE_ID = 2;

    private static HashMap<String, String> SITES_PROJECTION_MAP;

    @Override
    public boolean onCreate() {
        // Defines a handle to the Room database
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());

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
        uriMatcher.addURI(PROVIDER_NAME, Table_Site, SITES);


        // Sets the code for a single row to 2. In this case, the "#" wildcard is
        // used. "content://com.example.sami.visitmetz_v2.ContentProvider/sites_table/3" matches, but
        // "content://com.example.sami.visitmetz_v2.ContentProvider/sites_table doesn't.

        uriMatcher.addURI(PROVIDER_NAME, Table_Site + "/#", SITE_ID);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(Table_Site);

        /*
         * Choose the table to query and a sort order based on the code returned for the incoming
         * URI. Here, too, only the statements for sites_table are shown.
         */
        switch (uriMatcher.match(uri)) {

            // If the incoming URI was for all of sites_table
            case SITES:
                qb.setProjectionMap(SITES_PROJECTION_MAP);
                sortOrder = "_ID DESC";
                break;

            /*
             * Because this URI was for a single row, the _ID value part is
             * present. Get the last path segment from the URI; this is the _ID value.
             * Then, append the value to the WHERE clause for the query
             */
            case SITE_ID:
                String id = uri.getPathSegments().get(1);
                qb.appendWhere( "_ID = " + id);
                break;

            default:
                /*
                 * By default sort on student names
                 */
                sortOrder = "NOM";
        }

        if (sortOrder == null || sortOrder.equals("")) {
            /*
             * By default sort on student names
             */
            sortOrder = "NOM";
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
            case SITES:
                count = db.delete(Table_Site, selection, selectionArgs);
                break;

            case SITE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(Table_Site, "_ID = " + id +
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
        //db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            /*
             * update all site records
             */
            case SITES:
                count = db.update(Table_Site, values, selection, selectionArgs);
                break;
            /*
             * update a particular site
             */
            case SITE_ID:
                /*
                 * Because this URI was for a single row, the _ID value part is
                 * present. Get the last path segment from the URI; this is the _ID value.
                 * Then, append the value to the WHERE clause for the query
                 */
                String id = uri.getPathSegments().get(1);
                count = db.update(Table_Site, values,"_ID = " + id +
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
            case SITES:
                return "vnd.android.cursor.dir/vnd.example.sites_table";
            /*
             * Get a particular site
             */
            case SITE_ID:
                return "vnd.android.cursor.item/vnd.example.sites_table";
            default:
                // If the URI is not recognized, do some error handling.
                throw new IllegalArgumentException("L'URI " + uri + " est inconnue");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //Add a new site into database
        long result = db.insert(Table_Site,null, values);
        //Check if value is correctly inserted
        if (result > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, result);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        // If the URI is not recognized, do some error handling.
        throw new SQLException("Échec de l'enregistrement de " + uri);
    }
}

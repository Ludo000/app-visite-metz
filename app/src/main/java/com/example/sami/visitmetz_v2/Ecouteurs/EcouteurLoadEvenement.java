package com.example.sami.visitmetz_v2.Ecouteurs;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.widget.Toast;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;
import com.example.sami.visitmetz_v2.Sites.SitesOverviewFragment;

public class EcouteurLoadEvenement implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;

    SitesOverviewFragment.MyAdapter mAdapter;


    // If non-null, this is the current filter the user has provided.
    private String mCurFilter;

    // Projection contains the columns we want
    private String[] projection = new String[]{"_id", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE",
            "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

    public EcouteurLoadEvenement(Context context, SitesOverviewFragment.MyAdapter adapter, String curFilter) {
        this.context = context;
        this.mAdapter = adapter;
        this.mCurFilter = curFilter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri = SitesProvider.CONTENT_URI;
        CursorLoader cursorLoader;
        if (this.mCurFilter == null || this.mCurFilter.trim().length() == 0) {
            cursorLoader = new CursorLoader(this.context, baseUri, projection, null, null, "_id desc");
            //Toast.makeText(this.context, "Aucun site retrouvé!", Toast.LENGTH_LONG).show();
        }
        else {
            cursorLoader = new CursorLoader(this.context, baseUri, projection, "NOM LIKE ?", new String[]{"%"+this.mCurFilter.trim()+"%"}, null);
            //Toast.makeText(this.context, "Site retrouvé: " + this.mCurFilter, Toast.LENGTH_SHORT).show();
        }
        //and get a CursorLoader from my contentprovider
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }
}

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

import com.example.sami.visitmetz_v2.Categories.CategoriesOverviewFragment;
import com.example.sami.visitmetz_v2.ContentProvider.CategoriesProvider;

public class EcouteurLoadEvenement_3 implements LoaderManager.LoaderCallbacks<Cursor> {

    private Context context;

    private CategoriesOverviewFragment.MyAdapter mAdapter;


    // If non-null, this is the current filter the user has provided.
    private String mCurFilter;

    // Projection contains the columns we want
    private String[] projection = new String[]{"_idCategorie", "nom"};

    public EcouteurLoadEvenement_3(Context context, CategoriesOverviewFragment.MyAdapter adapter, String curFilter) {
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
        Uri baseUri = CategoriesProvider.CONTENT_URI;
        CursorLoader cursorLoader;
        if (this.mCurFilter == null || this.mCurFilter.trim().length() == 0) {
            cursorLoader = new CursorLoader(this.context, baseUri, projection, null, null, "_idCategorie desc");
            //Toast.makeText(this.context, "Aucun site retrouvé!", Toast.LENGTH_LONG).show();
        }
        else {
            cursorLoader = new CursorLoader(this.context, baseUri, projection, "nom LIKE ?", new String[]{"%"+this.mCurFilter.trim()+"%"}, null);
            //Toast.makeText(this.context, "Categorie retrouvé: " + this.mCurFilter, Toast.LENGTH_SHORT).show();
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

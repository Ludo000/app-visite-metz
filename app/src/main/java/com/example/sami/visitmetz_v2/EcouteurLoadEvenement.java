package com.example.sami.visitmetz_v2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.sami.visitmetz_v2.ContentProvider.SitesProvider;

public class EcouteurLoadEvenement implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;

    private SitesOverviewFragment.MyAdapter  mAdapter;

    // If non-null, this is the current filter the user has provided.
    private String mCurFilter = null;

    // Projection contains the columns we want
    private String[] projection = new String[]{"_id", "ID_EXT", "NOM", "LATITUDE", "LONGITUDE",
            "ADRESSE_POSTALE", "CATEGORIE", "RESUME", "IMAGE"};

    EcouteurLoadEvenement(Context context, SitesOverviewFragment.MyAdapter adapter) {
        this.context = context;
        this.mAdapter = adapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.
        Uri baseUri;
        if (mCurFilter != null) {
            baseUri = Uri.withAppendedPath(SitesProvider.CONTENT_URI,
                    Uri.encode(mCurFilter));
        } else {
            baseUri = SitesProvider.CONTENT_URI;
        }

        //and get a CursorLoader from my contentprovider
        return new CursorLoader(this.context, baseUri, projection, null, null, "_id desc");
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

package org.ktln2.android.callstat;

import java.util.TreeSet;
import java.io.*;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.DisplayPhoto;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentUris;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


/*
 * This class contains all the statistical data useful to
 * show information about calls.
 *
 * The key argument passed to the constructor is the number called or
 * the number of the callee. Internally this class resolves the contact
 * associated with that number.
 *
 */
public class CallStat {
    private TreeSet<Long> mDurations;
    private long mMax, mMin = 100000, mTotal;
    private String mKey;
    private String mContactName;
    private Bitmap mContactPhoto = null;

    public CallStat(String key, Context context) {
        mKey = key;
        mDurations = new TreeSet<Long>();

        /*
         * I don't like very much that we need to pass a Context object
         * around in this class, seems an extraneous object.
         */
        Uri uri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI, Uri.encode(key));
        Cursor cursor = context.getContentResolver().query(
            uri, new String[]{Contacts._ID, Contacts.DISPLAY_NAME_PRIMARY}, null, null, null
        );

        // if the cursor is in a good state and has results
        // continue and go for it
        if (cursor != null && cursor.moveToFirst()) {
            mContactName = cursor.getCount() > 0 ?
                    cursor.getString(cursor.getColumnIndexOrThrow(Contacts.DISPLAY_NAME_PRIMARY)) : "";

            try {
                mContactPhoto = BitmapFactory.decodeStream(
                    openPhoto(
                        cursor.getLong(cursor.getColumnIndexOrThrow(Contacts._ID)),
                        context)
                );
            } catch (android.database.CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
    }

    /*
     * Following the link
     *
     *   <http://stackoverflow.com/questions/10501446/get-profile-picture-of-specific-phone-number-from-contacts-information>
     */
    private InputStream openPhoto(long contactId, Context context) {
        Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(
            photoUri,
            new String[] {Contacts.Photo.PHOTO},
            null,
            null,
            null
        );

        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    public void add(long value) {
        mMin = (value < mMin && value > 0) ? value : mMin;
        mMax = value > mMax ? value : mMax;
        mTotal += value;

        mDurations.add(value);
    }

    public String getKey() {
        return mKey;
    }

    public String getContactName() {
        return mContactName;
    }

    public Bitmap getContactPhoto() {
        return mContactPhoto;
    }

    public long getMinDuration() {
        return mMin;
    }

    public long getMaxDuration() {
        return mMax;
    }

    public long getAverageDuration() {
        return getTotalDuration()/getTotalCalls();
    }

    public long getTotalDuration() {
        return mTotal;
    }

    public int getTotalCalls() {
        return mDurations.size();
    }

    public TreeSet<Long> getAllDurations() {
        return mDurations;
    }
}

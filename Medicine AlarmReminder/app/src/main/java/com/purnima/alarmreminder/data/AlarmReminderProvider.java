package com.purnima.alarmreminder.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class AlarmReminderProvider extends ContentProvider {

    public static final String LOG_TAG = AlarmReminderProvider.class.getSimpleName();

    private static final int REMINDER = 100;

    private static final int REMINDER_ID = 101;


    private static final UriMatcher um = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        um.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE, REMINDER);

        um.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE + "/#", REMINDER_ID);

    }

    private AlarmReminderDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new AlarmReminderDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
    String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
       /*
        Cursor cursor=new Cursor() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public int getPosition() {
                return 0;
            }

            @Override
            public boolean move(int i) {
                return false;
            }

            @Override
            public boolean moveToPosition(int i) {
                return false;
            }

            @Override
            public boolean moveToFirst() {
                return false;
            }

            @Override
            public boolean moveToLast() {
                return false;
            }

            @Override
            public boolean moveToNext() {
                return false;
            }

            @Override
            public boolean moveToPrevious() {
                return false;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean isBeforeFirst() {
                return false;
            }

            @Override
            public boolean isAfterLast() {
                return false;
            }

            @Override
            public int getColumnIndex(String s) {
                return 0;
            }

            @Override
            public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
                return 0;
            }

            @Override
            public String getColumnName(int i) {
                return null;
            }

            @Override
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public byte[] getBlob(int i) {
                return new byte[0];
            }

            @Override
            public String getString(int i) {
                return null;
            }

            @Override
            public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer) {

            }

            @Override
            public short getShort(int i) {
                return 0;
            }

            @Override
            public int getInt(int i) {
                return 0;
            }

            @Override
            public long getLong(int i) {
                return 0;
            }

            @Override
            public float getFloat(int i) {
                return 0;
            }

            @Override
            public double getDouble(int i) {
                return 0;
            }

            @Override
            public int getType(int i) {
                return 0;
            }

            @Override
            public boolean isNull(int i) {
                return false;
            }

            @Override
            public void deactivate() {

            }

            @Override
            public boolean requery() {
                return false;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isClosed() {
                return false;
            }

            @Override
            public void registerContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void unregisterContentObserver(ContentObserver contentObserver) {

            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void setNotificationUri(ContentResolver contentResolver, Uri uri) {

            }

            @Override
            public Uri getNotificationUri() {
                return null;
            }

            @Override
            public boolean getWantsAllOnMoveCalls() {
                return false;
            }

            @Override
            public void setExtras(Bundle bundle) {

            }

            @Override
            public Bundle getExtras() {
                return null;
            }

            @Override
            public Bundle respond(Bundle bundle) {
                return null;
            }
        }*/
        Cursor cursor = null;

        int match = um.match(uri);
        switch (match) {
            case REMINDER:
                cursor = database.query(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = um.match(uri);
        switch (match) {
            case REMINDER:
                return AlarmReminderContract.AlarmReminderEntry.CONTENT_LIST_TYPE;
            case REMINDER_ID:
                return AlarmReminderContract.AlarmReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
    private void StopReminder(ContentValues contentValues){
      /*  Toast.makeText(AlarmReminderProvider.this, " Generate toast",
                Toast.LENGTH_LONG).show();

       */
    }

    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = um.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }




    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = um.match(uri);
        switch (match) {
            case REMINDER:
                rowsDeleted = database.delete(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }
/*
    @Override
    public boolean refresh(Uri uri, @Nullable Bundle args, @Nullable CancellationSignal cancellationSignal) {
        return super.refresh(uri, args, cancellationSignal);
    }
*/
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = um.match(uri);
        switch (match) {
            case REMINDER:
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private void setLogTag1(){
       // Log.i(TAG, "setLogTag1.getView() — get item number " + position);
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}

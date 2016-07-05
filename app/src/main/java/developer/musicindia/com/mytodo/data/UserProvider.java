package developer.musicindia.com.mytodo.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class UserProvider extends ContentProvider {

    static final String PROVIDER_NAME = "developer.musicindia.com.mytodo.data";


    static final String URL_TODO = "content://" + PROVIDER_NAME + "/todo";
    public static final Uri CONTENT_URI_TODO = Uri.parse(URL_TODO);


    public static final String _TODOID = "_id";
    public static final String _TODO_TEXT = "todoText";
    public static final String _TODO_TEXT_DES = "todoTextDes";
    public static final String _TODO_REMINDER = "reminderTime";
    public static final String _TODO_COMPLETED = "completed";
    

    private static HashMap<String, String> USER_TODO_PROJECTION_MAP;

    static final int USER_TODO  = 1;
    static final int USER_TODO_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PROVIDER_NAME, "todo", USER_TODO);
        uriMatcher.addURI(PROVIDER_NAME, "todo/#", USER_TODO_ID);


    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;

    public static final String DATABASE_NAME = "TODODATABASE";
    public static final String USER_TODO_TABLE_NAME = "todo";
    static final int DATABASE_VERSION = 1;

    static final String CREATE_DB_TABLE_TODO =
            " CREATE TABLE " + USER_TODO_TABLE_NAME +
                    " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " todoText TEXT NOT NULL, " +
                    " todoTextDes TEXT NOT NULL, " +
                    " reminderTime TEXT NOT NULL, " +
                    " completed INTEGER NOT NULL);";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE_TODO);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  USER_TODO_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        Uri _uri = null;
        switch (uriMatcher.match(uri)) {

            case USER_TODO: {
                long rowID = db.insert(USER_TODO_TABLE_NAME, "", values);

                /**
                 * If record is added successfully
                 */

                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_TODO, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }

                throw new SQLException("Failed to add a record into " + uri);
                //break;
            }
            default:{
               // return _uri;
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }

       // return _uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        //qb.setTables(USER_TABLE_NAME);

        switch (uriMatcher.match(uri)) {

            case USER_TODO:
                qb.setTables(USER_TODO_TABLE_NAME);
                qb.setProjectionMap(USER_TODO_PROJECTION_MAP);
                break;

            case USER_TODO_ID:
                qb.setTables(USER_TODO_TABLE_NAME);
                qb.appendWhere( _TODOID + "=" + uri.getPathSegments().get(1));
                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){

            case USER_TODO:
                count = db.delete(USER_TODO_TABLE_NAME, selection, selectionArgs);
                break;

            case USER_TODO_ID:
                String mid = uri.getPathSegments().get(1);
                count = db.delete( USER_TODO_TABLE_NAME, _TODOID +  " = " + mid +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;



            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){

            case USER_TODO: {
                count = db.update(USER_TODO_TABLE_NAME, values, selection, selectionArgs);


                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));
                Log.e("Record Updated", Integer.toString(count));

                break;
            }
            case USER_TODO_ID:
                count = db.update(USER_TODO_TABLE_NAME, values, _TODOID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){

            case USER_TODO:
                // return "vnd.android.cursor.dir/vnd.example.students";
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "todo";
            case USER_TODO_ID:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + PROVIDER_NAME + "/" + "todo";


            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
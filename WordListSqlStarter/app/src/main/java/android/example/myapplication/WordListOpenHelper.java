package android.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordListOpenHelper extends SQLiteOpenHelper {
    // It's a good idea to always define a log tag like this.
    public static final String TAG = WordListOpenHelper.class.getSimpleName();

    // has to be 1 first time or app will crash
    private static final int DATABASE_VERSION = 1;
    private static final String WORD_LIST_TABLE = "word_entries";
    private static final String DATABASE_NAME = "wordlist";

    // Column names...
    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";

    // ... and a string array of columns.
    private static final String[] COLUMNS = {KEY_ID, KEY_WORD};

    // Build the SQL query that creates the table.
    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE" + WORD_LIST_TABLE + " (" +
                    KEY_ID + "INTEGER PRIMARY TABLE, " +
                    // id will auto-increment if no value passed
                    KEY_WORD + "TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public WordListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct WordListOpenHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORD_LIST_TABLE);
        fillDatabaseWithData(db);
    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Liu Kang", "Jax Briggs", "Sonya Blade", "Raiden",
                "Johnny Cage", "Kung Lao", "Scorpion", "Reptile", "Sheeva",
                "Sub-zero", "Smoke","Kitana", "Tanya", "NightWolf"};

        // Create a container for the data.
        ContentValues values = new ContentValues();

        for (int i=0; i < words.length; i++) {
        // Put column/value pairs into the container.
        // put() overrides existing values.
            values.put(KEY_WORD, words[i]);
            db.insert(WORD_LIST_TABLE, null, values);
            //three arguments (table, string, contentvalue)
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(WordListOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(db);
//Every SQLiteOpenHelper must implement the onUpgrade() method, which determines what happens if
// the database version number changes. This may happen if you have existing users of your app
// that use an older version of the database. This method is triggered when a
// database is first opened.
    }

    public WordItem query(int position){
        String query = "SELECT * FROM " + WORD_LIST_TABLE +
                "ORDER BY " + KEY_WORD + "ASC " +
                 "LIMIT " + position + ",1";

        Cursor cursor = null;
        //The SQLiteDatabase always presents the results as a Cursor
        // in a table format that resembles of a SQL database.
        //A cursor is a pointer into a row of structured data. You can think of it as an array
        // of rows. The Cursor class provides methods for moving the cursor through that structure,
        // and methods to get the data from the columns of each row.

        WordItem entry = new WordItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setWord(cursor.getString(cursor.getColumnIndex(KEY_WORD)));
            
        } catch (Exception e) {
            Log.d(TAG, "EXCEPTION! " + e);
            e.printStackTrace();

        } finally {
            cursor.close();
            return entry;

        }
    }

    public long insert(String word){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);

        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
            e.printStackTrace();

        }
        return newId;
    }

    public long count(){ //getCountItem
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
    }

    public int delete(int id) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(WORD_LIST_TABLE,
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
            //The "?" is a placeholder that gets filled with the string.
            // This is a more secure way of building queries.

        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
            e.printStackTrace();

        }
        return deleted;
    }

}

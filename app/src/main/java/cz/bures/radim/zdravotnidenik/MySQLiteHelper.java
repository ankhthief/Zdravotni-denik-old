package cz.bures.radim.zdravotnidenik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_VENTURES = "zdravotni_denik";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name_of_venture";
    public static final String COLUMN_PLACE = "place_of_venture";

    private static final String DATABASE_NAME = "zdravotni_denik.db";
    private static final int DATABASE_VERSION = 1;

    // Vytvoření databáze
    private static final String DATABASE_CREATE = "create table "
            + TABLE_VENTURES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_PLACE
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENTURES);
        onCreate(db);
    }

}
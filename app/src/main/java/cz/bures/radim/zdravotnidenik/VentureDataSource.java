package cz.bures.radim.zdravotnidenik;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.w3c.dom.Comment;

public class VentureDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_PLACE };

    public VentureDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Venture createVenture(String name, String place) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_PLACE, place);
        long insertId = database.insert(MySQLiteHelper.TABLE_VENTURES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_VENTURES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
       Venture newVenture = cursorToVenture(cursor);
        cursor.close();
        return newVenture;
    }

    public void deleteVenture(Venture venture) {
        long id = venture.getId();
        System.out.println("Venture deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_VENTURES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Venture> getAllVentures() {
        List<Venture> ventures = new ArrayList<Venture>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_VENTURES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Venture venture = cursorToVenture(cursor);
            ventures.add(venture);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ventures;
    }

    private Venture cursorToVenture(Cursor cursor) {
        Venture venture = new Venture();
        venture.setId(cursor.getLong(0));
        venture.setName_of_venture(cursor.getString(1));
        venture.setPlace_of_venture(cursor.getString(1));
        return venture;
    }
}

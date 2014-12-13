package cz.bures.radim.zdravotnidenik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String TAG = "DBAdapter";
			
	// názvy sloupců tabulky Events
	public static final String EVENT_ROWID = "_id";
	public static final String EVENT_NAME = "name";
	public static final String EVENT_PLACE = "place";

    //názvy sloupců tabulky Participants:
    public static final String PARTICIPANT_ROWID = "_id";
    public static final String PARTICIPANT_NAME = "name";
    public static final String PARTICIPANT_SURNAME = "surname";
    public static final String PARTICIPANT_ID = "id_event";

    //názvy sloupců tabulky Injuries:
    public static final String INJURIES_ROWID = "_id";
    public static final String INJURIES_NAME = "name";
    public static final String INJURIES_TEXT = "text";
    public static final String INJURIES_ID = "id_participant";
	
	public static final String[] ALL_KEYS_EVENT = new String[] {EVENT_ROWID, EVENT_NAME, EVENT_PLACE};
    public static final String[] ALL_KEYS_PARTICIPANT = new String[] {PARTICIPANT_ROWID, PARTICIPANT_NAME, PARTICIPANT_SURNAME, PARTICIPANT_ID};
    public static final String[] ALL_KEYS_INJURIES = new String[] {INJURIES_ROWID,INJURIES_NAME, INJURIES_TEXT, INJURIES_ID};


	// INFO o databázi:
	public static final String DATABASE_NAME = "medical_reports.db";
	public static final String DATABASE_TABLE_EVENTS = "events";
    public static final String DATABASE_TABLE_PARTICIPANTS= "participants";
    public static final String DATABASE_TABLE_INJURIES = "injuries";
	public static final int DATABASE_VERSION = 3; // Tohle číslo se musí změnit pokaždé, když se mění databáze
		
	//SQL pro vytvoření tabulky Events
	private static final String DATABASE_CREATE_SQL_EVENTS =
			"CREATE TABLE " + DATABASE_TABLE_EVENTS
			+ " (" + EVENT_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ EVENT_NAME + " TEXT NOT NULL, "
			+ EVENT_PLACE + " TEXT"
			+ ");";

    //SQL pro vytvoření tabulky Participants
    private static final String DATABASE_CREATE_SQL_PARTICIPANTS =
            "CREATE TABLE " + DATABASE_TABLE_PARTICIPANTS
            + " (" + PARTICIPANT_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PARTICIPANT_NAME + " TEXT NOT NULL, "
            + PARTICIPANT_SURNAME + " TEXT, "
            + PARTICIPANT_ID + " TEXT"
            + ");";

    //SQL pro vytvoření tabulky Injuries
    private static final String DATABASE_CREATE_SQL_INJURIES =
            "CREATE TABLE " + DATABASE_TABLE_INJURIES
            + " (" + INJURIES_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + INJURIES_NAME + " TEXT NOT NULL, "
            + INJURIES_TEXT + " TEXT, "
            + INJURIES_ID + " TEXT"
            + ");";

    private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
        Context context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}
	
	// Otevře databázi
	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}
	
	// Zavře databázi
	public void close() {
		myDBHelper.close();
	}
	
	// Vytvoří novou sadu hodnot, která se má přidat do tabulky Events
	public long insertRowEvent(String name, String place) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(EVENT_NAME, name);
		initialValues.put(EVENT_PLACE, place);
				
		// Vloží data do tabulky Events
		return db.insert(DATABASE_TABLE_EVENTS, null, initialValues);
	}

    // Vytvoření sady hodnot pro tabulku Participants
    public long insertRowParticipant(String name, String surname, long id_event) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(PARTICIPANT_NAME, name);
        initialValues.put(PARTICIPANT_SURNAME, surname);
        initialValues.put(PARTICIPANT_ID, id_event);

        // Vloží data do tabulky Participants
        return db.insert(DATABASE_TABLE_PARTICIPANTS, null, initialValues);
    }

    // Vytvoření sady hodnot pro tabulku Injuries
    public long insertRowInjuries(String name, String text, long id_participant) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(INJURIES_NAME, name);
        initialValues.put(INJURIES_TEXT, text);
        initialValues.put(INJURIES_ID, id_participant);

        // Vloží data do tabulky Injuries
        return db.insert(DATABASE_TABLE_INJURIES, null, initialValues);
    }

	
	// vymaže řádek z db podle EVENT_ROWID
	public boolean deleteRowEvent(long rowId) {
		String where = EVENT_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE_EVENTS, where, null) != 0;
	}

    // vymaže řádek z db podle PARTICIPANT_ROWID
    public boolean deleteRowParticipants(long rowId) {
        String where = PARTICIPANT_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE_PARTICIPANTS, where, null) != 0;
    }

    // vymaže řádek z db podle INJURIES_ROWID
    public boolean deleteRowInjuries(long rowId) {
        String where = INJURIES_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE_INJURIES, where, null) != 0;
    }
	
	// Vrátí všechny data z tabulky Events
	public Cursor getAllRowsEvent() {
		Cursor c = 	db.query(true, DATABASE_TABLE_EVENTS, ALL_KEYS_EVENT, null, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

    // Vrátí všechny data z tabulky Participants
    public Cursor getAllRowsParticipants(long radek) {
        Cursor c = 	db.rawQuery("SELECT * FROM participants WHERE id_event= " + radek + ";", null);
                //db.query(true, DATABASE_TABLE_PARTICIPANTS, ALL_KEYS_PARTICIPANT, PARTICIPANT_ID = String.valueOf(radek), null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Vrátí všechny data z tabulky Injuries
    public Cursor getAllRowsInjuries(long radek) {
        Cursor c = db.rawQuery("SELECT * FROM injuries WHERE id_participant= " + radek + ";", null);
                //db.query(true, DATABASE_TABLE_INJURIES, ALL_KEYS_INJURIES, null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // fce pro úpravu jména eventu
    public String getNameUpdateEvent(long id) {
        Cursor mCursor =
                db.rawQuery("select name from events WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updateNameEvent;
        updateNameEvent = mCursor.getString(mCursor.getColumnIndex("name"));
        return updateNameEvent;
    }

    // fce pro úpravu jména participanta
    public String getNameUpdateParticipant(long id) {
        Cursor mCursor =
                db.rawQuery("select name from participants WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updateNameParticipant;
        updateNameParticipant = mCursor.getString(mCursor.getColumnIndex("name"));
        return updateNameParticipant;
    }

    // fce pro úpravu jména zranění
    public String getNameUpdateInjury(long id) {
        Cursor mCursor =
                db.rawQuery("select name from injuries WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updateNameInjury;
        updateNameInjury = mCursor.getString(mCursor.getColumnIndex("name"));
        return updateNameInjury;
    }

    // fce pro úpravu jména zranění
    public String getTextUpdateInjury(long id) {
        Cursor mCursor =
                db.rawQuery("select text from injuries WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updateTextInjury;
        updateTextInjury = mCursor.getString(mCursor.getColumnIndex("text"));
        return updateTextInjury;
    }

    // fce pro úpravu příjmení participanta
    public String getSurNameUpdateParticipant(long id) {
        Cursor mCursor =
                db.rawQuery("select surname from participants WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updateSurNameParticipant;
        updateSurNameParticipant = mCursor.getString(mCursor.getColumnIndex("surname"));
        return updateSurNameParticipant;
    }

    // fce pro úpravu místa eventu
    public String getPlaceUpdateEvent(long id) {
        Cursor mCursor =
                db.rawQuery("select place from events WHERE _id=" + id + ";", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        String updatePlaceEvent;
        updatePlaceEvent = mCursor.getString(mCursor.getColumnIndex("place"));
        return updatePlaceEvent;
    }



	// Get a specific row (by rowId)
	public Cursor getRow(long rowId) {
		String where = EVENT_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE_EVENTS, ALL_KEYS_EVENT,
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	
	// změní řádek na nový řádek (update)
	public boolean updateRowEvent(long rowId, String task, String date) {
		String where = EVENT_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(EVENT_NAME, task);
		newValues.put(EVENT_PLACE, date);
		// vložení změn do db
		return db.update(DATABASE_TABLE_EVENTS, newValues, where, null) != 0;
	}

    // změní řádek na nový řádek (update)
    public boolean updateRowParticipant(long rowId, String task, String date) {
        String where = PARTICIPANT_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(PARTICIPANT_NAME, task);
        newValues.put(PARTICIPANT_SURNAME, date);
        // vložení změn do db
        return db.update(DATABASE_TABLE_PARTICIPANTS, newValues, where, null) != 0;
    }
    // změní řádek na nový řádek (update)
    public boolean updateRowInjury(long rowId, String name, String text) {
        String where = INJURIES_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(INJURIES_NAME, name);
        newValues.put(INJURIES_TEXT, text);
        // vložení změn do db
        return db.update(DATABASE_TABLE_INJURIES, newValues, where, null) != 0;
    }

	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {

            _db.execSQL(DATABASE_CREATE_SQL_EVENTS);
            _db.execSQL(DATABASE_CREATE_SQL_PARTICIPANTS);
            _db.execSQL(DATABASE_CREATE_SQL_INJURIES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Zničení staré db:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EVENTS);
			
			// Znovuvytvoření db:
			onCreate(_db);
		}
	}


}


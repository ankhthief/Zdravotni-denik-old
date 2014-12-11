package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListOfParticipants extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_participants);
        openDB();
        populateListViewParticipants();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_participants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDB() {
        myDb =  new DBAdapter(this);
        myDb.open();
    }

    public void populateListViewParticipants() {
        Bundle extras = getIntent().getExtras();
        String id;
        if (extras != null) {
            id = extras.getString("id_eventu");
        }
        // TODO tady je potřeba pořešit předávání toho indexu
        Cursor cursor1 = myDb.getAllRowsParticipants("2");
        String[] fromParticipantsNames = new String[] {DBAdapter.PARTICIPANT_NAME, DBAdapter.PARTICIPANT_SURNAME};
        int[] toViewIDsParticipants = new int[] {R.id.participant_name, R.id.participant_surname};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.row_participant, cursor1, fromParticipantsNames, toViewIDsParticipants,0 );
        ListView myList = (ListView) findViewById(R.id.list_participants);
        myList.setAdapter(myCursorAdapter);
    }

    public void onClickInsertParticipant (MenuItem item) {
        Intent intent = new Intent(this, InsertParticipant.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}

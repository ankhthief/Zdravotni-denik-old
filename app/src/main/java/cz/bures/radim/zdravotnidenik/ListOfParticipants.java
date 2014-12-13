package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListOfParticipants extends Activity {

    DBAdapter myDb;
    long id_eventu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_participants);
        ListView list_participants = (ListView) findViewById(R.id.list_participants);
        openDB();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_eventu = extras.getLong("id_eventu");
        }
        //Toast.makeText(getApplicationContext(),"id:" + id_eventu, Toast.LENGTH_SHORT).show();
        populateListViewParticipants();
        registerForContextMenu(list_participants);
        list_participants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListOfParticipants.this, ListOfInjuries.class);
                //Cursor cur = (Cursor) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(),"id:" + id + " rowid:" + cur.getInt(cur.getColumnIndex("_id")), Toast.LENGTH_SHORT).show();
                intent.putExtra("id_participant",id);
                startActivity(intent);
            }
        });
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
        Cursor cursor1 = myDb.getAllRowsParticipants(id_eventu);
        String[] fromParticipantsNames = new String[] {DBAdapter.PARTICIPANT_NAME, DBAdapter.PARTICIPANT_SURNAME};
        int[] toViewIDsParticipants = new int[] {R.id.participant_name, R.id.participant_surname};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.row_participant, cursor1, fromParticipantsNames, toViewIDsParticipants,0 );
        ListView myList = (ListView) findViewById(R.id.list_participants);
        myList.setAdapter(myCursorAdapter);
    }

    public void onClickInsertParticipant (MenuItem item) {
        Intent intent = new Intent(this, InsertParticipant.class);
        intent.putExtra("id_eventu",id_eventu);
        startActivity(intent);
        finish();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list_participants) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.event_popup, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long id = info.id;
        switch(item.getItemId()) {
            case R.id.edit_event_popup:
                Intent intent = new Intent(ListOfParticipants.this, InsertParticipant.class);
                intent.putExtra("id",id);
                intent.putExtra("id_eventu",id_eventu);
                intent.putExtra("edit",true);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.delete_event_popup:
                myDb.deleteRowParticipants(id);
                populateListViewParticipants();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

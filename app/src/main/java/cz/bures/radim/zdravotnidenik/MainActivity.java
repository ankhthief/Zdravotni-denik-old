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
import android.widget.Toast;


public class MainActivity extends Activity {

    DBAdapter myDb;

    // TODO chování aplikace tak, aby tlačítka zpět apod. fungovali správně, tedy návrat byl zpět na správnou activitu
    // TODO promazání komentů a dopsání komentářů

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        populateListView();
        ListView myList = (ListView) findViewById(R.id.list_events);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ListOfParticipants.class);
                Cursor cur = (Cursor) parent.getItemAtPosition(position);
                //Toast.makeText(getApplicationContext(),"id:" + id + " rowid:" + cur.getInt(cur.getColumnIndex("_id")), Toast.LENGTH_SHORT).show();
                intent.putExtra("id_eventu",id);
                startActivity(intent);
            }
        });
        registerForContextMenu(myList);

    }

    public void onClickInsertEvent (MenuItem item) {
        Intent intent = new Intent(this, InsertEvent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void populateListView() {
        Cursor cursor = myDb.getAllRowsEvent();
        String[] fromEventNames = new String[] {DBAdapter.EVENT_NAME, DBAdapter.EVENT_PLACE};
        int[] toViewIDs = new int[] {R.id.name_of_event, R.id.location_of_event};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.row_event, cursor, fromEventNames, toViewIDs,0 );
        ListView myList = (ListView) findViewById(R.id.list_events);
        myList.setAdapter(myCursorAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.list_events) {
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
                Intent intent = new Intent(MainActivity.this, InsertEvent.class);
                intent.putExtra("id",id);
                startActivity(intent);
                finish();
                return true;
            case R.id.delete_event_popup:
                myDb.deleteRowEvent(id);
                populateListView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void closeDB() {
        myDb.close();
    }
}

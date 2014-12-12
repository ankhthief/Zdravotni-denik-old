package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class ListOfInjuries extends Activity {

    DBAdapter myDb;
    long id_participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_injuries);
        //ListView list_injuries = (ListView) findViewById(R.id.list_injuries);
        // TODO edit a delete context
        // TODO proklik na popis
        openDB();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_participant = extras.getLong("id_participant");
        }
        populateListViewInjuries();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_of_injuries, menu);
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

    public void populateListViewInjuries() {
        // TODO tady je potřeba pořešit předávání toho indexu
        Cursor cursor1 = myDb.getAllRowsInjuries(id_participant);
        String[] fromInjuriesNames = new String[] {DBAdapter.INJURIES_NAME, DBAdapter.INJURIES_TEXT};
        int[] toViewIDsInjuries = new int[] {R.id.injuries_name, R.id.injuries_text};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.row_injuries, cursor1, fromInjuriesNames, toViewIDsInjuries,0 );
        ListView list_injuries = (ListView) findViewById(R.id.list_injuries);
        list_injuries.setAdapter(myCursorAdapter);
    }

    public void onClickInsertInjury (MenuItem item) {
        Intent intent = new Intent(this, InsertInjury.class);
        intent.putExtra("id_participant",id_participant);
        startActivity(intent);
        finish();
    }
}

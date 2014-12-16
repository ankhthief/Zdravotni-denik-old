package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class InsertEvent extends Activity {

    DBAdapter myDb;
    EditText name ;
    EditText place ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_event);
        name = (EditText) findViewById(R.id.edit_event_name);
        place = (EditText) findViewById(R.id.edit_event_place);
        openDB();
        Bundle extras = getIntent().getExtras();
        long id;

        if (extras != null) {
            id = extras.getLong("id");
            name.setText(myDb.getNameUpdateEvent(id));
            place.setText(myDb.getPlaceUpdateEvent(id));
        }

    }

    public void onCancel(View view) {
        myDb.close();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_event, menu);
        return true;
    }*/

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

    public void onClick_AddEvent (View view) {
        Bundle extras = getIntent().getExtras();
        long id;

        if (extras != null) {
            id = extras.getLong("id");
            myDb.updateRowEvent(id,name.getText().toString(), place.getText().toString());

        } else {
        if(!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(place.getText())){
            myDb.insertRowEvent(name.getText().toString(), place.getText().toString());
            myDb.close();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        } else {
            Toast.makeText(getApplicationContext(), "Fields can not be empty! ", Toast.LENGTH_SHORT).show();
        }
        }

    }


}

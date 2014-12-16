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


public class InsertParticipant extends Activity {

    DBAdapter myDb;
    EditText name;
    EditText surname;
    long id_event;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_participant);
        name = (EditText) findViewById(R.id.edit_participant_name);
        surname = (EditText) findViewById(R.id.edit_participant_surname);
       openDB();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_event = extras.getLong("id_eventu");
            //Toast.makeText(getApplicationContext(),"nacetlo se i id eventu " + id_event, Toast.LENGTH_SHORT).show();
        }
        if (extras != null) {
            if (extras.getBoolean("edit")) {
                //Toast.makeText(getApplicationContext(),"jedna se o edit", Toast.LENGTH_SHORT).show();
                id = extras.getLong("id");
                name.setText(myDb.getNameUpdateParticipant(id));
                surname.setText(myDb.getSurNameUpdateParticipant(id));
            }
        }
        //Toast.makeText(getApplicationContext(),"id:" + id_event, Toast.LENGTH_SHORT).show();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_participant, menu);
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

    public void onCancel(View view) {
        myDb.close();
        //Toast.makeText(getApplicationContext(),"id:" + id_event, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListOfParticipants.class);
        intent.putExtra("id_eventu",id_event);
        startActivity(intent);
        this.finish();
    }

    private void openDB() {
        myDb =  new DBAdapter(this);
        myDb.open();
    }

    public void onClick_AddParticipant(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("edit")) {
            id = extras.getLong("id");
            myDb.updateRowParticipant(id, name.getText().toString(), surname.getText().toString());
            myDb.close();
            Intent intent = new Intent(this, ListOfParticipants.class);
            intent.putExtra("id_eventu",id_event);
            startActivity(intent);
            this.finish();


        } else {
            if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(surname.getText())) {
                myDb.insertRowParticipant(name.getText().toString(), surname.getText().toString(), id_event);
                myDb.close();
                //Toast.makeText(getApplicationContext(),"id:" + id_event, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListOfParticipants.class);
                intent.putExtra("id_eventu",id_event);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(getApplicationContext(),"Fields can not be empty! ", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

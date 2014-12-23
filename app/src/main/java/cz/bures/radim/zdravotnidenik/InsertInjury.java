package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class InsertInjury extends Activity {

    DBAdapter myDb;
    long id_participant;
    EditText name;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_injury);
        name = (EditText) findViewById(R.id.edit_injury_name);
        text = (EditText) findViewById(R.id.edit_injury_text);
        openDB();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_participant = extras.getLong("id_participant");
            //Toast.makeText(getApplicationContext(),"nacetlo se i id participanta " + id_participant, Toast.LENGTH_SHORT).show();
        }
        if (extras != null) {
            if (extras.getBoolean("edit")) {
                //Toast.makeText(getApplicationContext(), "jedna se o edit", Toast.LENGTH_SHORT).show();
                long id;
                id = extras.getLong("id");
                name.setText(myDb.getNameUpdateInjury(id));
                text.setText(myDb.getTextUpdateInjury(id));
            }
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insert_injury, menu);
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
        //Toast.makeText(getApplicationContext(),"id:" + id_participant, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ListOfInjuries.class);
        intent.putExtra("id_participant",id_participant);
        startActivity(intent);
        this.finish();
    }

    private void openDB() {
        myDb =  new DBAdapter(this);
        myDb.open();
    }

    public void onClick_AddInjury(View view) {
        Bundle extras = getIntent().getExtras();
        long id;
        if (extras != null && extras.getBoolean("edit")) {
            id = extras.getLong("id");
            myDb.updateRowInjury(id, name.getText().toString(), text.getText().toString());
            myDb.close();
            //Toast.makeText(getApplicationContext(),"id:" + id_participant, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ListOfInjuries.class);
            intent.putExtra("id_participant",id_participant);
            startActivity(intent);
            this.finish();


        } else {
            if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(text.getText())) {
                myDb.insertRowInjuries(name.getText().toString(), text.getText().toString(), id_participant);
                myDb.close();
                //Toast.makeText(getApplicationContext(),"id:" + id_participant, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListOfInjuries.class);
                intent.putExtra("id_participant",id_participant);
                startActivity(intent);
                this.finish();

            } else {
                Toast.makeText(getApplicationContext(), "Fields can not be empty! ", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

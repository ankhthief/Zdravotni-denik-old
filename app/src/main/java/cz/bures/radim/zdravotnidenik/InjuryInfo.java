package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class InjuryInfo extends Activity {

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injury_info);
        TextView name = (TextView) findViewById(R.id.injuries_name_info);
        TextView text = (TextView) findViewById(R.id.injuries_text_info);
        Bundle extras = getIntent().getExtras();
        long id = extras.getLong("id");
        openDB();
        name.setText(myDb.getNameUpdateInjury(id));
        text.setText(myDb.getTextUpdateInjury(id));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_injury_info, menu);
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
    public void share(MenuItem item) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hello, try this awesome app. Download it at www.medicalreports.com";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check this app!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void about(MenuItem item) {
        Intent about = new Intent(this, Aboutapp.class);
        startActivity(about);
    }
}

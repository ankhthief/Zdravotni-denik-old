package cz.bures.radim.zdravotnidenik;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Random;


public class MainActivity extends ListActivity {
    private VentureDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datasource = new VentureDataSource(this);
        datasource.open();

        List<Venture> values = datasource.getAllVentures();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Venture> adapter = new ArrayAdapter<Venture>(this,
                android.R.layout.simple_list_item_1 , values);
        setListAdapter(adapter);
    }
    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Venture> adapter = (ArrayAdapter<Venture>) getListAdapter();
        Venture venture = null;
        switch (view.getId()) {
            case R.id.add:
                String[] ventures = new String[] { "Cool", "Very nice", "Hate it" };
                int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                venture = datasource.createVenture(ventures[nextInt],"pokus");
                adapter.add(venture);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    venture = (Venture) getListAdapter().getItem(0);
                    datasource.deleteVenture(venture);
                    adapter.remove(venture);
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
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
}

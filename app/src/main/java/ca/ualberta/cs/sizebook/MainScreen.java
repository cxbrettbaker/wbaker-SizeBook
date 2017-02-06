package ca.ualberta.cs.sizebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * The type Main screen.
 */
public class MainScreen extends Activity {

    private static final String FILENAME = "file.sav";
	private ListView oldEntriesList;

	private ArrayList<EntryData> entryList;
	private ArrayAdapter<EntryData> adapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button addEntryButton = (Button) findViewById(R.id.add);
		oldEntriesList = (ListView) findViewById(R.id.oldEntriesList);


        registerForContextMenu(oldEntriesList);

	}


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){

            case R.id.edit_id:
                Intent intent = new Intent(this, EditScreen.class);

                EntryData selected = entryList.get(info.position);
                Bundle bundle = selected.toBundle();
                bundle.putInt("current_pos",info.position);

                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.delete_id:

                entryList.remove(info.position);
                adapter.notifyDataSetChanged();
                saveInFile();

                TextView countText = (TextView) findViewById(R.id.oldEntriesTitle);
                Integer size = entryList.size();
                countText.setText(String.format ("%d Entries Stored:", size));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
	protected void onStart() {
		super.onStart();

        loadFromFile();

		TextView countEntries = (TextView) findViewById(R.id.oldEntriesTitle);
		Integer size = entryList.size();
		countEntries.setText(String.format ("%d Entries Stored:", size));

		adapter = new ArrayAdapter<EntryData>(this,
				R.layout.list_item, entryList);
		oldEntriesList.setAdapter(adapter);

		adapter.notifyDataSetChanged();
	}

    /**
     * To entries.
     *
     * @param view the view
     */
    public void toEntries (View view) {
		Intent intent = new Intent(this, EntryScreen.class);
		startActivity(intent);
	}

    private void loadFromFile() {
		try {
			FileInputStream fis = openFileInput(FILENAME);
			BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-01-24 18:19
            Type listType = new TypeToken<ArrayList<EntryData>>(){}.getType();
            entryList = gson.fromJson(in, listType);

		} catch (FileNotFoundException e) {
            entryList = new ArrayList<EntryData>();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

    private void saveInFile() {
		try {
			FileOutputStream fos = openFileOutput(FILENAME,
					Context.MODE_PRIVATE); //Note "Context.MODE_PRIVATE" is default, i.e same as "0"
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(entryList, out);
            out.flush();

			fos.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException();
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}

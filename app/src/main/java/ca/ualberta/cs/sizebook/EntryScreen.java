package ca.ualberta.cs.sizebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

/**
 * The type Entry screen.
 */
public class EntryScreen extends Activity {

    private EditText nameText;
    private EditText dateText;
    private EditText neckText;
    private EditText bustText;
    private EditText chestText;
    private EditText waistText;
    private EditText hipText;
    private EditText inseamText;
    private EditText commentText;
    private static final String FILENAME = "file.sav";
    private ArrayList<EntryData> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entries);
        nameText = (EditText) findViewById(R.id.name);
        dateText = (EditText) findViewById(R.id.date);
        neckText = (EditText) findViewById(R.id.neck);
        bustText = (EditText) findViewById(R.id.bust);
        chestText = (EditText) findViewById(R.id.chest);
        waistText = (EditText) findViewById(R.id.waist );
        hipText = (EditText) findViewById(R.id.hip);
        inseamText = (EditText) findViewById(R.id.inseam);
        commentText = (EditText) findViewById(R.id.comment);
    }

    /**
     * Add entry.
     *
     * @param view the view
     */
    public void addEntry(View view) {

        String currentInput = "";

        try {
            Context context = getApplicationContext();

            String name = nameText.getText().toString();

            if (name.length() == 0){
                Toast.makeText(context, "Failed to create Entry. Name field is required.", Toast.LENGTH_LONG).show();

            }else {
                EntryData entryData = new EntryData(name);

                entryData.setDate(dateText.getText().toString());

                currentInput = "Neck";
                entryData.setNeck(neckText.getText().toString());

                currentInput = "Bust";
                entryData.setBust(bustText.getText().toString());

                currentInput = "Chest";
                entryData.setChest(chestText.getText().toString());

                currentInput = "Waist";
                entryData.setWaist(waistText.getText().toString());

                currentInput = "Hip";
                entryData.setHip(hipText.getText().toString());

                currentInput = "Inseam";
                entryData.setInseam(inseamText.getText().toString());

                currentInput = "Comment";
                String comment = commentText.getText().toString();
                entryData.setComment(comment);


                Toast.makeText(context, "Entry successfully created.", Toast.LENGTH_LONG).show();

                loadFromFile();

                entryList.add(entryData);

                saveInFile();

            }


        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (InputException e) {

            Context context = getApplicationContext();
            Toast.makeText(context, "Failed to create entry. "+currentInput+" value is invalid.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }  catch (InputDateException e) {

            Context context = getApplicationContext();
            Toast.makeText(context, "Failed to create entry. Date is not valid", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        finish();

    }

    /**
     * Cancel entry.
     *
     * @param view the view
     */
    public void cancelEntry (View view) {
        finish();
    }


    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput("file.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            //Taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            //2017-01-24 18:19

            Type listType = new TypeToken<ArrayList<EntryData>>(){}.getType();
            entryList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            entryList = new ArrayList<EntryData>();
        }

    }

    private void saveInFile(){

        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
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

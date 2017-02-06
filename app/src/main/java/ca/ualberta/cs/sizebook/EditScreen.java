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
 * The type Edit screen.
 */
public class EditScreen extends Activity {

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
    private int editPosition;
    private ArrayList<EntryData> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        nameText = (EditText) findViewById(R.id.edit_name);
        dateText = (EditText) findViewById(R.id.edit_date);
        neckText = (EditText) findViewById(R.id.edit_neck);
        bustText = (EditText) findViewById(R.id.edit_bust);
        chestText = (EditText) findViewById(R.id.edit_chest);
        waistText = (EditText) findViewById(R.id.edit_waist );
        hipText = (EditText) findViewById(R.id.edit_hip);
        inseamText = (EditText) findViewById(R.id.edit_inseam);
        commentText = (EditText) findViewById(R.id.edit_comment);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        editPosition = bundle.getInt("current_pos");

        String name_string = bundle.getString("current_name");
        String date_string = bundle.getString("current_date");
        String comment_string = bundle.getString("current_comment");
        nameText.setText(name_string);
        dateText.setText(date_string);
        commentText.setText(comment_string);


        if (Double.parseDouble(bundle.getString("current_neck")) > 0) {
            neckText.setText(bundle.getString("current_neck"));
        }

        if (Double.parseDouble(bundle.getString("current_bust")) > 0) {
            bustText.setText(bundle.getString("current_bust"));
        }

        if (Double.parseDouble(bundle.getString("current_chest")) > 0) {
            chestText.setText(bundle.getString("current_chest"));
        }

        if (Double.parseDouble(bundle.getString("current_waist")) > 0) {
            waistText.setText(bundle.getString("current_waist"));
        }

        if (Double.parseDouble(bundle.getString("current_hip")) > 0) {
            hipText.setText(bundle.getString("current_hip"));
        }

        if (Double.parseDouble(bundle.getString("current_inseam")) > 0) {
            inseamText.setText(bundle.getString("current_inseam"));
        }

    }

    /**
     * Add edit.
     *
     * @param view the view
     */
    public void addEdit(View view) {

        String currentInput = "";

        try {
            Context context = getApplicationContext();

            String name = nameText.getText().toString();

            if (name.length() == 0){
                Toast.makeText(context, "Failed to edit Entry. Name field is required.", Toast.LENGTH_LONG).show();

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


                Toast.makeText(context, "Entry successfully edited.", Toast.LENGTH_LONG).show();

                loadFromFile();

                entryList.set(editPosition, entryData);

                saveInFile();

            }


        } catch (NullPointerException e) {
            e.printStackTrace();

        } catch (InputException e) { //exception when parse string to double

            Context context = getApplicationContext();
            Toast.makeText(context, "Failed to edit entry. "+currentInput+" value is invalid.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }  catch (InputDateException e) {

            Context context = getApplicationContext();
            Toast.makeText(context, "Failed to edit entry. Date is not valid", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

        finish();

    }

    /**
     * Cancel edit.
     *
     * @param view the view
     */
    public void cancelEdit (View view) {
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

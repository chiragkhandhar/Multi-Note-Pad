package ml.chiragkhandhar.multinodepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNotes extends AppCompatActivity {

    private static final String TAG = "EditNotes";
    private EditText title, desc;
    private TextView date, counter;
    private Notes n;
    private String bT = "",bD = "", aT="",aD="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        setupComps();

        title.setTextIsSelectable(true);
        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setTextIsSelectable(true);

        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                int length = desc.length();
                counter.setText(length +" "+getString(R.string.chars));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupComps()
    {
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        date = findViewById(R.id.dateVH);
        counter = findViewById(R.id.counter);
    }

//    @Override
//    protected void onResume()
//    {
//        n = loadFile();
//        if (n != null)
//        {
//            bT = n.getTitle();
//            bD = n.getDesc();
//            title.setText(n.getTitle());
//            desc.setText(n.getDesc());
//            date.setText(n.getDate());
//        }
//
//        super.onResume();
//    }

    private Notes loadFile()
    {
        n = new Notes();
        try
        {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            // Creating JSON Object
            JSONObject jo = new JSONObject(sb.toString());
            String title = jo.getString("title");
            String desc = jo.getString("desc");
            String date = jo.getString("date");

            n.setTitle(title);
            n.setDesc(desc);
            n.setDate(date);

        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return n;
    }

//    @Override
//    protected void onPause()
//    {
//        Date d;
//
//        aT = title.getText().toString();
//        aD = desc.getText().toString();
//        n.setTitle(aT);
//        n.setDesc(aD);
//        Log.d(TAG, "bp: bT = "+bT+" bD = "+bD+" aT = "+aT+" aD = "+aD);
//
//        if(bT.equals("") && bD.equals("") && aT.equals("") && aD.equals(""))
//        {
//            n.setDate("");
//            Log.d(TAG, "bp: Date Not Updated");
//        }
//        else if(!bT.equals(aT) || !bD.equals(aD))
//        {
//            d = new Date();
//            SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm:ss a ");
//            n.setDate(ft.format(d));
//            Log.d(TAG, "bp: Date Updated.");
//        }
//
//        saveNotes();
//        super.onPause();
//    }

    private void saveNotes()
    {
        try
        {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("title").value(n.getTitle());
            writer.name("desc").value(n.getDesc());
            writer.name("date").value(n.getDate());
            writer.endObject();
            writer.close();
            Toast.makeText(this,"Notes Saved",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.svBtn:
                if (title.getText().toString().equals("") && desc.getText().toString().equals(""))
                {
                    finish();

                }
                else if (title.getText().toString().equals(""))
                {
                    finish();
                }
                else if (desc.getText().toString().equals(""))
                {
                    finish();
                }
                else
                {
                    saveClicked();
                }
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void saveClicked()
    {
        Date d = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm:ss a ");

        Log.d(TAG, "saveClicked: bp: Executed saveNotes()");
        Intent data = new Intent();
        data.putExtra("title", title.getText().toString());
        data.putExtra("desc",desc.getText().toString());
        data.putExtra("date",ft.format(d));
        setResult(RESULT_OK,data);
        finish();

    }
}
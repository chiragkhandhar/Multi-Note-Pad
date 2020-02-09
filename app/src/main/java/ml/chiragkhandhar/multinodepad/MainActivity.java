package ml.chiragkhandhar.multinodepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private EditText title, desc;
    private TextView date;
    private Notes n;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupComps();

        title.setTextIsSelectable(true);
        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setTextIsSelectable(true);
    }

    private void setupComps()
    {
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        date = findViewById(R.id.date);
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "lf: Entering on Resume() ");

        n = loadFile();
        Log.d(TAG, "lf: Title: "+n.getTitle());
        Log.d(TAG, "lf: Desc: "+n.getDesc());
//        Log.d(TAG, "lf: Date: "+n.getLut());
        if (n != null)
        {
            Log.d(TAG, "lf: Entering onResume|if| ");
            title.setText(n.getTitle());
            desc.setText(n.getDesc());
//            date.setText(n.getLut().toString());
            Log.d(TAG, "lf: Leaving onResume|if| ");

        }
        Log.d(TAG, "lf: Leaving on Resume() ");
        super.onResume();
    }

    private Notes loadFile()
    {
        Log.d(TAG, "lf: Entering loadFile() ");
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
//            String sdate = jo.getString("date");
//            Date dt = new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
            n.setTitle(title);
            n.setDesc(desc);
//            n.setLut(dt);

        }
        catch (FileNotFoundException e)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Log.d(TAG, "lf: Leaving loadFile() ");
        return n;


    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "lf: Entering onPause() ");
        n.setTitle(title.getText().toString());
        n.setDesc(desc.getText().toString());
//        n.setLut(new Date());
        saveNotes();
        Log.d(TAG, "lf: Leaving onPause() ");
        Log.d(TAG, "lf: Title: "+n.getTitle());
        Log.d(TAG, "lf: Desc: "+n.getDesc());
//        Log.d(TAG, "lf: Date: "+n.getLut());
        super.onPause();
    }

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
//            writer.name("date").value(n.getLut().toString());
            writer.endObject();
            writer.close();
            Toast.makeText(this,"Notes Saved",Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}

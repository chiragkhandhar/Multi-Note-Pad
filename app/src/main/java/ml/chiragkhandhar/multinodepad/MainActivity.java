package ml.chiragkhandhar.multinodepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView rv;
    private static final int SV_RC = 1, ED_RC = 2;
    private ArrayList<Notes> notesArrayList = new ArrayList<>();
    private  NotesAdapter notesAdapter;
    private static final String TAG = "MainActivity";
    private Notes n;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupComps();


        notesAdapter = new NotesAdapter(notesArrayList,this);

        rv.setAdapter(notesAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        loadJsonFile();
    }

    private void setupComps()
    {
        rv = findViewById(R.id.recycler);
    }

    public void loadJsonFile()
    {
        try
        {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader jReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            jReader.beginArray();
            while (jReader.hasNext())
            {
                Notes n = new Notes();
                jReader.beginObject();
                while (jReader.hasNext())
                {
                    String name = jReader.nextName();
                    switch (name)
                    {
                        case "title":
                            n.setTitle(jReader.nextString());
                            break;
                        case "desc":
                            n.setDesc(jReader.nextString());
                            break;
                        case "date":
                            n.setDate(jReader.nextString());
                            break;
                        default:
                            jReader.skipValue();
                            break;
                    }
                }
                jReader.endObject();
                notesArrayList.add(n);
            }
            jReader.endArray();
        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.aboutOption:
                break;
            case R.id.newOption:
                Intent i = new Intent(this, EditNotes.class);
                startActivityForResult(i,SV_RC);
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch (requestCode)
        {
            case SV_RC:
                if(resultCode == RESULT_OK)
                {
                    Notes temp = new Notes();

                    temp.setTitle(data.getStringExtra("title"));
                    temp.setDesc(data.getStringExtra("desc"));
                    temp.setDate(data.getStringExtra("date"));
                    notesArrayList.add(0,temp);

                    Log.d(TAG, "onActivityResult: bp:  Title: " + data.getStringExtra("title"));
                    Log.d(TAG, "onActivityResult: bp: Desc: " + data.getStringExtra("desc"));
                    Log.d(TAG, "onActivityResult: bp: Date: " + data.getStringExtra("date"));
                    notesAdapter.notifyDataSetChanged();
                }
                break;
            case ED_RC:
                break;
            default:
                Log.d(TAG, "onActivityResult: Request Code" + requestCode);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonFile();
    }

    public void writeJsonFile()
    {
        try
        {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            jWriter.setIndent(" ");
            jWriter.beginArray();
            for (Notes n : notesArrayList)
            {
                jWriter.beginObject();
                jWriter.name("title").value(n.getTitle());
                jWriter.name("desc").value(n.getDesc());
                jWriter.name("date").value(n.getDate());
                jWriter.endObject();
            }
            jWriter.endArray();
            jWriter.close();
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }
}

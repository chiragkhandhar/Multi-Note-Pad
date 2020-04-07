package ml.chiragkhandhar.multinodepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{
    private RecyclerView rv;
    private FloatingActionButton newNote;
    private static final int SV_RC = 1, ED_RC = 2;
    private ArrayList<Notes> notesArrayList = new ArrayList<>();
    private  NotesAdapter notesAdapter;
    private static final String TAG = "MainActivity";

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
        updateTitle();

    }


    public void updateTitle()
    {
        int totalNotes = notesArrayList.size();
        if (totalNotes != 0)
            setTitle(getString(R.string.app_name)+ " [" + totalNotes + "]");
        else
            setTitle(getString(R.string.app_name));
    }

    @Override
    public void onClick(View view)
    {
        int pos = rv.getChildLayoutPosition(view);
        Notes n = notesArrayList.get(pos);
        Intent data = new Intent(this, EditNotes.class);
        data.putExtra("noteData", n);
        data.putExtra("position", pos);
        startActivityForResult(data,ED_RC);
    }

    @Override
    public boolean onLongClick(View view)
    {
        deleteAlert(view);
        return true;
    }

    private void deleteAlert(final View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                int pos = rv.getChildLayoutPosition(v);
                notesArrayList.remove(pos);
                notesAdapter.notifyDataSetChanged();
                updateTitle();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

            }
        });

        builder.setTitle("Delete this note?");
        builder.setMessage("This action cannot be undone.");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setupComps()
    {
        rv = findViewById(R.id.recycler);
        newNote = findViewById(R.id.newNote);
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

    public void newNoteClicked(View v)
    {
        Intent i2 = new Intent(this, EditNotes.class);
        startActivityForResult(i2,SV_RC);
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
                Intent i1 = new Intent(this,AboutActivity.class);
                startActivity(i1);
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            switch (requestCode) {
                case SV_RC:
                    if (resultCode == RESULT_OK) {
                        Notes temp = new Notes();
                        assert data != null;
                        temp.setTitle(data.getStringExtra("title"));
                        temp.setDesc(data.getStringExtra("desc"));
                        temp.setDate(data.getStringExtra("date"));
                        notesArrayList.add(0, temp);
                        notesAdapter.notifyDataSetChanged();
                    }
                    break;
                case ED_RC:
                    if (resultCode == RESULT_OK) {
                        Notes temp = new Notes();
                        assert data != null;
                        temp.setTitle(data.getStringExtra("title"));
                        temp.setDesc(data.getStringExtra("desc"));
                        temp.setDate(data.getStringExtra("date"));
                        notesArrayList.remove(data.getIntExtra("position", -1));
                        notesArrayList.add(0, temp);
                        notesAdapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Request Code" + requestCode);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Null Pointer encountered.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume()
    {
        updateTitle();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonFile();
        updateTitle();
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
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
    private TextView counter;
    private Notes n;
    private String bT = "",bD = "", aT="",aD="";
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        setupComps();

        title.setTextIsSelectable(true);
        desc.setMovementMethod(new ScrollingMovementMethod());
        desc.setTextIsSelectable(true);

        Intent data = getIntent();
        if (data.hasExtra("noteData"))
        {
            n = (Notes) data.getSerializableExtra("noteData");
            if (n != null)
            {
                title.setText(n.getTitle());
                desc.setText(n.getDesc());
                bT = n.getTitle();
                bD = n.getDesc();
            }
        }
       
        if (data.hasExtra("position"))
        {
            pos = (int) data.getIntExtra("position",-1);
        }
        updateCounter();
        desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                updateCounter();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateCounter()
    {
        int length = desc.length();
        if (length!=0)
            counter.setText(length +" "+getString(R.string.chars));
        else
            counter.setText("");
    }

    private void setupComps()
    {
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        counter = findViewById(R.id.counter);
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
                    Toast.makeText(this,getString(R.string.noNotes),Toast.LENGTH_LONG).show();
                    finish();

                }
                else if (title.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noTitle),Toast.LENGTH_LONG).show();
                    finish();
                }
                else if (desc.getText().toString().equals(""))
                {
                    Toast.makeText(this,getString(R.string.noDesc),Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    if(detectChange())
                        saveClicked();
                    else
                        finish();
                }
                break;
            default:
                Toast.makeText(this,"Invalid Option",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean detectChange()
    {
        aT = title.getText().toString();
        aD = desc.getText().toString();

        if(bT.equals("") && bD.equals("") && aT.equals("") && aD.equals(""))
            return false;
        else if(!bT.equals(aT) || !bD.equals(aD))
            return true;
        else
            return false;

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

        Intent data = new Intent();
        data.putExtra("title", title.getText().toString());
        data.putExtra("desc",desc.getText().toString());
        data.putExtra("date",ft.format(d));
        if(pos!=-1)
            data.putExtra("position",pos);
        setResult(RESULT_OK,data);
        finish();
    }
}
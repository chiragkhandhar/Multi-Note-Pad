package ml.chiragkhandhar.multinodepad;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder>
{
    private static final String TAG = "NotesAdapter";
    private List<Notes> noteList;
    private  MainActivity mainAct;

    NotesAdapter(List<Notes> noteList, MainActivity mainAct) {
        this.noteList = noteList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_entry,parent,false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) 
    {
        Log.d(TAG, "onBindViewHolder: ");
        Notes n = noteList.get(position);
        holder.noteTitleVH.setText(n.getTitle());
        holder.noteDescVH.setText(n.getDesc());
        holder.dateVH.setText(n.getDate());

    }

    @Override
    public int getItemCount() 
    {
        Log.d(TAG, "getItemCount: ");
        return noteList.size();
    }
}

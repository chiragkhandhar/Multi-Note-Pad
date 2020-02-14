package ml.chiragkhandhar.multinodepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class NoteViewHolder extends RecyclerView.ViewHolder
{
    TextView noteTitleVH, dateVH, noteDescVH;

    NoteViewHolder(View itemView)
    {
        super(itemView);
        noteTitleVH = itemView.findViewById(R.id.noteTitleVH);
        dateVH = itemView.findViewById(R.id.dateVH);
        noteDescVH = itemView.findViewById(R.id.noteDescVH);
    }

}

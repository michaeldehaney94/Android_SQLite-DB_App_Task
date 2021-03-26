package android.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {
    private static final String TAG = WordListAdapter.class.getSimpleName();

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";

    private final LayoutInflater mInflater;
    Context mContext;

    WordListOpenHelper mDB;

    public WordListAdapter(Context context, WordListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        //holder.wordItemView.setText("placeholder");
        WordItem current = mDB.query(position);
        holder.wordItemView.setText(current.getmWord());

        final WordViewHolder h = holder;
        holder.delete_button.setOnClickListener(
                new MyButtonOnClickListener(current.getId(), null) {
                    @Override
                    public void onClick(View v ) {
                        int deleted = mDB.delete(id);
                        if (deleted >= 0)
                            notifyItemRemoved(h.getAdapterPosition());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }

    //view holder with a text view and two buttons.
    public class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        Button delete_button;
        Button edit_button;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            wordItemView = (TextView) itemView.findViewById(R.id.word);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);
        }
    }
}

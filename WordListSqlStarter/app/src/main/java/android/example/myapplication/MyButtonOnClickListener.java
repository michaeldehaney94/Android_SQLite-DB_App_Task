package android.example.myapplication;

import android.view.View;

public class MyButtonOnClickListener implements View.OnClickListener{
    private static final String TAG = View.OnClickListener.class.getSimpleName();

    int id;
    String word;

    public MyButtonOnClickListener(int id, String word) {
        this.id = id;
        this.word = word;
    }

    @Override
    public void onClick(View view) {
        // Implemented in WordListAdapter

    }
}

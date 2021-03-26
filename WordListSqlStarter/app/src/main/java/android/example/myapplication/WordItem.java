package android.example.myapplication;

//A data model is a class that encapsulates a complex data structure and provides an API for
// accessing and manipulating the data in that structure. You need a data model to pass data
// retrieved from the database to the UI.

public class WordItem {
    private int mId;
    private String mWord;

    public WordItem() {

    }

    public int getId(){
        return this.mId;
    }

    public String getmWord() {
        return mWord;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public void setWord(String mWord) {
        this.mWord = mWord;
    }


}

package info.corvax.android.testapp;

public class Note {
    private long id;
    private String title;
    private String text;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return  text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // will be used for ArrayAdapter
    @Override
    public String toString() {
        return title;
    }
}

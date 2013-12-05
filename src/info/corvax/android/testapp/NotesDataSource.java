package info.corvax.android.testapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotesDataSource {
    // database objects
    private SQLiteDatabase database;
    private NotesDbHelper dbHelper;
    private String[] allColumns = {
            NotesDbHelper.NotesEntry._ID,
            NotesDbHelper.NotesEntry.COLUMN_NAME_TITLE,
            NotesDbHelper.NotesEntry.COLUMN_NAME_TEXT
    };

    // Create()
    public NotesDataSource(Context context) {
        dbHelper = new NotesDbHelper(context);
    }

    // mapper for the Note class
    private Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setTitle(cursor.getString(1));
        note.setText(cursor.getString(2));
        return note;
    }

    // open database
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Note createNote(String title, String text) {
        // create values container
        ContentValues values = new ContentValues();

        // put column name and the value into the container
        values.put(NotesDbHelper.NotesEntry.COLUMN_NAME_TITLE, title);
        values.put(NotesDbHelper.NotesEntry.COLUMN_NAME_TEXT, text);

        // insert the data
        long insertId = database.insert(NotesDbHelper.NotesEntry.TABLE_NAME, null, values);

        // read the data back from the database
        Cursor cursor = database.query(
                NotesDbHelper.NotesEntry.TABLE_NAME,
                allColumns,
                NotesDbHelper.NotesEntry._ID + '=' + insertId,
                null,
                null,
                null,
                null);

        cursor.moveToFirst();

        // create Note instance and fill with data
        Note newNote = cursorToNote(cursor);

        // close cursor
        cursor.close();

        // return Note instance
        return newNote;
    }

    public void deleteNote(Note note) {
        // get primary key
        long id = note.getId();

        // delete record
        database.delete(
                NotesDbHelper.NotesEntry.TABLE_NAME,
                NotesDbHelper.NotesEntry._ID + '=' + id,
                null);

    }

    public List<Note> getAllNotes() {
        // create an empty list of Note
        List<Note> noteList = new ArrayList<Note>();

        // get the data from the database
        Cursor cursor = database.query(
                NotesDbHelper.NotesEntry.TABLE_NAME,
                allColumns,
                null,
                null,
                null,
                null,
                null);

        // for every record do
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // fill the class with data
            Note note = cursorToNote(cursor);
            // add to the list
            noteList.add(note);
            // next
            cursor.moveToNext();
        }

        // close the cursor
        cursor.close();

        // return the list
        return noteList;
    }
}

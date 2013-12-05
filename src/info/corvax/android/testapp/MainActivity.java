package info.corvax.android.testapp;

import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.app.ListActivity;
import android.database.SQLException;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	// database access
    private NotesDataSource notesDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }

        // create helpers
        notesDataSource = new NotesDataSource(this);
        // open the database
        try {
			notesDataSource.open();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // read all records into a list object
        List<Note> values = notesDataSource.getAllNotes();

        // use SimpleCursorAdapter to show the elements in the list
        ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                values);

        setListAdapter(adapter);
    }

    // onClick handler for the buttons
    public void onClick(View view) {
        // what is this for?
        //@SuppressWarnings("unchecked")

        // get adapter
        ArrayAdapter<Note> adapter = (ArrayAdapter<Note>) getListAdapter();

        // init
        Note note = null;

        // check the buttons pressed
        switch (view.getId()) {
            case R.id.btn_add:
                // add a random number to the string
                int nextId = new Random().nextInt(1000);

                // create a note object
                note = notesDataSource.createNote("Title " + nextId, "This is a text " + nextId);

                // add to the list
                adapter.add(note);

                break;

            case R.id.btn_del:
                // check if elements exist
                if (getListAdapter().getCount() > 0) {
                    // get the first element from the list
                    note = (Note) getListAdapter().getItem(0);

                    // delete from the database
                    notesDataSource.deleteNote(note);

                    // delete from the list
                    adapter.remove(note);
                }
                break;
        }
        // notify the database about the changes
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        notesDataSource.close();
        super.onPause();
    }

    @Override
    public void onResume() {
        try {
			notesDataSource.open();
		} catch (java.sql.SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        super.onResume();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
}

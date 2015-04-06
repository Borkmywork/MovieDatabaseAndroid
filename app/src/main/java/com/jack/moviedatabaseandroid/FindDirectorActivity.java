package com.jack.moviedatabaseandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Jack on 3/30/15.
 */
public class FindDirectorActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_director);


        //Results List
        final ListView listDirectorResults = (ListView) findViewById(R.id.listDirectorResults);

        //Example list of movies
        String[] movies = new String[]{
                "Movie 1",
                "Movie 2",
                "Movie 3",
                "Movie 4",
                "Movie 5",
                "Movie 6",
                "Movie 7",
                "Movie 8",
                "Movie 9",
                "Movie 10"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listDirectorResults.setAdapter(adapter);


        //ListView item click listener
        listDirectorResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get item value
                String itemValue = (String) listDirectorResults.getItemAtPosition(position);

                //Toast
                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });

        //Search button
        final Button btnDirectorSearch = (Button) findViewById(R.id.btnDirectorSearch);

        btnDirectorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the search button is pressed
                listDirectorResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        //Text Changed Listener
        final EditText editTxtDirectorName = (EditText) findViewById(R.id.editTxtDirectorName);
        editTxtDirectorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTxtDirectorName.getText().toString().isEmpty())
                    btnDirectorSearch.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    btnDirectorSearch.setEnabled(false);    //Disable button if EditText is empty
                    listDirectorResults.setVisibility(View.INVISIBLE);      //Hide ListView
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_director, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

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
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by Bence on 4/15/15.
 */
public class SearchMovie extends Activity{

    ListView listMovieResults;
    Button buttonMovieSearch;
    EditText editTxtMovieName;
    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_movie);


        //Results List
        listMovieResults = (ListView) findViewById(R.id.listMovieResults);
        //listProducerResults = (ListView) findViewById(R.id.listProducerResults);
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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listMovieResults.setAdapter(adapter);


        //ListView item click listener
        listMovieResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get item value
                String itemValue = (String) listMovieResults.getItemAtPosition(position);

                //Toast
                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });

        //Search button
        buttonMovieSearch = (Button) findViewById(R.id.buttonMovieSearch);

        buttonMovieSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the search button is pressed
                listMovieResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        //Text Changed Listener
        editTxtMovieName = (EditText) findViewById(R.id.editTxtMovieName);
        editTxtMovieName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTxtMovieName.getText().toString().isEmpty())
                    buttonMovieSearch.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    buttonMovieSearch.setEnabled(false);    //Disable button if EditText is empty
                    listMovieResults.setVisibility(View.INVISIBLE);      //Hide ListView
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
        getMenuInflater().inflate(R.menu.menu_find_movie, menu);
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

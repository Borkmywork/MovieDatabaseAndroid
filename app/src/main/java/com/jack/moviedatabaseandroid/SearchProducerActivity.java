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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class SearchProducerActivity extends Activity {

    ListView listProducerResults;
    Button btnProducerSearch;
    AutoCompleteTextView editTxtProducerName;
    ArrayList<String> producers = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_producer);

        //Add producer names for the auto-complete search
        producers.add("Nolan");
        producers.add("Jack");
        producers.add("Bennett");
        producers.add("Dan");
        producers.add("Cruze");
        producers.add("Bence");

        //Results List
        listProducerResults = (ListView) findViewById(R.id.listProducerResults);
        //Search button
        btnProducerSearch = (Button) findViewById(R.id.btnProducerSearch);
        //Producer search field
        editTxtProducerName = (AutoCompleteTextView) findViewById(R.id.editTxtProducerName);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);


        //Assign adapter to ListView
        listProducerResults.setAdapter(adapter);

        //ListView item click listener
        listProducerResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get item value
                String itemValue = (String) listProducerResults.getItemAtPosition(position);

                //Toast
                Toast.makeText(getApplicationContext(), itemValue + " by " + editTxtProducerName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnProducerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the search button is pressed
                listProducerResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        //Text Changed Listener
        editTxtProducerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If name in producers array is already typed, close the dropdown
                if (producers.contains(editTxtProducerName.getText().toString()))
                    editTxtProducerName.dismissDropDown();

                listProducerResults.setVisibility(View.INVISIBLE);      //Hide ListView
                if (!editTxtProducerName.getText().toString().isEmpty())
                    btnProducerSearch.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    btnProducerSearch.setEnabled(false);    //Disable button if EditText is empty
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * DROP DOWN AUTO-COMPLETE SEARCH
         */
        //Adapter to use the producers array with the search list
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, producers);
        //Set the adapter to the search list
        editTxtProducerName.setAdapter(autoCompleteAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_producer, menu);
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
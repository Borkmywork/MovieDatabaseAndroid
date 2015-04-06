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


public class SearchProducerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_producer);

        //Results List
        final ListView listProducerResults = (ListView) findViewById(R.id.listProducerResults);

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
        listProducerResults.setAdapter(adapter);

        //ListView item click listener
        listProducerResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get item value
                String itemValue = (String) listProducerResults.getItemAtPosition(position);

                //Toast
                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
            }
        });

        //Search button
        final Button btnProducerSearch = (Button) findViewById(R.id.btnProducerSearch);

        btnProducerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the search button is pressed
                listProducerResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        //Text Changed Listener
        final EditText editTxtProducerName = (EditText) findViewById(R.id.editTxtProducerName);
        editTxtProducerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!editTxtProducerName.getText().toString().isEmpty())
                    btnProducerSearch.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    btnProducerSearch.setEnabled(false);    //Disable button if EditText is empty
                    listProducerResults.setVisibility(View.INVISIBLE);      //Hide ListView
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
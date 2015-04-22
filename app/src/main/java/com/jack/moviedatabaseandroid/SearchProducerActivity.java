package com.jack.moviedatabaseandroid;

import android.app.Activity;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;


public class SearchProducerActivity extends Activity {

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    ListView listProducerResults;
    AutoCompleteTextView editTxtProducerName;
    Button btnProducerSearch;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;


    ArrayList<String> producers = new ArrayList<>();
    ArrayAdapter<String> autoCompleteAdapter;
    ProgressBar bar;
    String typedText;

    int count;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_producer);

        bar = (ProgressBar) findViewById(R.id.progressBarProducer);

        //Results List
        listProducerResults = (ListView) findViewById(R.id.listProducerResults);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listProducerResults.setAdapter(mAdapter);


        //ListView item click listener
//        listActorResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Get item value
//                String itemValue = (String) listActorResults.getItemAtPosition(position);
//
//                //Toast
//                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
//            }
//        });

        //Search button
        btnProducerSearch = (Button) findViewById(R.id.btnProducerSearch);
        editTxtProducerName = (AutoCompleteTextView) findViewById(R.id.editTxtProducerName);

        //DropDown adapter
        autoCompleteAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, producers);
        //Set the adapter to the search list
        editTxtProducerName.setAdapter(autoCompleteAdapter);


        btnProducerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.clear();
                mAdapter.notifyDataSetChanged();
                //When the search button is pressed
                fullName = editTxtProducerName.getText().toString();
                String[] splited = fullName.split(" ");
                firstName = splited[0];
                lastName = splited[splited.length - 1];
                lastCommaFirst = "'" + lastName + ", " + firstName + "'";

                bar.setVisibility(View.VISIBLE);
                excecuteQuery();
                listProducerResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        count = 0;

        editTxtProducerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                newTime = System.currentTimeMillis() / 1000L;
                typedText = editTxtProducerName.getText().toString();
                //execute background dropdown
//                if(oldTime != 0 && (newTime - oldTime) >= 3) {
                producers.clear();
                excecuteDropdown();
//                }

//                oldTime = newTime;




                //If name in producers array is already typed, close the dropdown
                if (producers.contains(editTxtProducerName.getText().toString()))
                    // editTxtActorName.dismissDropDown();

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




    }

    public void excecuteQuery() {
        new ProducerDBConnect().execute("");
    }

    public void excecuteDropdown() {
        new DropdownDBConnect().execute("");
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


    private class ProducerDBConnect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String entry = "";
            try {

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(url, user, pass);

            } catch (java.sql.SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            try {

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT distinct title.title FROM cast_info\n" +
                        "INNER JOIN name ON name.id=person_id\n" +
                        "INNER JOIN title on title.id=cast_info.movie_id\n" +
                        "WHERE title.kind_id = 1 AND name.name LIKE " + lastCommaFirst + " AND cast_info.role_id = 3\n" +
                        "ORDER BY title.title");
                while (rs.next()) {
                    movies.add(rs.getString(1));
                    System.out.println(rs.getString(1));
                }
                rs.close();
                stmt.close();
                conn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
            if(movies.size() == 0){
                Toast.makeText(getApplicationContext(), "Nothing found...", Toast.LENGTH_SHORT).show();
            }
            bar.setVisibility(View.GONE);
        }
    }

    private class DropdownDBConnect extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String entry = "";
            try {

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection(url, user, pass);

            } catch (java.sql.SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            try {

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT name.name FROM cast_info INNER JOIN name ON cast_info.person_id = name.id WHERE name.name LIKE '%" + typedText + "' AND cast_info.role_id = 3 ORDER BY name.name LIMIT 5");
                while (rs.next()) {
                    String name = rs.getString(1);
                    String[] both = name.split(",");
                    String fl;
                    try {
                        fl = both[1] + " " + both[0];
                    }catch (ArrayIndexOutOfBoundsException e){
                        fl = both[0];
                    }
                    if(!producers.contains(fl))
                        producers.add(fl);
                    System.out.println(fl);
                }
                rs.close();
                stmt.close();
                conn.close();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateDropdown();
        }
    }

    private void updateDropdown() {
        autoCompleteAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, producers);
        //Set the adapter to the search list
        editTxtProducerName.setAdapter(autoCompleteAdapter);

        if(producers.size() < 40)
            editTxtProducerName.setThreshold(1);
        else
            editTxtProducerName.setThreshold(2);

        editTxtProducerName.showDropDown();
    }





}
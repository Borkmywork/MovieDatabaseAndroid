package com.jack.moviedatabaseandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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


/**
 * Created by Dan on 3/30/15.
 */
public class FindActorActivity extends Activity{

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    ListView listActorResults;
    AutoCompleteTextView editTxtActorName;
    Button btnfindactor;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;
    static Timer timer;


    ArrayList<String> actors = new ArrayList<>();
    ArrayAdapter<String> autoCompleteAdapter;
    ProgressBar bar;
    String typedText;

    DropdownDBConnect dropAsync;
    int count;

    long oldTime = 0;
    long newTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_actor);

        bar = (ProgressBar) findViewById(R.id.progressBarActor);
        //Search button
        btnfindactor = (Button) findViewById(R.id.btnfindactor);
        editTxtActorName = (AutoCompleteTextView) findViewById(R.id.editTxtActorName);



        //Results List
        listActorResults = (ListView) findViewById(R.id.listActorResults);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listActorResults.setAdapter(mAdapter);


        //DropDown adapter
        autoCompleteAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, actors);
        //Set the adapter to the search list
        editTxtActorName.setAdapter(autoCompleteAdapter);


        btnfindactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.clear();
                mAdapter.notifyDataSetChanged();
                //When the search button is pressed
                fullName = editTxtActorName.getText().toString();
                String[] splited = fullName.split(" ");
                firstName = splited[0];
                lastName = splited[splited.length - 1];
                lastCommaFirst = "'" + lastName + ", " + firstName + "'";

                bar.setVisibility(View.VISIBLE);
                excecuteQuery();
                listActorResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        count = 0;

        editTxtActorName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                typedText = editTxtActorName.getText().toString();

                //execute background dropdown
                actors.clear();
                excecuteDropdown();


                //If name in producers array is already typed, close the dropdown
                if (actors.contains(editTxtActorName.getText().toString()))
                    // editTxtActorName.dismissDropDown();

                    listActorResults.setVisibility(View.INVISIBLE);      //Hide ListView
                if (!editTxtActorName.getText().toString().isEmpty())
                    btnfindactor.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    btnfindactor.setEnabled(false);    //Disable button if EditText is empty
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void excecuteQuery() {
        new ActorDBConnect().execute("");
    }

    public void excecuteDropdown() {
        new DropdownDBConnect().execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_actor, menu);
        return true;
    }



    private class ActorDBConnect extends AsyncTask<String, Void, Void> {

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
                        "WHERE title.kind_id = 1 AND name.name LIKE " + lastCommaFirst + " AND cast_info.role_id = 1\n" +
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
                ResultSet rs = stmt.executeQuery("SELECT name.name FROM cast_info INNER JOIN name ON cast_info.person_id = name.id WHERE name.name LIKE '%" + typedText + "' AND cast_info.role_id = 1 ORDER BY name.name LIMIT 5");
                while (rs.next()) {
                    String name = rs.getString(1);
                    String[] both = name.split(",");
                    String fl;
                    try {
                        fl = both[1] + " " + both[0];
                    }catch (ArrayIndexOutOfBoundsException e){
                        fl = both[0];
                    }
                    if(!actors.contains(fl))
                        actors.add(fl);
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
                this, android.R.layout.simple_dropdown_item_1line, actors);
        //Set the adapter to the search list
        editTxtActorName.setAdapter(autoCompleteAdapter);

        if(actors.size() < 40)
            editTxtActorName.setThreshold(1);
        else
            editTxtActorName.setThreshold(2);

        editTxtActorName.showDropDown();
    }





}

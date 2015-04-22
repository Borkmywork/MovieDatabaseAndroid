package com.jack.moviedatabaseandroid;

/**
 * Created by cruzegoodin on 4/22/15.
 */

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

public class FindMovieActivity extends Activity{

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    ListView listMovieResults;
    AutoCompleteTextView editTxtMovieName;
    Button buttonMovieSearch;
    String movieName;

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
        setContentView(R.layout.find_movie);

        bar = (ProgressBar) findViewById(R.id.progressBarActor);
        actors.add("John");

        //Results List
        listMovieResults = (ListView) findViewById(R.id.listMovieResults);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listMovieResults.setAdapter(mAdapter);


        //ListView item click listener
//        listMovieResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Get item value
//                String itemValue = (String) listMovieResults.getItemAtPosition(position);
//
//                //Toast
//                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
//            }
//        });

        //Search button
        buttonMovieSearch = (Button) findViewById(R.id.buttonMovieSearch);
        editTxtMovieName = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        //DropDown adapter
        autoCompleteAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, actors);
        //Set the adapter to the search list
        editTxtMovieName.setAdapter(autoCompleteAdapter);


        buttonMovieSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.clear();
                mAdapter.notifyDataSetChanged();
                //When the search button is pressed
                movieName = editTxtMovieName.getText().toString();

                bar.setVisibility(View.VISIBLE);
                excecuteQuery();
                listMovieResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

        count = 0;

        editTxtMovieName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                newTime = System.currentTimeMillis() / 1000L;
                typedText = editTxtMovieName.getText().toString();
                //execute background dropdown
//                if(oldTime != 0 && (newTime - oldTime) >= 3) {
                actors.clear();
                excecuteDropdown();
//                }

//                oldTime = newTime;




                //If name in producers array is already typed, close the dropdown
                if (actors.contains(editTxtMovieName.getText().toString()))
                    // editTxtMovieName.dismissDropDown();

                    listMovieResults.setVisibility(View.INVISIBLE);      //Hide ListView
                if (!editTxtMovieName.getText().toString().isEmpty())
                    buttonMovieSearch.setEnabled(true);     //Enable button if EditText isn't empty
                else {
                    buttonMovieSearch.setEnabled(false);    //Disable button if EditText is empty
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
                        "WHERE title.kind_id = 1 AND name.name LIKE " + movieName + " AND cast_info.role_id = 1\n" +
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
        editTxtMovieName.setAdapter(autoCompleteAdapter);

        if(actors.size() < 40)
            editTxtMovieName.setThreshold(1);
        else
            editTxtMovieName.setThreshold(2);

        editTxtMovieName.showDropDown();
    }






}

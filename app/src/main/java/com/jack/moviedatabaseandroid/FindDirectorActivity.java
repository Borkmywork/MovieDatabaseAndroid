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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by YABOY CRUZE GOOOOODDDIIIIIIINNNN on 4/20/15.
 */
public class FindDirectorActivity extends Activity {

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    ListView listDirectorResults;
    EditText editTxtDirectorName;
    Button btnDirectorSearch;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_director);


        //Results List
        listDirectorResults = (ListView) findViewById(R.id.listDirectorResults);


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listDirectorResults.setAdapter(mAdapter);


        //ListView item click listener
//        listDirectorResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Get item value
//                String itemValue = (String) listDirectorResults.getItemAtPosition(position);
//
//                //Toast
//                Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
//            }
//        });

        //Search button
        btnDirectorSearch = (Button) findViewById(R.id.btnDirectorSearch);

        btnDirectorSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.clear();
                mAdapter.notifyDataSetChanged();
                //When the search button is pressed
                fullName = editTxtDirectorName.getText().toString();
                String[] splited = fullName.split(" ");
                firstName = splited[0];
                lastName = splited[splited.length - 1];
                lastCommaFirst = "'" + lastName + ", " + firstName + "'";

                excecuteQuery();
                //When the search button is pressed
                listDirectorResults.setVisibility(View.VISIBLE);    //Show ListView
            }
        });

//        //Text Changed Listener
//        final EditText editTxtDirectorName = (EditText) findViewById(R.id.editTxtDirectorName);
//        editTxtDirectorName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!editTxtDirectorName.getText().toString().isEmpty())
//                    btnDirectorSearch.setEnabled(true);     //Enable button if EditText isn't empty
//                else {
//                    btnDirectorSearch.setEnabled(false);    //Disable button if EditText is empty
//                    listDirectorResults.setVisibility(View.INVISIBLE);      //Hide ListView
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    public void excecuteQuery() {
        new DirectorDBConnect().execute("");
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

    private class DirectorDBConnect extends AsyncTask<String, Void, Void> {

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
                }
                rs.close();
                stmt.close();
                conn.close();
                System.out.println(entry);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
            if (movies.size() == 0) {
                Toast.makeText(getApplicationContext(), "Nothing found...", Toast.LENGTH_SHORT).show();
            }


        }
    }
}


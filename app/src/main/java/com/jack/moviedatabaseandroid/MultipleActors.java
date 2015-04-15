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
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemSelectedListener;


public class MultipleActors extends Activity {

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ListView movieList;
    EditText editActor1;
    EditText editActor2;
    EditText editActor3;
    EditText editActor4;
    EditText editActor5;
    Button search_button;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;
    EditText[] texts;
    ArrayList<EditText> visibleTexts;
    ArrayAdapter<String> mAdapter;
    int count;


    ArrayList<String> arrayOriginal;
    ArrayList<String> arrayDuplicates;
    int howMany;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mult_actor);

        texts = new EditText[5];
        visibleTexts = new ArrayList<>();

        editActor1 = (EditText) findViewById(R.id.actor1);
        texts[0] = editActor1;

        editActor2 = (EditText) findViewById(R.id.actor2);
        texts[1] = editActor2;

        editActor3 = (EditText) findViewById(R.id.actor3);
        texts[2] = editActor3;

        editActor4 = (EditText) findViewById(R.id.actor4);
        texts[3] = editActor4;

        editActor5 = (EditText) findViewById(R.id.actor5);
        texts[4] = editActor5;

        for(EditText each: texts){
            each.setVisibility(View.GONE);
        }

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                howMany = position + 1;
                makeEditsVisible();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Results List
        movieList = (ListView) findViewById(R.id.movieList);

        arrayOriginal = new ArrayList<>();
        arrayDuplicates = new ArrayList<>();


        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, arrayOriginal);

        //Assign adapter to ListView
        movieList.setAdapter(mAdapter);




        //Search button
        search_button = (Button) findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the search button is pressed
                movies.clear();
                mAdapter.notifyDataSetChanged();
                count = 0;
                for(EditText each: visibleTexts) {
                    fullName = each.getText().toString();
                    String[] splited = fullName.split(" ");
                    firstName = splited[0];
                    lastName = splited[splited.length - 1];
                    lastCommaFirst = "'" + lastName + ", " + firstName + "'";
                    excecuteQuery();
                    count++;
                }
                count = 0;
                movieList.setVisibility(View.VISIBLE);
            }
        });


    }

    private void makeEditsVisible() {
        for(EditText each: texts){
            each.setVisibility(View.GONE);
        }

        visibleTexts.clear();

        for(int i = 0; i < howMany; i++){
            texts[i].setVisibility(View.VISIBLE);
            visibleTexts.add(texts[i]);
        }
    }

    public void excecuteQuery() {
        new ActorDBConnect().execute("");
    }

    public void mergeLists(){
        for(String movie: arrayDuplicates){
            if(!arrayOriginal.contains(movie)){
                arrayOriginal.remove(movie);
            }
        }
        arrayDuplicates.clear();
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
                        "WHERE title.kind_id = 1 AND name.name LIKE " + lastCommaFirst + " AND cast_info.role_id = 1\n" +
                        "ORDER BY title.title");
                while (rs.next()) {
                    if(count == 0){
                        arrayOriginal.add(rs.getString(1));
                    } else {
                        arrayDuplicates.add(rs.getString(1));
                        mergeLists();
                    }
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
        }
    }

}
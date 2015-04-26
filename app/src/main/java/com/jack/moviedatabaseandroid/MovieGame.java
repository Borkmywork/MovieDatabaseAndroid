package com.jack.moviedatabaseandroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;

import org.apache.commons.collections4.bidimap.TreeBidiMap;



public class MovieGame extends Activity{

    String url = "jdbc:mysql://98.130.0.90:3306/pggarla_movies";
    String user = "pggarla_preader";
    String pass = "Csc4610mysql";
    Connection conn;
    ArrayList<String> movies = new ArrayList<>();
    ArrayAdapter<String> mAdapter;
    ListView listGameResults;
    EditText editTextGame;
    Button btnMovieGame;
    String fullName;
    String firstName;
    String lastName;
    String lastCommaFirst;
    String fullName2;
    String firstName2;
    String lastName2;
    static Timer timer;


    ArrayList<String> actors = new ArrayList<>();
    ArrayAdapter<String> autoCompleteAdapter;
    ProgressBar bar;
    String typedText;

    int count;
    EditText editTextGame2;
    String secondActor;



    //~~~~GAME VARIABLES~~~~~//
    ArrayList<String> firstRoundMovies;
    ArrayList<String> firstRoundActors;

    ArrayList<String> secondRoundMovies;
    ArrayList<String> secondRoundActors;

    HashMap<String, ArrayList> gameTree;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);

        bar = (ProgressBar) findViewById(R.id.progressBarDirector);
        //Search button
        btnMovieGame = (Button) findViewById(R.id.btnMovieGame);

        editTextGame = (EditText) findViewById(R.id.editTextGame);
        editTextGame2 = (EditText) findViewById(R.id.editTextGame2);



        //Results List
        listGameResults = (ListView) findViewById(R.id.listGameResults);

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, movies);

        //Assign adapter to ListView
        listGameResults.setAdapter(mAdapter);



        firstRoundMovies = new ArrayList<>();
        secondRoundMovies = new ArrayList<>();
        firstRoundActors = new ArrayList<>();
        secondRoundActors = new ArrayList<>();

        gameTree = new HashMap<>();


        btnMovieGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                //mAdapter.notifyDataSetChanged();
                //When the search button is pressed
                fullName = editTextGame.getText().toString();
                String[] splited = fullName.split(" ");
                firstName = splited[0];
                lastName = splited[splited.length - 1];
                lastCommaFirst = "'" + lastName + ", " + firstName + "'";



                fullName2 = editTextGame2.getText().toString();
                String[] splited2 = fullName2.split(" ");
                firstName2 = splited2[0];
                lastName2 = splited2[splited2.length - 1];
                secondActor = "'" + lastName2 + ", " + firstName2 + "'";



                bar.setVisibility(View.VISIBLE);
                getMoviesByActor();

            }
        });





    }

    private void getMoviesByActor() {
        new MoviesDBConnect().execute("");
    }

    private void getActorsByMovie() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_game, menu);
        return true;
    }




    //~~~~GET MOVIES BY ACTOR~~~~~~//

    private class MoviesDBConnect extends AsyncTask<String, Void, Void> {

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
                    if(count == 0)
                        firstRoundMovies.add(rs.getString(1));
                    else if (count == 1)
                        secondRoundMovies.add(rs.getString(1));
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




            count++;
        }
    }







    //~~~~GET ACTORS FROM MOVIE~~~~~~//

    private class ActorsDBConnect extends AsyncTask<String, Void, Void> {

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
//                    if(count == 0)
//                        firstRoundMovies.add(rs.getString(1));
//                    else if (count == 1)
//                        secondRoundMovies.add(rs.getString(1));
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

        }
    }








}

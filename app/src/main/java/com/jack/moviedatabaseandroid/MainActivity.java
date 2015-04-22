package com.jack.moviedatabaseandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);


        Button findActor = (Button) findViewById(R.id.find_actor);
        Button findDirector = (Button) findViewById(R.id.find_director);
        Button findProducer = (Button) findViewById(R.id.find_producer);
        Button findMovie = (Button) findViewById(R.id.find_movie);
        Button playGame = (Button) findViewById(R.id.play_game);
        Button multipleActors = (Button) findViewById(R.id.multiple_actors);


        findActor.setOnClickListener(handler);
        findDirector.setOnClickListener(handler);
        findProducer.setOnClickListener(handler);
        findMovie.setOnClickListener(handler);
        playGame.setOnClickListener(handler);
        multipleActors.setOnClickListener(handler);

    }

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.find_actor:
                    startActivity(new Intent(v.getContext(), FindActorActivity.class));
                    break;
                case R.id.find_director:
                    //do stuff
                    startActivity(new Intent(v.getContext(), FindDirectorActivity.class));   //Load FindDirectorActivity
                    break;
                case R.id.find_producer:
                    //do stuff
                    startActivity(new Intent(v.getContext(), SearchProducerActivity.class)); //Load SearchProducerActivity
                    break;
                case R.id.find_movie:
                    //do stuff
                    break;
                case R.id.play_game:
                    startActivity(new Intent(v.getContext(), MovieGame.class));
                    //do stuff
                    break;
                case R.id.multiple_actors:
                    //do stuff
                    startActivity(new Intent(v.getContext(), MultipleActors.class));
                    break;

            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

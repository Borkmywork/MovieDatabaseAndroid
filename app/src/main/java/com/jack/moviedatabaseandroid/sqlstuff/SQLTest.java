package com.jack.moviedatabaseandroid.sqlstuff;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

/**
 * Created by Jack on 4/6/15.
 */
public class SQLTest extends AsyncTask<String, Void, String> {

    private Context context;

    @Override
    protected String doInBackground(String... arg0) {
        try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];
            String link = "http://myphpmysqlweb.hostei.com/login.php?username="
                    +username+"&password="+password;
            URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }catch(Exception e){
            return new String("Exception: " + e.getMessage());
        }


    }
}

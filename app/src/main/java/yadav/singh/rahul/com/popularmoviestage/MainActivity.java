package yadav.singh.rahul.com.popularmoviestage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
listView = (ListView) findViewById(R.id.list);
listView.setOnClickListener(this);

new checkInConnection().execute("https://api.themoviedb.org/3/movie/popular?api_key=895017f30ca46b5a3b82ecae80b7b67c&language=en-US&page=1");

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();


    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//Log.i("OnItem click","The Method is invoked");

        Intent intent =new Intent(this,MovieDetailActivity.class);
        intent.putExtra("MOVIE_DETAILS", (MovieDetails) parent.getItemAtPosition(position));
    startActivity(intent);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }


    class checkInConnection extends AsyncTask<String,Void,String>{

      @Override
      protected void onPreExecute() {
          super.onPreExecute();

      }

      // will execute in background
      @Override
      protected String doInBackground(String... strings) {

          URL url = null;
          try {
              url = new URL(strings[0]);
          } catch (MalformedURLException e) {
              e.printStackTrace();
          }

          try {
              HttpsURLConnection urlCommection = (HttpsURLConnection) url.openConnection();
//          return String.valueOf(urlCommection.getResponseCode());

              InputStream inputStream = urlCommection.getInputStream();
              //reading response
              BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
              String s= bufferedReader.readLine();
              bufferedReader.close();
              return s;
          } catch (IOException e) {
              Log.e("Error",e.getMessage(),e);
          }

      return null;
      }

      @Override
      protected void onPostExecute(String s) {
          super.onPostExecute(s);
          JSONObject jsonObject = null;
          try {

              jsonObject = new JSONObject(s);
// it will contain movie details
              ArrayList<MovieDetails> movieList = new ArrayList<>();
              JSONArray jsonArray = jsonObject.getJSONArray("results");
              for(int i=0;i<jsonArray.length();i++)
              {
                  //reading jsondata
                  JSONObject object = jsonArray.getJSONObject(i);
MovieDetails movieDetails = new MovieDetails();
movieDetails.setOriginal_title(object.getString("original_title"));
movieDetails.setVote_average(object.getString("vote_average"));
movieDetails.setRelease_date(object.getString("release_date"));
movieDetails.setOverview(object.getString("overview"));
movieDetails.setPoster_path(object.getString("poster_path"));

movieList.add(movieDetails);
              }


              MovieArrayAdapter movieArrayAdapter = new MovieArrayAdapter(MainActivity.this,R.layout.movie_list,movieList);
              listView.setAdapter(movieArrayAdapter);

          } catch (JSONException e) {
              e.printStackTrace();
          }

      }

  }
}



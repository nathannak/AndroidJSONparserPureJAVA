package com.n.nathan.kamcordminiproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //
    public TextView txt_view;
    public String json_string;

    //
    ImageView img_view;
    //

    List<String> testList =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        //txt_view = (TextView) findViewById(R.id.test_text_view);
        img_view = (ImageView) findViewById(R.id.image_test);
        //

        try
        {
            Load_json loadJson = new Load_json();
            loadJson.execute();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //
    }

    public class Load_json extends AsyncTask<Void, Void, Void>
    {
        //
        InputStream in;
        Bitmap bitmap;
        //
        @Override
        protected void onPreExecute()
        {

        }
        //
        @Override
        protected Void doInBackground(Void... params)
        {

            try {
                sendingGetRequestFirstPage();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //
            try {
                in = (InputStream) new URL( testList.get(0) ).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
            publishProgress();
            //
            try {

                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            //

            return null;
        }

        //
        @Override
        protected void onPostExecute(Void aVoid)
        {

            //testing remove later
            //txt_view.setText(json_string);

            //
            //InputStream in = null; //Reads whatever content found with the given URL Asynchronously And returns.

//            try {
//                in = (InputStream) new URL( testList.get(0) ).getContent();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            Bitmap bitmap = BitmapFactory.decodeStream(in); //Decodes the stream returned from getContent and converts It into a Bitmap Format
//            img_view.setImageBitmap(bitmap); //Sets the Bitmap to ImageView

//            try {
//
//                    in.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            //

        }
        //


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            //
            img_view.setImageBitmap(bitmap); //Sets the Bitmap to ImageView
            //
        }
        //
    }
    //end of AsuncTask

    //

    // HTTP GET request for first page
    public void sendingGetRequestFirstPage() throws Exception {
        //
        try {

            String url = Uri.parse("https://api.kamcord.com/v1/feed/set/featuredShots")
                       .buildUpon()
                       .appendQueryParameter("count", "20")
                       .build().toString();

            json_string = getUrlString(url);
            Log.v("myapp", "Received JSON: " + json_string);

            // create json object form json string
            JSONObject mainObject = new JSONObject(json_string);
            JSONArray groups_arr = mainObject.getJSONArray("groups");
            JSONObject groups_obj = groups_arr.getJSONObject(0);
            JSONArray cards = groups_obj.getJSONArray("cards");
            //

            for(int i =0; i < cards.length(); i++)
            {
                //
                JSONObject jsonOBJ_middle = cards.getJSONObject(i);
                JSONObject shotCardData = jsonOBJ_middle.getJSONObject("shotCardData");

                String heartCount = shotCardData.getString("heartCount");
                //remeber these string values may be empty
                Log.v("myapp","heartCount number " + i + " is: " + heartCount);
                JSONObject play = shotCardData.getJSONObject("play");
                //remeber these string values may be empty
                String mp4 = play.getString("mp4");
                //
                JSONObject avatarThumbnail = shotCardData.getJSONObject("avatarThumbnail");
                String medium = avatarThumbnail.getString("medium");
                //remeber these string values may be empty
                if(medium != null)
                {
                    testList.add(medium);
                    Log.v("myapp","image link number " + i + " is: " + medium);
                }
                //
            }
            //
        } catch (IOException ioe) {
            Log.v("myapp", "Failed to fetch items", ioe);
        }
    }
    //

    // HTTP GET request for pagination
    public void sendingGetRequestOtherPages() throws Exception {
        //
        try {

            //probably stays the same
            String feedid = "ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl";

            //String url = Uri.parse("https://api.kamcord.com/v1/feed/set/featuredShots?count=20")
            String url = Uri.parse("https://api.kamcord.com/v1/feed/" + feedid)
            //String url = Uri.parse("https://bnest.co/echo-http-request.php")
            //String url = Uri.parse("https://www.github.com")

                    /*

            https://api.kamcord.com/v1/feed/?count=20&page=40.FEATURED_SHOTS.subfeed_featured _shots.40.40 '

                    */
                    .buildUpon()
                    .appendQueryParameter("count", "20")
                    //this probably changes
                    .appendQueryParameter("page", "20.FEATURED_SHOTS.subfeed_featured_shots.20.20,CgKhM8750Gf")
//                     .appendQueryParameter("feedid", "20.FEATURED_SHOTS.subfeed_featured_shots.20.20,CgKhM8750Gf")
//                     .appendQueryParameter("accept-language","en")
//                     .appendQueryParameter("device-token","abc123")
//                     .appendQueryParameter("client-name","android")

                    .build().toString();

            String jsonString = getUrlString(url);
            Log.v("myapp", "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.v("myapp", "Failed to fetch items", ioe);
        }
    }
    //
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    //
    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("device-token","abc123");
        connection.setRequestProperty("client-name","android");
        connection.setRequestProperty("accept-language","en");
        connection.setInstanceFollowRedirects(true);
        //connection.setDoInput(true);
        //connection.setDoOutput(true);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
            {
                throw new IOException(connection.getResponseMessage() +  ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    //
}
//

//        curl -X GET --header
//        'Accept: application/json'
//                --header 'accept-language: en'
//                --header 'device-token: abc123'
//                --header 'client-name: android'
//        "https://api.kamcord.com/v1/feed/set/featuredShots?count=20"

/* legacy

    // HTTP GET request for pagination
    public static void sendingGetRequestOtherPages() throws Exception {
        //
        try {

            //probably stays the same
            String feedid = "ZmVlZElkPWZlZWRfZmVhdHVyZWRfc2hvdCZ1c2VySWQmdG9waWNJZCZzdHJlYW1TZXNzaW9uSWQmbGFuZ3VhZ2VDb2Rl";

            //String url = Uri.parse("https://api.kamcord.com/v1/feed/set/featuredShots?count=20")
            String url = Uri.parse("https://api.kamcord.com/v1/feed/" + feedid)
                    //String url = Uri.parse("https://bnest.co/echo-http-request.php")
                    //String url = Uri.parse("https://www.github.com")

            //https://api.kamcord.com/v1/feed/?count=20&page=40.FEATURED_SHOTS.subfeed_featured _shots.40.40 '


.buildUpon()
        .appendQueryParameter("count", "20")
        //this probably changes
        .appendQueryParameter("page", "20.FEATURED_SHOTS.subfeed_featured_shots.20.20,CgKhM8750Gf")
//                     .appendQueryParameter("feedid", "20.FEATURED_SHOTS.subfeed_featured_shots.20.20,CgKhM8750Gf")
//                     .appendQueryParameter("accept-language","en")
//                     .appendQueryParameter("device-token","abc123")
//                     .appendQueryParameter("client-name","android")

        .build().toString();

        String jsonString = getUrlString(url);
        Log.v("myapp", "Received JSON: " + jsonString);
        } catch (IOException ioe) {
        Log.v("myapp", "Failed to fetch items", ioe);
        }
        }

 */
package com.ognid.sunshine;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            getForecast();

            return rootView;
        }

        void getForecast(){
            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadForecast(getActivity()).execute();
            } else {
                // display error test b
                Log.d("problem","Houston we have a problem");
            }
        }


    }

    public static class DownloadForecast extends AsyncTask<Void,Void,List<String>>{

        Activity activity2;

        DownloadForecast(Activity ac){
            activity=ac;
        }

        @Override
        protected List<String> doInBackground(Void ... f) {
            try{
                return download();
            }
            catch (IOException e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> weekForecast) {
            super.onPostExecute(weekForecast);

            ArrayAdapter<String> adapter=new ArrayAdapter<String>(activity,R.layout.list_item_forecast,R.id.list_item_forecast_textView,weekForecast);
            ListView listView=(ListView) activity.findViewById(R.id.listView_forecast);
            listView.setAdapter(adapter);
        }

        private List<String> download() throws IOException {
            InputStream is=null;
            try{
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Rio de Janeiro, BR&cnt=7&mode=json&units=metric&APPID=43d651f8e1f3bb5f9b38ba0a16da7087");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("response",Integer.toString(response));

                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is);
                Log.d("string",contentAsString);
                return parse(contentAsString);

            }
            finally {
                if(is!=null)is.close();
            }
        }

        public String readIt(InputStream is) throws IOException, UnsupportedEncodingException {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        List<String> parse(String s){
            try{
                Log.d("string2",s);
                JSONObject json =new JSONObject(s);
                JSONArray array=json.getJSONArray("list");
                List<String>list=new LinkedList<>();

                Log.d("lengthArray",Integer.toString(array.length()));
                for(int i=0;i<array.length();i++){


                    String weatherCondition=array.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main");
                    if(i==0)  list.add("Today- "+weatherCondition);
                    else {
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.DATE, i);
                        String weekDay=new SimpleDateFormat("EE").format(c.getTime());
                        list.add(weekDay+"- "+weatherCondition);
                    }
                }
                Log.d("lengthList",Integer.toString(list.size()));
                return list;
            }
            catch (JSONException e){
                e.printStackTrace();
                return null;
            }

        }

    }
}

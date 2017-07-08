package com.hci.roi.hciproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Roi on 08/07/2017.
 * API KEY : AIzaSyB2uOrMiqG6QDmuzYsd5Zs8mOOfxyEgQ7o
 */

public class LocationManagerHelper {
    private String TAG = "YONATAN_ROI";
    private Context mContext;

    public LocationManagerHelper(Context context){
        mContext = context;
    }

    public void doTest(double lat , double lng){
        new ElevationTask(lat,lng).execute();
    }

    public String test(final double lati , final double lng) throws Exception {

        final StringBuilder msg = new StringBuilder();
        new Thread(new Runnable() {
            public void run() {
                final double lon = lng;
                final double lat = lati;
                final String alt = getElevationFromGoogleMaps(lon, lat)+"";
               // msg.append("Lon: ");
                //msg.append(lon);
                //msg.append(" Lat: ");
               // msg.append(lat);
                msg.append(" Alt: ");
                msg.append(alt);
            }
        }).run();
        return msg.toString();
    }


    class ElevationTask extends AsyncTask<String, Void, Void> {

        private Exception exception;
        private String str = "Elevation:";
        private double longtitude;
        private double latitude;

        public ElevationTask(double lat , double lng){
            latitude = lat;
            longtitude = lng;
        }

        protected Void doInBackground(String... urls) {
            try {
                str += test(longtitude,latitude);
            } catch (Exception e) {
                this.exception = e;
            }
            Log.e(TAG,str);
            //Toast.makeText(mContext,str,Toast.LENGTH_LONG);
            publishProgress(null);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void mVoid) {
            super.onPostExecute(mVoid);
            Log.d(TAG,"LocationManagerHelper onPostExecute called.");
            MapFragment.printElevation(str);
            //Toast.makeText(mContext,str,Toast.LENGTH_LONG);
        }
    }

    private double getElevationFromGoogleMaps(double longitude, double latitude) {
        double result = Double.NaN;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String url = "http://maps.googleapis.com/maps/api/elevation/"
                + "xml?locations=" + String.valueOf(latitude)
                + "," + String.valueOf(longitude)
                + "&sensor=true";
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                int r = -1;
                StringBuffer respStr = new StringBuffer();
                while ((r = instream.read()) != -1)
                    respStr.append((char) r);
                String tagOpen = "<elevation>";
                String tagClose = "</elevation>";
                if (respStr.indexOf(tagOpen) != -1) {
                    int start = respStr.indexOf(tagOpen) + tagOpen.length();
                    int end = respStr.indexOf(tagClose);
                    String value = respStr.substring(start, end);
                    result = (double)(Double.parseDouble(value)*3.2808399); // convert from meters to feet
                }
                instream.close();
            }
        } catch (ClientProtocolException e) {}
        catch (IOException e) {}

        return result;
    }

}

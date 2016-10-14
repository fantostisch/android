package net.tuxed.vpnconfigimporter.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {

    public String downloadFile(String vpnUrl, String bearerToken, String configName) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(vpnUrl);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Bearer " + bearerToken);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestMethod("POST");

            String body = "name=" + configName;

            Log.i("body", body);

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setFixedLengthStreamingMode(body.length());

            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(body.getBytes("UTF-8"));
            out.flush();

            urlConnection.connect();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append("\n");
            }

            return total.toString();
        } catch (MalformedURLException e) {
            Log.e("MalformedURLException", e.getMessage());
        } catch (IOException e) {
            Log.e("Error", urlConnection.getErrorStream().toString());
            Log.e("IOException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
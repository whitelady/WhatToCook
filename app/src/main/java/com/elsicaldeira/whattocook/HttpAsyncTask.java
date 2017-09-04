package com.elsicaldeira.whattocook;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Elsi on 19/08/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        return openHttpConnection(urls[0]);
    }

    /**
     * openHttpConnection
     * Open connection to the service
     * @param urlStr
     * @return result
     */
    private String openHttpConnection(String urlStr) {
        InputStream in = null;
        String result = "";
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            in = urlConn.getInputStream();
            result = readStream(in);
            urlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    // progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

}

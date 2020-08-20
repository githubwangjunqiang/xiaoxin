package com.xiaoqiang.xiaoxin;

import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpUtils {
    /**
     * post 请求
     *
     * @param httpUrl
     * @param json
     * @return
     */
    public static String postJson(String httpUrl, String json) {
        HttpURLConnection httpURLConnection = null;
        String data = null;
        try {
            URL url = new URL(httpUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(30 * 1000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.connect();

            OutputStream dataOutputStream = httpURLConnection.getOutputStream();
            dataOutputStream.write(json.getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder builder = new StringBuilder();
            String bady = null;
            while ((bady = bufferedReader.readLine()) != null) {
                builder.append(bady);
            }
            if (builder.length() > 0) {
                data = builder.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                httpURLConnection = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }
    }


    /**
     * 获取文件长度
     *
     * @param url
     * @return
     */
    public static Data get(String url) {
        HttpURLConnection connection = null;
        long fileLength = -1;
        Data data = new Data();
        data.setSuccess(false);
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(30 * 1000);
            connection.addRequestProperty("Accept-Encoding", "identity");
            connection.connect();
            int responseCode = connection.getResponseCode();
            data.setHttpCode(responseCode);
            if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                String responseMessage = connection.getResponseMessage();
                data.setHttpMsg(responseMessage);
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder builder = new StringBuilder();
                String bady = null;
                while ((bady = bufferedReader.readLine()) != null) {
                    builder.append(bady);
                }
                if (builder.length() > 0) {
                    String s = builder.toString();
                    data.setSuccess(true);
                    data.setMsg(s);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            data.setErrorMsg(e.getLocalizedMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }


}

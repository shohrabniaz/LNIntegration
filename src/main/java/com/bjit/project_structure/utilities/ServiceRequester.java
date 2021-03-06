/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bjit.project_structure.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author BJIT
 */
public class ServiceRequester {

    public static synchronized String callService(String serviceUrl, String data) throws MalformedURLException, IOException, Exception {
        try {

            validateUrlAndBodyData(serviceUrl, data);

            URL url = new URL(serviceUrl);
            final HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Content-Type", "application/json");

            httpUrlConnection.setDoOutput(true);

            setRequestBody(httpUrlConnection, data);

            if (httpUrlConnection.getResponseCode() >= 300) {
                String errorMessage = getDataFromInputStream(httpUrlConnection.getErrorStream());
                throw new Exception(errorMessage);
            }

            String responseData = getDataFromInputStream(httpUrlConnection.getInputStream());
            return responseData;
        } catch (MalformedURLException exp) {
            throw exp;
        } catch (NullPointerException exp) {
            throw exp;
        } catch (IOException exp) {
            throw exp;
        } catch (Exception exp) {
            throw exp;
        }
    }

    private static String getDataFromInputStream(InputStream inputStream) throws Exception {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            final StringBuilder jsonString = new StringBuilder();
            String output;
            while ((output = bufferedReader.readLine()) != null) {
                jsonString.append(output);
            }
            return jsonString.toString();
        } catch (Exception exp) {
            throw exp;
        }
    }

    private static void setRequestBody(HttpURLConnection httpUrlConnection, String bodyData) throws UnsupportedEncodingException, IOException {
        byte[] outputInBytes = bodyData.getBytes("UTF-8");
        try (OutputStream outputStream = httpUrlConnection.getOutputStream()) {
            outputStream.write(outputInBytes);
            outputStream.flush();
        } catch (Exception exp) {
            throw exp;
        }
    }

    private static void validateUrlAndBodyData(String url, String bodyData) {
        if (ObjectUtility.isNullOrEmpty(url)) {
            throw new NullPointerException("URL is empty");
        }

        if (ObjectUtility.isNullOrEmpty(bodyData)) {
            throw new NullPointerException("Request body data is empty");
        }
    }
}

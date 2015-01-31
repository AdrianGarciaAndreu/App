package com.draw_lessons.app;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by javichaques on 29/1/15.
 */
public class webservice {

    private String respuesta = null;

    //Devuelve la respuesta codificada en JSON
    public String makeServiceCall(String url) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            HttpGet httpGet = new HttpGet(url);
            httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            respuesta = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}
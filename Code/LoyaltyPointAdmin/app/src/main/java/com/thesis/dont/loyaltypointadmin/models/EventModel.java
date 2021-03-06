package com.thesis.dont.loyaltypointadmin.models;

import android.util.Pair;

import com.thesis.dont.loyaltypointadmin.controllers.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinntt on 3/24/2015.
 */
public class EventModel {

    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }
    public static native String getAddEvent();
    public static native String getGetListEvents();
    public static native String getEditEvent();
    public static native String getCalculatePoint();

    public static void addEvent(Event event, final String shopId, final String cardId, final OnAddEventResult onAddEventResult){
        final String json = Helper.objectToJson(event);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getAddEvent();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("event", json));
                nameValuePairs.add(new BasicNameValuePair("token", Global.userToken));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    /*ResponseHandler<String> responseHandler = Helper.getResponseHandler();*/
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CreateEventResult createEventResult = (CreateEventResult)Helper.jsonToObject(response, CreateEventResult.class);

                    if(createEventResult.error.equals(""))
                        onAddEventResult.onSuccess(createEventResult);
                    else
                        onAddEventResult.onError(createEventResult.error);
                } catch (UnsupportedEncodingException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListEvents(final String shopID, final String cardID, final OnGetListResult onGetListResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListEvents();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", Global.userToken));
                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListEvents result = (GetListEvents) Helper.jsonToObject(response, GetListEvents.class);
                    if(result.error.equals("")) {
                        ArrayList<Event> listEvents = new ArrayList<Event>();
                        for(int i=0; i<result.listEvents.length-1; i++) {
                            listEvents.add(result.listEvents[i]);
                        }
                        onGetListResult.onSuccess(listEvents);
                    }
                    else
                        onGetListResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    onGetListResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    onGetListResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    onGetListResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void editEvent(final String token, final String shopID, final String cardID, final Event event,
                                 final OnEditEventResult mOnEditEventResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(event);

                String link = getEditEvent();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("event", json));
                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    EditEventResult result = (EditEventResult) Helper.jsonToObject(response, EditEventResult.class);
                    if(result.error.equals(""))
                        mOnEditEventResult.onSuccess(result);
                    else
                        mOnEditEventResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnEditEventResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnEditEventResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnEditEventResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void calculatePoint(final String token, final String shopId, final String cardId, final int totalMoney, final ArrayList<Product> products, final OnCalculatePointResult mOnCalculatePointResult){
        //final String json = Helper.objectToJson(event);
        final String listproducts = Helper.objectToJson(products);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCalculatePoint();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(4);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("total_money", String.valueOf(totalMoney)));
                nameValuePairs.add(new BasicNameValuePair("list_products", listproducts));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CalculatePointResult calculatePointResult = (CalculatePointResult)Helper.jsonToObject(response, CalculatePointResult.class);

                    if(calculatePointResult.error.equals("")) {
                        // parse from AchievedEvent[] -> ArrayList<AchievedEvent>
                        ArrayList<AchievedEvent> events = new ArrayList<AchievedEvent>();
                        for(int i=0; i<calculatePointResult.achievedEvents.length-1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                            events.add(calculatePointResult.achievedEvents[i]);
                        }
                        mOnCalculatePointResult.onSuccess(events, calculatePointResult.pointsFromMoney, calculatePointResult.totalPoints);
                    }
                    else
                        mOnCalculatePointResult.onError(calculatePointResult.error);
                } catch (UnsupportedEncodingException e) {
                    mOnCalculatePointResult.onError(e.toString());
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCalculatePointResult.onError(e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCalculatePointResult.onError(e.toString());
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnAddEventResult{
        public void onSuccess(CreateEventResult createEventResult);

        public void onError(String error);
    }

    public class CreateEventResult {
        public String error;
        public String bucketName;
        public String fileName;
    }

    public interface OnGetListResult{
        public void onSuccess(ArrayList<Event> listEvents);

        public void onError(String error);
    }

    public class GetListEvents {
        public String error;
        public Event[] listEvents;
    }

    public interface OnEditEventResult{
        public void onSuccess(EditEventResult result);
        public void onError(String error);
    }

    public interface OnCalculatePointResult{
        public void onSuccess(ArrayList<AchievedEvent> result, int pointFromMoney, int totalPoint);
        public void onError(String error);
    }

    public class EditEventResult{
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class CalculatePointResult{
        public String error;
        public int pointsFromMoney;
        public AchievedEvent[] achievedEvents;
        public int totalPoints;
    }
}

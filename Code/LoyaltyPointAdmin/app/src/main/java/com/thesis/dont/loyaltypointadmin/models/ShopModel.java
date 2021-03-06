package com.thesis.dont.loyaltypointadmin.models;

import com.google.gson.Gson;
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
 * Created by tinntt on 3/5/2015.
 */
public class ShopModel {
    static HttpPost httppost;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static OnCreateShopResult mOnCreateShopResult;
    static OnGetShopInfoResult mOnGetShopInfoResult;
    static OnSelectListShopResult mOnSelectListShopResult;
    static OnEditShopInfoResult mOnEditShopInfoResult;

    static {
        System.loadLibrary("services");
    }
    public static native String getCreateShop();
    public static native String getGetListShop();
    public static native String getGetShopInfo();
    public static native String getEditShopInfo();
    public static native String getUpdateBackground();
    public static native String getGetFollowingUsers();
    public static native String getGetCustomerInfo();
    public static native String getUpdatePoint();
    public static native String getGetListRegisters();
    public static native String getAcceptRegisterRequest();
    public static native String getCancelRegisterRequest();
    public static native String getGetNumUserEventAward();
    public static native String getGetNewestUserEventAward();

    public static void createShop(Shop shop, final String card_id, String token){
        final String token_string = token;
        final String json = Helper.objectToJson(shop);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCreateShop();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("shop", json));
                nameValuePairs.add(new BasicNameValuePair("card_id", card_id));
                nameValuePairs.add(new BasicNameValuePair("token", token_string));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CreateShopResult result = (CreateShopResult) Helper.jsonToObject(response, CreateShopResult.class);
                    if(result.error.equals(""))
                        mOnCreateShopResult.onSuccess(result);
                    else
                        mOnCreateShopResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCreateShopResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCreateShopResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCreateShopResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void editShop(final String token, final String shopID, final String cardID, Shop shop){

        final String json = Helper.objectToJson(shop);

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getEditShopInfo();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("shop", json));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardID));
                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();

                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    EditShopResult result = (EditShopResult) Helper.jsonToObject(response, EditShopResult.class);
                    if(result.error == "")
                        mOnEditShopInfoResult.onSuccess(result);
                    else
                        mOnEditShopInfoResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnEditShopInfoResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnEditShopInfoResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnEditShopInfoResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void updateBackgroundShop(final String token, final String shopID, final OnUpdateBackgroundResult mOnUpdateBackgroundResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getUpdateBackground();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();

                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    UpdateBackgroundResult result = (UpdateBackgroundResult) Helper.jsonToObject(response, UpdateBackgroundResult.class);
                    if(result.error.equals(""))
                        mOnUpdateBackgroundResult.onSuccess(result);
                    else
                        mOnUpdateBackgroundResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnUpdateBackgroundResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnUpdateBackgroundResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnUpdateBackgroundResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public class CreateShopResult {
        public String error;
        public String shopID;
        public String bucketName;
        public String fileName;
    }

    public class EditShopResult {
        public String error;
        public String shopID;
        public String bucketName;
        public String fileName;
    }

    public class UpdateBackgroundResult {
        public String error;
        public String shopID;
        public String bucketName;
        public String fileName;
    }

    public static void getShopInfo(final String token, final String shopID, final String cardID){
        /*final String token_string = token;
        final String json = Helper.objectToJson(shop);*/
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetShopInfo();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));
                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    if(response.equals("wrong token") || response.equals("") || response.equals("not your shop"))
                        mOnGetShopInfoResult.onError(response);
                    else {
                        Shop shop = (Shop) Helper.jsonToObject(response, Shop.class);
                        mOnGetShopInfoResult.onSuccess(shop);
                    }

                } catch (UnsupportedEncodingException e) {
                    mOnGetShopInfoResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetShopInfoResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetShopInfoResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getFollowingUsers(String token, String shopId, final String cardId, final OnSelectFollowingUsersResult mOnSelectFollowingUsersResult){
        final String token_string = token;
        final String shopId_string = shopId;
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetFollowingUsers();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", Global.userToken));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId_string));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetListFollowingUsers result = (GetListFollowingUsers) Helper.jsonToObject(response, GetListFollowingUsers.class);

                    if(result.error.equals("")){

                        ArrayList<Customer> listUsers = new ArrayList<Customer>();
                        for(int i=0; i<result.listUsers.length-1; i++) {
                            listUsers.add(result.listUsers[i]);
                        }
                        mOnSelectFollowingUsersResult.onSuccess(listUsers);
                    }
                    else
                        mOnSelectFollowingUsersResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnSelectFollowingUsersResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnSelectFollowingUsersResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnSelectFollowingUsersResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListRegisters(final String token, final String cardId, final OnGetListRegistersResult mOnGetListRegistersResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListRegisters();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetListRegisters result = (GetListRegisters) Helper.jsonToObject(response, GetListRegisters.class);

                    if(result.error.equals("")){
                        ArrayList<User> listRegisters = new ArrayList<User>();
                        for(int i=0; i<result.listRegisters.length-1; i++) {
                            listRegisters.add(result.listRegisters[i]);
                        }
                        mOnGetListRegistersResult.onSuccess(listRegisters);
                    }
                    else
                        mOnGetListRegistersResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetListRegistersResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetListRegistersResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetListRegistersResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void acceptRegisterRequest(final String token, final String cardID, final String username, final OnAcceptRegisterRequestResult mOnAcceptRegisterRequestResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getAcceptRegisterRequest();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));
                nameValuePairs.add(new BasicNameValuePair("username", username));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    AcceptRegisterRequestResult result = (AcceptRegisterRequestResult) Helper.jsonToObject(response, AcceptRegisterRequestResult.class);

                    if(result.error.equals("")){
                        mOnAcceptRegisterRequestResult.onSuccess();
                    }
                    else
                        mOnAcceptRegisterRequestResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnAcceptRegisterRequestResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnAcceptRegisterRequestResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnAcceptRegisterRequestResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void cancelRegisterRequest(final String token, final String cardID, final String username, final OnCancelRegisterRequestResult mOnCancelRegisterRequestResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCancelRegisterRequest();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));
                nameValuePairs.add(new BasicNameValuePair("username", username));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    AcceptRegisterRequestResult result = (AcceptRegisterRequestResult) Helper.jsonToObject(response, AcceptRegisterRequestResult.class);

                    if(result.error.equals("")){
                        mOnCancelRegisterRequestResult.onSuccess();
                    }
                    else
                        mOnCancelRegisterRequestResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCancelRegisterRequestResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCancelRegisterRequestResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCancelRegisterRequestResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getCustomerInfo(final String token, final String cardId, final String username, final OnGetCustomerInfoResult mOnGetCustomerInfoResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetCustomerInfo();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("username", username));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetCustomerInfoResult result = (GetCustomerInfoResult) Helper.jsonToObject(response, GetCustomerInfoResult.class);

                    if(result.error.equals(""))
                        mOnGetCustomerInfoResult.onSuccess(result.user);
                    else
                        mOnGetCustomerInfoResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetCustomerInfoResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetCustomerInfoResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetCustomerInfoResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getNumUserEventAward(final String token, final String shopId, final String cardId, final OnGetNumUserAwardEventResult mOnGetNumUserAwardEventResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetNumUserEventAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetNumUserAwardEventResult result = (GetNumUserAwardEventResult) Helper.jsonToObject(response, GetNumUserAwardEventResult.class);

                    if(result.error.equals(""))
                        mOnGetNumUserAwardEventResult.onSuccess(result);
                    else
                        mOnGetNumUserAwardEventResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetNumUserAwardEventResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetNumUserAwardEventResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetNumUserAwardEventResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getNewestUserEventAward(final String token, final String shopId, final String cardId, final OnGetNewestUserAwardEventResult mOnGetNumUserAwardEventResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetNewestUserEventAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetNewestUserAwardEventResult result = (GetNewestUserAwardEventResult) Helper.jsonToObject(response, GetNewestUserAwardEventResult.class);

                    if(result.error.equals("")) {
                        ArrayList<Customer> listCustomers = new ArrayList<Customer>();
                        for (int i = 0; i < result.user_list.length - 1; i++) {
                            listCustomers.add(result.user_list[i]);
                        }
                        ArrayList<Event> listEvents = new ArrayList<Event>();
                        for (int i = 0; i < result.event_list.length - 1; i++) {
                            listEvents.add(result.event_list[i]);
                        }
                        ArrayList<Award> listAwards = new ArrayList<Award>();
                        for (int i = 0; i < result.award_list.length - 1; i++) {
                            listAwards.add(result.award_list[i]);
                        }
                        mOnGetNumUserAwardEventResult.onSuccess(listCustomers, listEvents, listAwards);
                    }
                    else
                        mOnGetNumUserAwardEventResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetNumUserAwardEventResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetNumUserAwardEventResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetNumUserAwardEventResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void updatePoint(final String token, final ArrayList<AchievedEvent> achivedEventList, final String shopId, final String username,
                                   final String fullname, final String phone,
                                   final int point, final String billCode, final String time, final String cardId,
                                   final OnUpdatePointResult mOnUpdatePointResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getUpdatePoint();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(9);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("fullname", fullname));
                nameValuePairs.add(new BasicNameValuePair("phone", phone));
                nameValuePairs.add(new BasicNameValuePair("point", String.valueOf(point)));
                nameValuePairs.add(new BasicNameValuePair("billCode", billCode));
                nameValuePairs.add(new BasicNameValuePair("event_list",Helper.objectToJson(achivedEventList)));
                nameValuePairs.add(new BasicNameValuePair("time", time));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    UpdatePointResult result = (UpdatePointResult) Helper.jsonToObject(response, UpdatePointResult.class);

                    if(result.error.equals(""))
                        mOnUpdatePointResult.onSuccess(result);
                    else
                        mOnUpdatePointResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnUpdatePointResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnUpdatePointResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnUpdatePointResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnGetNewestUserAwardEventResult{
        public void onSuccess(ArrayList<Customer> listUsers, ArrayList<Event> listEvents, ArrayList<Award> listAwards);
        public void onError(String error);
    }

    public class GetNewestUserAwardEventResult{
        public String error;
        public Customer[] user_list;
        public Award[] award_list;
        public Event[] event_list;
    }

    public interface OnGetNumUserAwardEventResult{
        public void onSuccess(GetNumUserAwardEventResult result);
        public void onError(String error);
    }

    public interface OnCancelRegisterRequestResult{
        public void onSuccess();
        public void onError(String error);
    }

    public class GetNumUserAwardEventResult{
        public String error;
        public String user;
        public String award;
        public String event;
    }

    public interface OnCreateShopResult{
        public void onSuccess(CreateShopResult result);
        public void onError(String error);
    }

    public static OnCreateShopResult getOnCreateShopResult() { return mOnCreateShopResult; }
    public static void setOnCreateShopResult(OnCreateShopResult mOnCreateShopResult){
        ShopModel.mOnCreateShopResult = mOnCreateShopResult;
    }

    public static void setOnGetShopInfoResult(OnGetShopInfoResult mOnGetShopInfoResult) {
        ShopModel.mOnGetShopInfoResult = mOnGetShopInfoResult;
    }

    public interface OnSelectListShopResult{
        public void onSuccess(ArrayList<Shop> listShops);
        public void onError(String error);
    }

    public interface OnGetShopInfoResult{
        public void onSuccess(Shop shop);
        public void onError(String error);
    }

    public interface OnEditShopInfoResult {
        public void onSuccess(EditShopResult result);
        public void onError(String error);
    }

    public interface OnUpdateBackgroundResult {
        public void onSuccess(UpdateBackgroundResult result);
        public void onError(String error);
    }

    public interface OnSelectFollowingUsersResult{
        public void onSuccess(ArrayList<Customer> listUsers);
        public void onError(String error);
    }

    public interface OnGetListRegistersResult{
        public void onSuccess(ArrayList<User> listRegisters);
        public void onError(String error);
    }

    public interface OnGetCustomerInfoResult{
        public void onSuccess(User user);
        public void onError(String error);
    }

    public interface OnUpdatePointResult{
        public void onSuccess(UpdatePointResult result);
        public void onError(String error);
    }

    public interface OnAcceptRegisterRequestResult{
        public void onSuccess();
        public void onError(String error);
    }

    public static OnSelectListShopResult getOnSelectListShopResult() {return mOnSelectListShopResult;}
    public static void setOnSelectListShopResult(OnSelectListShopResult mOnSelectListShopResult){
        ShopModel.mOnSelectListShopResult = mOnSelectListShopResult;
    }

    public static void setOnEditShopInfoResult(OnEditShopInfoResult mOnEditShopInfoResult) {
        ShopModel.mOnEditShopInfoResult = mOnEditShopInfoResult;
    }

    public class GetListShops {
        public String error;
        public Shop[] listShops;
    }

    public class GetListFollowingUsers{
        public String error;
        public Customer[] listUsers;
    }

    public class GetListRegisters{
        public String error;
        public User[] listRegisters;
    }

    public class GetCustomerInfoResult{
        public String error;
        public User user;
    }

    public class UpdatePointResult{
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class AcceptRegisterRequestResult{
        public String error;
    }
}

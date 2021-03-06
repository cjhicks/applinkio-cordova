//
//  AppLinkIOPlugin.java
//
//  Copyright 2018 AppLinkIO.io Inc. All rights reserved.
//

package io.applink.cordova;


import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.util.Log;

import io.applink.applinkio.AppLinkIO;


public class AppLinkIOPlugin extends CordovaPlugin {
  private static final String TAG = "AppLinkIOPlugin";


  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    Log.d(TAG, "Initializing AppLinkIOPlugin");
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

    if (action.equals("initAppLinkIO")) {
        String appToken = optString(args, 0);
        AppLinkIO.initAppLinkIO(cordova.getActivity().getApplicationContext(), appToken);
        callbackContext.success();
        return true;
    } else if (action.equals("initAppLinkIOWithOptions")) {
      if (args.length() == 2) {
        String appToken = optString(args, 0);
        Map<String, String> appOptions = optStringMap(args, 1);
        AppLinkIO.initAppLinkIOWithOptions(cordova.getActivity().getApplicationContext(), appToken, appOptions);
        callbackContext.success();
      } else {
        callbackContext.error("Requires String appToken, Map<String, String> appOptions.");
      }
      return true;
    } else if (action.equals("unlinkAppLinkIO")) {
      AppLinkIO.unlinkAppLinkIO();
      callbackContext.success();
      return true;
    } else if (action.equals("linkUser")) {
      String userEmailAddress = optString(args, 0);
      AppLinkIO.linkUser(userEmailAddress);
      callbackContext.success();
      return true;      
    } else if (action.equals("setUserAttribute")) {

      if (args.length() == 2) {
        String attributeName = optString(args, 0);
        String attributeValue = optString(args, 1);
        AppLinkIO.setUserAttribute(attributeName, attributeValue);
        callbackContext.success();
      } else {
        callbackContext.error("Requires String attributeName, String attributeValue.");
      }
      return true;
    } else if (action.equals("trackScreenView")) {
      String screenName = optString(args, 0);
      AppLinkIO.trackScreenView(screenName);
      callbackContext.success();
      return true;
    } else if (action.equals("trackScreenViewWithExtras")) {
      if (args.length() == 2) {
        String screenName = optString(args, 0);
        Map<String, String> screenExtras = optStringMap(args, 1);
        AppLinkIO.trackScreenViewWithOptions(screenName, screenExtras);
        callbackContext.success();
      } else {
        callbackContext.error("Requires String screenName, Map<String, String> screenExtras.");
      }
      return true;      
    } else if (action.equals("trackEvent")) {
      String eventName = optString(args, 0);
      AppLinkIO.trackEvent(eventName);
      callbackContext.success();
      return true;
    } else if (action.equals("trackEventWithExtras")) {
      if (args.length() == 2) {
        String eventName = optString(args, 0);
        Map<String, String> eventExtras = optStringMap(args, 1);
        AppLinkIO.trackEventWithOptions(eventName, eventExtras);
        callbackContext.success();
      } else {
        callbackContext.error("Requires String eventName, Map<String, String> eventExtras.");
      }
      return true;    
    } else if (action.equals("trackImpression")) {
      Map<String, String> displayDetails = optStringMap(args, 0);
      AppLinkIO.trackImpression(displayDetails);
      callbackContext.success();
      return true;      
    } else if (action.equals("trackInteraction")) {
      if (args.length() == 2) {
        Map<String, String> displayDetails = optStringMap(args, 0);
        String interactionType = optString(args, 1);
        AppLinkIO.trackInteraction(displayDetails, interactionType);
        callbackContext.success();
      } else {
        callbackContext.error("Requires Map<String, String> displayDetails, String interactionType.");
      }
      return true; 
    } else if (action.equals("trackConversion")) {
      if (args.length() == 2) {
        Map<String, String> displayDetails = optStringMap(args, 0);
        Map<String, String> conversionDetails = optStringMap(args, 1);
        AppLinkIO.trackConversion(displayDetails, conversionDetails);
        callbackContext.success();
      } else {
        callbackContext.error("Requires Map<String, String> displayDetails, Map<String, String> conversionDetails.");
      }
      return true; 
    } else if (action.equals("trackSearch")) {
      Map<String, String> searchDetails = optStringMap(args, 0);
      AppLinkIO.trackSearch(searchDetails);
      callbackContext.success();
      return true;      
    }
    return false;
  }

    /*******************
     * Private Methods
     ******************/
    private boolean isNotNull(JSONArray jsonArray, int index) throws JSONException {
        return jsonArray != null && jsonArray.length() > index && !jsonArray.isNull(index);
    }



    private String optString(JSONArray jsonArray, int index) throws JSONException {
        if (isNotNull(jsonArray, index)) {
            return jsonArray.getString(index);
        }

        return null;
    }



    private HashMap<String, String> optStringMap(JSONArray jsonArray, int index) throws JSONException {
        if (isNotNull(jsonArray, index)) {
            return convertToStringMap(jsonArray.getJSONObject(index));
        }

        return null;
    }


    private HashMap<String, String> convertToStringMap(JSONObject jsonObject) throws JSONException {
        HashMap<String, String> map = null;
        if (jsonObject != null && jsonObject.length() > 0) {
            map = new HashMap<String, String>();
            Iterator<?> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
        }

        return map;
    }

        


}

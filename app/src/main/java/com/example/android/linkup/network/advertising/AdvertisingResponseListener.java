package com.example.android.linkup.network.advertising;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.example.android.linkup.models.AdvertisingImage;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.candidates.GetLinksResponseListener;
import com.example.android.linkup.utils.Base64Converter;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AdvertisingResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        ArrayList<AdvertisingImage> advertisingImages = new ArrayList<>();
        AdvertisingImage advertisingImage;
        Log.d("Respuesta",response.toString());
        try {
            advertisingImage = new AdvertisingImage();
            advertisingImage.img = response.getString("image");
            byte[] decodedString = Base64.decode(advertisingImage.img,Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
            advertisingImage.image = decodedByte;
            advertisingImages.add(advertisingImage);

        } catch (JSONException e) {
            Log.e(NetworkErrorMessages.ADVERTISING_TAG, e.toString());
            e.printStackTrace(System.err);
        }
        AdvertisingResponseListener.OnGetAdvertisingSuccessEvent event = new AdvertisingResponseListener.OnGetAdvertisingSuccessEvent();
        event.ads = advertisingImages;
        EventBus.getDefault().post(event);
    }

    public static class OnGetAdvertisingSuccessEvent {
        public ArrayList<AdvertisingImage> ads;
    }
}

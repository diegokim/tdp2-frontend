package com.example.android.linkup.network.get_profile;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.models.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class GetProfileResponseListener implements Response.Listener<JSONObject> {

    private static final String INTERESTS_KEY = "interests" ;
    private static final String DESCRIPTION_KEY = "description" ;
    private static final String GENDER_KEY =  "gender";
    private static final String OCUPATION_KEY = "work" ;
    private static final String EDUCATION_KEY = "education";
    private static final String PROFILE_PHOTO_KEY = "photo";
    private static final String PHOTOS_KEY = "photos";
    private static final String NAME_KEY = "name";
    private static final String BIRTHDAY_KEY = "birthday";

    private Profile profileToFill;

    public GetProfileResponseListener (Profile profileToFill) {
        this.profileToFill = profileToFill;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            String birthday = response.getString(BIRTHDAY_KEY);
            profileToFill.age = getAgeFromBirthDay(birthday);

            profileToFill.name = response.getString(NAME_KEY);
            profileToFill.description = response.getString(DESCRIPTION_KEY);
            profileToFill.gender = response.getString(GENDER_KEY);
            profileToFill.ocupation = response.getString(OCUPATION_KEY);
            profileToFill.education = response.getString(EDUCATION_KEY);
            profileToFill.profilePhoto = response.getString(PROFILE_PHOTO_KEY);

            JSONArray photos = response.getJSONArray(PHOTOS_KEY);
            profileToFill.photos = getStringArrayFrom(photos);

            JSONArray interests = response.getJSONArray(INTERESTS_KEY);
            profileToFill.interests = getStringArrayFrom(interests);

            profileToFill.commitChanges();

        } catch (Exception e) {
            Log.e("ERROR", e.getStackTrace().toString());
        }
    }




    public int getAgeFromBirthDay(String birthdate) {
        String partsOfBirthdate[] = birthdate.split("/");
        int day = Integer.parseInt(partsOfBirthdate[0]);
        int month = Integer.parseInt(partsOfBirthdate[1]);
        int year = Integer.parseInt(partsOfBirthdate[2]);
        return getAge(year, month, day);
    }

    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }

    public String[] getStringArrayFrom(JSONArray jsonArr) throws JSONException {
        String arr[] = new String[jsonArr.length()];
        for (int i = 0; i < jsonArr.length(); i++) {
            arr[i] = jsonArr.getString(i);
        }
        return arr;
    }
}

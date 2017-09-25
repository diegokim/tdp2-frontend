package com.example.android.linkup.utils;

import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    private static final String INTERESTS_KEY = "interests" ;
    private static final String DESCRIPTION_KEY = "description" ;
    private static final String GENDER_KEY =  "gender";
    private static final String OCUPATION_KEY = "work" ;
    private static final String EDUCATION_KEY = "education";
    private static final String PROFILE_PHOTO_KEY = "photo";
    private static final String PHOTOS_KEY = "photos";
    private static final String NAME_KEY = "name";
    private static final String AGE_KEY = "age";
    private static final String ID_KEY = "id";

    public static Settings getSettings (JSONObject settings) {
        Settings result = new Settings();
        try {
            result.invisible = settings.getBoolean("invisible");
            result.range = settings.getJSONObject("distRange").getInt("max");
            result.age_from = settings.getJSONObject("ageRange").getInt("min");
            result.age_to = settings.getJSONObject("ageRange").getInt("max");
            String interestType = settings.getString("interestType");
            if (interestType.equals("male")) {
                result.just_friends = false;
                result.pareja = true;
                result.hombres = true;
                result.mujeres = false;
            } else {
                if (interestType.equals("female")) {
                    result.just_friends = false;
                    result.pareja = true;
                    result.hombres = false;
                    result.mujeres = true;
                } else {
                    if (interestType.equals("both")) {
                        result.just_friends = false;
                        result.pareja = true;
                        result.hombres = true;
                        result.mujeres = true;
                    } else {
                        result.just_friends = true;
                        result.pareja = false;
                        result.hombres = false;
                        result.mujeres = false;
                    }
                }
            }
            return  result;
        } catch (Exception e) {
            return null;
        }
    }

    public static Profile getProfile (JSONObject profile) {
        Profile result;
        try {
            result = getProfileWithoutPhotos(profile);
            JSONArray photos = profile.getJSONArray(PHOTOS_KEY);
            result.photos = getStringArrayFrom(photos);
            return  result;
        } catch (Exception e) {
            return null;
        }
    }

    public static Profile getProfileWithoutPhotos(JSONObject profile) {
        Profile result = new Profile();
        try {
            result.age = profile.getInt(AGE_KEY);
            result.name = profile.getString(NAME_KEY);
            result.description = profile.getString(DESCRIPTION_KEY);
            result.gender = profile.getString(GENDER_KEY);
            result.work = profile.getString(OCUPATION_KEY);
            result.education = profile.getString(EDUCATION_KEY);
            result.profilePhoto = profile.getString(PROFILE_PHOTO_KEY);
            result.id = profile.getString(ID_KEY);
            JSONArray interests = profile.getJSONArray(INTERESTS_KEY);
            result.interests = getStringArrayFrom(interests);
            return  result;

        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getStringArrayFrom(JSONArray jsonArr) throws JSONException {
        String arr[] = new String[jsonArr.length()];
        for (int i = 0; i < jsonArr.length(); i++) {
            arr[i] = jsonArr.getString(i);
        }
        return arr;
    }
}

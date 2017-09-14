package com.example.android.linkup.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.utils.Base64Converter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import static android.R.color.black;

public class PhotosFragment extends Fragment implements Observer{

    private final Profile profile;
    private ViewPager viewPager;

    public PhotosFragment (Profile profile) {
        super();
        this.profile = profile;
        profile.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_photos, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.photos_fragment_layout);

        ArrayList<Bitmap> photos = getPhotos();

        PhotosAdapter adapter = new PhotosAdapter(this.getContext(), photos);
        viewPager.setAdapter(adapter);

//        updatePhotos();
        return view;
    }

    private ArrayList<Bitmap> getPhotos () {
        ArrayList<Bitmap> result = new ArrayList<>();
        if (profile.photos != null) {
            for (int i = 0; i < profile.photos.length; i++) {
                Log.e("ALSDJALSKDJ","ALSKJDALSKDJ");
                Base64Converter converter = new Base64Converter();
                Bitmap bitmap = converter.Base64ToBitmap(profile.photos[i]);
                //bitmap = converter.resizeBitmap(bitmap, 600);
                result.add(bitmap);
            }
        }
        return result;
    }

    @Override
    public void update(Observable o, Object arg) {
        ArrayList<Bitmap> photos = getPhotos();
        PhotosAdapter adapter = new PhotosAdapter(this.getContext(), photos);
        viewPager.setAdapter(adapter);
    }
}

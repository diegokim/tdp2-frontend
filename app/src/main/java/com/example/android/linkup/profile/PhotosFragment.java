package com.example.android.linkup.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.utils.Base64Converter;

import java.util.Observable;
import java.util.Observer;

import static android.R.color.black;

public class PhotosFragment extends Fragment implements Observer{

    private final Profile profile;
    private LinearLayout layout;

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
        layout = (LinearLayout) view.findViewById(R.id.photos_fragment_layout);
        if (profile.photos != null) {
            for (int i = 0; i < profile.photos.length; i++) {
                ImageView imageView = new ImageView(this.getContext());
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                Base64Converter converter = new Base64Converter();
                Bitmap bitmap = converter.Base64ToBitmap(profile.photos[i]);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                layout.addView(imageView);
            }
        }
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (profile.photos != null) {
            for (int i = 0; i < profile.photos.length; i++) {
                ImageView imageView = new ImageView(this.getContext());
                imageView.setId(i);
                imageView.setPadding(2, 2, 2, 2);
                Base64Converter converter = new Base64Converter();
                Bitmap bitmap = converter.Base64ToBitmap(profile.photos[i]);
                bitmap = converter.getResizedBitmap(bitmap, 1600);

                imageView.setImageBitmap(bitmap);

                //imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                layout.addView(imageView);
            }
        }
    }
}

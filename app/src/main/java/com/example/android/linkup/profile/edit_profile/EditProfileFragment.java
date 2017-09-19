package com.example.android.linkup.profile.edit_profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.edit_profile.EditProfileRequestGenerator;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.Subscribe;


public class EditProfileFragment extends Fragment {

    private Profile profile;
    private String profilePhotoSelected;
    private ImageView profilePhoto;
    private ImageView photos[];
    private Base64Converter converter;
    private TextInputEditText descriptionInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profile = Session.getInstance().myProfile;
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        descriptionInput = (TextInputEditText) view.findViewById(R.id.edit_description_field);
        descriptionInput.setText(profile.description);

        converter = new Base64Converter();
        profilePhoto = (ImageView) view.findViewById(R.id.profile_picture);
        profilePhotoSelected = profile.profilePhoto;

        updateProfilePhotoBitmap();

        photos = new ImageView[5];
        photos[0] = (ImageView) view.findViewById(R.id.photo1);
        photos[1] = (ImageView) view.findViewById(R.id.photo2);
        photos[2] = (ImageView) view.findViewById(R.id.photo3);
        photos[3] = (ImageView) view.findViewById(R.id.photo4);
        photos[4] = (ImageView) view.findViewById(R.id.photo5);

        for (int i = 0 ; i < photos.length ; i ++) {
            final int j = i;
            if (profile.photos.length == 5 ) {
                photos[i].setImageBitmap(converter.Base64ToBitmap(profile.photos[i]));
            }
            photos[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = new AlphaAnimation(1, 0);
                    animation.setDuration(130);
                    v.startAnimation(animation);
                    if (profile.photos.length == 5 ) {
                        profilePhotoSelected = profile.photos[j];
                        updateProfilePhotoBitmap();
                    }

                }
            });
        }

        Button button = (Button) view.findViewById(R.id.edit_profile_save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionInput.getText().toString();
                WebServiceManager.getInstance(getContext()).updateProfile(profilePhotoSelected, description);
            }
        });
        return view;
    }




    private void updateProfilePhotoBitmap() {
        Bitmap profilePhotoBitmap = converter.Base64ToBitmap(profilePhotoSelected);
        if (profilePhotoBitmap != null) {
            profilePhoto.setImageBitmap(profilePhotoBitmap);
        }
    }



    @Subscribe
    public void onEditProfileSuccess(EditProfileRequestGenerator.EditProfileResponseListener.EditProfileSuccessEvent event) {
        profile.description = descriptionInput.getText().toString();
        profile.profilePhoto = profilePhotoSelected;
        profile.commitChanges();
    }

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent event) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getContext(), event.message, duration);
        toast.show();
    }


}

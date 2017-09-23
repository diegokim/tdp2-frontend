package com.example.android.linkup.profile.edit_profile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.edit_profile.EditProfileRequestGenerator;
import com.example.android.linkup.utils.Base64Converter;

import org.greenrobot.eventbus.Subscribe;


public class EditProfileActivity extends BaseActivity {

    private static final String EDIT_PROFILE_TITLE = "Editar perfil" ;
    private Profile profile;
    private String profilePhotoSelected;
    private ImageView profilePhoto;
    private ImageView photos[];
    private Base64Converter converter;
    private TextInputEditText descriptionInput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(bar);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(EDIT_PROFILE_TITLE);

        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_mode_edit_black_24dp, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        profile = Session.getInstance().myProfile;

        profile = Session.getInstance().myProfile;
        descriptionInput = (TextInputEditText) findViewById(R.id.edit_description_field);
        descriptionInput.setText(profile.description);

        converter = new Base64Converter();
        profilePhoto = (ImageView) findViewById(R.id.profile_picture);
        profilePhotoSelected = profile.profilePhoto;

        updateProfilePhotoBitmap();

        photos = new ImageView[5];
        photos[0] = (ImageView) findViewById(R.id.photo1);
        photos[1] = (ImageView) findViewById(R.id.photo2);
        photos[2] = (ImageView) findViewById(R.id.photo3);
        photos[3] = (ImageView) findViewById(R.id.photo4);
        photos[4] = (ImageView) findViewById(R.id.photo5);

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

        Button button = (Button) findViewById(R.id.edit_profile_save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionInput.getText().toString();
                WebServiceManager.getInstance(EditProfileActivity.this).updateProfile(profilePhotoSelected, description);
            }
        });
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
        finish();
    }

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent event) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, event.message, duration);
        toast.show();
    }


}

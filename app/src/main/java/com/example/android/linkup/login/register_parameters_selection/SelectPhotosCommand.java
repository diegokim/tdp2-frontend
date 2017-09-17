package com.example.android.linkup.login.register_parameters_selection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.utils.Command;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.register.RegisterData;
import com.example.android.linkup.login.AuthManager;

import java.util.ArrayList;

public class SelectPhotosCommand implements Command {


    private final BaseActivity activity;
    private final LayoutInflater inflater;
    private Photos photos;
    private ArrayList<String> photosSelected;
    private String profilePhoto;
    private String description;

    private android.app.AlertDialog mDialog;

    public SelectPhotosCommand (BaseActivity activity, Photos photos) {
        this.photos = photos;
        this.activity = activity;
        this.photosSelected = new ArrayList<>();
        this.profilePhoto = "";
        this.description = "";
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public void excecute() {
        createFivePhotosSelectionDialog();
    }

    public class FivePhotosSelectionListener implements PhotoSelectionAdapter.Listener {
        @Override
        public void onItemClick(String item, ImageView view) {
            if (photosSelected.contains(item)) {
                photosSelected.remove(item);
                view.setImageAlpha(255);
            } else if (photosSelected.size() < 5) {
                photosSelected.add(item);
                view.setImageAlpha(160);
            }
        }
    }


    private void createPhotosDialog(String title, DialogInterface.OnShowListener onShowListener, PhotoSelectionAdapter.Listener onPhotoClickListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = inflater.inflate(R.layout.select_photo_dialog, null);
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.photo_selection_recyclerview);

        PhotoSelectionAdapter adapter = new PhotoSelectionAdapter(photos.getPhotos(),activity, onPhotoClickListener);

        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);

        mBuilder.setView(mView);
        mBuilder.setTitle(title);
        mBuilder.setPositiveButton("OK", null );

        AlertDialog dialog = mBuilder.create();

        dialog.setOnShowListener(onShowListener);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                AuthManager.getInstance(activity).signOut();
            }
        });
        mDialog = dialog;
        dialog.show();
    }

    private void createFivePhotosSelectionDialog() {
        createPhotosDialog("Selecciona 5 fotos", new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new FivePhotosSelectedOkListener());
            }
        },
        new FivePhotosSelectionListener());
    }

    class FivePhotosSelectedOkListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (photosSelected.size() == 5) {
                mDialog.dismiss();
                createProfilePhotoSelectionDialog();
            }
        }
    }

    private void createProfilePhotoSelectionDialog() {
        createPhotosDialog( "Selecciona una foto de perfil" ,new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new ProfilePhotoSelectedOkListener());
            }
        },
        new ProfilePhotoSelectionListener());
    }



    public class ProfilePhotoSelectionListener implements PhotoSelectionAdapter.Listener {
        @Override
        public void onItemClick(String item, ImageView view) {
            if (profilePhoto.equals(item)) {
                profilePhoto = "";
                view.setImageAlpha(255);
            } else if (profilePhoto.equals("")) {
                profilePhoto = item;
                view.setImageAlpha(160);
            }
        }
    }

    class ProfilePhotoSelectedOkListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (!profilePhoto.equals("")) {
                //TODO: communicarse tirar el request e ir al perfil o a descripcion.
                mDialog.dismiss();
                createDescriptionInputDialog();
            }
        }
    }


    public void createDescriptionInputDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = inflater.inflate(R.layout.description_input_dialog, null);

        final TextView descriptionTextView = (TextView) mView.findViewById(R.id.description_text_field);

        mBuilder.setView(mView);
        mBuilder.setTitle("Descripcion");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                description = descriptionTextView.getText().toString();

                RegisterData data = new RegisterData();
                data.description = description;
                data.photos = photosSelected;
                data.profilePhoto = profilePhoto;

                WebServiceManager.getInstance(activity).register(data);
            }
        } );

        AlertDialog dialog = mBuilder.create();

        mDialog = dialog;
        dialog.show();
    }


}

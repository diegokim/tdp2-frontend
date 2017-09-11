package com.example.android.linkup.login.photo_selection;

import android.app.Activity;
import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.android.linkup.R;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.login.RegisterOnSuccessCommand;
import com.example.android.linkup.network.Command;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.ToastErrorCommand;
import com.example.android.linkup.network.register.RegisterData;
import com.example.android.linkup.network.register.RegisterErrorListener;
import com.example.android.linkup.network.register.RegisterRequestGenerator;

import java.util.ArrayList;

public class SelectPhotosCommand implements Command {

    private final Context context;
    private final Activity activity;
    private final LayoutInflater inflater;
    private Photos photos;
    private int amountOfPhotosSelected;
    private ArrayList<String> photosSelected;
    private String profilePhoto;
    private String description;

    private android.app.AlertDialog mDialog;

    public SelectPhotosCommand (Context context, LayoutInflater inflater, Photos photos, Activity activity) {
        this.photos = photos;
        this.context = context;
        this.activity = activity;
        this.inflater = inflater;
        this.amountOfPhotosSelected = 0;
        this.photosSelected = new ArrayList<>();
        this.profilePhoto = "";
        this.description = "";
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
                Log.e("PHOTOS SIZE" ,Integer.toString(photosSelected.size()));
            } else if (photosSelected.size() < 5) {
                photosSelected.add(item);
                view.setImageAlpha(160);
                Log.e("PHOTOS SIZE" ,Integer.toString(photosSelected.size()));
            } else {
                Toast.makeText(context, "Solo puedes seleccionar 5 fotos",Toast.LENGTH_LONG);
            }
        }
    }

    private void createFivePhotosSelectionDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.select_photo_dialog, null);
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.photo_selection_recyclerview);

        PhotoSelectionAdapter adapter = new PhotoSelectionAdapter(photos.getPhotos(),context, new FivePhotosSelectionListener());

        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

        mBuilder.setView(mView);
        mBuilder.setTitle("Selecciona 5 fotos");
        mBuilder.setPositiveButton("OK", null );
        mBuilder.setNegativeButton("Cancelar", null );

        AlertDialog dialog = mBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new FivePhotosSelectedOkListener(context));
            }
        });

        mDialog = dialog;
        dialog.show();
    }

    class FivePhotosSelectedOkListener implements View.OnClickListener {
        private final Context context;

        public FivePhotosSelectedOkListener (Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (photosSelected.size() == 5) {
                mDialog.dismiss();
                createProfilePhotoSelectionDialog();
            }
        }
    }

    private void createProfilePhotoSelectionDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.select_photo_dialog, null);
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.photo_selection_recyclerview);

        PhotoSelectionAdapter adapter = new PhotoSelectionAdapter(photosSelected,context, new ProfilePhotoSelectionListener());

        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

        mBuilder.setView(mView);
        mBuilder.setTitle("Selecciona una foto de perfil");
        mBuilder.setPositiveButton("OK", null );
        mBuilder.setNeutralButton("Cancelar", null );
        AlertDialog dialog = mBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new ProfilePhotoSelectedOkListener(context));
            }
        });
        mDialog = dialog;
        dialog.show();
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
            } else {
                Toast.makeText(context, "Solo puedes seleccionar una foto de perfil",Toast.LENGTH_LONG);
            }
        }
    }

    class ProfilePhotoSelectedOkListener implements View.OnClickListener {
        private final Context context;

        public ProfilePhotoSelectedOkListener (Context context) {
            this.context = context;
        }

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
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = inflater.inflate(R.layout.description_input_dialog, null);

        final TextView descriptionTextView = (TextView) mView.findViewById(R.id.description_text_field);

        mBuilder.setView(mView);
        mBuilder.setTitle("Contanos un poquito acerca de vos");
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                description = descriptionTextView.getText().toString();
                Command  onSuccessCommand = new RegisterOnSuccessCommand(activity);
                Command onErrCommand = new ToastErrorCommand(context, NetworkConfiguration.SERVER_REQUEST_ERROR);

                RegisterData data = new RegisterData();
                data.description = description;
                data.photos = photosSelected;
                data.profilePhoto = profilePhoto;

                Request registerRequest = RegisterRequestGenerator.generate(onSuccessCommand, onErrCommand, data);
                NetworkRequestQueue.getInstance(context).addToRequestQueue(registerRequest);
            }
        } );
        mBuilder.setNegativeButton("Cancelar", null );

        AlertDialog dialog = mBuilder.create();

        mDialog = dialog;
        dialog.show();
    }


}

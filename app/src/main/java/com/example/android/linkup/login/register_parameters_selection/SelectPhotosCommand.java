package com.example.android.linkup.login.register_parameters_selection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.profile.information_fragments.PhotosAdapter;
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
    private Location location;

    private android.app.AlertDialog mDialog;

    public SelectPhotosCommand (BaseActivity activity, Photos photos, Location location) {
        this.photos = photos;
        this.activity = activity;
        this.photosSelected = new ArrayList<>();
        this.profilePhoto = "";
        this.description = "";
        this.inflater = activity.getLayoutInflater();
        this.location = location;
    }

    @Override
    public void excecute() {
        createFivePhotosSelectionDialog();
    }

    public class FivePhotosSelectionListener implements PhotoSelectionAdapter.Listener {
        private PhotoSelectionAdapter adapter;

        @Override
        public void onItemClick(String item, ImageView view) {
            PhotoSelectionViewHolder viewHolder = (PhotoSelectionViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            if (photosSelected.contains(item)) {
                photosSelected.remove(item);
                adapter.isSelectedList.set(position,false);
                viewHolder.update(false);
            } else if (photosSelected.size() < 5) {
                photosSelected.add(item);
                view.setImageAlpha(160);
                adapter.isSelectedList.set(position,true);
                viewHolder.update(true);
            }
        }

        @Override
        public void setAdapter(PhotoSelectionAdapter adapter) {
            this.adapter = adapter;
        }
    }


    private void createPhotosDialog(String title, DialogInterface.OnShowListener onShowListener, PhotoSelectionAdapter.Listener onPhotoClickListener, Photos photosToDisplay) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = inflater.inflate(R.layout.select_photo_dialog, null);
        RecyclerView mRecyclerView = (RecyclerView) mView.findViewById(R.id.photo_selection_recyclerview);

        PhotoSelectionAdapter adapter = new PhotoSelectionAdapter(photosToDisplay.getPhotos(),activity, onPhotoClickListener);
        onPhotoClickListener.setAdapter(adapter);
        mRecyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(layoutManager);

        mBuilder.setView(mView);
        mBuilder.setTitle(title);
        mBuilder.setPositiveButton("OK", null );

        AlertDialog dialog = mBuilder.create();

        dialog.setOnShowListener(onShowListener);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("LOGOUT" , "laksjdlfkajsdlfj");
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
        new FivePhotosSelectionListener(), photos);
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
        new ProfilePhotoSelectionListener(), new Photos(photosSelected));
    }



    public class ProfilePhotoSelectionListener implements PhotoSelectionAdapter.Listener {

        private PhotoSelectionAdapter adapter;

        public void setAdapter (PhotoSelectionAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(String item, ImageView view) {
            PhotoSelectionViewHolder viewHolder = (PhotoSelectionViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            if (profilePhoto.equals(item)) {
                profilePhoto = "";
                adapter.isSelectedList.set(position,false);
                viewHolder.update(false);
            } else if (profilePhoto.equals("")) {
                profilePhoto = item;
                adapter.isSelectedList.set(position,true);
                viewHolder.update(true);
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
        descriptionTextView.setText("Soy una persona divertida, a la que le gusta arriesgarse y vivir la vida al máximo.");
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
                data.location = location;

                WebServiceManager.getInstance(activity).register(data);
            }
        } );

        AlertDialog dialog = mBuilder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AuthManager.getInstance(activity).signOut();

            }
        });
        mDialog = dialog;
        dialog.show();
    }


}

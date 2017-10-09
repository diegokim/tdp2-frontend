package com.example.android.linkup.links;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.chat.ChatActivity;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.Base64Converter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LinksAdapter extends RecyclerView.Adapter<LinksViewHolder>{

    ArrayList<Profile> links;
    LayoutInflater inflater;
    Base64Converter converter;
    Context context;

    public LinksAdapter (Context context, ArrayList<Profile> links) {
        this.links = links;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        converter = new Base64Converter();
    }

    @Override
    public LinksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_link, parent, false);
        return new LinksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LinksViewHolder holder, int position) {
        final Profile profile = links.get(position);
        Bitmap photo = converter.Base64ToBitmap(profile.profilePhoto);

        holder.profilePhoto.setImageBitmap(photo);
        holder.header.setText(profile.name + ", " + Integer.toString(profile.age));
        // TODO: Set the real id
        final String myId = Session.getInstance().myProfile.id;
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats/"+myId+"/messages/"+profile.id);

        Query lastQuery = ref.orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0 ) {
                    holder.lastMessage.setText("Haz click para conversar!");
                } else {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //Log.d("user key",child.getKey());
                        //Log.d("user val",child.child("message").getValue().toString());
                        holder.lastMessage.setText(child.child("message").getValue().toString());
                        String lastMessageUser = child.child("messageUser").getValue().toString();
                        if (! lastMessageUser.equals(Session.getInstance().myProfile.name)) {
                            holder.lastMessage.setText(lastMessageUser + ": " + child.child("message").getValue().toString());
                            Boolean viewed = (Boolean) child.child("viewed").getValue();
                            if (! viewed ) {
                                holder.lastMessage.setTypeface(null, Typeface.BOLD_ITALIC);
                            }
                        } else {
                            holder.lastMessage.setText("yo: " + child.child("message").getValue().toString());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                holder.lastMessage.setText("Error al obtener mensajes!");
            }
        });

//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("ASDJ","ASLKDJ");
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        holder.moreVert.setOnClickListener(new LinkManagementPopUpClickListener(profile));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ActiveChatProfile.getInstance().update(profile);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats/"+ myId +"/messages/"+profile.id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DatabaseReference innerRef = FirebaseDatabase.getInstance().getReference("chats/"+ myId +"/messages/"+profile.id);
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            innerRef.child(key).child("viewed").setValue(true);
                        }
                        innerRef.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                context.startActivity(new Intent(context, ChatActivity.class));
            }
        });
    }

    public static class LinkManagementPopUpClickListener implements View.OnClickListener{
        private Profile profile;

        public LinkManagementPopUpClickListener (Profile profile) {
            this.profile = profile;
        }
        @Override
        public void onClick(View v) {
            final Context context = v.getContext();
            final PopupMenu popup = new PopupMenu(context, v);

            popup.getMenuInflater()
                    .inflate(R.menu.links_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().equals("Eliminar")){
                        // TODO: Send delete link request
                        WebServiceManager.getInstance(context).deleteLink(profile.id);
                    } else if (item.getTitle().equals("Bloquear")) {
                        // TODO: send block user request
                        WebServiceManager.getInstance(context).blockUser(profile.id);
                    } else if (item.getTitle().equals("Reportar")) {
                        // TODO: open dialog asking for reason and then send report request
                        String reason = "razon";
                        WebServiceManager.getInstance(context).reportUser(profile.id,reason);
                    }
                    return true;
                }
            });
            popup.show();
        }
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}

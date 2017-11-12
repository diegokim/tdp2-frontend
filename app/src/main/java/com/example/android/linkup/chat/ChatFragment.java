package com.example.android.linkup.chat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.ChatMessage;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatFragment extends Fragment {

    private FirebaseListAdapter<ChatMessage> adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.activity_chat,container, false);
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // User Not signed In
            Toast.makeText(getActivity(),
                    "Error de red!",
                    Toast.LENGTH_LONG)
                    .show();
            getActivity().finish();
        } else {
            displayChatMessages(v);
        }

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) v.findViewById(R.id.input);

                String id_guest = ActiveChatProfile.getInstance().profile.id;
                String id_host = Session.getInstance().myProfile.id;

                boolean highlight = true;


                WebServiceManager.getInstance().sendChatMessage(input.getText().toString(),id_guest);

                input.setText("");
            }
        });


        return v;
    }

    private void displayChatMessages(View view) {
        ListView listOfMessages = (ListView) view.findViewById(R.id.list_of_messages);

        String id_guest = ActiveChatProfile.getInstance().profile.id;
        String id_host = Session.getInstance().myProfile.id;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats/"+id_host+"/messages/"+id_guest);

        adapter = new FirebaseListAdapter<ChatMessage>(getActivity(), ChatMessage.class,
                R.layout.message, ref) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml

                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessage());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
}

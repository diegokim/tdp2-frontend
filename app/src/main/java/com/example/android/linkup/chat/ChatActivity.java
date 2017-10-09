package com.example.android.linkup.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.ChatMessage;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.candidates.ActionOnCandidateResponseListener;
import com.example.android.linkup.profile.ProfileActivity;
import com.example.android.linkup.profile.edit_profile.EditProfileActivity;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ChatActivity extends AppCompatActivity {

    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(ActiveChatProfile.getInstance().profile.name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // User Not signed In
            Toast.makeText(this,
                    "Error de red!",
                    Toast.LENGTH_LONG)
                    .show();
            finish();
        } else {
            displayChatMessages();
        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                String id_guest = ActiveChatProfile.getInstance().profile.id;
                String id_host = Session.getInstance().myProfile.id;

                boolean highlight = true;

                if (noForbbidenMessages(input)) {
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance()
                            .getReference("chats/"+id_host+"/messages/"+id_guest)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),id_guest,highlight)
                            );

                    FirebaseDatabase.getInstance()
                            .getReference("chats/"+id_guest+"/messages/"+id_host)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),id_guest,highlight)
                            );

                    // Clear the input
                    input.setText("");
                } else {
                    if (!input.getText().toString().equals("")) {
                        input.setText("Cochino!!!");
                    }
                }
            }
        });
    }

    private boolean noForbbidenMessages(EditText input) {
        boolean respuesta = true;
        if (input.getText().toString().equals("caca")) respuesta = false;
        if (input.getText().toString().equals("")) respuesta = false;
        if (input.getText().toString().equals("pedo")) respuesta = false;

        return respuesta;
    }

    private void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

        String id_guest = ActiveChatProfile.getInstance().profile.id;
        String id_host = Session.getInstance().myProfile.id;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("chats/"+id_host+"/messages/"+id_guest);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem blockItem = menu.add("Bloquear");
        final String id = ActiveChatProfile.getInstance().profile.id;
        blockItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                WebServiceManager.getInstance(getApplicationContext()).blockUser(id);
                return false;
            }
        });

        MenuItem reportItem = menu.add("Denunciar");
        reportItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                createReportDialog(id);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void createReportDialog(final String userId) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.description_input_dialog, null);

        final TextView descriptionTextView = (TextView) mView.findViewById(R.id.description_text_field);
        mBuilder.setView(mView);
        mBuilder.setTitle("Razon");
        mBuilder.setPositiveButton("Denunciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reason = descriptionTextView.getText().toString();
                WebServiceManager.getInstance(getApplicationContext()).reportUser(userId, reason);
            }
        } );

        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onActionSuccessEvent(ActionOnCandidateResponseListener.OnActionSuccessEvent event) {
        finish();
    }
}

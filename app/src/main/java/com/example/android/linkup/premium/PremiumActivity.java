package com.example.android.linkup.premium;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.linkup.R;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;
import com.example.android.linkup.settings.SettingsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PremiumActivity extends AppCompatActivity {

    private Settings mySettings;
    private Switch premium_switch = null;
    private static final int REQUEST_UPDATE_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mySettings = new Settings();
        mySettings.update(Session.getInstance().mySettings);

        //PREMIUM
        boolean isPremium = mySettings.accountType.equals("premium");

        premium_switch = (Switch) findViewById(R.id.premium_account_switch);
        premium_switch.setChecked(isPremium);
        premium_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //TODO: Simulate Payment

                    final View view = getLayoutInflater().inflate(R.layout.credit_card_dialog, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(PremiumActivity.this);
                    final AlertDialog alertDialog = new AlertDialog.Builder(PremiumActivity.this).create();

                    alertDialog.setTitle("Confirmar Datos");
                    alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_credit_card));
                    alertDialog.setMessage("Desea volverse usuario premium?");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mySettings.accountType = "premium";
                            mySettings.updateSettings(PremiumActivity.this);
                            Toast.makeText(PremiumActivity.this, "Datos Verificados", Toast.LENGTH_SHORT).show();
                        }
                    });


                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            premium_switch.setChecked(false);
                            mySettings.accountType = "free";
                            mySettings.updateSettings(PremiumActivity.this);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setView(view);
                    alertDialog.show();
                } else {
                    mySettings.accountType = "free";
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            if (premium_switch.isChecked()) {
                mySettings.accountType = "premium";
                Session.getInstance().mySettings.accountType = "premium";
            } else {
                Session.getInstance().mySettings.accountType = "free";
                mySettings.accountType = "free";
            }
            mySettings.updateSettings(PremiumActivity.this);
            setResult(RESULT_OK);
            finish(); // close this activity and return to preview activity
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent event) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, event.message, duration);
        toast.show();
    }

    @Subscribe
    public void onUpdateSettingsSuccess (SaveSettingsResponseListener.SaveSettingsSuccessEvent event) {
        //Toast.makeText(this,"Configuración guardada con éxito!",Toast.LENGTH_LONG).show();
        Session.getInstance().mySettings.update(event.settings);
        setResult(RESULT_OK);
        //finish();
    }
}

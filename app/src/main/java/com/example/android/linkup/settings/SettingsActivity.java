package com.example.android.linkup.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.login.LoginActivity;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import io.apptik.widget.MultiSlider;

public class SettingsActivity extends AppCompatActivity {

    private static final int REQUEST_UPDATE_SETTINGS = 1;
    private MultiSlider age_slider = null;
    private SeekBar distance_slider = null;
    private Switch invisible_switch = null;
    private Switch notifications_switch = null;
    //private Switch premium_switch = null;
    private RadioButton just_friends_radioButton = null;
    private RadioButton findCouple_radioButton = null;
    private CheckBox men_checkbox = null;
    private CheckBox women_checkbox = null;
    private Settings mySettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mySettings = new Settings();
        mySettings.update(Session.getInstance().mySettings);

        //EDAD
        final TextView min_age_status = (TextView) findViewById(R.id.min_age_value);
        final TextView max_age_status = (TextView) findViewById(R.id.max_age_value);
        age_slider = (MultiSlider) findViewById(R.id.edad_bar);
        age_slider.setMax(150);
        age_slider.setMin(18);
        age_slider.getThumb(0).setValue(mySettings.age_from);
        age_slider.getThumb(1).setValue(mySettings.age_to);
        min_age_status.setText(String.valueOf(age_slider.getThumb(0).getValue()));
        max_age_status.setText(String.valueOf(age_slider.getThumb(1).getValue()));

        age_slider.setOnThumbValueChangeListener(new MultiSlider.SimpleChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if (thumbIndex == 0) {
                    min_age_status.setText(String.valueOf(value));
                    mySettings.age_from = value;
                } else {
                    max_age_status.setText(String.valueOf(value));
                    mySettings.age_to = value;
                }
            }
        });

        //DISTANCIA
        final TextView distance_status = (TextView) findViewById(R.id.km_value);
        distance_slider = (SeekBar) findViewById(R.id.distancia_bar);
        distance_slider.setProgress(mySettings.range);
        distance_status.setText(String.valueOf(distance_slider.getProgress()));
        distance_slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distance_status.setText(String.valueOf(progress));
                mySettings.range = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //INVISIBLE
        invisible_switch = (Switch) findViewById(R.id.invisible_switch);
        invisible_switch.setChecked(mySettings.invisible);
        invisible_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mySettings.invisible = b;
            }
        });

        //NOTIFICATIONS
        notifications_switch = (Switch) findViewById(R.id.notifications_switch);
        notifications_switch.setChecked(mySettings.notifications);
        notifications_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mySettings.notifications = b;
            }
        });
/*
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    final AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();

                    alertDialog.setTitle("Confirmar Datos");
                    alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_credit_card));
                    alertDialog.setMessage("Desea volverse usuario premium?");

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SettingsActivity.this, "Datos Verificados", Toast.LENGTH_SHORT).show();
                            mySettings.accountType = "premium";
                        }
                    });


                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            premium_switch.setChecked(false);
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
*/
        //ENCONTRAR PAREJA
        findCouple_radioButton = (RadioButton) findViewById(R.id.pareja_radioButton);
        just_friends_radioButton = (RadioButton) findViewById(R.id.solo_amigos_radioButton);

        men_checkbox = (CheckBox) findViewById(R.id.hombres_checkbox);
        women_checkbox = (CheckBox) findViewById(R.id.mujeres_checkbox);

        if (mySettings.pareja) {
            findCouple_radioButton.setChecked(true);
            men_checkbox.setChecked(mySettings.hombres);
            women_checkbox.setChecked(mySettings.mujeres);
        } else {
            //SOLO AMIGOS
            just_friends_radioButton.setChecked(true);
        }


    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        CheckBox hombres = (CheckBox) findViewById(R.id.hombres_checkbox);
        CheckBox mujeres = (CheckBox) findViewById(R.id.mujeres_checkbox);
        switch(view.getId()) {
            case R.id.pareja_radioButton:
                if (checked){
                    mySettings.pareja = true;
                    mySettings.solo_amigos = false;
                    hombres.setChecked(true);
                    mySettings.hombres = true;
                    mujeres.setChecked(true);
                    mySettings.mujeres = true;
                }
                break;
            case R.id.solo_amigos_radioButton:
                if (checked){
                    mySettings.pareja = false;
                    mySettings.solo_amigos = true;
                    hombres.setChecked(false);
                    mySettings.hombres = false;
                    mujeres.setChecked(false);
                    mySettings.mujeres = false;
                }
                break;
        }
    }

    public void onCheckboxClicked (View view) {
        boolean checked = ((CheckBox) view).isChecked();
        RadioButton solo_amigos = (RadioButton) findViewById(R.id.solo_amigos_radioButton);
        CheckBox hombres = (CheckBox) findViewById(R.id.hombres_checkbox);
        CheckBox mujeres = (CheckBox) findViewById(R.id.mujeres_checkbox);
        if (!solo_amigos.isChecked()) {
            switch(view.getId()) {
                case R.id.mujeres_checkbox:
                    if (checked){
                        mySettings.mujeres = true;
                    }
                    else {
                        if (hombres.isChecked()) {
                            mySettings.mujeres = false;
                        } else {
                            mySettings.mujeres = true;
                            mujeres.setChecked(true);
                        }
                    }

                    break;
                case R.id.hombres_checkbox:
                    if (checked){
                        mySettings.hombres = true;
                    }
                    else {
                        if (mujeres.isChecked()) {
                            mySettings.hombres = false;
                        } else {
                            mySettings.hombres = true;
                            hombres.setChecked(true);
                        }
                    }
                    break;

            }
        } else {
            ((CheckBox) view).setChecked(false);
        }
    }

    public void sendSettings(View view) {
        if (view.getId() == R.id.guardar_cambios) {
            mySettings.updateSettings(SettingsActivity.this);
        }
    }

    public void borrarCuenta(View view) {
        if (view.getId() == R.id.borrar_cuenta) {
            final View viewdialog = getLayoutInflater().inflate(R.layout.delete_account_dialog, null);

            final AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();

            final EditText descriptionTextView = (EditText) viewdialog.findViewById(R.id.editText);

            alertDialog.setTitle("Eliminar mi Cuenta");
            alertDialog.setIcon(getResources().getDrawable(R.drawable.ic_report));
            alertDialog.setMessage("Desea eliminar su cuenta definitivamente?");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (descriptionTextView.getText().toString().equals("Quiero borrar mi cuenta")) {
                        Toast.makeText(SettingsActivity.this, "Vuelve pronto! :)", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        WebServiceManager.getInstance().deleteAccount();
                        startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SettingsActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setView(viewdialog);
            alertDialog.show();
        }
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
        Toast.makeText(this,"Configuración guardada con éxito!",Toast.LENGTH_LONG).show();
        Session.getInstance().mySettings.update(event.settings);
        setResult(RESULT_OK);
        finish();
    }
}

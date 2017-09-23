package com.example.android.linkup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    private SeekBar edad = null;
    private SeekBar distancia = null;
    private boolean pareja = true;
    private boolean hombres = true;
    private boolean mujeres = true;
    private boolean solo_amigos = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        edad = (SeekBar) findViewById(R.id.edad_bar);
        distancia = (SeekBar) findViewById(R.id.distancia_bar);

        edad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            //Used to notify that the progress level has changed.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            //Used to notify that the user has started a touch gesture.
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //Used to notify that the user has finished a touch gesture
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(PreferencesActivity.this,"seek bar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.pareja_radioButton:
                if (checked)
                    this.pareja = true;
                    this.solo_amigos = false;
                    break;
            case R.id.solo_amigos_radioButton:
                if (checked)
                    this.pareja = false;
                    this.solo_amigos = true;
                    break;
        }
    }
}

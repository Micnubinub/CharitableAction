package bigshots.people_helping_people;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import bigshots.people_helping_people.utilities.Utils;
import bigshots.people_helping_people.views.MaterialSwitch;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class Preferences extends Activity {
    //Banner autostart
    private MaterialSwitch autoStart, toast;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        autoStart = (MaterialSwitch) findViewById(R.id.auto_start_boot);
        autoStart.setText("Banner at startup");
        autoStart.setChecked(prefs.getBoolean(Utils.AUTO_START_BOOL, false));
        autoStart.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utils.AUTO_START_BOOL, isChecked).commit();
            }
        });

        toast = (MaterialSwitch) findViewById(R.id.toast_before);
        toast.setText("Fullscreen Ad warning");
        toast.setChecked(prefs.getBoolean(Utils.TOAST_BEFORE_BOOL, false));
        toast.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utils.TOAST_BEFORE_BOOL, isChecked).commit();
            }
        });
    }


}

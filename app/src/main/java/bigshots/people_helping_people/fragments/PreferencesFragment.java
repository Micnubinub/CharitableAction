package bigshots.people_helping_people.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.utilities.Utils;
import bigshots.people_helping_people.views.MaterialSwitch;


public class PreferencesFragment extends BaseFragment {
    private MaterialSwitch autoStart, toast, adsAtBoot;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public PreferencesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.context);
        editor = prefs.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.preferences, container, false);

        autoStart = (MaterialSwitch) view.findViewById(R.id.auto_start_boot);
        autoStart.setText("Banner at startup");
        autoStart.setChecked(prefs.getBoolean(Utils.AUTO_START_BOOL, false));
        autoStart.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utils.AUTO_START_BOOL, isChecked).commit();
            }
        });

        toast = (MaterialSwitch) view.findViewById(R.id.toast_before);
        toast.setText("Fullscreen Ad warning");
        toast.setChecked(prefs.getBoolean(Utils.TOAST_BEFORE_BOOL, true));
        toast.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utils.TOAST_BEFORE_BOOL, isChecked).commit();
            }
        });

        adsAtBoot = (MaterialSwitch) view.findViewById(R.id.scheduled_ads_at_boot);
        adsAtBoot.setText("Scheduled ads at boot");
        adsAtBoot.setChecked(prefs.getBoolean(Utils.ADS_AT_START_BOOL, true));
        adsAtBoot.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utils.ADS_AT_START_BOOL, isChecked).commit();
            }
        });
        return view;
    }


    @Override
    protected void update() {
        try {
            autoStart.setChecked(prefs.getBoolean(Utils.AUTO_START_BOOL, false));
            toast.setChecked(prefs.getBoolean(Utils.TOAST_BEFORE_BOOL, true));
            adsAtBoot.setChecked(prefs.getBoolean(Utils.ADS_AT_START_BOOL, true));
        } catch (Exception e) {

        }
    }
}

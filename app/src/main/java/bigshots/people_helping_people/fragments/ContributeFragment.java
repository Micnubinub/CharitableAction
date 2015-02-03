package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.Connector;
import bigshots.people_helping_people.schedule_wheel.AbstractWheel;
import bigshots.people_helping_people.schedule_wheel.OnWheelChangedListener;
import bigshots.people_helping_people.schedule_wheel.adapters.NumericWheelAdapter;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.services.ScheduledAdsManager;
import bigshots.people_helping_people.utilities.Utility;
import bigshots.people_helping_people.views.MaterialCheckBox;
import bigshots.people_helping_people.views.MaterialSwitch;


public class ContributeFragment extends BaseFragment {

    private static String prefix;
    private static int frequencyMinutes;
    private static MaterialSwitch reminderSwitch;
    private static Dialog dialog;

    private static final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.full_screen:
                    MainMenu.fullScreenClicked = true;
                    MainMenu.adManager.getFullscreenAd().show();
                    break;
                case R.id.video_ad:
                    MainMenu.videoClicked = true;
                    MainMenu.adManager.getVideoAd().show();
                    break;
                case R.id.share_app:
                    final Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out People Helping People : https://play.google.com/store/apps/details?id=bigshots.people_helping_people");
                    sendIntent.setType("text/plain");
                    MainMenu.context.startActivity(sendIntent);
                    break;
                case R.id.configure_scheduled_ads:
                    dialog = getScheduledAds();
                    dialog.show();
                    break;
                case R.id.save:
                    save();
                case R.id.cancel:
                    if (dialog != null)
                        dialog.dismiss();
                    break;
            }
        }
    };

    private static MaterialSwitch autoStart, toast, adsAtBoot;
    private static SharedPreferences.Editor editor;
    private static SharedPreferences prefs;
    private static boolean loopBool = true, enable_schedule;

    public ContributeFragment() {

    }

    private static Dialog getScheduledAds() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.scheduled_dialog);
        prefix = prefs.getBoolean(Utility.LOOP_SCHEDULE, true) ? "A full screen Ad will be shown every : " : "A full screen Ad will be shown in : ";
        final TextView frequency = (TextView) dialog.findViewById(R.id.frequency);
        final AbstractWheel hours = (AbstractWheel) dialog.findViewById(R.id.hours);
        final AbstractWheel minutes = (AbstractWheel) dialog.findViewById(R.id.minutes);
        final MaterialCheckBox loop = (MaterialCheckBox) dialog.findViewById(R.id.loop_checkbox);

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.save).setOnClickListener(listener);
        dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel).setOnClickListener(listener);

        hours.setViewAdapter(new NumericWheelAdapter(MainMenu.context, 0, 23));
        hours.setCyclic(true);

        minutes.setViewAdapter(new NumericWheelAdapter(MainMenu.context, 0, 59));
        minutes.setCyclic(true);

        // set current time
        frequencyMinutes = prefs.getInt(Utility.FULLSCREEN_AD_FREQUENCY_MINUTES, 20);

        hours.setCurrentItem(frequencyMinutes / 60);
        minutes.setCurrentItem(frequencyMinutes % 60);

        frequency.setText(prefix + (frequencyMinutes == 1 ? " minute" : frequencyMinutes + " minutes"));

        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                final StringBuilder text = new StringBuilder();

                int mins = minutes.getCurrentItem();
                int hr = hours.getCurrentItem();
                frequencyMinutes = (hr * 60) + mins;
                text.append(prefix);
                if (!(hr == 0)) {
                    text.append(hr);
                    text.append(hr == 1 ? " hour" : " hours");
                    text.append(" and ");
                }

                if (!(mins == 0)) {
                    text.append(mins);
                    text.append(mins == 1 ? " minute" : " minutes");
                }

                final String out = text.toString();

                if (out.equals(prefix))
                    frequency.post(new Runnable() {
                        @Override
                        public void run() {
                            frequency.setText("No full screen ads will be scheduled (will remove current schedule)");
                        }
                    });
                else
                    frequency.post(new Runnable() {
                        @Override
                        public void run() {
                            frequency.setText(out);
                        }
                    });
            }
        };

        loop.setChecked(prefs.getBoolean(Utility.LOOP_SCHEDULE, true));
        loop.setText("Repeat?");
        loop.setOnCheckedChangeListener(new MaterialCheckBox.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialCheckBox materialCheckBox, boolean isChecked) {
                loopBool = isChecked;
                prefix = isChecked ? "A full screen Ad will be shown every : " : "A full screen Ad will be shown in : ";
                final StringBuilder text = new StringBuilder();

                int mins = minutes.getCurrentItem();
                int hr = hours.getCurrentItem();
                frequencyMinutes = (hr * 60) + mins;
                text.append(prefix);
                if (!(hr == 0)) {
                    text.append(hr);
                    text.append(hr == 1 ? " hour" : " hours");
                    text.append(" and ");
                }

                if (!(mins == 0)) {
                    text.append(mins);
                    text.append(mins == 1 ? " minute" : " minutes");
                }

                final String out = text.toString();

                if (out.equals(prefix))
                    frequency.post(new Runnable() {
                        @Override
                        public void run() {
                            frequency.setText("No full screen ads will be scheduled (will remove current schedule)");
                        }
                    });
                else
                    frequency.post(new Runnable() {
                        @Override
                        public void run() {
                            frequency.setText(out);
                        }
                    });

            }
        });

        hours.addChangingListener(wheelListener);
        minutes.addChangingListener(wheelListener);
        return dialog;
    }

    private static void save() {
        if (frequencyMinutes > 0)
            if (frequencyMinutes == 1)
                Toast.makeText(MainMenu.context, "Scheduled for 1 minute", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainMenu.context, String.format("Scheduled for %d minutes", frequencyMinutes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainMenu.context, "Schedule cleared", Toast.LENGTH_SHORT).show();

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Utility.FULLSCREEN_AD_FREQUENCY_MINUTES, frequencyMinutes).apply();
        editor.putBoolean(Utility.LOOP_SCHEDULE, loopBool).apply();

        if (prefs.getBoolean(Utility.ENABLE_SCHEDULED_ADS, false)) {
            if (!ScheduledAdsManager.isServiceRunning()) {
                MainMenu.context.startService(new Intent(MainMenu.context, ScheduledAdsManager.class));
            }
            ScheduledAdsManager.scheduleNext(MainMenu.context, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.contribute, container, false);

        view.findViewById(R.id.full_screen).setOnClickListener(listener);
        view.findViewById(R.id.video_ad).setOnClickListener(listener);
        view.findViewById(R.id.share_app).setOnClickListener(listener);
        view.findViewById(R.id.configure_scheduled_ads).setOnClickListener(listener);


        final MaterialSwitch scheduledAdsSwitch = (MaterialSwitch) view.findViewById(R.id.enable_scheduled_ads);
        scheduledAdsSwitch.setChecked(prefs.getBoolean(Utility.ENABLE_SCHEDULED_ADS, false));
        scheduledAdsSwitch.setText("Enable Scheduled Ads");
        scheduledAdsSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utility.ENABLE_SCHEDULED_ADS, isChecked).apply();
                if (isChecked) {
                    if (!ScheduledAdsManager.isServiceRunning()) {
                        MainMenu.context.startService(new Intent(MainMenu.context, ScheduledAdsManager.class));
                    }
                    ScheduledAdsManager.showNotification(MainMenu.context);
                    ScheduledAdsManager.scheduleNext(MainMenu.context, true);
                } else
                    ScheduledAdsManager.cancelNotification(MainMenu.context);
            }
        });

        reminderSwitch = (MaterialSwitch) view.findViewById(R.id.enable_reminders);
        reminderSwitch.setChecked(prefs.getBoolean(Utility.ENABLE_REMINDER, false));
        reminderSwitch.setText("Enable reminders");
        reminderSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utility.ENABLE_REMINDER, isChecked).apply();
                if (isChecked) {
                    getReminderDialog().show();
                } else
                    ScheduledAdsManager.cancelReminder(MainMenu.context);
            }
        });

        autoStart = (MaterialSwitch) view.findViewById(R.id.auto_start_boot);
        autoStart.setText("Banner at startup");
        autoStart.setChecked(prefs.getBoolean(Utility.AUTO_START_BOOL, false));
        autoStart.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utility.AUTO_START_BOOL, isChecked).commit();
            }
        });

        toast = (MaterialSwitch) view.findViewById(R.id.toast_before);
        toast.setText("Fullscreen Ad warning");
        toast.setChecked(prefs.getBoolean(Utility.TOAST_BEFORE_BOOL, true));
        toast.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utility.TOAST_BEFORE_BOOL, isChecked).commit();
            }
        });

        adsAtBoot = (MaterialSwitch) view.findViewById(R.id.scheduled_ads_at_boot);
        adsAtBoot.setText("Scheduled ads at boot");
        adsAtBoot.setChecked(prefs.getBoolean(Utility.ADS_AT_START_BOOL, true));
        adsAtBoot.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                editor.putBoolean(Utility.ADS_AT_START_BOOL, isChecked).commit();
            }
        });
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Connector().getCharityManager().monthlyCharity();
        prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.context);
        editor = prefs.edit();
    }

    private Dialog getReminderDialog() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.reminder);
        final AbstractWheel hours = (AbstractWheel) dialog.findViewById(R.id.hours);
        final AbstractWheel minutes = (AbstractWheel) dialog.findViewById(R.id.minutes);

        hours.setViewAdapter(new NumericWheelAdapter(MainMenu.context, 0, 23));
        hours.setCyclic(true);

        minutes.setViewAdapter(new NumericWheelAdapter(MainMenu.context, 0, 59));
        minutes.setCyclic(true);

        hours.setCurrentItem(prefs.getInt(Utility.REMINDER_TIME_HOURS_INT, 12));
        minutes.setCurrentItem(prefs.getInt(Utility.REMINDER_TIME_MINS_INT, 30));

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ScheduledAdsManager.cancelReminder(MainMenu.context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.putInt(Utility.REMINDER_TIME_MINS_INT, minutes.getCurrentItem()).commit();
                editor.putInt(Utility.REMINDER_TIME_HOURS_INT, hours.getCurrentItem()).commit();
                ScheduledAdsManager.scheduleNextReminder(MainMenu.context);
                Toast.makeText(MainMenu.context, String.format("Scheduled for : %d:%d", hours.getCurrentItem(), minutes.getCurrentItem()), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                editor.putBoolean(Utility.ENABLE_REMINDER, false).apply();
                reminderSwitch.setChecked(false);
            }
        });

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(Utility.ENABLE_REMINDER, false).apply();
                dialog.dismiss();
            }
        });
        return dialog;
    }

    @Override
    protected void update() {
        try {
            autoStart.setChecked(prefs.getBoolean(Utility.AUTO_START_BOOL, false));
            toast.setChecked(prefs.getBoolean(Utility.TOAST_BEFORE_BOOL, true));
            adsAtBoot.setChecked(prefs.getBoolean(Utility.ADS_AT_START_BOOL, true));
        } catch (Exception e) {

        }
    }
}

package bigshots.people_helping_people.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import java.util.ArrayList;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.io.AdManager;
import bigshots.people_helping_people.io.AsyncConnector;
import bigshots.people_helping_people.io.Charity;
import bigshots.people_helping_people.io.Connector;
import bigshots.people_helping_people.io.UserStats;
import bigshots.people_helping_people.schedule_wheel.AbstractWheel;
import bigshots.people_helping_people.schedule_wheel.OnWheelChangedListener;
import bigshots.people_helping_people.schedule_wheel.adapters.NumericWheelAdapter;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;
import bigshots.people_helping_people.scroll_iew_lib.ParallaxScrollView;
import bigshots.people_helping_people.services.ScheduledAdsManager;
import bigshots.people_helping_people.utilities.Interfaces;
import bigshots.people_helping_people.utilities.Utils;
import bigshots.people_helping_people.views.MaterialCheckBox;
import bigshots.people_helping_people.views.MaterialSwitch;


public class ContributeFragment extends BaseFragment {
    private String prefix;
    private AdListener fullScreen = new AdListener() {
        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Toast.makeText(MainMenu.context, "Opened", Toast.LENGTH_SHORT).show();
            fullScreenClicked = false;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Toast.makeText(MainMenu.context, "Loaded", Toast.LENGTH_SHORT).show();
            if (fullScreenClicked) {
                adManager.getFullscreenAd().show();
                Utils.addScore(MainMenu.context, 15);
            }
        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            adManager.loadFullscreenAd();
        }
    };
    private int frequencyMinutes;
    private MaterialSwitch reminderSwitch;
    private Dialog dialog;
    private AdManager adManager;
    private String currentCharity;
    private final Interfaces.ASyncListener aSyncListener = new Interfaces.ASyncListener() {
        @Override
        public void onCompleteSingle(Charity charity) {
            currentCharity = charity.getUrl();
        }

        @Override
        public void onCompleteArray(ArrayList<Charity> charities) {

        }

        @Override
        public void onCompleteRank(int rank) {

        }

        @Override
        public void onCompleteLeaderBoardList(ArrayList<UserStats> stats) {

        }
    };
    private boolean videoClicked, fullScreenClicked;
    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.full_screen:
                    fullScreenClicked = true;
                    adManager.getFullscreenAd().show();
                    break;
                case R.id.video_ad:
                    videoClicked = true;
                    adManager.getVideoAd().show();
                    break;
                case R.id.current_charity:
                    try {
                        if (currentCharity != null && currentCharity.length() > 3) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.savethechildren.org.au/"));//currentCharity));
                            startActivity(browserIntent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    private AdListener video = new AdListener() {
        @Override
        public void onAdOpened() {
            super.onAdOpened();
            Toast.makeText(MainMenu.context, "Opened", Toast.LENGTH_SHORT).show();
            videoClicked = false;
        }

        @Override
        public void onAdLoaded() {
            super.onAdLoaded();
            Toast.makeText(MainMenu.context, "loaded", Toast.LENGTH_SHORT).show();
            if (videoClicked) {
                adManager.getVideoAd().show();
                Utils.addScore(MainMenu.context, 20);
            }

        }

        @Override
        public void onAdClosed() {
            super.onAdClosed();
            adManager.loadVideoAd();
        }
    };
    private SharedPreferences prefs;
    private boolean loopBool = true, enable_schedule;

    public ContributeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.contribute, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(MainMenu.context);
        adManager = new AdManager(MainMenu.context);
        adManager.loadFullscreenAd();
        adManager.loadVideoAd();
        adManager.getFullscreenAd().setAdListener(fullScreen);
        adManager.getVideoAd().setAdListener(video);

        view.findViewById(R.id.full_screen).setOnClickListener(listener);
        view.findViewById(R.id.current_charity).setOnClickListener(listener);
        view.findViewById(R.id.video_ad).setOnClickListener(listener);
        view.findViewById(R.id.configure_scheduled_ads).setOnClickListener(listener);
        view.findViewById(R.id.current_charity).setOnClickListener(listener);

        ((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);

        MaterialSwitch scheduledAdsSwitch = (MaterialSwitch) view.findViewById(R.id.enable_scheduled_ads);
        scheduledAdsSwitch.setChecked(prefs.getBoolean(Utils.ENABLE_SCHEDULED_ADS, false));
        scheduledAdsSwitch.setText("Enable Scheduled Ads");
        scheduledAdsSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                prefs.edit().putBoolean(Utils.ENABLE_SCHEDULED_ADS, isChecked).commit();
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
        reminderSwitch.setChecked(prefs.getBoolean(Utils.ENABLE_REMINDER, false));
        reminderSwitch.setText("Enable reminders");
        reminderSwitch.setOnCheckedChangeListener(new MaterialSwitch.OnCheckedChangedListener() {
            @Override
            public void onCheckedChange(MaterialSwitch materialSwitch, boolean isChecked) {
                prefs.edit().putBoolean(Utils.ENABLE_REMINDER, isChecked).commit();
                if (isChecked) {
                    getReminderDialog().show();
                } else
                    ScheduledAdsManager.cancelReminder(MainMenu.context);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncConnector.setListener(aSyncListener);
        new Connector().getCharityManager().monthlyCharity();
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

        hours.setCurrentItem(prefs.getInt(Utils.REMINDER_TIME_HOURS_INT, 12));
        minutes.setCurrentItem(prefs.getInt(Utils.REMINDER_TIME_MINS_INT, 30));

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ScheduledAdsManager.cancelReminder(MainMenu.context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prefs.edit().putInt(Utils.REMINDER_TIME_MINS_INT, minutes.getCurrentItem()).commit();
                prefs.edit().putInt(Utils.REMINDER_TIME_HOURS_INT, hours.getCurrentItem()).commit();
                ScheduledAdsManager.scheduleNextReminder(MainMenu.context);
                Toast.makeText(MainMenu.context, String.format("Scheduled for : %d:%d", hours.getCurrentItem(), minutes.getCurrentItem()), Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                prefs.edit().putBoolean(Utils.ENABLE_REMINDER, false).commit();
                reminderSwitch.setChecked(false);
            }
        });

        dialog.findViewById(R.id.save_cancel).findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putBoolean(Utils.ENABLE_REMINDER, false).commit();
                dialog.dismiss();
            }
        });
        return dialog;
    }

    private Dialog getScheduledAds() {
        final Dialog dialog = new Dialog(MainMenu.context, R.style.CustomDialog);
        dialog.setContentView(R.layout.scheduled_dialog);
        prefix = prefs.getBoolean(Utils.LOOP_SCHEDULE, true) ? "A full screen Ad will be shown every : " : "A full screen Ad will be shown in : ";
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
        frequencyMinutes = prefs.getInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, 20);

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

        loop.setChecked(prefs.getBoolean(Utils.LOOP_SCHEDULE, true));
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

    private void save() {
        if (frequencyMinutes > 0)
            if (frequencyMinutes == 1)
                Toast.makeText(MainMenu.context, "Scheduled for 1 minute", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(MainMenu.context, String.format("Scheduled for %d minutes", frequencyMinutes), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainMenu.context, "Schedule cleared", Toast.LENGTH_SHORT).show();

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Utils.FULLSCREEN_AD_FREQUENCY_MINUTES, frequencyMinutes).commit();
        editor.putBoolean(Utils.LOOP_SCHEDULE, loopBool).commit();

        if (prefs.getBoolean(Utils.ENABLE_SCHEDULED_ADS, false)) {
            if (!ScheduledAdsManager.isServiceRunning()) {
                MainMenu.context.startService(new Intent(MainMenu.context, ScheduledAdsManager.class));
            }
            ScheduledAdsManager.scheduleNext(MainMenu.context, true);
        }
    }

    @Override
    protected void update() {
        //Todo
    }
}

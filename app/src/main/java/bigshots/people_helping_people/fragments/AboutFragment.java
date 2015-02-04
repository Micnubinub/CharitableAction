package bigshots.people_helping_people.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class AboutFragment extends BaseFragment {

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.playstore_link:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=The+Big+Shots"));
                    break;
                case R.id.website_link:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thebigshots.net/charity/"));
                    break;
                case R.id.cred_strip:
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/astuetz/PagerSlidingTabStrip"));
                    break;
            }
            startActivity(intent);
        }
    };

    public AboutFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about, container, false);
        view.findViewById(R.id.playstore_link).setOnClickListener(listener);
        view.findViewById(R.id.website_link).setOnClickListener(listener);
        view.findViewById(R.id.cred_strip).setOnClickListener(listener);
        return view;
    }


    @Override
    protected void update() {
    }
}

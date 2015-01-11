package bigshots.people_helping_people.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import bigshots.people_helping_people.MainMenu;
import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.BaseFragment;


public class AboutFragment extends BaseFragment {

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.playstore_link:
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=The+Big+Shots"));
                    startActivity(browserIntent);
                    break;
                case R.id.website_link:
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://thebigshots.net/charity/"));
                    startActivity(intent);
                    break;
            }
        }
    };

    public AboutFragment() {
    }

//    @Override
//    public void onScroll(ScrollView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
//        if (mScrollTabHolder != null)
//            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, pagePosition);
//    }
//
//    @Override
//    public void adjustScroll(int scrollHeight) {
//
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Create", "create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View view = inflater.inflate(R.layout.about, container, false);
//        view.findViewById(R.id.playstore_link).setOnClickListener(listener);
//        view.findViewById(R.id.website_link).setOnClickListener(listener);
//        ((ParallaxScrollView) view.findViewById(R.id.scroll_view)).setScrollListener(scrollListener);
        Log.e("Create view", "createView");

        final View view = inflater.inflate(R.layout.simple_fragment_test, container, false);
        ((TextView) view.findViewById(R.id.num)).setText(String.valueOf(Math.random()));
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenu.context, String.valueOf(getFragmentManager().getFragments().indexOf(AboutFragment.this)), Toast.LENGTH_LONG).show();

            }
        });
        return view;
    }

}

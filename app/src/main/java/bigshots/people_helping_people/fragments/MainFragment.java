package bigshots.people_helping_people.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bigshots.people_helping_people.R;
import bigshots.people_helping_people.scroll_iew_lib.ParallaxViewLayout;

/**
 * Created by root on 12/01/15.
 */
public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.material_main, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        new ParallaxViewLayout(inflater.getContext(), view);
        return view;
    }
}

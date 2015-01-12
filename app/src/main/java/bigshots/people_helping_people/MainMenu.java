package bigshots.people_helping_people;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import bigshots.people_helping_people.fragments.MainFragment;

/**
 * Created by root on 18/11/14.
 */
@SuppressWarnings("ALL")
public class MainMenu extends FragmentActivity {

    public static Context context;
    public static FragmentActivity fragmentActivity;
    private static Fragment fragment;

    public static FragmentManager getFragManager() {
        return fragment.getChildFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        fragmentActivity = this;
        fragment = new MainFragment();
        setContentView(R.layout.material_main_menu);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        transaction.commit();
    }
}

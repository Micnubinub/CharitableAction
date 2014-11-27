package bigshots.people_helping_people;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import bigshots.people_helping_people.fragments.Page1;
import bigshots.people_helping_people.fragments.Page2;
import bigshots.people_helping_people.fragments.Page3;
import bigshots.people_helping_people.fragments.Page4;

/**
 * Created by root on 28/11/14.
 */
public class Intro extends FragmentActivity {
    private final Fragment[] pages = new Fragment[]{new Page1(), new Page2(), new Page3(), new Page4()};
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.skip:
                    startActivity(new Intent(Intro.this, MainMenu.class));
                    break;
            }

        }
    };
    private Button button;
    private ViewPager pager;
    private boolean appLaunched;
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {
            if (i == (pages.length - 2) && v > 0.2) {
                finish();
                if (!appLaunched)
                    startActivity(new Intent(Intro.this, MainMenu.class));
                appLaunched = true;
            }
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);
        button = (Button) findViewById(R.id.skip);
        button.setOnClickListener(listener);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        pager.setOnPageChangeListener(pageChangeListener);
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            return obj;
        }

        @Override
        public Fragment getItem(int i) {
            return pages[i];
        }

        @Override
        public int getCount() {
            return pages.length;
        }
    }
}

package bigshots.people_helping_people;

import android.app.Activity;

/**
 * Created by root on 28/11/14.
 */
public class Intro extends Activity {
//    private final Fragment[] pages = new Fragment[]{new Page1(), new Page2(), new Page3(), new Page4()};
//    private final View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.skip:
//                    save();
//                    finish();
//                    startActivity(new Intent(Intro.this, MainMenu.class));
//                    break;
//            }
//
//        }
//    };
//    private boolean appLaunched;
//    private final ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
//        @Override
//        public void onPageScrolled(int i, float v, int i2) {
//            if (i == (pages.length - 2) && v > 0.2) {
//                save();
//                finish();
//                if (!appLaunched)
//                    startActivity(new Intent(Intro.this, MainMenu.class));
//                appLaunched = true;
//            }
//        }
//
//        @Override
//        public void onPageSelected(int i) {
//
//        }
//
//        @Override
//        public void onPageScrollStateChanged(int i) {
//
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Utils.INTRO_SHOWN, false)) {
//            finish();
//            startActivity(new Intent(Intro.this, MainMenu.class));
//        }
//        setContentView(R.layout.intro);
//        final Button button = (Button) findViewById(R.id.skip);
//        button.setOnClickListener(listener);
//        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
//        pager.setOnPageChangeListener(pageChangeListener);
//    }
//
//    private void save() {
//        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Utils.INTRO_SHOWN, true).commit();
//    }
//
//    class PagerAdapter extends FragmentPagerAdapter {
//
//        public PagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            return super.instantiateItem(container, position);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            return pages[i];
//        }
//
//        @Override
//        public int getCount() {
//            return pages.length;
//        }
//    }
}

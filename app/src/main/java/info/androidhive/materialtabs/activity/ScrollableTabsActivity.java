package info.androidhive.materialtabs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.fragments.FragmentFunctions;
import info.androidhive.materialtabs.fragments.FragmentPid;
import info.androidhive.materialtabs.fragments.FourFragment;
import info.androidhive.materialtabs.fragments.FragmentCalibrateAcc;
import info.androidhive.materialtabs.fragments.FragmentGimbalConfiguration;
import info.androidhive.materialtabs.fragments.FragmentScripts;
import info.androidhive.materialtabs.fragments.FragmentSetup;
import info.androidhive.materialtabs.fragments.FragmentData;
import info.androidhive.materialtabs.fragments.FragmentRcInput;
import info.androidhive.materialtabs.fragments.FragmentPan;
import info.androidhive.materialtabs.fragments.ThreeFragment;
import info.androidhive.materialtabs.fragments.FragmentConnection;

public class ScrollableTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ThreeFragment three = new ThreeFragment();
    private FourFragment four = new FourFragment();
    private FragmentConnection fragmentConnection = new FragmentConnection();

    private FragmentScripts fragmentScripts = new FragmentScripts();
    private FragmentSetup fragmentSetup = new FragmentSetup();
    private FragmentGimbalConfiguration fragmentGimbalConfiguration = new FragmentGimbalConfiguration();
    private FragmentCalibrateAcc fragmentCalibrateAcc = new FragmentCalibrateAcc();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollable_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());



        adapter.addFrag(fragmentConnection, "CONN");
        FragmentData fragmentData = new FragmentData();

//        Bundle args = new Bundle();
        //args.putInt("someInt", someInt);
        //myFragment.setArguments(args);

        fragmentData.setFragmentConnection(fragmentConnection);
        adapter.addFrag(fragmentData, "DATA");
        adapter.addFrag(new FragmentPan(), "PAN");
        adapter.addFrag(new FragmentPid(), "PID");
        adapter.addFrag(new FragmentRcInput(), "RC INPUT");
        adapter.addFrag(new FragmentFunctions(), "FUNCTIONS");
        adapter.addFrag(new FragmentScripts(), "SCRIPTS");
        adapter.addFrag(new FragmentSetup(), "SETUP");
        adapter.addFrag(new FragmentGimbalConfiguration(), "GIMBAL CONF.");
        adapter.addFrag(new FragmentCalibrateAcc(), "CALIBRATE ACC");

        /*
        adapter.addFrag(new NineFragment(), "NINE");
        adapter.addFrag(new TenFragment(), "TEN");
        adapter.addFrag(three, "THREE");
        adapter.addFrag(four, "FOUR");
        */
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            Object obj = super.instantiateItem(container, position);
            //Toast toast = Toast.makeText(getApplicationContext(), "page instantiated:" + position, Toast.LENGTH_SHORT);
            if(position == 3){
                //three.
                three.inv();
            }
            //toast.show();
            return obj;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

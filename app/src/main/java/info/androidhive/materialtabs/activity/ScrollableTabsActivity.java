package info.androidhive.materialtabs.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import android.content.IntentFilter;

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

    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    PendingIntent mPermissionIntent;

    private static final int targetVendorID= 9025;
    private static final int targetProductID = 32828;
    UsbDevice deviceFound = null;

    UsbInterface usbInterface;
    UsbDeviceConnection usbDeviceConnection;


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


        // tu bylo usb

    }

    public void onResume() {
        super.onResume();


        if(false) {
//        updateAllControlsAccordingToOptionList();
            Intent intent = getIntent();

            if (intent != null) {
                String action = intent.getAction();

                Toast toast = Toast.makeText(this, "Aintent! action=" + action, Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast.show();


                if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {

                    Toast toast2 = Toast.makeText(this, "A USB set device!", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast2.show();
                    //UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //setDevice(device);
                /*
                UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

                while(deviceIterator.hasNext()){
                    UsbDevice device = deviceIterator.next();
                    // Your code here!
                }
                */
                } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                    Toast toast3 = Toast.makeText(this, "A USB detached...", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast3.show();

                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                /*
                if (mDevice != null && mDevice.equals(device)) {
                    setDevice(null);
                }*/
                }
            }
        }
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

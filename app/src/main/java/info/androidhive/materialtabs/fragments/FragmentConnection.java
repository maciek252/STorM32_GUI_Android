package info.androidhive.materialtabs.fragments;

import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.content.BroadcastReceiver;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.os.Handler;
import java.util.ArrayList;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Spinner;


import info.androidhive.materialtabs.R;


import com.macroyau.blue2serial.BluetoothDeviceListDialog;
import com.macroyau.blue2serial.BluetoothSerial;
import com.macroyau.blue2serial.BluetoothSerialListener;

import org.mavlink.*;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import info.androidhive.materialtabs.storm32.*;
import quadcopter.Bluetooth;
import utils.InterFragmentCom;


public class FragmentConnection extends Fragment
        implements BluetoothSerialListener, BluetoothDeviceListDialog.OnDeviceSelectedListener,
        View.OnClickListener {

    // CONNECTED_SAVED - clicked "SAVE" but not "SAVE TO EEPROM"
    /*
    CONNECT USB - active after connecting the usb cable (also if BT connection is running), inactive after starting an usb connection
    CONNECT BT - active when bt turned on (even if USB connection is running), inactive after establishing a connection
    DISCONNECT - active when USB or BT connection is opened
    READ - active when BT or USB connection active
    SAVE - active if at least one change done and READ active
    Button "SAVE TO EEPROM" - active when SAVE clicked, disables SAVE
     */
    public enum ConnectionState {
        DISCONNECTED, CONNECTED_NOT_READ, CONNECTED_READ, CONNECTED_CHANGED, CONNECTED_SAVED
    };



    ConnectionState connectionState = ConnectionState.DISCONNECTED;

    ViewPager vp = null;

    private String statusStr = "";

    public final String TAG = "Main";

    private SeekBar elevation;
    private TextView debug;
    private TextView textStatusUSB;
    TextView textInfo;
    TextView textInfoInterface;
    TextView textEndPoint;

    TextView textDeviceName;


    private TextView status;
    private Bluetooth bt;

    private UsbManager mUsbManager;
    private UsbDevice mDevice;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpointIntr;

    Spinner spInterface;
    ArrayList<String> listInterface;
    ArrayList<UsbInterface> listUsbInterface;
    ArrayAdapter<String> adapterInterface;

    Spinner spEndPoint;
    ArrayList<String> listEndPoint;
    ArrayList<UsbEndpoint> listUsbEndpoint;
    ArrayAdapter<String> adapterEndpoint;


    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";
    PendingIntent mPermissionIntent;

    private static final int targetVendorID = 1155;
    private static final int targetProductID = 22336;
    UsbDevice deviceFound = null;

    UsbInterface usbInterface;
    UsbDeviceConnection usbDeviceConnection;


    String messageBuffer = "";

    //byte [] options = null;


    Button button = null;
    Button button_2_readVersion = null;
    Button button_2_setName = null;
    Button button_2_readOptions = null;

    Button button_2_SaveOptions = null;
    Button button_2_readOption = null;
    Button button_2_changeOption = null;
    Button button_2_disconnect = null;
    Button button_2_saveToEeprom = null;
    Button button_connectUSB = null;
    Button button_btTest = null;
    TextView tv = null;
    TextView tv_connectionStatus = null;
    TextView tv_receivedBt = null;
    TextView tv_board_version = null;
    TextView tv_software_version = null;
    TextView tv_board = null;
    EditText et_name = null;

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothSerial bluetoothSerial;

    public FragmentConnection() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check Bluetooth availability on the device and set up the Bluetooth adapter
        bluetoothSerial.setup();
        //setControlsNotConnected();
        //setConnectionStateForDisplay(ConnectionState.DISCONNECTED);
    }


    private BroadcastReceiver mUsbDeviceReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {


                        deviceFound = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        Toast.makeText(getActivity(),
                                "ACTION_USB_DEVICE_ATTACHED: \n" +
                                        deviceFound.toString(),
                                Toast.LENGTH_LONG).show();
                        //textStatus.setText("ACTION_USB_DEVICE_ATTACHED: \n" +
                        //      deviceFound.toString());

                        //checkDeviceInfoSkipDeviceSearching();
                        //doRawDescriptors();
                        button_connectUSB.setEnabled(true);
                    } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);


                        Toast.makeText(getActivity(),
                                "ACTION_USB_DEVICE_DETACHED: \n" +
                                        device.toString(),
                                Toast.LENGTH_LONG).show();
                        button_connectUSB.setEnabled(false);
                        //textStatus.setText("ACTION_USB_DEVICE_DETACHED: \n" +
//                                device.toString());

                        if (device != null) {
                            //if(device == deviceFound){
                            //  releaseUsb();
                            //}
                        }

                        /*
                        textInfo.setText("");
                        textInfoInterface.setText("");
                        textEndPoint.setText("");

                        listInterface.clear();
                        listUsbInterface.clear();
                        adapterInterface.notifyDataSetChanged();

                        listEndPoint.clear();
                        listUsbEndpoint.clear();
                        adapterEndpoint.notifyDataSetChanged();
                        */
                    }
                }

            };


    private void doReadRawDescriptors(UsbDevice device) {
        final int STD_USB_REQUEST_GET_DESCRIPTOR = 0x06;
        final int LIBUSB_DT_STRING = 0x03;

        boolean forceClaim = true;

        byte[] buffer = new byte[255];
        int indexManufacturer = 14;
        int indexProduct = 15;
        String stringManufacturer = "";
        String stringProduct = "";

        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        usbDeviceConnection = manager.openDevice(device);
        if (usbDeviceConnection != null) {
            usbInterface = device.getInterface(0);
            usbDeviceConnection.claimInterface(usbInterface, forceClaim);

            byte[] rawDescriptors = usbDeviceConnection.getRawDescriptors();

            int lengthManufacturer = usbDeviceConnection.controlTransfer(
                    UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_STANDARD,
                    STD_USB_REQUEST_GET_DESCRIPTOR,
                    (LIBUSB_DT_STRING << 8) | rawDescriptors[indexManufacturer],
                    0,
                    buffer,
                    0xFF,
                    0);
            try {
                stringManufacturer = new String(buffer, 2, lengthManufacturer - 2, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(getActivity(), "RAW:" + e.toString(), Toast.LENGTH_LONG).show();
                //textStatus.setText(e.toString());
            }

            int lengthProduct = usbDeviceConnection.controlTransfer(
                    UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_STANDARD,
                    STD_USB_REQUEST_GET_DESCRIPTOR,
                    (LIBUSB_DT_STRING << 8) | rawDescriptors[indexProduct],
                    0,
                    buffer,
                    0xFF,
                    0);
            try {
                stringProduct = new String(buffer, 2, lengthProduct - 2, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Toast.makeText(getActivity(),
                    "RAWManufacturer: " + stringManufacturer + "\n" +
                            "RAWProduct: " + stringProduct,
                    Toast.LENGTH_LONG).show();
            //textStatus.setText("Manufacturer: " + stringManufacturer + "\n" +
            //      "Product: " + stringProduct);
        } else {
            Toast.makeText(getActivity(),
                    "RAWopen failed",
                    Toast.LENGTH_LONG).show();
            //textStatus.setText("open failed");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);



        // Create a new instance of BluetoothSerial
        bluetoothSerial = new BluetoothSerial(getContext(), this);
        bt = new Bluetooth(getContext(), mHandler);

        //mUsbManager = (UsbManager)getActivity().getSystemService(Context.USB_SERVICE);

        // usb




        //register the broadcast receiver
        mPermissionIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        getActivity().registerReceiver(mUsbReceiver, filter);

        getActivity().registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        //registerReceiver(mUsbDeviceReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));

        //connectUsb();

    }

    private void connectUsb() {

        Toast.makeText(getActivity(),
                "connectUsb()",
                Toast.LENGTH_LONG).show();
        textStatusUSB.setText("connectUsb()");

        checkDeviceInfo();
        if (deviceFound != null) {
                //doRawDescriptors();
        }

        // albo doRawDescriptors, albo to co nizej
        //if(true)
          //  return;


        if(deviceFound != null) {
            UsbInterface intf = null;
            try {
                intf = deviceFound.getInterface(1);
            } catch (Exception e){
                textStatusUSB.setText("ifejsow:" + deviceFound.getInterfaceCount() + "error getting intf:" +
                        e.getMessage());
                return;
            }


            if(intf == null) {

                textStatusUSB.setText("coldn't get interface");
                return;
            }

            textStatusUSB.setText("got interface, num of i=" + deviceFound.getInterfaceCount());
            //if(true)
            //  return;

            if (intf.getEndpointCount() != 2) {
                //Log.e(TAG, "could not find endpoint");

                Toast.makeText(getActivity(),
                        "could not find endpoint",
                        Toast.LENGTH_LONG).show();
                return;
            }

            textStatusUSB.setText("got endpoint");



            UsbEndpoint ep = intf.getEndpoint(0);
            /*
            if (ep.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {
                Toast.makeText(getActivity(),
                        "endpoint type (not interrupt)=" + ep.getType() + ",epOut=" + ep.getDirection(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "endpoint is not interrupt type");
                return;
            }
            */

            UsbEndpoint epIN = intf.getEndpoint(1);

            textStatusUSB.setText("got endpoint OK");


            UsbDeviceConnection connection = null;
            try {
                mUsbManager = (UsbManager)getActivity().getSystemService(Context.USB_SERVICE);
                 connection = mUsbManager.openDevice(deviceFound);
                textStatusUSB.setText("got connection");
            } catch (Exception e){
                textStatusUSB.setText("problem" + e.getMessage());
            }




            if(connection == null){
                textStatusUSB.setText("USB connection null...!");
            } else {



                if (connection.claimInterface(intf, true)) {
                    Log.d(TAG, "open SUCCESS");
                    textStatusUSB.setText("open SUceess");


                    //if(true)
                      //  return;


                    byte[] message = new byte[1];
                    //message[0] = (byte) 'v'; // version - OK
                    message[0] = (byte) 'g'; // options

                    // Send command via a control request on endpoint zero
                    mConnection = connection;
                    //mConnection.controlTransfer(0x21, 0x9, 0x200, 0, message, message.length, 0);
                    int sentBytes = connection.bulkTransfer(ep, message, message.length, 1000);

//                Thread thread = new Thread(this);
//                thread.start();
                    textStatusUSB.setText("USB message sent!" + sentBytes);

                } else {
                    Log.d(TAG, "open FAIL");
                    //mConnection = null;
                    textStatusUSB.setText("open claim interface failed!");
                }
            }

            /*
            UsbRequest request = new UsbRequest();
            request.initialize(mConnection, epIN);
            byte status = -1;
            while (true) {

            }
            */
            //byte[] readBytes = new byte[64]; // OK works but we need more
            byte[] readBytes = new byte[512];
            int recvBytes = 0;
            int recvBytesSum = 0;
            do {
                recvBytes = connection.bulkTransfer(epIN, readBytes, readBytes.length, 3000);
                recvBytesSum += recvBytes;
                if (recvBytes > 0) {
                //    Toast.makeText(getActivity(),
                  //          "Got some data: len=" + recvBytes + " dane:" + new String(readBytes),
                    //        Toast.LENGTH_LONG).show();

                    optionList.addToTempBuffer(readBytes, recvBytes);


                    //Log.d("trebla", "Got some data: " + new String(readBytes));
                } else {
                    Toast.makeText(getActivity(),
                            "Didng't get any data...",
                            Toast.LENGTH_LONG).show();
                    //Log.d("trebla", "Did not get any data: " + recvBytes);
                }
            } while (recvBytes > 0);

            Toast.makeText(getActivity(),
                    "Got some data sum:" + recvBytesSum,
                    Toast.LENGTH_LONG).show();


            if(recvBytesSum == 380){
                if (optionList.checkMessageCRC()) {
                    optionList.decodeOptions();
//                        Toast.makeText(getActivity(),
                    //                              "Got some data: OK USB OPTIONS",
                    //                            Toast.LENGTH_LONG).show();
                }
            }



            //private UsbDeviceConnection mConnection;
            //private UsbEndpoint mEndpointIntr;
        }

    }

    private void doRawDescriptors(){
        UsbDevice deviceToRead = deviceFound;
        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);

        Boolean permitToRead = manager.hasPermission(deviceToRead);

        if(permitToRead){
            doReadRawDescriptors(deviceToRead);
        }else{
            manager.requestPermission(deviceToRead, mPermissionIntent);
            Toast.makeText(getActivity(),
                    "Permission: " + permitToRead,
                    Toast.LENGTH_LONG).show();
            textStatusUSB.setText("Permission: " + permitToRead);
        }
    }

    private void checkDeviceInfo() {

        deviceFound = null;

        UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();

            if (device.getVendorId() == targetVendorID) {
                if (device.getProductId() == targetProductID) {
                    deviceFound = device;
                }
            }

           // deviceFound = device;
        }

        textInfo.setText("");
        textInfoInterface.setText("");
        textEndPoint.setText("");

        if (deviceFound == null) {
            Toast.makeText(getActivity(),
                    "device not found",
                    Toast.LENGTH_LONG).show();
            textStatusUSB.setText("device not found");
        } else {
            String i = deviceFound.toString() + "\n" +
                    "DeviceID: " + deviceFound.getDeviceId() + "\n" +
                    "DeviceName: " + deviceFound.getDeviceName() + "\n" +
                    "DeviceClass: " + deviceFound.getDeviceClass() + " - "
                    + translateDeviceClass(deviceFound.getDeviceClass()) + "\n" +
                    "DeviceSubClass: " + deviceFound.getDeviceSubclass() + "\n" +
                    "VendorID: " + deviceFound.getVendorId() + "\n" +
                    "ProductID: " + deviceFound.getProductId() + "\n" +
                    "InterfaceCount: " + deviceFound.getInterfaceCount();
            textInfo.setText(i);

            checkUsbDevicve(deviceFound);
        }

    }

    private String translateDeviceClass(int deviceClass) {
        switch (deviceClass) {
            case UsbConstants.USB_CLASS_APP_SPEC:
                return "Application specific USB class";
            case UsbConstants.USB_CLASS_AUDIO:
                return "USB class for audio devices";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "USB class for CDC devices (communications device class)";
            case UsbConstants.USB_CLASS_COMM:
                return "USB class for communication devices";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "USB class for content security devices";
            case UsbConstants.USB_CLASS_CSCID:
                return "USB class for content smart card devices";
            case UsbConstants.USB_CLASS_HID:
                return "USB class for human interface devices (for example, mice and keyboards)";
            case UsbConstants.USB_CLASS_HUB:
                return "USB class for USB hubs";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "USB class for mass storage devices";
            case UsbConstants.USB_CLASS_MISC:
                return "USB class for wireless miscellaneous devices";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "USB class indicating that the class is determined on a per-interface basis";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "USB class for physical devices";
            case UsbConstants.USB_CLASS_PRINTER:
                return "USB class for printers";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "USB class for still image devices (digital cameras)";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return "Vendor specific USB class";
            case UsbConstants.USB_CLASS_VIDEO:
                return "USB class for video devices";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "USB class for wireless controller devices";
            default:
                return "Unknown USB class!";

        }
    }


    private void checkUsbDevicve(UsbDevice d) {
        listInterface = new ArrayList<String>();
        listUsbInterface = new ArrayList<UsbInterface>();

        for (int i = 0; i < d.getInterfaceCount(); i++) {
            UsbInterface usbif = d.getInterface(i);
            listInterface.add(usbif.toString());
            listUsbInterface.add(usbif);
        }

        adapterInterface = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, listInterface);
        adapterInterface.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInterface.setAdapter(adapterInterface);
        spInterface.setOnItemSelectedListener(interfaceOnItemSelectedListener);
    }

    AdapterView.OnItemSelectedListener interfaceOnItemSelectedListener =
            new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View view, int position, long id) {

                    UsbInterface selectedUsbIf = listUsbInterface.get(position);

                    String sUsbIf = "\n" + selectedUsbIf.toString() + "\n"
                            + "Id: " + selectedUsbIf.getId() + "\n"
                            + "InterfaceClass: " + selectedUsbIf.getInterfaceClass() + "\n"
                            + "InterfaceProtocol: " + selectedUsbIf.getInterfaceProtocol() + "\n"
                            + "InterfaceSubclass: " + selectedUsbIf.getInterfaceSubclass() + "\n"
                            + "EndpointCount: " + selectedUsbIf.getEndpointCount();

                    textInfoInterface.setText(sUsbIf);
                    //   checkUsbInterface(selectedUsbIf);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };


    private final BroadcastReceiver mUsbReceiver =
            new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (ACTION_USB_PERMISSION.equals(action)) {

                        Toast.makeText(getActivity(),
                                "ACTION_USB_PERMISSION",
                                Toast.LENGTH_LONG).show();
                        //textStatus.setText("ACTION_USB_PERMISSION");

                        synchronized (this) {
                            UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                            if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                                if(device != null){
                                    doReadRawDescriptors(device);
                                }
                            }
                            else {
                                Toast.makeText(getActivity(),
                                        "permission denied for device " + device,
                                        Toast.LENGTH_LONG).show();
                                //textStatus.setText("permission denied for device " + device);
                            }
                        }
                    }
                }
            };

/*
    private void setDevice(UsbDevice device) {
        Log.d(TAG, "setDevice " + device);
        if (device.getInterfaceCount() != 1) {

            Toast toast = Toast.makeText(getContext(), "could not find USB devce, count = " + device.getInterfaceCount(), Toast.LENGTH_SHORT);
            //toast.setDuration;
            toast.show();

            Log.e(TAG, "could not find interface");
            return;
        }
        UsbInterface intf = device.getInterface(0);
        // device should have one endpoint
        if (intf.getEndpointCount() != 1) {
            Log.e(TAG, "could not find endpoint");
            Toast toast = Toast.makeText(getContext(), "could not find USB endpoint!", Toast.LENGTH_SHORT);
            //toast.setDuration;
            toast.show();
            return;
        }
        // endpoint should be of type interrupt
        UsbEndpoint ep = intf.getEndpoint(0);
        if (ep.getType() != UsbConstants.USB_ENDPOINT_XFER_INT) {
            Log.e(TAG, "endpoint is not interrupt type");
            Toast toast = Toast.makeText(getContext(), "USB endpoint is not interrupt type?!", Toast.LENGTH_SHORT);
            //toast.setDuration;
            toast.show();
            return;
        }
        mDevice = device;
        mEndpointIntr = ep;
        if (device != null) {
            UsbDeviceConnection connection = mUsbManager.openDevice(device);
            if (connection != null && connection.claimInterface(intf, true)) {
                Log.d(TAG, "open SUCCESS");
                Toast toast = Toast.makeText(getContext(), "USB open success!", Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast.show();
                mConnection = connection;
                //Thread thread = new Thread(this);
                //thread.start();

                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        runUSB();

                    }
                };
                timer.run();



            } else {
                Log.d(TAG, "open FAIL");
                Toast toast = Toast.makeText(getContext(), "USB open failed!", Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast.show();
                mConnection = null;
            }
        }
    }

    public void runUSB() {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        UsbRequest request = new UsbRequest();
        request.initialize(mConnection, mEndpointIntr);
        byte status = -1;
        while (true) {
            // queue a request on the interrupt endpoint
            request.queue(buffer, 1);
            // send poll status command
            //       sendCommand(COMMAND_STATUS);
            // wait for status event
            if (mConnection.requestWait() == request) {
                byte newStatus = buffer.get(0);
                Toast toast = Toast.makeText(getContext(), "got sth via usb!", Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast.show();


                if (newStatus != status) {
                    Log.d(TAG, "got status " + newStatus);
                    status = newStatus;
                    //             if ((status & COMMAND_FIRE) != 0) {
                    // stop firing
                    //               sendCommand(COMMAND_STOP);
                    //         }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            } else {
                Log.e(TAG, "requestWait failed, exiting");
                break;
            }
        }
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_connection, container, false);


        View v = inflater.inflate(R.layout.fragment_connection, container, false);
        button = (Button) v.findViewById(R.id.connection_button_detect_bt);
        button.setOnClickListener(this);


        button_connectUSB = (Button) v.findViewById(R.id.connection_button_usb_connect);
        button_connectUSB.setOnClickListener(this);

        debug = (TextView) v.findViewById(R.id.textDebug);
        status = (TextView) v.findViewById(R.id.textStatus);

        /*
        v.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //connectService();

                //UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

            }
        });*/

        button_btTest = (Button) v.findViewById(R.id.connection_button_btTest);
        button_btTest.setOnClickListener(this);

        button_2_readVersion = (Button) v.findViewById(R.id.connection_button_readVersion);
        button_2_readVersion.setOnClickListener(this);
        button_2_setName = (Button) v.findViewById(R.id.connection_button_setName);
        button_2_setName.setOnClickListener(this);

        button_2_readOptions = (Button) v.findViewById(R.id.connection_button_readOptions);
        button_2_readOptions.setOnClickListener(this);

        button_2_SaveOptions = (Button) v.findViewById(R.id.connection_button_saveOptions);
        button_2_SaveOptions.setOnClickListener(this);

        button_2_readOption = (Button) v.findViewById(R.id.connection_button_detect_bt);
        button_2_readOption.setOnClickListener(this);

        button_2_disconnect = (Button) v.findViewById(R.id.connection_button_disconnectBT);
        button_2_disconnect.setOnClickListener(this);

        button_2_saveToEeprom = (Button) v.findViewById(R.id.connection_button_saveToEeprom);
        button_2_saveToEeprom.setOnClickListener(this);

        tv = (TextView) v.findViewById(R.id.textViewDetectedBT);
        tv_connectionStatus = (TextView) v.findViewById(R.id.textView_connection_connectionStatus);
        tv_receivedBt =  (TextView) v.findViewById(R.id.textView_2_rcvBt);

        tv_receivedBt.setMovementMethod(new ScrollingMovementMethod());


        tv_board_version = (TextView) v.findViewById(R.id.textView_connection_board_version);
        tv_software_version = (TextView) v.findViewById(R.id.textView_connection_software_version);
        tv_board = (TextView) v.findViewById(R.id.textView_2_board);
        et_name = (EditText) v.findViewById(R.id.editText_connection_name);
        et_name.setText("                ");

        textStatusUSB = (TextView) v.findViewById(R.id.textStatusUSB);
        textInfo = (TextView) v.findViewById(R.id.textInfo);
        textInfoInterface = (TextView) v.findViewById(R.id.textInfoInterface);
        textEndPoint = (TextView) v.findViewById(R.id.textInfoEndPoint);

        spInterface = (Spinner) v.findViewById(R.id.spInterface);
        spEndPoint = (Spinner) v.findViewById(R.id.spEndPoint);


        //final InputMethodManager imm =(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        /*
        tv_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }else{
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                imm.toggleSoftInput(0, 0);
            }
        });
        */
        /*
        tv_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                tv_name.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(tv_name, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        tv_name.requestFocus();
*/
        setConnectionStateForDisplay(connectionState);
        return v;
    }

    public void onResume() {
        super.onResume();

        status.setText(statusStr);

        /*
//        updateAllControlsAccordingToOptionList();
        Intent intent = getActivity().getIntent();
        Log.d(TAG, "intent: " + intent);
        if(intent != null) {
            String action = intent.getAction();

            Toast toast = Toast.makeText(getContext(), "intent! action="+action, Toast.LENGTH_SHORT);
            //toast.setDuration;
            toast.show();


            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {

                Toast toast2 = Toast.makeText(getContext(), "USB set device!", Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast2.show();
                //UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                //setDevice(device);
                UsbManager manager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

                while(deviceIterator.hasNext()){
                    UsbDevice device = deviceIterator.next();
                    // Your code here!
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {

                Toast toast3 = Toast.makeText(getContext(), "USB detached...", Toast.LENGTH_SHORT);
                //toast.setDuration;
                toast3.show();

                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                if (mDevice != null && mDevice.equals(device)) {
                    setDevice(null);
                }
            }
        }
        */
    }

    private void readOptions(){
        bt.queryMode = Bluetooth.QueryMode.GET_OPTIONS;
        //bluetoothSerial.write("g");
        bt.sendMessage("g");

    }

    public void readData_d(){
        bt.queryMode = Bluetooth.QueryMode.GET_DATA_D;
        bt.sendMessage("d");
    }


    protected void setConnectionStateForDisplay(ConnectionState cs){

        if(connectionState != ConnectionState.DISCONNECTED && cs == ConnectionState.CONNECTED_NOT_READ){

        } else {
            connectionState = cs;
        }

        if(connectionState == ConnectionState.DISCONNECTED)
            setControlsNotConnected();
        else if(connectionState == ConnectionState.CONNECTED_NOT_READ)
            setControlsBTConnected();
        else if(connectionState == ConnectionState.CONNECTED_READ)
            setControlsDataRead();

    }

    protected void setControlsNotConnected(){
        button_2_disconnect.setEnabled(false);
        button_2_readOptions.setEnabled(false);
        button_2_readVersion.setEnabled(false);
        button_2_SaveOptions.setEnabled(false);
        button_2_saveToEeprom.setEnabled(false);
        button_2_saveToEeprom.setEnabled(false);
    }


    protected void setControlsBTConnected(){
        button_2_disconnect.setEnabled(true);
        button_2_readOptions.setEnabled(true);
        button_2_readVersion.setEnabled(true);
        button_2_SaveOptions.setEnabled(false);
        button_2_saveToEeprom.setEnabled(false);
    }

    protected void setControlsDataRead(){
        button_2_SaveOptions.setEnabled(true);
        button_2_saveToEeprom.setEnabled(true);
    }


    @Override
    public void onClick(View v) {

        bt.queryMode = Bluetooth.QueryMode.NONE;

        switch (v.getId()) {
            case R.id.connection_button_btTest:

                IntermediateFragment.setColorToAllControls();

                BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(getActivity());
                dialog.setOnDeviceSelectedListener(this);
                dialog.setTitle("paired devices");
                dialog.setDevices(bluetoothSerial.getPairedDevices());
                dialog.showAddress(true);
                dialog.show();
                break;
            case  R.id.connection_button_readVersion:
                //bluetoothSerial.getConnectedDeviceName().

                bt.queryMode = Bluetooth.QueryMode.GET_VERSION;

                InterFragmentCom.clearData();
                String getVersion = "v";
                bt.write2(getVersion.getBytes());
                break;

            case R.id.connection_button_saveToEeprom:



                bt.queryMode = Bluetooth.QueryMode.SAVE_TO_EEPROM;
                String saveToEeprom = "xs";
                bt.write2(saveToEeprom.getBytes());
                break;

            case R.id.connection_button_usb_connect:

                /*
                if (mConnection != null) {
                    Toast toast = Toast.makeText(getContext(), "usb != null!", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast.show();
                    byte[] message = new byte[1];
                    message[0] = (byte)'s';
                    // Send command via a control request on endpoint zero
                    mConnection.controlTransfer(0x21, 0x9, 0x200, 0, message, message.length, 0);
                } else {
                    Toast toast = Toast.makeText(getContext(), "usb = null!", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast.show();

                }*/
                connectUsb();

                break;

            case R.id.connection_button_setName:
                //  my $res = ExecuteCmdwCrc( 'xn', %name, 0 );
                /*
                sub ExecuteCmdwCrc{
                      my $cmd= shift; my $params= shift; my $reslen= shift;
                        return ExecuteCmd( $cmd.add_crc_to_data($params), $reslen );

                 */

                String wej = "testName";
                String o = String.format("%-20s", wej);
                o = o.substring(0,16);

                  bluetoothSerial.write("xn", false);
  //              bluetoothSerial.write(o, false);
                /* //OK DZIAŁA
                bluetoothSerial.write(n);
*/
                //o = "0123456789012345";
                o = et_name.getText().toString();
                //o = tv_name.getText().toString();
                if(o.length() > 16)
                    o = o.substring(0,16);
                else if(o.length() < 16)
                    o = String.format("%0$-16s", o) ;
                byte [] i = MAVLinkCRC.stringToByte("xn" + o + "cc");
                //i[18] = (byte) 0x93;
                //i[19] = (byte) 0xe6;

                byte [] s= MAVLinkCRC.stringToByte(o);
                int crc = MAVLinkCRC.crc_calculate(s);
                byte[] crcArray = MAVLinkCRC.intToHexVax(crc);
                i[18] = crcArray[0];
                i[19] = crcArray[1];
//                bluetoothSerial.write(i);
                bt.write2(i);



                break;
            case R.id.connection_button_readOptions:

                readOptions();

                break;
            case R.id.connection_button_saveOptions:
                //options = optionList.getOptions();
                bt.queryMode = Bluetooth.QueryMode.SET_OPTIONS;
                ;

                if(optionList.getOptions() != null) {
                    optionList.encodeOptions();
                    tv_receivedBt.setText("size of opTions = " + optionList.getOptions().length);
                }else {
                    tv_receivedBt.setText("optins = null");
                    return;
                }

                byte [] optionsFull = new byte[381];
                optionsFull[0] = 'p';
                //optionsFull[380] = 'e'; // i tak nadpisane, dla testu
                tv_receivedBt.append("oko");
                byte [] optionsWrite = new byte [125*2 + 128 ];
                for(int ii = 0; ii < 125*2 + 128 ; ii++){

                    byte b = optionList.getOptions()[ii];
                    optionsWrite[ii] = b;
                    optionsFull[ii+1] = b;
                }
                int crc2 = MAVLinkCRC.crc_calculate(optionsWrite);
                byte[] crcArray2 = MAVLinkCRC.intToHexVax(crc2);

                //for(int ii = 0; ii < 125*2; ii++) // 2 crc, 'o'
// 378 elements - contents 0-377
                // in write - 1 - 378

                optionsFull[379] = crcArray2[0];
                optionsFull[380] = crcArray2[1];

                tv_receivedBt.append("crc=" + String.format("%01X",crcArray2[0])  + "," + String.format("%01X",crcArray2[1]));

                //bluetoothSerial.write(optionsFull);
                bt.write2(optionsFull);

                //SystemClock.sleep(1000);

                // Execute some code after 2 seconds have passed
                break;
            case R.id.connection_button_detect_bt:



                //tv_receivedBt.setText("  " + String.format("%01X",options[36])  + "," + String.format("%01X",options[37]));
                //optionList.voltageCorrection = options[38];
                showDeviceListDialog();


                break;

            case R.id.connection_button_disconnectBT:

                //bluetoothSerial.stop();
                bt.stop();
                break;

        }
    }

    /* Implementation of BluetoothSerialListener */

    @Override
    public void onBluetoothNotSupported() {

        new AlertDialog.Builder(getActivity())
                .setMessage("ni ma BT")
                .setPositiveButton("boo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                })
                .setCancelable(false)
                .show();

    }



    @Override
    public void onBluetoothDisabled() {
        Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetooth, REQUEST_ENABLE_BLUETOOTH);
    }

    @Override
    public void onBluetoothDeviceDisconnected() {
        //invalidateOptionsMenu();
        //updateBluetoothState();
        tv_connectionStatus.setText("disconnected");
        tv_connectionStatus.setBackgroundColor(Color.RED);
    }

    @Override
    public void onConnectingBluetoothDevice() {
        //updateBluetoothState();
        tv_connectionStatus.setText("connecting");
        tv_connectionStatus.setBackgroundColor(Color.YELLOW);
    }

    @Override
    public void onBluetoothDeviceConnected(String name, String address) {
        //invalidateOptionsMenu();
        //updateBluetoothState();
        tv_connectionStatus.setText("connected BT");
        tv_connectionStatus.setBackgroundColor(Color.GREEN);
        //setControlsBTConnected();
    }

    @Override
    public void onBluetoothSerialRead(byte [] m) {
        int i = m.length;
        String s1 = String.format("%01X", m[0]);
        String s2 = String.format("%01X", m[1]);
        int i4 = (m[0] & 0x0F)  * 16 + (m[0] & 0xF0);
        int i5 = (m[0] & 0xF0)  * 16 + (m[0] & 0x0F);
    }

    @Override
    public void onBluetoothSerialRead(String message) {
        /*
        tv_receivedBt.append("<" + message + ">");

        if(bt.queryMode == Bluetooth.QueryMode.GET_OPTIONS){
            if(options == null )
                options = MAVLinkCRC.stringToByte(message);
            else {

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                try {
                    outputStream.write(options);
                    outputStream.write(MAVLinkCRC.stringToByte(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                options = outputStream.toByteArray();

                if(options.length >= 381 && options[380] == 'o'){

                    // check CRC (?)


                    Toast toast = Toast.makeText(getContext(), "options received, CRC OK!", Toast.LENGTH_SHORT);
                    //toast.setDuration;
                    toast.show();
                    optionList.setOptions(options);
                    optionList.decodeOptions();
                }

            }
        }


        messageBuffer = messageBuffer + message;
        tv_receivedBt.append("{" + messageBuffer + "}");
        //if( message.length() == 0)
//            return;
        if(messageBuffer.startsWith("v") && messageBuffer.endsWith("o")){

            String version = messageBuffer.substring(0,16);
            String name = messageBuffer.substring(16,16);
            //String board = messageBuffer.substring(32,16);

            tv_name.append(name);
            //tv_board.setText(board);
            tv_board.append("ff");
            tv_version.append(version);
        }
*/
        // Print the incoming message on the terminal screen
        /*
        tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getConnectedDeviceName(),
                message));
        svTerminal.post(scrollTerminalToBottom);
        */
    }

    @Override
    public void onBluetoothSerialWrite(String message) {
        // Print the outgoing message on the terminal screen



        /*
        tvTerminal.append(getString(R.string.terminal_message_template,
                bluetoothSerial.getLocalAdapterName(),
                message));
                */
        //svTerminal.post(scrollTerminalToBottom);

    }

    /* Implementation of BluetoothDeviceListDialog.OnDeviceSelectedListener */

    @Override
    public void onBluetoothDeviceSelected(BluetoothDevice device) {
        // Connect to the selected remote Bluetooth device
        //bluetoothSerial.connect(device);

        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                //bt.connectDevice("HC-06");
                //bt.connectDevice(device.getName());
                bt.connectDeviceByAddress(device.getAddress());


                Log.d(TAG, "Btservice started - listening");
                status.setText("Connected2");
                //setControlsBTConnected();
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }

    }

    /* End of the implementation of listeners */


/*
    public void connectService(){
        try {
            status.setText("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                bt.start();
                bt.connectDevice("HC-06");
                Log.d(TAG, "Btservice started - listening");
                status.setText("Connected3");
                setControlsBTConnected();
            } else {
                Log.w(TAG, "Btservice started - bluetooth is not enabled");
                status.setText("Bluetooth Not enabled");
            }
        } catch(Exception e){
            Log.e(TAG, "Unable to start bt ",e);
            status.setText("Unable to connect " +e);
        }
    }
*/

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_STATE_CHANGE:
                    Log.d(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch(msg.arg1){
                        case 0:
                            statusStr = "disconnected";
                            setConnectionStateForDisplay(ConnectionState.DISCONNECTED);
                            break;
                        case 1:
                            statusStr = "listening for incoming2";
                            //setConnectionStateForDisplay(ConnectionState.DISCONNECTED);
                            //setConnectionStateForDisplay(ConnectionState.CONNECTED_NOT_READ);
                            break;
                        case 2:
                            statusStr = "listening for incoming3";
                            //setConnectionStateForDisplay(ConnectionState.DISCONNECTED);
                            break;
                        case 3:
                            statusStr = "CONNECTED4!";
                            setConnectionStateForDisplay(ConnectionState.CONNECTED_NOT_READ);
                            break;
                        default:
                            statusStr = "??";
                            break;
                    }
                    status.setText(statusStr);

                    /*

                    public static final int STATE_NONE = 0; // we're doing nothing
                    public static final int STATE_LISTEN = 1; // now listening for incoming connections
                    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
                    public static final int STATE_CONNECTED = 3; // now connected to a remote device
                    */

                    break;
                case Bluetooth.MESSAGE_WRITE:
                    Log.d(TAG, "MESSAGE_WRITE ");
                    break;



                case Bluetooth.MESSAGE_READ:
                    Log.d(TAG, "MESSAGE_READ " + msg.arg1 + " " + msg.arg2);
                    if(bt.queryMode == Bluetooth.QueryMode.GET_OPTIONS && msg.arg2 == 1){
                        //setControlsDataRead();
                        setConnectionStateForDisplay(ConnectionState.CONNECTED_READ);

                        Toast toast = Toast.makeText(getContext(), "options received CRC OK!", Toast.LENGTH_SHORT);
                        //toast.setDuration;
                        toast.show();

                    }else if(bt.queryMode == Bluetooth.QueryMode.GET_VERSION) {

                        if (msg.arg2 == 1) {
                            byte[] softVersion = Arrays.copyOfRange(bt.bufferExternalComm, 0, 15);
                          //  String softVersionStr = String.valueOf(softVersion);
                            try {
                                String softVersionStr = new String(softVersion, "UTF-8");
                                tv_software_version.setText(softVersionStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            byte[] boardName = Arrays.copyOfRange(bt.bufferExternalComm, 16, 31);
                            try {
                                String boardNameStr = new String(boardName, "UTF-8");
                                //tv_name.setText(boardNameStr);
                                et_name.setText(boardNameStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            byte[] boardVersion = Arrays.copyOfRange(bt.bufferExternalComm, 32, 48);
                            try {
                                String boardVersionStr = new String(boardVersion, "UTF-8");
                                tv_board_version.setText(boardVersionStr);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(bt.queryMode == Bluetooth.QueryMode.SAVE_TO_EEPROM){

                        if(msg.arg2 == 1){
                            Toast toast = Toast.makeText(getContext(), "SAVE TO EEPROM OK, will re-read", Toast.LENGTH_SHORT);
                            //toast.setDuration;
                            toast.show();
                            rereadOptions();
                        }
                    } else if(bt.queryMode == Bluetooth.QueryMode.SET_OPTIONS){

                        if(msg.arg2 == 1){
                            Toast toast = Toast.makeText(getContext(), "SAVE OK, will re-read", Toast.LENGTH_SHORT);
                            //toast.setDuration;
                            toast.show();
                            rereadOptions();
                        }
                    } else if(bt.queryMode == Bluetooth.QueryMode.GET_DATA_D) {
                        //byte[] ar = new byte[14];

                        //ar[5] = 4;
                        //InterFragmentCom.setData_d(ar);

                    }
                    break;
                case Bluetooth.MESSAGE_DEVICE_NAME:
                    Log.d(TAG, "MESSAGE_DEVICE_NAME "+msg);
                    break;
                case Bluetooth.MESSAGE_TOAST:
                    Log.d(TAG, "MESSAGE_TOAST "+msg);
                    break;
            }
        }
    };

    private void rereadOptions(){
        SystemClock.sleep(1000);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                readOptions();
            }
        }, 1000);

    }

    private void showDeviceListDialog() {
        // Display dialog for selecting a remote Bluetooth device
        BluetoothDeviceListDialog dialog = new BluetoothDeviceListDialog(getContext());
        dialog.setOnDeviceSelectedListener(this);
        //dialog.setTitle(R.string.paired_devices);
        dialog.setTitle("paired devices");
        //dialog.setDevices(bluetoothSerial.getPairedDevices());
        dialog.setDevices(bt.getPairedDevicesName());
        dialog.showAddress(true);
        dialog.show();
    }

}

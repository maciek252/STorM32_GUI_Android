/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quadcopter;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import info.androidhive.materialtabs.storm32.optionList;
import utils.InterFragmentCom;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for incoming
 * connections, a thread for connecting with a device, and a thread for
 * performing data transmissions when connected.
 */
public class Bluetooth {
	// Debugging
    public static byte[] bufferExternalComm = new byte[1024];

	private static final String TAG = "BluetoothService";
	private static final boolean D = true;

	// Name for the SDP record when creating server socket
	private static final String NAME = "BluetoothChat";

	// Unique UUID for this application
	private static final UUID MY_UUID = UUID.fromString("0001101-0000-1000-8000-00805F9B34FB");

	// INSECURE "8ce255c0-200a-11e0-ac64-0800200c9a66"
	// SECURE "fa87c0d0-afac-11de-8a39-0800200c9a66"
	// SPP "0001101-0000-1000-8000-00805F9B34FB"

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

	public enum QueryMode {GET_OPTIONS, GET_VERSION, SET_OPTIONS, SET_VERSION, SAVE_TO_EEPROM, NONE, GET_DATA_D};

	public Bluetooth.QueryMode queryMode = Bluetooth.QueryMode.NONE;

	// Member fields
	private final BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private AcceptThread mAcceptThread;
	private ConnectThread mConnectThread;
	private ConnectedThread mConnectedThread;
	private int mState;

	// Constants that indicate the current connection state
	public static final int STATE_NONE = 0; // we're doing nothing
	public static final int STATE_LISTEN = 1; // now listening for incoming connections
	public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3; // now connected to a remote device

	/**
	 * Constructor. Prepares a new BluetoothChat session.
	 *
	 * @param context
	 *            The UI Activity Context
	 * @param handler
	 *            A Handler to send messages back to the UI Activity
	 */
	public Bluetooth(Context context, Handler handler) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();

		if (mAdapter != null && D)
			for (BluetoothDevice bd: mAdapter.getBondedDevices()) Log.d(TAG, "Bounded device "+bd);
		mState = STATE_NONE;
		mHandler = handler;
	}

	/**
	 * Set the current state of the chat connection
	 *
	 * @param state
	 *            An integer defining the current connection state
	 */
	private synchronized void setState(int state) {
		if (D)
			Log.d(TAG, "setState() " + mState + " -> " + state);
		mState = state;



		// Give the new state to the Handler so the UI Activity can update
		mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1)
				.sendToTarget();
	}

	/**
	 * Return the current connection state.
	 */
	public synchronized int getState() {
		return mState;
	}

	/**
	 * Start the chat service. Specifically start AcceptThread to begin a
	 * session in listening (server) mode. Called by the Activity onResume()
	 */
	public synchronized void start() {
		if (D)
			Log.d(TAG, "start");

		// Cancel any thread attempting to make a connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		setState(STATE_LISTEN);

		// Start the thread to listen on a BluetoothServerSocket
		if (mAcceptThread == null) {
			mAcceptThread = new AcceptThread();
			mAcceptThread.start();
		}
	}

	/**
	 * Start the ConnectThread to initiate a connection to a remote device.
	 *
	 * @param device
	 *            The BluetoothDevice to connect
	 */
	private synchronized void connect(BluetoothDevice device) {
		if (D)
			Log.d(TAG, "connect to: " + device);

		// Cancel any thread attempting to make a connection
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Start the thread to connect with the given device
		mConnectThread = new ConnectThread(device);
		mConnectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * Start the ConnectedThread to begin managing a Bluetooth connection
	 *
	 * @param socket
	 *            The BluetoothSocket on which the connection was made
	 * @param device
	 *            The BluetoothDevice that has been connected
	 */
	public synchronized void connected(BluetoothSocket socket,
									   BluetoothDevice device, final String socketType) {
		if (D)
			Log.d(TAG, "connected, Socket Type:" + socketType);

		// Cancel the thread that completed the connection
		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		// Cancel any thread currently running a connection
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		// Cancel the accept thread because we only want to connect to one
		// device
		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		// Start the thread to manage the connection and perform transmissions
		mConnectedThread = new ConnectedThread(socket, socketType);
		mConnectedThread.start();

		// Send the name of the connected device back to the UI Activity
		Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
		Bundle bundle = new Bundle();
		bundle.putString("Connected", device.getName());
		msg.setData(bundle);
		mHandler.sendMessage(msg);
		//byte [] g = {'g'};
		//mConnectedThread.write(g);

		setState(STATE_CONNECTED);
	}

	/**
	 * Stop all threads
	 */
	public synchronized void stop() {
		if (D)
			Log.d(TAG, "stop");

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (mAcceptThread != null) {
			mAcceptThread.cancel();
			mAcceptThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * Write to the ConnectedThread in an unsynchronized manner
	 *
	 * @param out
	 *            The bytes to write
	 * //@see quadcopter.coconauts.net.quadcopter.Bluetooth.ConnectedThread#write(byte[])
	 */
	private void write(byte[] out) {
		// Create temporary object

		//
		optionList.resetTempBuffer();

		ConnectedThread r;
		// Synchronize a copy of the ConnectedThread
		synchronized (this) {
			if (mState != STATE_CONNECTED) return;
			r = mConnectedThread;
		}
		// Perform the write unsynchronized
		r.write(out);
	}

	/**
	 * Indicate that the connection attempt failed and notify the UI Activity.
	 */
	private void connectionFailed() {
		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("Toast", "Unable to connect device");
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		// Start the service over to restart listening mode
		Bluetooth.this.start();
	}

	/**
	 * Indicate that the connection was lost and notify the UI Activity.
	 */
	private void connectionLost() {
		// Send a failure message back to the Activity
		Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString("Toast", "Device connection was lost");
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		// Start the service over to restart listening mode
		Bluetooth.this.start();
	}

	/**
	 * This thread runs while listening for incoming connections. It behaves
	 * like a server-side client. It runs until a connection is accepted (or
	 * until cancelled).
	 */
	private class AcceptThread extends Thread {
		// The local server socket
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;

		public AcceptThread() {
			BluetoothServerSocket tmp = null;

			// Create a new listening server socket
			try {
				tmp = mAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
			}
			mmServerSocket = tmp;
		}

		public void run() {
			if (D)
				Log.d(TAG, "Socket Type: " + mSocketType
						+ "BEGIN mAcceptThread" + this);
			setName("AcceptThread" + mSocketType);

			BluetoothSocket socket = null;

			// Listen to the server socket if we're not connected
			while (mState != STATE_CONNECTED) {
				try {
					// This is a blocking call and will only return on a
					// successful connection or an exception
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					Log.e(TAG, "Socket Type: " + mSocketType
							+ "accept() failed", e);
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					synchronized (Bluetooth.this) {
						switch (mState) {
						case STATE_LISTEN:
						case STATE_CONNECTING:
							// Situation normal. Start the connected thread.
							connected(socket, socket.getRemoteDevice(),
									mSocketType);
							break;
						case STATE_NONE:
						case STATE_CONNECTED:
							// Either not ready or already connected. Terminate
							// new socket.
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			if (D)
				Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

		}

		public void cancel() {
			if (D)
				Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "Socket Type" + mSocketType
						+ "close() of server failed", e);
			}
		}
	}

	/**
	 * This thread runs while attempting to make an outgoing connection with a
	 * device. It runs straight through; the connection either succeeds or
	 * fails.
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String mSocketType;

		public ConnectThread(BluetoothDevice device) {
			mmDevice = device;
			BluetoothSocket tmp = null;

			// Get a BluetoothSocket for a connection with the
			// given BluetoothDevice
			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
			}
			mmSocket = tmp;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
			setName("ConnectThread" + mSocketType);

			// Always cancel discovery because it will slow down a connection
			mAdapter.cancelDiscovery();

			// Make a connection to the BluetoothSocket
			try {
				// This is a blocking call and will only return on a
				// successful connection or an exception
				mmSocket.connect();
			} catch (IOException e) {
                Log.e(TAG,"Unable to connect socket ",e);
				// Close the socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
					Log.e(TAG, "unable to close() " + mSocketType
							+ " socket during connection failure", e2);
				}
				connectionFailed();
				return;
			}

			// Reset the ConnectThread because we're done
			synchronized (Bluetooth.this) {
				mConnectThread = null;
			}

			// Start the connected thread
			connected(mmSocket, mmDevice, mSocketType);
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect " + mSocketType
						+ " socket failed", e);
			}
		}
	}

	/**
	 * This thread runs during a connection with a remote device. It handles all
	 * incoming and outgoing transmissions.
	 */
	private class ConnectedThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;


		public ConnectedThread(BluetoothSocket socket, String socketType) {
			Log.d(TAG, "create ConnectedThread: " + socketType);
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the BluetoothSocket input and output streams
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			Log.i(TAG, "BEGIN mConnectedThread");
			byte[] buffer = new byte[1024];
			int bytes;



			// Keep listening to the InputStream while connected
			while (true) {
				try {



					int arg2 = 0;
					byte[] optionso = optionList.getOptions();
					// Read from the InputStream
					bytes = mmInStream.read(buffer);
					if(queryMode == QueryMode.GET_OPTIONS) {
						optionList.addToTempBuffer(buffer, bytes);
						if (optionList.checkMessageCRC()) {
							arg2 = 1;
							optionList.decodeOptions();
						} else
							arg2 = 2;



					} else if(queryMode == QueryMode.SAVE_TO_EEPROM){
						if(bytes == 1 && buffer[0] == 'o')
							arg2 = 1;
						else
							arg2 = 0;


					}else if(queryMode == QueryMode.GET_VERSION){
                        // version str "v": v2.02 ntOlliW 323BGCv1.30 F103RC�_�Lo

                        if(bytes >= 1){
                            //byte[] restr = Arrays.copyOf(buffer, 14);
                            //InterFragmentCom.setData_d(restr);
                            InterFragmentCom.addToTempBuffer(buffer, bytes);
                        }
						int len = InterFragmentCom.getBufferLen();
                        if(len >= 57 ) {
                            arg2 = 1;
                            bufferExternalComm = InterFragmentCom.getData_d();



                            String b = String.valueOf(buffer);
						    System.out.println(b);
                        }else
							arg2 = 0;


					}
					else if(queryMode == QueryMode.SET_OPTIONS){

						if(bytes == 1 && buffer[0] == 'o')
							arg2 = 1;
						else
							arg2 = 0;
					} else if(queryMode == QueryMode.GET_DATA_D){
						arg2 = 1;
						if(bytes >= 1){
							//byte[] restr = Arrays.copyOf(buffer, 14);
							//InterFragmentCom.setData_d(restr);
							InterFragmentCom.addToTempBuffer(buffer, bytes);
						}


					}

					Log.d(TAG, "message bytes " + bytes);
					Log.d(TAG, "message string bytes " + String.valueOf(bytes));
					//Log.d(TAG, "message buffer " + new String(buffer));
					Log.d(TAG, "message buffer " + writeStringAsHex(buffer));
					Log.d(TAG, "message buffer len" + buffer.length);
					// Send the obtained bytes to the UI Activity
					mHandler.obtainMessage(MESSAGE_READ, bytes,
							arg2, buffer).sendToTarget();
				} catch (IOException e) {
					Log.e(TAG, "disconnected", e);
					connectionLost();
					// Start the service over to restart listening mode
					Bluetooth.this.start();
					break;
				}
			}
		}

		/**
		 * Write to the connected OutStream.
		 *
		 * @param buffer
		 *            The bytes to write
		 */
		public void write(byte[] buffer) {
			try {
				mmOutStream.write(buffer);

				// Share the sent message back to the UI Activity
				mHandler.obtainMessage(MESSAGE_WRITE, -1, -1,
						buffer).sendToTarget();
			} catch (IOException e) {
				Log.e(TAG, "Exception during write", e);
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect socket failed", e);
			}
		}
	}

	public String writeStringAsHex(byte [] b){
		String result = "";
		for(byte bb : b)
			result += String.format("%01X", bb) + " ";
		return result;
	}

    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (this.getState() != Bluetooth.STATE_CONNECTED) {
            Log.w(TAG, "bluetooth is not connected");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            char EOT = (char)3 ;
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = (message + EOT).getBytes();
            this.write(send);
        }
    }

	public void write2(byte [] w) {
		// Check that we're actually connected before trying anything
		if (this.getState() != Bluetooth.STATE_CONNECTED) {
			Log.w(TAG, "bluetooth is not connected");
			return;
		}

		// Check that there's actually something to send
		if (w.length > 0) {

			this.write(w);
		}
	}

	public void connectDeviceByAddress(String address) {
		// Get the device MAC address

		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

		// 98:D3:31:60:0A:7B
		// 80:B0:8A:24:C6:9E

		//address = "98:D3:31:60:0A:7B"; // ten OK!!!

		try {
			BluetoothDevice device = adapter.getRemoteDevice(address); // Get the BluetoothDevice object
			this.connect(device); // Attempt to connect to the device
		} catch (Exception e){
			Log.e("Unbl conn to dev adr "+ address,e.getMessage());
		}
	}


	public void connectDevice(String deviceName) {
        // Get the device MAC address
        String address = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        for(BluetoothDevice d: adapter.getBondedDevices()){
			String name = d.getName();
			//name = "HC-06";
            if (name.equals(deviceName)) {
				address = d.getAddress();
			}
        }
		// 98:D3:31:60:0A:7B
		// 80:B0:8A:24:C6:9E

		//address = "98:D3:31:60:0A:7B"; // ten OK!!!

        try {
            BluetoothDevice device = adapter.getRemoteDevice(address); // Get the BluetoothDevice object
            this.connect(device); // Attempt to connect to the device
        } catch (Exception e){
            Log.e("Unbl conn to dev adr "+ address,e.getMessage());
        }
    }

	public Set<BluetoothDevice> getPairedDevicesName() {

		HashSet<BluetoothDevice> devices = new HashSet<>();
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		for(BluetoothDevice d: adapter.getBondedDevices()) {
			//String name = d.getName();
			devices.add(d);

			//devices.add(name);

		}
		return devices;
		/*
		if (mPairedDevices != null) {
			String[] name = new String[mPairedDevices.size()];
			int i = 0;
			for (BluetoothDevice d : mPairedDevices) {
				name[i] = d.getName();
				i++;
			}
			return name;
		}*/


	}

}

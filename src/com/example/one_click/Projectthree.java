package com.example.one_click;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

//import android.widget.CheckBox;

public class Projectthree extends Activity implements LocationListener {
	TextView text, or, temp;
	EditText custom_message;
	String templates[] = { "call me", "message me", "help", "emergency",
			"custom message" };
	Spinner spin;
	LinearLayout linear;
	Button send, locat;
	GoogleMap mymaps;
	LocationManager man;
	String provider;
	boolean isGPSEnabled;
	Location location;
	int f = 0;
	String template_message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projectthree);
		text = (TextView) findViewById(R.id.textView1);
		or = (TextView) findViewById(R.id.textView2);
		temp = (TextView) findViewById(R.id.textView3);
		linear = (LinearLayout) findViewById(R.id.linear1);
		custom_message = (EditText) findViewById(R.id.editText1);
		send = (Button) findViewById(R.id.button1);
		locat = (Button) findViewById(R.id.button2);
		spin = (Spinner) findViewById(R.id.spinner1);
		Log.d("msg", "started");

		ArrayAdapter<String> array = new ArrayAdapter<String>(
				Projectthree.this, android.R.layout.simple_spinner_item,
				templates);
		array.setDropDownViewResource(android.R.layout.simple_spinner_item);
		spin.setAdapter(array);
		spin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				template_message = "";
				// TODO Auto-generated method stub
				if (templates[arg2].equalsIgnoreCase("custom message")) {
					linear.setVisibility(View.VISIBLE);

				} else {
					template_message = templates[arg2];

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
		locat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				man = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				isGPSEnabled = man
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

				Criteria c = new Criteria();// tells the best provider to next
											// line
				provider = man.getBestProvider(c, true);
				if (provider == null) {
					Toast.makeText(Projectthree.this, "provider not found",
							Toast.LENGTH_SHORT).show();

				} else {
					boolean isNetworkEnabled = man
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

					if (!isNetworkEnabled) {
						// no network provider is enabled
					} else {
						boolean canGetLocation = true;
						if (isNetworkEnabled) {
							man.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER, 1000L,
									8000f, Projectthree.this);
							Log.d("Network", "Network Enabled");
							if (man != null) {
								location = man
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null) {

									man.requestLocationUpdates(
											LocationManager.GPS_PROVIDER, 1000,
											8000, Projectthree.this);
									Log.d("GPS", "GPS Enabled");
									Double latit = location.getLatitude();
									Double longit = location.getLongitude();
									String lat = latit.toString();
									String longi = longit.toString();
									Log.d("msg=", lat + " " + longi);
									// Log.d("msg", "sent" + no);

									sendlocationMessage(lat, longi);
								}
							}
						}
					}

				}
			}
		});

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String cust_messagge = custom_message.getText().toString();
				if (cust_messagge.length() > 0 && f == 0) {
					sendMessage(cust_messagge);
					f = 1;
				} else {

					sendMessage(template_message);
				}
			}

		});
	}

	void sendMessage(String message) {
		// Toast.makeText(Projectthree.this, message,
		// Toast.LENGTH_SHORT).show();

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> users = MainActivity.smsContact;

		for (String contact : users) {
			String user = contact.substring(0, contact.length());
			Log.d("user=", user + ",msg=" + message);
			sms.sendTextMessage(user, null, message, null, null);
			Toast.makeText(getApplicationContext(), "message sent to " + user,
					Toast.LENGTH_SHORT).show();
		}

	}

	void sendlocationMessage(String mylat, String mylong) {
		String sent = "sent", not_sent = "not sent";
		ArrayList<String> users = MainActivity.smsContact;

		String user_location = mylat + ", " + mylong;
		Log.d("msg.location=", user_location);
		PendingIntent sentPi = PendingIntent.getBroadcast(this, 0, new Intent(
				sent), 0);
		PendingIntent notsentPi = PendingIntent.getBroadcast(this, 0,
				new Intent(not_sent), 0);

		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Log.d("msg=", "message sennt");

					break;

				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Log.d("msg=", "failure");

					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Log.d("msg=", "no service");

					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Log.d("msg=", "null pdu");

					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Log.d("msg=", "off radio");

					break;

				}
			}
		}, new IntentFilter(sent));

		registerReceiver(new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Log.d("msg=", "delvr");

					break;

				case Activity.RESULT_CANCELED:
					Log.d("msg=", "not delivered");

					break;

				}
			}
		}, new IntentFilter(not_sent));
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage("8004976681", null, user_location, sentPi,
				notsentPi);

		// for (String contact : users) {
		// String user = contact.substring(0, contact.length());
		// Log.d("user=", user + ",msg=" + mylat + mylong);
		// sms.sendTextMessage(user, null, user_location, null, null);
		// Toast.makeText(getApplicationContext(), "message sent to " + user,
		// Toast.LENGTH_SHORT).show();
		// }

	}

	private Location getLastKnownLocation() {
		List<String> providers = man.getProviders(true);
		Location bestlocation = null;
		for (String provider : providers) {
			Log.d("found1=", providers.toString());

			Location l = man.getLastKnownLocation("passive");
			if (l == null) {
				Log.d("dr", l.toString());

				continue;

			}
			if (bestlocation == null
					|| l.getAccuracy() < bestlocation.getAccuracy()) {
				Log.d("found=", l.toString());
				bestlocation = l;
			}
		}
		if (bestlocation == null) {
			return null;
		}
		return bestlocation;
	}

	@Override
	public void onLocationChanged(Location location) {
		test t = new test(this);
		t.execute(location);
	}

	public void onProviderDisabled(String provider) {

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}

class test extends AsyncTask<Location, Void, String> {// to keep the process in
														// background because
														// the process of
														// geocoder class can be
														// too long
	Context con;
	Geocoder geo;
	String info;

	public test(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;

		geo = new Geocoder(con, Locale.getDefault());
	}

	@Override
	protected String doInBackground(Location... params) {
		// TODO Auto-generated method stub
		Location l = params[0];
		try {
			List<android.location.Address> add = geo.getFromLocation(
					l.getLatitude(), l.getLongitude(), 1);
			if (add != null) {
				android.location.Address a = add.get(0);
				info = a.getAddressLine(0) + " " + a.getLocality() + " "
						+ a.getCountryName();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// Toast.makeText(con, result, Toast.LENGTH_SHORT).show();
	}

}

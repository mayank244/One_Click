package com.example.one_click;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	ArrayList<String> listname;
	ArrayList<String> contacts;
	static public ArrayList<String> selectedContact;
	static public ArrayList<String> smsContact;

	// ArrayList<String> list_no;
	Context context;
	String store[];
	ListView contactlistView;
	String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ListView list = getListView();
		listname = new ArrayList<String>();
		contacts = new ArrayList<String>();

		selectedContact = new ArrayList<String>();
		smsContact = new ArrayList<String>();

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));

				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						// Do something with phones
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						contacts.add(phoneNo);
						listname.add(name + "\n" + phoneNo);

					}
					pCur.close();
				}
			}

		}
		Contact c = new Contact(this, listname, contacts);
		list.setAdapter(c);
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Toast.makeText(MainActivity.this, selectedContact.toString(),
				Toast.LENGTH_SHORT).show();

	}

	class Contact extends BaseAdapter {
		Context myContext;
		ArrayList<String> listname;
		ArrayList<String> contact;
		LayoutInflater inflater;

		public Contact(Context contactActivity, ArrayList<String> list,
				ArrayList<String> contact) {
			// TODO Auto-generated constructor stub
			this.myContext = contactActivity;
			listname = list;
			this.contact = contact;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listname.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {

				inflater = (LayoutInflater) myContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
			convertView = inflater.inflate(R.layout.activity_main, null);
			TextView t = (TextView) convertView.findViewById(R.id.name);
			CheckBox c = (CheckBox) convertView.findViewById(R.id.checkBox);
			t.setText(listname.get(position));
			c.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// Toast.makeText(myContext, listname.get(position),
					// Toast.LENGTH_SHORT).show();
					selectedContact.add(listname.get(position));
					smsContact.add(contact.get(position));
				}
			});
			// final ViewHolder viewHolder = new ViewHolder();
			// viewHolder.text_name = (TextView) convertView
			// .findViewById(R.id.name);
			// viewHolder.id = (TextView) convertView.findViewById(R.id.name);
			// viewHolder.checkBox = (CheckBox) convertView
			// .findViewById(R.id.checkBox);
			//
			// convertView.setTag(viewHolder);
			// }

			// final ViewHolder holder = (ViewHolder) convertView.getTag();
			// // holder.text_name.setText(list_no.get(position));
			// holder.id.setText(listname.get(position));
			// Log.d("txt", listname.get(position));
			// if (holder != null) {
			// holder.checkBox
			// .setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			// public void onCheckedChanged(CompoundButton buttonView,
			// boolean isChecked) {
			//
			// }
			// });

			// }
			return convertView;
		}
	}
}

class ViewHolder {
	TextView text_name, id;
	CheckBox checkBox;
	// EditText search;
}

package com.ndadheech.android.howdy;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * Copyright Nibha Dadheech
 * A controller that interacts with model and view to retrieve the details of contact.
 * @author ndadheech
 */
public class HowdyFragment extends Fragment {
    private static final int REQUEST_CONTACT = 0;
    private static final String TAG="HowdyFragment";
    private static final String SAVED_STATE_FRIEND_NAME = "SAVED_STATE_FRIEND_NAME";
    private static final String SAVED_STATE_FRIEND_PHONE_NUMBER = "SAVED_STATE_FRIEND_PHONE_NUMBER";

    private String savedFriendName;
    private String savedFriendPhoneNumber;
    private Button mSendButton;
    private Button mSelectFriend;
    private Friend mFriend;
    private String mLastHowdySentFormat;
    private String mHowdyDateFormat;
    private TextView mLastHowdySent;

    @Override
    public void onSaveInstanceState(final Bundle saveState) {
        super.onSaveInstanceState(saveState);
        if (mFriend != null) {
            saveState.putString(SAVED_STATE_FRIEND_NAME, mFriend.getFriendName());
            saveState.putString(SAVED_STATE_FRIEND_PHONE_NUMBER, mFriend.getPhoneNumber());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Retrieve saved data on rotate or restore
        Log.d(TAG, "CreatedView Invoked");
        if(mFriend == null)
            mFriend = new Friend();

        if(savedInstanceState != null){
            savedFriendName = savedInstanceState.getString(SAVED_STATE_FRIEND_NAME);
            savedFriendPhoneNumber = savedInstanceState.getString(SAVED_STATE_FRIEND_PHONE_NUMBER);
            mFriend.setFriendName(savedFriendName);
            mFriend.setPhoneNumber(savedFriendPhoneNumber);
        }

        View v = inflater.inflate(R.layout.fragment_howdy, container, false);
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        mSelectFriend = (Button) v.findViewById(R.id.select_friend);
        if(savedFriendName != null){
            mSelectFriend.setText(mFriend.getFriendName());
        }
        mSelectFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mSendButton = (Button) v.findViewById(R.id.send_howdy);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFriend.getPhoneNumber() != null) {
                    sendHowdy();
                } else {
                    sendCustomizedHowdyWithoutPhoneNumber();
                }
            }
        });

        mHowdyDateFormat = getString(R.string.dateFormat);
        mLastHowdySentFormat = getString(R.string.latestHowdyFormat);
        mLastHowdySent = (TextView) v.findViewById(R.id.last_howdy_sent);
        String lastHowdyText = HowdyLab.get(getContext()).getLatestHowdy(mLastHowdySentFormat, mHowdyDateFormat);
        if (lastHowdyText == null) {
            lastHowdyText = getString(R.string.emptyLatestHowdy);
        }
        mLastHowdySent.setText(lastHowdyText);

        return v;
    }

    /**
     * Method responsible for sending howdy message
     * @return true if message sent successfully, false otherwise
     */
    private void sendHowdy() {
        Log.d(TAG, "Sending 'Howdy' to " + mFriend.getFriendName());
        SmsManager smsManager;
        try {
            smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mFriend.getPhoneNumber(), null, getString(R.string.message), null, null);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.sent),
                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "Successfully sent 'Howdy' to " + mFriend.getFriendName());
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.howdy_error),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            saveHowdy();
            setLatestHowdy();
            mSelectFriend.setText(getString(R.string.select_friend));
        }
    }

    /**
     * Method responsible for sending howdy message without phone number
     */
    private void sendCustomizedHowdyWithoutPhoneNumber(){
        Toast.makeText(getActivity().getApplicationContext(), R.string.noPhoneNumber, Toast.LENGTH_SHORT).show();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.message));
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CONTACT && data!=null) {
            Uri contactUri = data.getData();
            populateName(contactUri);
        }
    }

    /**
     * Retrieves the contact name and phone number from Address Book
     * @param contactUri
     */
    private void populateName(Uri contactUri){
        String[] queryFields = new String[] {
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts._ID
        };

        Cursor cursor = null;
        try{
            cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            cursor.moveToFirst();
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            String name = cursor.getString(nameColumnIndex);

            mFriend.setFriendName(name);
            mSelectFriend.setText(name);

            if(name != null){
                String hasPhone =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                String id =cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                populatePhoneNumber(hasPhone.equalsIgnoreCase("1"), id);
            }
        } finally {
            if(cursor!=null)
            {
                cursor.close();
                cursor=null;
            }
        }
    }

    /**
     * Retrieves the phone number from Address Book
     * @param hasPhone true if phone number is found, false otherwise
     * @param id id of the contact
     */
    private void populatePhoneNumber(boolean hasPhone, String id){
        String phoneNumber = null;
        if(hasPhone){
            Cursor phones = null;
            try {
                // Attempt at retrieving phones associated to contact
                phones = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,
                        null, null);
                phones.moveToFirst();
                phoneNumber = phones.getString(phones.getColumnIndex("data1"));
            } finally {
                if(phones!=null){
                    phones.close();
                    phones = null;
                }
            }
        }
        mFriend.setPhoneNumber(phoneNumber);
    }

    private void saveHowdy () {
        Howdy howdy = new Howdy(mFriend, new Date());
        Log.d(TAG, "Saving howdy to " + mFriend.getFriendName() + " in SQLite database");
        HowdyLab.get(getContext()).addHowdy(howdy);
    }

    private void setLatestHowdy() {
        String lastHowdyText = HowdyLab.get(getContext()).getLatestHowdy(mLastHowdySentFormat, mHowdyDateFormat);
        if (lastHowdyText == null) {
            lastHowdyText = getString(R.string.emptyLatestHowdy);
        }
        mLastHowdySent.setText(lastHowdyText);
    }
}
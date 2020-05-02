package ru.ktrofimov.parrotmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Kirill on 20.04.2016.
 */
public class phoneStateReceiver extends BroadcastReceiver {

    TelephonyManager telephony;
    public class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            RearGearReceiver.phoneState = state;

            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("DEBUG", "IDLE");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("DEBUG", "OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("DEBUG", "RINGING");
                    break;
            }
        }
    }
    public void onReceive(Context context, Intent intent) {
        MyPhoneStateListener phoneListener = new MyPhoneStateListener();
        telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}

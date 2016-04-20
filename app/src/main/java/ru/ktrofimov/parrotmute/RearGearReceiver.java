package ru.ktrofimov.parrotmute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Kirill Trofimov on 16.04.2016.
 */
public class RearGearReceiver   extends BroadcastReceiver {

    final String TAG ="ParrotMuteReceiver";

    boolean savedStreamMuted = false;
    public void onReceive(Context paramContext, Intent paramIntent)
    {
        if (paramIntent.getAction().equals("com.parrot.reargear"))
        {
            AudioManager am =(AudioManager)paramContext.getSystemService(Context.AUDIO_SERVICE);
            boolean rear_gear_on = paramIntent.getBooleanExtra("com.parrot.reargear.status", false);
            SharedPreferences sets = PreferenceManager.getDefaultSharedPreferences( paramContext );

            // Log.d("RearGearReceiver", "Rear Gear Intent is received on = " + rear_gear_on );
            if ( rear_gear_on )
            {
                //mute audio
                int rearVolume = sets.getInt("RearVolume", 0 );
                int notif_level = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                int alarm_level = am.getStreamVolume(AudioManager.STREAM_ALARM);
                int music_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                int ring_level = am.getStreamVolume(AudioManager.STREAM_RING);
                int system_level = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
                SharedPreferences.Editor ed = sets.edit();
                ed.putInt( "Notification", notif_level );
                ed.putInt( "Alarm", alarm_level );
                ed.putInt( "Music", music_level );
                ed.putInt( "Ring", ring_level );
                ed.putInt( "System", system_level );
                ed.apply();
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, rearVolume, 0 );
                am.setStreamVolume(AudioManager.STREAM_ALARM, rearVolume, 0 );
                am.setStreamVolume(AudioManager.STREAM_MUSIC, rearVolume, 0 );
                am.setStreamVolume(AudioManager.STREAM_RING, rearVolume, 0 );
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, rearVolume, 0 );
                // Log.d( TAG, "muteAudio: NOT="+notif_level+" ALR="+alarm_level+" MUS="+music_level+" RNG="+ring_level+" RV="+rearVolume );
            }
            else
            {
                //unmute audio
                int notif_level = sets.getInt("Notification", am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
                int alarm_level = sets.getInt("Alarm", am.getStreamMaxVolume(AudioManager.STREAM_ALARM));
                int music_level = sets.getInt("Music", am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                int ring_level = sets.getInt("Ring", am.getStreamMaxVolume(AudioManager.STREAM_RING));
                int system_level = sets.getInt("System", am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
                am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notif_level, 0 );
                am.setStreamVolume(AudioManager.STREAM_ALARM, alarm_level, 0 );
                am.setStreamVolume(AudioManager.STREAM_MUSIC, music_level, 0 );
                am.setStreamVolume(AudioManager.STREAM_RING, ring_level, 0 );
                am.setStreamVolume(AudioManager.STREAM_SYSTEM, system_level, 0 );
                // Log.d( TAG, "unmuteAudio: NOT="+notif_level+" ALR="+alarm_level+" MUS="+music_level+" RNG="+ring_level );
            }
        }
    }
}

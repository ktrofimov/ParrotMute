package ru.ktrofimov.parrotmute;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    final String TAG ="ParrotMute";

    void storeVolume()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
        AudioManager am =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int notif_level = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int alarm_level = am.getStreamVolume(AudioManager.STREAM_ALARM);
        int music_level = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        int ring_level = am.getStreamVolume(AudioManager.STREAM_RING);
        int system_level = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt( "Notification", notif_level );
        ed.putInt( "Alarm", alarm_level );
        ed.putInt( "Music", music_level );
        ed.putInt( "Ring", ring_level );
        ed.putInt( "System", system_level );
        ed.apply();
        // Log.d( TAG, "storeVolume: NOT="+notif_level+" ALR="+alarm_level+" MUS="+music_level+" RNG="+ring_level+" SYS="+system_level );
    }

    void restoreVolume()
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
        AudioManager am =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int notif_level = settings.getInt("Notification", am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        int alarm_level = settings.getInt("Alarm", am.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        int music_level = settings.getInt("Music", am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int ring_level = settings.getInt("Ring", am.getStreamMaxVolume(AudioManager.STREAM_RING));
        int system_level = settings.getInt("System", am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, notif_level, 0 );
        am.setStreamVolume(AudioManager.STREAM_ALARM, alarm_level, 0 );
        am.setStreamVolume(AudioManager.STREAM_MUSIC, music_level, 0 );
        am.setStreamVolume(AudioManager.STREAM_RING, ring_level, 0 );
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, system_level, 0 );
        // Log.d( TAG, "restoreVolume: NOT="+notif_level+" ALR="+alarm_level+" MUS="+music_level+" RNG="+ring_level+" SYS="+system_level );
    }

    void setVolume( int volume )
    {
        AudioManager am =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0 );
        am.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0 );
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0 );
        am.setStreamVolume(AudioManager.STREAM_RING, volume, 0 );
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0 );
        // Log.d( TAG, "setVolume: "+volume );
    }

    void saveRearVolume( int volume )
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences( this );
        SharedPreferences.Editor ed = settings.edit();
        ed.putInt( "RearVolume", volume );
        ed.apply();
        // Log.d( TAG, "saveRearVolume: "+volume );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SeekBar rearVolume = (SeekBar)findViewById(R.id.rearVolume);
        AudioManager am =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        SharedPreferences sets = PreferenceManager.getDefaultSharedPreferences( this );
        if( rearVolume != null ) {
            rearVolume.setMax( am.getStreamMaxVolume(AudioManager.STREAM_MUSIC) );
            int progress = sets.getInt("RearVolume", 0);
            rearVolume.setProgress( progress );
            storeVolume();
            setVolume( progress );
            rearVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setVolume(progress);
                    saveRearVolume(progress);
                }
            });
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // Log.d( TAG, "onPause" );
        restoreVolume();
    }
}

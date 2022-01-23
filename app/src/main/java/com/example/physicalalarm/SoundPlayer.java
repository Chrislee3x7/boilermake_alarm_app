package com.example.physicalalarm;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

public class SoundPlayer  {

    static Ringtone r;
    static boolean isPlaying = false;

    public static void playSound(Context context) {
        Uri alarmSound = Uri.parse("android.resource://com.example.physicalalarm/" + R.raw.alarm);
        Ringtone r = RingtoneManager.getRingtone(context, alarmSound);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(true);
        };
        isPlaying = true;
        r.play();
    }

    public static void stopSound() {
        isPlaying = false;
        r.stop();
    }


}

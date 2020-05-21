package com.benjaminabel.vibration;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.util.List;

public class VibratorHelper {
    @SuppressWarnings("deprecation")
    static void vibrate(Vibrator vibrator, long duration, int amplitude) {
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, amplitude));
                } else {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            } else {
                vibrator.vibrate(duration);
            }
        }
    }

    @SuppressWarnings("deprecation")
    static void vibrate(Vibrator vibrator, List<Integer> pattern, int repeat) {
        long[] patternLong = new long[pattern.size()];

        for (int i = 0; i < patternLong.length; i++) {
            patternLong[i] = pattern.get(i).intValue();
        }

        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(patternLong, repeat));
            } else {
                vibrator.vibrate(patternLong, repeat);
            }
        }
    }

    @SuppressWarnings("deprecation")
    static void vibrate(Vibrator vibrator, List<Integer> pattern, int repeat, List<Integer> intensities) {
        long[] patternLong = new long[pattern.size()];
        int[] intensitiesArray = new int[intensities.size()];

        for (int i = 0; i < patternLong.length; i++) {
            patternLong[i] = pattern.get(i).intValue();
        }

        for (int i = 0; i < intensitiesArray.length; i++) {
            intensitiesArray[i] = intensities.get(i);
        }

        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (vibrator.hasAmplitudeControl()) {
                    vibrator.vibrate(VibrationEffect.createWaveform(patternLong, intensitiesArray, repeat));
                } else {
                    vibrator.vibrate(VibrationEffect.createWaveform(patternLong, repeat));
                }
            } else {
                vibrator.vibrate(patternLong, repeat);
            }
        }
    }

    static void doVibrate(
            Vibrator vibrator,
            Integer duration,
            List<Integer> pattern,
            Integer repeat,
            List<Integer> intensities,
            Integer amplitude
    ) {
        if (pattern.size() > 0 && intensities.size() > 0) {
            vibrate(vibrator, pattern, repeat, intensities);
        } else if (pattern.size() > 0) {
            vibrate(vibrator, pattern, repeat);
        } else {
            vibrate(vibrator, duration, amplitude);
        }
    }
}

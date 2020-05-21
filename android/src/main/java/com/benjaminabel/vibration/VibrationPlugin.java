package com.benjaminabel.vibration;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class VibrationPlugin implements MethodCallHandler {
    private final Vibrator vibrator;
    private final Registrar registrar;
    private static final String CHANNEL = "vibration";

    private VibrationPlugin(Registrar registrar) {
        this.registrar = registrar;
        this.vibrator = (Vibrator) registrar.context().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
        channel.setMethodCallHandler(new VibrationPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case "hasVibrator": {
                result.success(vibrator.hasVibrator());
                break;
            }
            case "hasAmplitudeControl": {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    result.success(vibrator.hasAmplitudeControl());
                } else {
                    // For earlier API levels, return false rather than raising a
                    // MissingPluginException in order to allow applications to handle
                    // non-existence gracefully.
                    result.success(false);
                }
                break;
            }
            case "vibrate": {
                Integer duration = call.argument("duration");
                List<Integer> pattern = call.argument("pattern");
                Integer repeat = call.argument("repeat");
                List<Integer> intensities = call.argument("intensities");
                Integer amplitude = call.argument("amplitude");

                Data.Builder dataBuilder = new Data.Builder();
                if (duration != null) {
                    dataBuilder.putInt("duration", duration);
                }
                if (repeat != null) {
                    dataBuilder.putInt("repeat", repeat);
                }
                if (amplitude != null) {
                    dataBuilder.putInt("amplitude", amplitude);
                }
                if (pattern != null && !pattern.isEmpty()) {
                    dataBuilder.putIntArray("pattern", toIntArray(pattern));
                }
                if (intensities != null && !intensities.isEmpty()) {
                    dataBuilder.putIntArray("intensities", toIntArray(intensities));
                }

                OneTimeWorkRequest.Builder requestBuilder =
                        new OneTimeWorkRequest.Builder(VibratorWorker.class);
                requestBuilder.setInputData(dataBuilder.build());

                WorkManager.getInstance(registrar.context()).enqueue(requestBuilder.build());

                result.success(null);
                break;
            }
            case "cancel": {
                vibrator.cancel();
                result.success(null);
                break;
            }
            default:
                result.notImplemented();
        }
    }

    private int[] toIntArray(List<Integer> list) {
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }
}

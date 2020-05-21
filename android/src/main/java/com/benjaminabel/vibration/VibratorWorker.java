package com.benjaminabel.vibration;

import android.content.Context;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

public class VibratorWorker extends Worker {
    public VibratorWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params
    ) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        int duration = getInputData().getInt("duration", 0);
        int repeat = getInputData().getInt("repeat", 0);
        int amplitude = getInputData().getInt("amplitude", 0);
        List<Integer> pattern = getListInt("pattern", getInputData());
        List<Integer> intensities = getListInt("intensities", getInputData());

        VibratorHelper.doVibrate(vibrator, duration, pattern, repeat, intensities, amplitude);

        return Result.success();
    }

    static private List<Integer> getListInt(String key, Data data) {
        int[] intArray = data.getIntArray(key);
        ArrayList<Integer> result = new ArrayList<>();

        if (intArray != null) {
            for (int i : intArray) {
                result.add(i);
            }
        }

        return result;
    }
}

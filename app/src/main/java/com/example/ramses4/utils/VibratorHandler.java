package com.example.ramses4.utils;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;

public class VibratorHandler {

    private final android.os.Vibrator vibrator;
    private VibrationEffect effectSuccess;
    private VibrationEffect effectFailure;
    private long[] patternSuccess;
    private long[] patternFailure;

    public VibratorHandler(Context context) {
        vibrator = (android.os.Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        buildEffectSuccess();
        buildEffectFailure();
    }

    public void vibrateForSuccess() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            vibrator.vibrate(effectSuccess);
        else
            vibrator.vibrate(patternSuccess, -1);
    }

    public void vibrateForFailure() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            vibrator.vibrate(effectFailure);
        else
            vibrator.vibrate(patternFailure, -1);

    }

    private void buildEffectSuccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            effectSuccess = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            effectSuccess = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);
        else
            patternSuccess = new long[]{0, 100, 0};
    }

    private void buildEffectFailure() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            effectFailure = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK);
        else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            effectFailure = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE);
        else
            patternFailure = new long[]{0, 100, 300, 100, 0};
    }

}

package com.example.systemshortcuts;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * SystemShortcutsPlugin
 */
public class SystemShortcutsPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    private MethodChannel channel;
    private Activity activity;

    /**
     * v2 plugin embedding
     */
    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(
                binding.getBinaryMessenger(), "system_shortcuts");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        channel = null;
    }

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onMethodCall(MethodCall call, @NonNull Result result) {
        if (activity == null) {
            result.error("ACTIVITY_NOT_AVAILABLE", "Activity is not available.", null);
            return;
        }
        switch (call.method) {
            case "home":
                home();
                break;
            case "back":
                back();
                break;
            case "volDown":
                volDown();
                break;
            case "volUp":
                volUp();
                break;
            case "orientLandscape":
                orientLandscape();
                break;
            case "orientPortrait":
                orientPortrait();
                break;
            case "wifi":
                wifi();
                break;
            case "checkWifi":
                result.success(checkWifi());
                break;
            case "bluetooth":
                bluetooth();
                break;
            case "checkBluetooth":
                result.success(checkBluetooth());
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void home() {
        if (this.activity == null) return;
        this.activity.startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void back() {
        if (this.activity == null) return;
        this.activity.onBackPressed();
    }

    private void volDown() {
        if (this.activity == null) return;
        AudioManager audioManager = (AudioManager) this.activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    private void volUp() {
        if (this.activity == null) return;
        AudioManager audioManager = (AudioManager) this.activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    private void orientLandscape() {
        if (this.activity == null) return;
        this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void orientPortrait() {
        if (this.activity == null) return;
        this.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void wifi() {
        if (this.activity == null) return;
        WifiManager wifiManager = (WifiManager) this.activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            } else {
                wifiManager.setWifiEnabled(true);
            }
        }
    }

    private boolean checkWifi() {
        if (this.activity == null) return false;
        WifiManager wifiManager = (WifiManager) this.activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager != null && wifiManager.isWifiEnabled();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private void bluetooth() {
        if (this.activity == null) return;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            } else {
                mBluetoothAdapter.enable();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private boolean checkBluetooth() {
        if (this.activity == null) return false;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

}

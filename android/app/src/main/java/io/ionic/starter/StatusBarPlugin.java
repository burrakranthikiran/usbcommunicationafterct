package io.ionic.starter;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.pdf.PdfRenderer;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@CapacitorPlugin(name = "MyStatusBar")
public class StatusBarPlugin extends Plugin {
  private static final String ACTION_USB_PERMISSION = "io.ionic.starter.USB_PERMISSION";
  public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 200;

  @PluginMethod()
  public void getHeight(PluginCall call){
    Resources res = getActivity().getApplication().getResources();
    int statusBarPlugin;
    int resourceId = res.getIdentifier("status_bar_height","dimen", "android");
    if(resourceId > 0){
      statusBarPlugin = res.getDimensionPixelSize(resourceId);
      JSObject ret = new JSObject();
      ret.put("height", statusBarPlugin / res.getDisplayMetrics().density);
      call.resolve(ret);
      checkAndRequestPermissions();
//      Toast.makeText(getActivity().getApplicationContext(), "Status bar height obtained", Toast.LENGTH_SHORT).show();
    } else {
      call.reject("Status bar height not obtained");
    }

  }

  @PluginMethod()
  public void getMobileSettings(PluginCall call) {
    Context context = getContext();

    // Create an intent to open the device settings
    Intent intent = new Intent(Settings.ACTION_SETTINGS);

    // Check if the intent can be resolved (if the settings activity is available)
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      // Start the settings activity
      context.startActivity(intent);

      // You can return a success message if needed
      JSObject ret = new JSObject();
      ret.put("message", "Opening device settings...");
      call.resolve(ret);
    } else {
      // If the settings activity is not available, you can reject the call
      call.reject("Unable to open device settings");
    }
  }



  @PluginMethod()
  public void getPermission(PluginCall call) {
    Context context = getContext();

    // Create an intent to open the device settings
    Intent intent = new Intent(getContext(),USBPrinter.class);

    // Check if the intent can be resolved (if the settings activity is available)
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      // Start the settings activity
      printUsb();

      // You can return a success message if needed
      JSObject ret = new JSObject();
      ret.put("message", "Opening device settings...");
      call.resolve(ret);
    } else {
      // If the settings activity is not available, you can reject the call
      call.reject("Unable to open device settings");
    }
  }
  @PluginMethod()
  public void getUSBPrinter(PluginCall call) {
    Context context = getContext();

    // Create an intent to open the device settings
    Intent intent = new Intent(getContext(),USBPrinter.class);

    // Check if the intent can be resolved (if the settings activity is available)
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      // Start the settings activity
      context.startActivity(intent);

      // You can return a success message if needed
      JSObject ret = new JSObject();
      ret.put("message", "Opening device settings...");
      call.resolve(ret);
    } else {
      // If the settings activity is not available, you can reject the call
      call.reject("Unable to open device settings");
    }
  }


  @PluginMethod()
  public void getVibration(PluginCall call) {
    Context context = getContext();

    // Check if the device supports vibration
    if (!hasVibrator(context)) {
      call.reject("Device does not support vibration");
      return;
    }

    // Check and request permissions if necessary
    if (!checkAndRequestPermissions(call)) {
      return;
    }

    // Vibrate for 1000 milliseconds (1 second)
    vibrate(context, 1000);

    // You can add more logic or return additional data as needed
    JSObject ret = new JSObject();
    ret.put("message", "Vibration done");
    call.resolve(ret);
  }

  private boolean hasVibrator(Context context) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    return vibrator != null && vibrator.hasVibrator();
  }

  private void vibrate(Context context, long duration) {
    Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    if (vibrator != null) {
      vibrator.vibrate(duration);
    }
  }

  private boolean checkAndRequestPermissions(PluginCall call) {
    Context context = getContext();
    List<String> permissionsToRequest = new ArrayList<>();

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.VIBRATE)
      != PackageManager.PERMISSION_GRANTED) {
      permissionsToRequest.add(android.Manifest.permission.VIBRATE);
    }

    if (!permissionsToRequest.isEmpty()) {
      ActivityCompat.requestPermissions(getActivity(), permissionsToRequest.toArray(new String[0]), 0);
      return false;
    }

    return true;
  }

  private  boolean checkAndRequestPermissions() {




    int bluetooth_permission = ContextCompat.checkSelfPermission(getContext(),BLUETOOTH_CONNECT);
    int bluetooth_permission_2 = ContextCompat.checkSelfPermission(getContext(),BLUETOOTH_SCAN);

    List<String> listPermissionsNeeded = new ArrayList<>();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      if(bluetooth_permission_2 != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(BLUETOOTH_SCAN);
      }
      if(bluetooth_permission != PackageManager.PERMISSION_GRANTED){
        listPermissionsNeeded.add(BLUETOOTH_CONNECT);
      }

    }
    if (!listPermissionsNeeded.isEmpty()) {
      ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
      return false;
    }
    return true;
  }

  private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (ACTION_USB_PERMISSION.equals(action)) {
        synchronized (this) {
          UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
          UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
          if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            if (usbManager != null && usbDevice != null) {
              // YOUR PRINT CODE HERE

              Log.e("Tesing_Module","Working");
            }
          }
        }
      }
    }
  };

  public void printUsb() {
    UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(getContext());
    UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
    if (usbConnection != null && usbManager != null) {
      PendingIntent permissionIntent = PendingIntent.getBroadcast(
        getContext(),
        0,
        new Intent(ACTION_USB_PERMISSION),
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
      );
      IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
      getActivity().registerReceiver(usbReceiver, filter);
      usbManager.requestPermission(((UsbConnection) usbConnection).getDevice(), permissionIntent);
    }
  }


}

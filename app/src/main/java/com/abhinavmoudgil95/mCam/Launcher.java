package com.abhinavmoudgil95.mCam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.os.Bundle;
import android.util.Log;
import android.util.Range;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;


public class Launcher extends Activity
{
    private CameraDevice mCameraDevice;
    private final static String TAG = "Camera2testJ";
    int sensitivityValue = 100;
    long exposureTimeValue = 100;
    int sensorFrameDurationValue = 100;
    boolean toggleSwitchValue = false;
  //  int lensFilterDensityValue = 100;
  //  int focalDistanceValue = 100;
  //  int lensApertureValue = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);

        //Open Camera to get its parameters
        Log.d(TAG, "Launcher - onCreate");
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.d(TAG, "Launcher - openCamera E");
        try{

            String cameraId = manager.getCameraIdList()[0];
            manager.openCamera(cameraId, mStateCallback, null);
            Log.d(TAG, "Launcher - OnCreate openCamera");

            //Spinner for focal lengths
            Spinner focalLengths = (Spinner) findViewById(R.id.focalDistancesSpinner);
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            if (characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) != CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
            {
                Toast.makeText(Launcher.this, "Manual camera control not supported!", Toast.LENGTH_LONG).show();
            }
            float[] focalLengthArray =  characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
            String[] stringValues = new String[focalLengthArray.length];
            for (int i=0;  i<focalLengthArray.length;  i++) {
                stringValues[i] = "" + focalLengthArray[i];
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, stringValues);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            focalLengths.setAdapter(adapter1);

            //Spinner for lens filter densities
            Spinner lensApertures = (Spinner) findViewById(R.id.lensApertureSpinner);
            float[] lensAperturesArray =  characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
            String[] apertureValues;
            if (lensAperturesArray != null && lensAperturesArray.length != 0)
            {
                apertureValues = new String[lensAperturesArray.length];
                for (int i = 0; i < lensAperturesArray.length; i++)
                {
                    apertureValues[i] = "" + lensAperturesArray[i];
                }
            }
            else
            {
                apertureValues = new String[1];
                apertureValues[0] = "No Values";
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, apertureValues);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lensApertures.setAdapter(adapter2);

            //Spinner for filter densities
            Spinner lensFilterDensities = (Spinner) findViewById(R.id.lensFilterDensitySpinner);
            float[] lensFilterDensityArray =  characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FILTER_DENSITIES);
           String[] lensFilterDensityValues;
            if (lensFilterDensityArray != null && lensFilterDensityArray.length != 0)
            {
                lensFilterDensityValues = new String[lensFilterDensityArray.length];
                for (int i = 0; i < lensFilterDensityArray.length; i++)
                {
                    lensFilterDensityValues[i] = "" + lensFilterDensityArray[i];
                }
            }
            else
            {
                lensFilterDensityValues = new String[1];
                lensFilterDensityValues[0] = "No Values";
            }
            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lensFilterDensityValues);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lensFilterDensities.setAdapter(adapter3);
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }

        // Auto Exposure Toggle Switch
        CompoundButton mySwitch = (CompoundButton) findViewById(R.id.toggleSwitch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                toggleSwitchValue = !toggleSwitchValue;
                Log.d(TAG, "Switch State = " + toggleSwitchValue);
                Spinner lensFilterDensities = (Spinner) findViewById(R.id.lensFilterDensitySpinner);
                Spinner focalLengths = (Spinner) findViewById(R.id.focalDistancesSpinner);
                Spinner lensApertures = (Spinner) findViewById(R.id.lensApertureSpinner);
                SeekBar sensitivity = (SeekBar) findViewById(R.id.sensitivitySeekBar);
                SeekBar exposureTime = (SeekBar) findViewById(R.id.exposureTimeSeekBar);
                SeekBar sensorFrameDuration = (SeekBar) findViewById(R.id.frameSeekBar);
                lensFilterDensities.setEnabled(!toggleSwitchValue);
                focalLengths.setEnabled(!toggleSwitchValue);
                lensApertures.setEnabled(!toggleSwitchValue);
                sensitivity.setEnabled(!toggleSwitchValue);
                exposureTime.setEnabled(!toggleSwitchValue);
                sensorFrameDuration.setEnabled(!toggleSwitchValue);
            }
        });

        //Sensitivity SeekBar
        SeekBar sensitivity = (SeekBar) findViewById(R.id.sensitivitySeekBar);
        sensitivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try
                {

                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
                    //   Log.d(TAG, "INFO_SUPPORTED_HARDWARE_LEVEL " + characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                    Log.d(TAG, "Sensitivity progress change");
                    if (characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
                    {
                        Range<Integer> range2 = characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
                        int max1 = range2.getUpper();//10000
                        int min1 = range2.getLower();//100
                        sensitivityValue = ((progress * (max1 - min1)) / 100 + min1);
                    }
                    //    mPreviewBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, iso);
                    //   mPreviewBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, (long) 123121);
                    //   mPreviewBuilder.set(CaptureRequest.SENSOR_FRAME_DURATION, (long) 800000000);
                } catch (CameraAccessException e)
                {
                    Log.e(TAG, "Launcher - CameraDevice is null, return");
                    e.printStackTrace();
                }
            }
        });

        //Exposure Time SeekBar
        SeekBar exposureTime = (SeekBar) findViewById(R.id.exposureTimeSeekBar);
        exposureTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try
                {
                    CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraDevice.getId());
                    Log.d(TAG, "Exposure time change ");
                    if (characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) == CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_FULL)
                    {
                        Range<Long> range2 = characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
                        long max1 = range2.getUpper();//10000
                        long min1 = range2.getLower();//100
                        exposureTimeValue = ((progress * (max1 - min1)) / 100 + min1);
                    }
                    //    mPreviewBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, iso);
                    //   mPreviewBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, (long) 123121);
                    //   mPreviewBuilder.set(CaptureRequest.SENSOR_FRAME_DURATION, (long) 800000000);
                } catch (CameraAccessException e)
                {
                    Log.e(TAG, "Launcher - CameraDevice is null, return");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "Launcher - onResume");
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.d(TAG, "Launcher - openCamera E");
        try{
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            manager.openCamera(cameraId, mStateCallback, null);
            Log.d(TAG, "INFO_SUPPORTED_HARDWARE_LEVEL " + characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
        }
        catch (CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "Launcher - onPause");
        super.onPause();
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    ///////////////////////////////////CAMERA STATE CALLBACK////////////////////////////////////

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {

            Log.i(TAG, "Launcher - onOpened");
            mCameraDevice = camera;
        }

        @Override
        public void onDisconnected(CameraDevice camera) {

            Log.e(TAG, "Launcher - onDisconnected");
        }

        @Override
        public void onError(CameraDevice camera, int error) {

            Log.e(TAG, "Launcher - onErrorStateCallback " + error);
        }
    };

    public void launchCamera(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Sensitivity", sensitivityValue);
        intent.putExtra("ExposureTime", exposureTimeValue);
        intent.putExtra("SensorFrameDuration", sensorFrameDurationValue);
        intent.putExtra("ToggleSwitch", toggleSwitchValue);
        Log.d(TAG, "Sensitivity -" + sensitivityValue + "Exposure Time - " + exposureTimeValue);
        startActivity(intent);
    }
}

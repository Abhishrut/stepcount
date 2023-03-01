package com.samsung.sdc21.stepcount;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.libraries.healthdata.HealthDataClient;
import com.google.android.libraries.healthdata.HealthDataService;
import com.google.android.libraries.healthdata.data.ReadAggregatedDataResponse;
import com.google.android.libraries.healthdata.permission.Permission;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.samsung.sdc21.stepcount.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Set;

import javax.annotation.ParametersAreNonnullByDefault;

public class MainActivity extends Activity implements SensorEventListener{
    public static final String APP_TAG = "StepCount";
    private StepsReader stepsReader = null;
    private Permissions permissions = null;

    private Button mConnectButton;
    private Button mStartButton;

    private PrintWriter output;
    private BufferedReader input;
    private TextView mIPTextView;
    private Socket socket;
    private String TAG="ABHI";
    private String SERVER_IP;
    private int SERVER_PORT=4321;
    private boolean connected=false;

    private SensorManager sensorManager;
    private Sensor heartbeatSensor;

    private TextView textViewHB;
    private TextView textViewSteps;
    private boolean started = false;

    class Data implements Serializable {
        public int steps;
        public int heartBeat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textViewHB = binding.HeartRateCount;
        textViewSteps = binding.StepCount;
        mIPTextView= binding.IPEditView;
        mConnectButton= binding.Connect;
        mStartButton = binding.Start;

        if (!HealthDataService.isHealthDataApiSupported()) {
            Toast.makeText(
                    this,
                    "Health Platform not available, make sure you're on Samsung device running Android"
                            + " Watch 4 and above",
                    Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        HealthDataClient healthDataClient = HealthDataService.getClient(this);
        stepsReader = new StepsReader(healthDataClient);
        permissions = new Permissions(healthDataClient);
        readStepsWithPermissionsCheck();
        //add sensor
        sensorInit();

    }

    private void sensorInit(){
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        heartbeatSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        mConnectButton.setOnClickListener(view -> {
            if(connected)
            {
                try {
                    if(socket!=null)
                    {
                        socket.close();
                        connected=false;
                        Log.i(TAG, "Closed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                SERVER_IP=mIPTextView.getText().toString();
                connected=true;
            }
        });
        mStartButton.setOnClickListener(view -> {
            if(started)
            {
                sensorManager.unregisterListener(this);
                started=false;
            }
            else
            {
                sensorManager.registerListener((SensorEventListener) view.getContext(), heartbeatSensor, SensorManager.SENSOR_DELAY_NORMAL);
                started= true;
                new ReadStepsBackGround().execute("");
            }
        });
    }

    public void onRefresh(View view) {
        readStepsWithPermissionsCheck();
    }

    private void permissionsExceptionHandler(PermissionsException exception) {
        Log.e(APP_TAG, "PermissionsException with message: " + exception.getMessage());
        MainActivity.this.runOnUiThread(() -> Toast.makeText(
                MainActivity.this,
                R.string.read_permissions_error,
                Toast.LENGTH_LONG)
                .show());
    }

    private void onPermissionsFailureHandler(Throwable t) {
        Log.e(APP_TAG, "Callback failed with message: " + t.toString());
        MainActivity.this.runOnUiThread(() -> Toast.makeText(
                MainActivity.this,
                R.string.read_permissions_error,
                Toast.LENGTH_LONG)
                .show());
    }

    private void readStepsWithPermissionsCheck() {
        try {
            ListenableFuture<Set<Permission>> permissionFuture = permissions.getGrantedPermissions();
            Futures.addCallback(permissionFuture, new FutureCallback<Set<Permission>>() {
                        @Override
                        public void onSuccess(@Nullable Set<Permission> result) {
                            if (permissions.arePermissionsGranted(result)) {
                                Log.d(APP_TAG, "All permissions granted. Read steps.");
                              //  readSteps();
                            } else {
                                Log.d(APP_TAG, "Permissions not granted. Request Permissions.");
                                readStepsWithRequestPermissions();
                            }
                        }

                        @Override
                        @ParametersAreNonnullByDefault
                        public void onFailure(Throwable t) {
                            onPermissionsFailureHandler(t);
                        }
                    },
                    ContextCompat.getMainExecutor(this /*context*/));
        } catch (PermissionsException exception) {
            permissionsExceptionHandler(exception);
        }
    }

    private void readStepsWithRequestPermissions() {
        try {
            ListenableFuture<Set<Permission>> requestPermissionFuture = permissions.requestPermissions();
            Futures.addCallback(requestPermissionFuture, new FutureCallback<Set<Permission>>() {
                        @Override
                        public void onSuccess(@Nullable Set<Permission> result) {
                            if (permissions.arePermissionsGranted(result)) {
                                Log.d(APP_TAG, "All permissions granted. Read steps.");
                            //    readSteps();
                            } else {
                                Log.e(APP_TAG, "Permissions not granted. Can't read steps.");
                            }
                        }

                        @Override
                        @ParametersAreNonnullByDefault
                        public void onFailure(Throwable t) {
                            onPermissionsFailureHandler(t);
                        }
                    },
                    ContextCompat.getMainExecutor(this /*context*/));
        } catch (PermissionsException exception) {
            permissionsExceptionHandler(exception);
        }
    }

    private void readSteps() {
        try {
            ListenableFuture<ReadAggregatedDataResponse> readFuture = stepsReader.readAggregatedData();
            Futures.addCallback(readFuture, new FutureCallback<ReadAggregatedDataResponse>() {
                        @Override
                        public void onSuccess(@Nullable ReadAggregatedDataResponse result) {
                            long steps = stepsReader.readStepsFromAggregatedDataResponse(result);
                            final String stepsStr = Long.toString(steps);
                            Log.d(APP_TAG, "Today steps count: " + steps);
                            runOnUiThread(() -> textViewSteps.setText(stepsStr));
                        }

                        @Override
                        @ParametersAreNonnullByDefault
                        public void onFailure(Throwable t) {
                            Log.e(APP_TAG, "readAggregatedData Callback failed with message: " + t);
                            MainActivity.this.runOnUiThread(() -> Toast.makeText(
                                    MainActivity.this,
                                    R.string.read_steps_failed,
                                    Toast.LENGTH_LONG)
                                    .show());
                        }
                    },
                    ContextCompat.getMainExecutor(this /*context*/));
        } catch (StepsReaderException exception) {
            Log.e(APP_TAG, "StepsReaderException with message: " + exception.getMessage());
            MainActivity.this.runOnUiThread(() -> Toast.makeText(
                    MainActivity.this,
                    R.string.read_steps_failed,
                    Toast.LENGTH_LONG)
                    .show());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(TAG, "Hello");
        if(event.sensor.getType()==Sensor.TYPE_HEART_RATE)
        {
            Log.i(TAG, "onSensorChanged: Heartbeat");
            textViewHB.setText((int) event.values[0]+"");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    class ReadStepsBackGround extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg0) {
            while(started)
            {
                try {
                    Log.i(TAG, "Connecting");
                    socket = new Socket(SERVER_IP, SERVER_PORT);
                    output = new PrintWriter(socket.getOutputStream());
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.i(TAG, "Connected"+socket.isInputShutdown());
                    Log.i(TAG, "Connected"+socket.isOutputShutdown());
                    Log.i(TAG, "Connected"+socket.isConnected());
                    //start a new thread for additional connections
                    //new Thread(new Thread1()).start();

                    //receive message from server
                /*
                String messageFromClient;
                messageFromClient = input.readLine();
                Log.i(TAG, "From Server: " + messageFromClient);
                */
                    //send message to server
                    Data data= new Data();
                    String message = "Hello from client\n";
                    while(!socket.isClosed())
                    {
                        readSteps();
                        data.heartBeat= Integer.valueOf(textViewHB.getText().toString());
                        data.steps= Integer.valueOf(textViewSteps.getText().toString());
                        output.write(data.heartBeat+"#"+data.steps+"\n");
                        output.flush();
                        Log.i(TAG, "Sending message");
                        Thread.sleep(1000);
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException | IOException e) {
                    Log.e(TAG, "Can't sleep");
                    break;
                }
            }
            return null;
        }
    }
}
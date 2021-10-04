package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.bluetooth.ble.advertising.ADPayloadParser;
import com.neovisionaries.bluetooth.ble.advertising.ADStructure;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneEID;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneTLM;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneUID;
import com.neovisionaries.bluetooth.ble.advertising.EddystoneURL;
import com.neovisionaries.bluetooth.ble.advertising.Flags;
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRV;
    private ExampleAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int LOCATION_PERMISSION_CODE = 100;

    Button pairedbtn, discoverbtn, onbtn, offbtn, pergrant, scanbtn;
    TextView statusBluetoothTv, pairedTv;
    ImageView bim;

    BluetoothAdapter bluetoothAdapter;
    // private LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

    private BluetoothLeScanner bluetoothLeScanner = null;
    private boolean scanning;
    private Handler handler = new Handler();

    ArrayList<ExampleItem> exampleList = new ArrayList<>();
    List<String> macAddressesList = new ArrayList<>();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ExampleItem scan = new ExampleItem();
//        exampleList.add(new ExampleItem());

        // ADAPTER CODE

        mRV = findViewById(R.id.mRV);
        layoutManager = new LinearLayoutManager(this);
        mRV.setLayoutManager(layoutManager);

        adapter = new ExampleAdapter(MainActivity.this, exampleList);

        mRV.setAdapter(adapter);

        onbtn = findViewById(R.id.onbtn);
//        offbtn = findViewById(R.id.offbtn);
//        pergrant = findViewById(R.id.pergrant);
//        // pairedbtn = findViewById(R.id.pairedbtn);
//        discoverbtn = findViewById(R.id.discoverbtn);
        statusBluetoothTv = findViewById(R.id.statusBluetoothTv);
        pairedTv = findViewById(R.id.pairedTv);
        scanbtn = findViewById(R.id.scanbtn);
//        bim = findViewById(R.id.bim);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        //checking avalibility
        if (bluetoothAdapter == null) {
            statusBluetoothTv.setText("Bluetooth is not avalible");
        } else {
            statusBluetoothTv.setText("Bluetooth is avalible");
        }

        if (bluetoothAdapter.isEnabled()) {
            //       bim.setImageResource(R.drawable.ic_action_on);
        } else {
            //       bim.setImageResource(R.drawable.ic_action_off);
        }

//        pergrant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
//            }
//        });


        onbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION_CODE);
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(MainActivity.this, "Turning on bluetooth", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    Toast.makeText(MainActivity.this, "Bluetooth is already ON", Toast.LENGTH_SHORT).show();
                }

            }
        });
//        offbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (bluetoothAdapter.isEnabled()) {
//                    bluetoothAdapter.disable();
//                    Toast.makeText(MainActivity.this, "Turning off bluetooth", Toast.LENGTH_SHORT).show();
//               //     bim.setImageResource(R.drawable.ic_action_off);
//                } else {
//                    Toast.makeText(MainActivity.this, "Bluetooth is already off", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanLeDevice();
//                if (bluetoothAdapter.isEnabled())
//                {
//              //      bim.setImageResource(R.drawable.ic_action_con);
//                }
            }
        });

//        pairedbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!bluetoothAdapter.isEnabled())
//                {
//                    pairedTv.setText("Paired Devices");
//                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
//                    for (BluetoothDevice device : devices)
//                    {
//                        pairedTv.append("\nDevice");
//                    }
//                }
//            }
//        });

//        discoverbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!bluetoothAdapter.isDiscovering()) {
//                    Toast.makeText(MainActivity.this, "Making your device discoverable", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
//                }
//            }
//        });


        if (exampleList.size() == macAddressesList.size()) {
            pairedTv.setText("list is equal");
        } else {
            pairedTv.setText("list not equal");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    //  bim.setImageResource(R.drawable.ic_action_on);
                    Toast.makeText(MainActivity.this, "Bluetooth is ON", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "could not ON bluetooth ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //     Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

//     This function is called when the user accepts or decline the permission.
//     Request Code is used to check which permission called this function.
//     This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void scanLeDevice() {
        if (bluetoothAdapter.isEnabled()) {
            if (!scanning) {
                // Stops scanning after a predefined scan period.
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanning = false;
                        bluetoothLeScanner.stopScan(leScanCallback);
                    }
                }, SCAN_PERIOD);

                scanning = true;
                bluetoothLeScanner.startScan(leScanCallback);
            } else {
                scanning = false;
                bluetoothLeScanner.stopScan(leScanCallback);
            }
        } else {
            Toast.makeText(MainActivity.this, "Turn ON bluetooth first", Toast.LENGTH_SHORT).show();
        }

    }


    private ScanCallback leScanCallback =
            new ScanCallback() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    byte[] scanRecord = result.getScanRecord().getBytes();
                    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);
                    ExampleItem saveddata = new ExampleItem();


                    for (ADStructure structure : structures) {

                        try {
                            Flags flags = (Flags)structure;

// (1) LE Limited Discoverable Mode
                            boolean limited = flags.isLimitedDiscoverable();
                            saveddata.setMf1(limited);

// (2) LE General Discoverable Mode
                            boolean general = flags.isGeneralDiscoverable();
                            saveddata.setMf2(general);

// (3) (inverted) BR/EDR Not Supported
                            boolean legacySupported = flags.isLegacySupported();
                            saveddata.setMf3(legacySupported);

// (4) Simultaneous LE and BR/EDR to Same Device Capable (Controller)
                            boolean controllerSimultaneity = flags.isControllerSimultaneitySupported();
                            saveddata.setMf4(controllerSimultaneity);

// (5) Simultaneous LE and BR/EDR to Same Device Capable (Host)
                            boolean hostSimultaneity = flags.isHostSimultaneitySupported();
                            saveddata.setMf5(hostSimultaneity);
                        }catch (Exception e){
                            e.printStackTrace();
                        }




                        // If the ADStructure instance can be cast to IBeacon.
                        if (structure instanceof IBeacon) {
                            IBeacon iBeacon = (IBeacon) structure;
                            saveddata.setType(1);
// (1) Proximity UUID
                            UUID uuid = iBeacon.getUUID();
                            saveddata.setMuuid(uuid);
// (2) Major number
                            int major = iBeacon.getMajor();
                            saveddata.setMmajor(major);
// (3) Minor number
                            int minor = iBeacon.getMinor();
                            saveddata.setMminor(minor);
// (4) Tx Power
                            int power = iBeacon.getPower();
                            saveddata.setMpower(power);
                        }


                        if (structure instanceof EddystoneUID) {
                            // Eddystone UID
                            EddystoneUID es = (EddystoneUID) structure;
                            saveddata.setType(2);

// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();
                            saveddata.setMpower1(power);

// (2) 10-byte Namespace ID
                            byte[] namespaceId = es.getNamespaceId();
                            String namespaceIdAsString = es.getNamespaceIdAsString();
                            saveddata.setMnamespaceId(namespaceId);

// (3) 6-byte Instance ID
                            byte[] instanceId = es.getInstanceId();
                            String instanceIdAsString = es.getInstanceIdAsString();
                            saveddata.setMinstanceId(instanceId);

// (4) 16-byte Beacon ID
                            byte[] beaconId = es.getBeaconId();
                            String beaconIdAsString = es.getBeaconIdAsString();
                            saveddata.setMbeaconId(beaconId);

                        }


                        if (structure instanceof EddystoneURL) {
                            // Eddystone URL
                            EddystoneURL es = (EddystoneURL) structure;
                            saveddata.setType(3);

// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();
                            saveddata.setMpower2(power);

// (2) URL
                            URL url = es.getURL();
                            saveddata.setMurl(url);
                        }


                        if (structure instanceof EddystoneTLM) {
                            // Eddystone TLM
                            EddystoneTLM es = (EddystoneTLM) structure;
                            saveddata.setType(4);

// (1) TLM Version
                            int version = es.getTLMVersion();
                            saveddata.setMversion(version);

// (2) Battery Voltage
                            int voltage = es.getBatteryVoltage();
                            saveddata.setMvoltage(voltage);

// (3) Beacon Temperature
                            float temperature = es.getBeaconTemperature();
                            saveddata.setMtemperature(temperature);

// (4) Advertisement count since power-on or reboot.
                            long count = es.getAdvertisementCount();
                            saveddata.setMcount(count);

// (5) Elapsed time in milliseconds since power-on or reboot.
                            long elapsed = es.getElapsedTime();
                            saveddata.setMelapsed(elapsed);

                        }


                        if (structure instanceof EddystoneEID) {
                            // Eddystone EID
                            EddystoneEID es = (EddystoneEID) structure;
                            saveddata.setType(5);

// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();
                            saveddata.setMpower3(power);

// (2) 8-byte EID
                            byte[] eid = es.getEID();
                            String eidAsString = es.getEIDAsString();
                            saveddata.setMeid(eid);
                        }

//

                    }

                    String devicename = result.getDevice().getName();
                    String devicersis = String.valueOf(result.getRssi());
                    String deviceaddress = result.getDevice().getAddress();
                    String hashcode = String.valueOf(result.hashCode());
                    String advertising = String.valueOf(result.getDevice().getUuids());


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        advertising = String.valueOf(result.getAdvertisingSid());
                    }

                    saveddata.setMdevicename(devicename);
                    saveddata.setDevicerssi(devicersis);
                    saveddata.setMdeviceaddress(deviceaddress);
                    saveddata.setMhashcode(hashcode);
                    saveddata.setMdeviceadvertising(advertising);

                    if (macAddressesList.size() > 0) {
                        if (macAddressesList.contains(deviceaddress)) {
                            if (exampleList.size() == macAddressesList.size()) {
                                int index = macAddressesList.indexOf(deviceaddress);
                                exampleList.set(index, saveddata);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            macAddressesList.add(deviceaddress);
                            exampleList.add(saveddata);
                            adapter.notifyDataSetChanged();
                        }

                    } else {
                        macAddressesList.add(deviceaddress);
                        exampleList.add(saveddata);
                        adapter.notifyDataSetChanged();
                    }


                    pairedTv.setText(result.getDevice().getAddress());


                }
            };

}

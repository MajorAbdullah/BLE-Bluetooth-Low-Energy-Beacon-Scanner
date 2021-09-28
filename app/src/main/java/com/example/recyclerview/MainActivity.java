package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
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
import com.neovisionaries.bluetooth.ble.advertising.IBeacon;
import com.neovisionaries.bluetooth.ble.advertising.Ucode;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRV;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int LOCATION_PERMISSION_CODE = 100;

    Button pairedbtn, discoverbtn, onbtn, offbtn, pergrant, scanbtn;
    TextView statusBluetoothTv, pairedTv;
    ImageView bim;

    BluetoothAdapter bluetoothAdapter;
    // private LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

    private BluetoothLeScanner bluetoothLeScanner;
    private boolean scanning;
    private Handler handler = new Handler();

    ArrayList<ExampleItem> exampleList = new ArrayList<>();

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
        mRV.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new ExampleAdapter(exampleList);
        mRV.setLayoutManager(layoutManager);
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
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    byte[] scanRecord = result.getScanRecord().getBytes();
                    List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);
                    ExampleItem saveddata = new ExampleItem();


                    for (ADStructure structure : structures)
                    {
                        structure.getType();
                        // If the ADStructure instance can be cast to IBeacon.
                        if (structure instanceof IBeacon)
                        {
                            IBeacon iBeacon = (IBeacon)structure;

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


                        if (structure instanceof EddystoneUID )
                        {
                            // Eddystone UID
                            EddystoneUID es = (EddystoneUID)structure;

// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();

// (2) 10-byte Namespace ID
                            byte[] namespaceId = es.getNamespaceId();
                            String namespaceIdAsString = es.getNamespaceIdAsString();

// (3) 6-byte Instance ID
                            byte[] instanceId = es.getInstanceId();
                            String instanceIdAsString = es.getInstanceIdAsString();

// (4) 16-byte Beacon ID
                            byte[] beaconId = es.getBeaconId();
                            String beaconIdAsString = es.getBeaconIdAsString();
                        }


                        if (structure instanceof EddystoneURL )
                        {
                            // Eddystone URL
                            EddystoneURL es = (EddystoneURL)structure;
// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();
// (2) URL
                            URL url = es.getURL();
                        }


                        if (structure instanceof EddystoneTLM)
                        {
                            // Eddystone TLM
                            EddystoneTLM es = (EddystoneTLM)structure;
// (1) TLM Version
                            int version = es.getTLMVersion();
// (2) Battery Voltage
                            int voltage = es.getBatteryVoltage();
// (3) Beacon Temperature
                            float temperature = es.getBeaconTemperature();
// (4) Advertisement count since power-on or reboot.
                            long count = es.getAdvertisementCount();
// (5) Elapsed time in milliseconds since power-on or reboot.
                            long elapsed = es.getElapsedTime();
                        }


                        if (structure instanceof EddystoneEID )
                        {
                            // Eddystone EID
                            EddystoneEID es = (EddystoneEID)structure;
// (1) Calibrated Tx power at 0 m.
                            int power = es.getTxPower();
// (2) 8-byte EID
                            byte[] eid = es.getEID();
                            String eidAsString = es.getEIDAsString();
                        }

                        if (structure instanceof Ucode)
                        {
                            Ucode ucode = (Ucode)structure;
// (1) Version
                            int version = ucode.getVersion();
// (2) Ucode (32 upper-case hex letters)
                            //         String ucode = ucode.getUcode();
// (3) Status
                            int status = ucode.getStatus();
// (4) The state of the battery
                            boolean low = ucode.isBatteryLow();
// (5) Transmission interval
                            int interval = ucode.getInterval();
// (6) Transmission power
                            int power = ucode.getPower();
// (7) Transmission count
                            int count = ucode.getCount();

                        }

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
//
//                    saveddata.setMuuid();
//                    saveddata.setMmajor();
//                    saveddata.setMminor();
//                    saveddata.setMpower();

                    exampleList.add(saveddata);
                    adapter.notifyDataSetChanged();

                    pairedTv.setText(result.getDevice().getAddress());

                }
            };

//    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//        // Parse the payload of the advertising packet.
//        List<ADStructure> structures = ADPayloadParser.getInstance().parse(scanRecord);
//
//        // For each AD structure contained in the advertising packet.
//        for (ADStructure structure : structures)
//        {
//            // If the ADStructure instance can be cast to IBeacon.
//            if (structure instanceof IBeacon)
//            {
//                IBeacon iBeacon = (IBeacon)structure;
//
//// (1) Proximity UUID
//                UUID uuid = iBeacon.getUUID();
//// (2) Major number
//                int major = iBeacon.getMajor();
//// (3) Minor number
//                int minor = iBeacon.getMinor();
//// (4) Tx Power
//                int power = iBeacon.getPower();
//            }
//
//
//            if (structure instanceof EddystoneUID )
//            {
//                // Eddystone UID
//                EddystoneUID es = (EddystoneUID)structure;
//
//// (1) Calibrated Tx power at 0 m.
//                int power = es.getTxPower();
//
//// (2) 10-byte Namespace ID
//                byte[] namespaceId = es.getNamespaceId();
//                String namespaceIdAsString = es.getNamespaceIdAsString();
//
//// (3) 6-byte Instance ID
//                byte[] instanceId = es.getInstanceId();
//                String instanceIdAsString = es.getInstanceIdAsString();
//
//// (4) 16-byte Beacon ID
//                byte[] beaconId = es.getBeaconId();
//                String beaconIdAsString = es.getBeaconIdAsString();
//            }
//
//
//            if (structure instanceof EddystoneURL )
//            {
//                // Eddystone URL
//                EddystoneURL es = (EddystoneURL)structure;
//// (1) Calibrated Tx power at 0 m.
//                int power = es.getTxPower();
//// (2) URL
//                URL url = es.getURL();
//            }
//
//
//            if (structure instanceof EddystoneTLM)
//            {
//                // Eddystone TLM
//                EddystoneTLM es = (EddystoneTLM)structure;
//// (1) TLM Version
//                int version = es.getTLMVersion();
//// (2) Battery Voltage
//                int voltage = es.getBatteryVoltage();
//// (3) Beacon Temperature
//                float temperature = es.getBeaconTemperature();
//// (4) Advertisement count since power-on or reboot.
//                long count = es.getAdvertisementCount();
//// (5) Elapsed time in milliseconds since power-on or reboot.
//                long elapsed = es.getElapsedTime();
//            }
//
//
//            if (structure instanceof EddystoneEID )
//            {
//                // Eddystone EID
//                EddystoneEID es = (EddystoneEID)structure;
//// (1) Calibrated Tx power at 0 m.
//                int power = es.getTxPower();
//// (2) 8-byte EID
//                byte[] eid = es.getEID();
//                String eidAsString = es.getEIDAsString();
//            }
//
//            if (structure instanceof Ucode)
//            {
//                Ucode ucode = (Ucode)structure;
//// (1) Version
//                int version = ucode.getVersion();
//// (2) Ucode (32 upper-case hex letters)
//       //         String ucode = ucode.getUcode();
//// (3) Status
//                int status = ucode.getStatus();
//// (4) The state of the battery
//                boolean low = ucode.isBatteryLow();
//// (5) Transmission interval
//                int interval = ucode.getInterval();
//// (6) Transmission power
//                int power = ucode.getPower();
//// (7) Transmission count
//                int count = ucode.getCount();
//
//            }
//
//
//
//
//        }
//
//
//    }
}

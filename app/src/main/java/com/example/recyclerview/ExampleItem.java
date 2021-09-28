package com.example.recyclerview;

import com.neovisionaries.bluetooth.ble.advertising.ADStructure;

import java.util.UUID;

public class ExampleItem extends ADStructure
{
    private String  mhashcode, mdevicename, mdeviceaddress, mdevicerssi, mdeviceadvertising;
    private int mmajor, mminor, mpower ;
    private UUID muuid ;
    int type = 0;

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }

    public ExampleItem()
    {

    }

    public ExampleItem (String hashcode, String devicename, String deviceaddress, String devicerssi, String deviceadvertising, UUID uuid, int major, int minor, int power  )
    {
        mhashcode = hashcode;
        mdevicename = devicename;
        mdeviceaddress = deviceaddress;
        mdevicerssi = devicerssi;
        mdeviceadvertising = deviceadvertising ;

        muuid = uuid;
        mmajor = major;
        mminor = minor;
        mpower = power;
    }



    public String getMdeviceadvertising() { return mdeviceadvertising; }

    public void setMdeviceadvertising(String mdeviceadvertising) { this.mdeviceadvertising = mdeviceadvertising; }

    public String getMhashcode() {
        return mhashcode;
    }

    public void setMhashcode(String mhashcode) { this.mhashcode = mhashcode; }

    public String getMdevicename() {
        return mdevicename;
    }

    public void setMdevicename(String mdevicename) { this.mdevicename = mdevicename; }

    public String getMdeviceaddress() {
        return mdeviceaddress;
    }

    public void setMdeviceaddress(String mdeviceaddress) {
        this.mdeviceaddress = mdeviceaddress;
    }

    public String getDevicerssi() {
        return mdevicerssi;
    }

    public void setDevicerssi(String devicerssi) {
        this.mdevicerssi = devicerssi;
    }

    // For ibeacon
    public int getMmajor() { return mmajor; }

    public void setMmajor(int mmajor) { this.mmajor = mmajor; }

    public int getMminor() { return mminor; }

    public void setMminor(int mminor) { this.mminor = mminor; }

    public int getMpower() { return mpower; }

    public void setMpower(int mpower) { this.mpower = mpower; }

    public UUID getMuuid() { return muuid; }

    public void setMuuid(UUID muuid) { this.muuid = muuid; }

}

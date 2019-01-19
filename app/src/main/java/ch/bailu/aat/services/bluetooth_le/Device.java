package ch.bailu.aat.services.bluetooth_le;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.annotation.RequiresApi;

import java.io.Closeable;
import java.util.List;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.AppBroadcaster;

@RequiresApi(api = 18)
public class Device extends BluetoothGattCallback implements Closeable {

    private final Executer execute = new Executer();

    private final HeartRateService heartRate;
    private final BatteryService battery = new BatteryService();
    private final CscService csc = new CscService();

    private final BluetoothDevice device;

    private final Context context;
    private BluetoothGatt gatt = null;

    private boolean connected = false;

    public Device(Context c, BluetoothDevice d) {
        device = d;
        context = c;

        heartRate = new HeartRateService(context);
    }


    public boolean isConnected() {
        return connected;
    }

    public boolean isValid() {
        return connected && (csc.isValid() || heartRate.isValid());
    }

    @Override
    public String toString() {
        String s = device.getName();

        if (csc.isValid()) {
            s = s + ", " + csc.toString();
        }

        if (heartRate.isValid()) {
            s = s+ ", " + heartRate.toString();
        }

        s = s + ", " + battery.getBatteryLevelPercentage() + "%";
        return s;
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt g, int status, int newState) {
        gatt = g;
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        } else {
            broadcast();
        }

        connected = (newState == BluetoothProfile.STATE_CONNECTED
                || newState == BluetoothProfile.STATE_CONNECTING);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(context, AppBroadcaster.BLE_DEVICE_SCANNED);
    }

    private void executeOrBroadcast(BluetoothGatt gatt) {
        if (!execute.next(gatt)) {
            broadcast();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        discover(gatt);

        if (isValid()) {
            executeOrBroadcast(gatt);
        } else {
            gatt.close();
        }
    }




    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        executeOrBroadcast(gatt);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic c) {

        heartRate.notify(c);
        csc.notify(c);

    }



    private void discover(BluetoothGatt gatt) {
        List<BluetoothGattService> list = gatt.getServices();

        for (BluetoothGattService service: list) {
            discover(service);
        }
    }


    private void discover(BluetoothGattService service) {
        List<BluetoothGattCharacteristic> list = service.getCharacteristics();

        for (BluetoothGattCharacteristic c : list) {
            heartRate.discovered(c, execute);
            battery.discovered(c, execute);
            csc.discovered(c, execute);

        }
    }


    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c,
                                     int status) {
        heartRate.read(c);
        battery.read(c);
        csc.read(c);

        executeOrBroadcast(gatt);
    }

    public String getAddress() {
        return device.getAddress();
    }

    public GpxInformation getInformation() {
        return heartRate.getInformation();
    }

    @Override
    public void close() {
        if (gatt != null) {
            gatt.close();
            connected = false;
            gatt = null;
        }
    }
}
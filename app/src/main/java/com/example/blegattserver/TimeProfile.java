package com.example.blegattserver;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.Calendar;
import java.util.UUID;

/**
 * Implementation of the Bluetooth GATT Time Profile.
 * https://www.bluetooth.com/specifications/adopted-specifications
 */
public class TimeProfile {
    private static final String TAG = TimeProfile.class.getSimpleName();

    /* Current Time Service UUID */
    public static UUID TIME_SERVICE = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");
    /* Mandatory Current Time Information Characteristic */
    public static UUID CURRENT_TIME    = UUID.fromString("00002a2b-0000-1000-8000-00805f9b34fb");

    // Adjustment Flags
    public static final byte ADJUST_NONE     = 0x0;
    public static final byte ADJUST_MANUAL   = 0x1;
    public static final byte ADJUST_TIMEZONE = 0x4;

    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * Current Time Service.
     */
    public static BluetoothGattService createTimeService() {
        BluetoothGattService service = new BluetoothGattService(TIME_SERVICE,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        // Current Time characteristic
        BluetoothGattCharacteristic currentTime = new BluetoothGattCharacteristic(CURRENT_TIME,
                //Read-only characteristic, supports notifications
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);

        service.addCharacteristic(currentTime);

        return service;
    }

    /**
     * Construct the field values for a Current Time characteristic
     * from the given epoch timestamp and adjustment reason.
     */
    public static byte[] getExactTime(long timestamp, byte adjustReason) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timestamp);

        byte[] field = new byte[] { (byte)(time.getTimeInMillis() % 2)};

        /*byte[] field = new byte[10];

        // Year
        int year = time.get(Calendar.YEAR);
        field[0] = (byte) (year & 0xFF);
        field[1] = (byte) ((year >> 8) & 0xFF);
        // Month
        field[2] = (byte) (time.get(Calendar.MONTH) + 1);
        // Day
        field[3] = (byte) time.get(Calendar.DATE);
        // Hours
        field[4] = (byte) time.get(Calendar.HOUR_OF_DAY);
        // Minutes
        field[5] = (byte) time.get(Calendar.MINUTE);
        // Seconds
        field[6] = (byte) time.get(Calendar.SECOND);
        // Day of Week (1-7)
        field[7] = getDayOfWeekCode(time.get(Calendar.DAY_OF_WEEK));
        // Fractions256
        field[8] = (byte) (time.get(Calendar.MILLISECOND) / 256);

        field[9] = adjustReason;*/

        return field;
    }

    /* Bluetooth Weekday Codes */
    private static final byte DAY_UNKNOWN = 0;
    private static final byte DAY_MONDAY = 1;
    private static final byte DAY_TUESDAY = 2;
    private static final byte DAY_WEDNESDAY = 3;
    private static final byte DAY_THURSDAY = 4;
    private static final byte DAY_FRIDAY = 5;
    private static final byte DAY_SATURDAY = 6;
    private static final byte DAY_SUNDAY = 7;

    /**
     * Convert a {@link Calendar} weekday value to the corresponding
     * Bluetooth weekday code.
     */
    private static byte getDayOfWeekCode(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return DAY_MONDAY;
            case Calendar.TUESDAY:
                return DAY_TUESDAY;
            case Calendar.WEDNESDAY:
                return DAY_WEDNESDAY;
            case Calendar.THURSDAY:
                return DAY_THURSDAY;
            case Calendar.FRIDAY:
                return DAY_FRIDAY;
            case Calendar.SATURDAY:
                return DAY_SATURDAY;
            case Calendar.SUNDAY:
                return DAY_SUNDAY;
            default:
                return DAY_UNKNOWN;
        }
    }
}
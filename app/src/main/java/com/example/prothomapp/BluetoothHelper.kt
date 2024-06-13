package com.example.prothomapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class BluetoothHelper {
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    companion object {
        private const val TAG = "BluetoothHelper"
        private val UUID_MONOCLE: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Standard UUID for SPP
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceName: String): Boolean {
        val device = bluetoothAdapter?.bondedDevices?.find { it.name == deviceName }
        if (device == null) {
            Log.e(TAG, "Device not found")
            return false
        }

        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID_MONOCLE)
            bluetoothSocket?.connect()
            outputStream = bluetoothSocket?.outputStream
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error connecting to device", e)
            false
        }
    }

    fun sendData(data: String) {
        try {
            outputStream?.write(data.toByteArray())
            outputStream?.flush()
        } catch (e: IOException) {
            Log.e(TAG, "Error sending data", e)
        }
    }

    fun disconnect() {
        try {
            outputStream?.close()
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing connection", e)
        }
    }
}
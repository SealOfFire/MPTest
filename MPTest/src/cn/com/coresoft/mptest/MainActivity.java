package cn.com.coresoft.mptest;

import java.util.HashMap;
import java.util.Iterator;

import jp.ksksue.driver.serial.FTDriver;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private FTDriver mSerial;
	private AirQualityIndex mAQI;
	private TextView mTvSerial;// ��ʾ������Ϣ���ı���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ��ʾ������Ϣ���ı���
		this.mTvSerial = (TextView) findViewById(R.id.showMessage);
		this.mTvSerial.setText(new AirQualityIndex().toString());
		IntentFilter filter = new IntentFilter(Constant.ACTION_USB_PERMISSION);
		registerReceiver(new UsbBroadcastReceiver(), filter);

		// this.test();
		Log.d("test", "start");
		this.readUsb();
		Log.d("test", "end");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** ��USB�豸��ȡ���� */
	public void readUsb() {
		//
		this.mAQI = new AirQualityIndex();
		// get service
		this.mSerial = new FTDriver(
				(UsbManager) getSystemService(Context.USB_SERVICE));

		// �����ȡusb�豸��Ȩ��
		PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(Constant.ACTION_USB_PERMISSION), 0);
		mSerial.setPermissionIntent(permissionIntent);

		if (mSerial.begin(FTDriver.BAUD9600)) {
			// ��ȡ�����߳�
			new Thread(new ReadUsbRunnable(this.mSerial, this.mAQI,
					this.mTvSerial)).start();
		}
	}

	public void openDevice(View view) {
		// if (!this.mSerial.isConnected()) {
		this.readUsb();
		// }
	}

	/** ����usb�ӿ� */
	public void test() {
		Log.d("test", "start");

		// usb�豸����
		UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		// ö��usb�豸
		HashMap<String, UsbDevice> devices = usbManager.getDeviceList();
		// ����usb�豸
		UsbDevice usbDevice = null;
		Iterator<UsbDevice> iterator = devices.values().iterator();
		while (iterator.hasNext()) {
			usbDevice = iterator.next();
		}
		// ѯ���û��Ƿ���Է���usb�豸
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
				Constant.ACTION_USB_PERMISSION), 0);
		usbManager.requestPermission(usbDevice, pi);
		// ��ȡusb�豸��pid��vid
		Integer pid = usbDevice.getProductId();
		Integer vid = usbDevice.getVendorId();
		Log.d("pid", pid.toString());
		Log.d("vid", vid.toString());

		// ����usb����
		// UsbDeviceConnection connection = usbManager.openDevice(usbDevice);

		Log.d("test", "end");
	}
}

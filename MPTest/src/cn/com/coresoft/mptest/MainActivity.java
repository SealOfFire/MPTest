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
	private TextView mTvSerial;// 显示文字消息的文本框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 显示文字消息的文本框
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

	/** 从USB设备读取数据 */
	public void readUsb() {
		//
		this.mAQI = new AirQualityIndex();
		// get service
		this.mSerial = new FTDriver(
				(UsbManager) getSystemService(Context.USB_SERVICE));

		// 申请读取usb设备的权限
		PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0,
				new Intent(Constant.ACTION_USB_PERMISSION), 0);
		mSerial.setPermissionIntent(permissionIntent);

		if (mSerial.begin(FTDriver.BAUD9600)) {
			// 读取数据线程
			new Thread(new ReadUsbRunnable(this.mSerial, this.mAQI,
					this.mTvSerial)).start();
		}
	}

	public void openDevice(View view) {
		// if (!this.mSerial.isConnected()) {
		this.readUsb();
		// }
	}

	/** 测试usb接口 */
	public void test() {
		Log.d("test", "start");

		// usb设备管理
		UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		// 枚举usb设备
		HashMap<String, UsbDevice> devices = usbManager.getDeviceList();
		// 遍历usb设备
		UsbDevice usbDevice = null;
		Iterator<UsbDevice> iterator = devices.values().iterator();
		while (iterator.hasNext()) {
			usbDevice = iterator.next();
		}
		// 询问用户是否可以访问usb设备
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(
				Constant.ACTION_USB_PERMISSION), 0);
		usbManager.requestPermission(usbDevice, pi);
		// 获取usb设备的pid和vid
		Integer pid = usbDevice.getProductId();
		Integer vid = usbDevice.getVendorId();
		Log.d("pid", pid.toString());
		Log.d("vid", vid.toString());

		// 建立usb连接
		// UsbDeviceConnection connection = usbManager.openDevice(usbDevice);

		Log.d("test", "end");
	}
}

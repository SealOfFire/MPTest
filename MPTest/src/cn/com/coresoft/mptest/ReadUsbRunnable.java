package cn.com.coresoft.mptest;

import jp.ksksue.driver.serial.FTDriver;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class ReadUsbRunnable implements Runnable {

	private FTDriver mSerial;
	private AirQualityIndex mAQI;
	private TextView mTextView;
	private Handler mHandler = new Handler();

	public ReadUsbRunnable(FTDriver serial, AirQualityIndex aqi,
			TextView textView) {
		this.mSerial = serial;
		this.mAQI = aqi;
		this.mTextView = textView;
	}

	@Override
	public void run() {
		int len;
		byte[] rbuf = new byte[4096];

		for (;;) {
			len = mSerial.read(rbuf);
			rbuf[len] = 0;

			if (len > 0) {
				Log.d("ReadUsbRunnable", "Length : " + len);
				for (int i = 0; i < len; ++i) {
					Log.d("ReadUsbRunnable", "Read  Data[" + i + "] : "
							+ rbuf[i]);
				}

				// ¼ÆËãÊý¾Ý
				mAQI.calculate(rbuf, len);
				mHandler.post(new Runnable() {
					public void run() {
						mTextView.setText(mAQI.toString());
						Log.d("mAQI", mAQI.toString());
					}
				});
				// this.mTextView.setText(this.mAQI.toString());
			}
		}
	}
}

package cn.com.coresoft.mptest;

/** 计算空气质量 */
public class AirQualityIndex {

	public float aqi;
	public float f_c1 = 0, f_c25 = 0, f_t = 0;

	public void calculate(byte[] value, int length) {
	}

	public void updateSample(int c1, int c25, int m) {
	}

	public float[] avrSample(float c1, float c25) {
		return new float[] { c1, c25 };
	}

	public String toString() {
		return "";
	}
}

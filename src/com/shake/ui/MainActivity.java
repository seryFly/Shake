package com.shake.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

/**
 * 
 * @author le.kang
 * 
 */
public class MainActivity extends Activity {
    private SoundPlayer player;
    private ImageView up;
    private ImageView down;

    private SensorManager sm;
    private Sensor acceleromererSensor;
    private SensorEventListener acceleromererListener;

    private long curTime;
    private long preShakeTime = -1;
    private Vibrator vibrator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	init();
	up = (ImageView) findViewById(R.id.shake_up);
	down = (ImageView) findViewById(R.id.shake_down);
    }

    private void init() {
	player = new SoundPlayer(this);
	player.loadSound();
	player.playSound(R.raw.sound);

	sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

	vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

	acceleromererListener = new SensorEventListener() {
	    public void onAccuracyChanged(Sensor arg0, int arg1) {

	    }

	    public void onSensorChanged(SensorEvent event) {
		curTime = System.currentTimeMillis();
		// 传感器信息改变时执行该方法
		float[] values = event.values;
		float x = values[0]; // x轴方向的重力加速度，向右为正
		float y = values[1]; // y轴方向的重力加速度，向前为正
		float z = values[2]; // z轴方向的重力加速度，向上为正
		// 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
		int medumValue = 14;
		if (Math.abs(x) > medumValue || Math.abs(y) > medumValue
			|| Math.abs(z) > medumValue) {
		    startAnim();
		    vibrator.vibrate(200);
		    shakeDo(curTime);
		}
	    }

	};
	sm.registerListener(acceleromererListener, acceleromererSensor,
		SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * 动画时间可调整
     */
    public void startAnim() { // 定义摇一摇动画动画
	AnimationSet animup = new AnimationSet(true);
	TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
		-0.5f);
	mytranslateanimup0.setDuration(1000);
	TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
		+0.5f);
	mytranslateanimup1.setDuration(1000);
	mytranslateanimup1.setStartOffset(1000);
	animup.addAnimation(mytranslateanimup0);
	animup.addAnimation(mytranslateanimup1);
	up.startAnimation(animup);

	AnimationSet animdn = new AnimationSet(true);
	TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
		+0.5f);
	mytranslateanimdn0.setDuration(1000);
	TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
		Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
		-0.5f);
	mytranslateanimdn1.setDuration(1000);
	mytranslateanimdn1.setStartOffset(1000);
	animdn.addAnimation(mytranslateanimdn0);
	animdn.addAnimation(mytranslateanimdn1);
	down.startAnimation(animdn);
    }

    private void shakeDo(long time) {
	long delta = time - preShakeTime;
	if (delta > 2000) {
	    player.playSound(R.raw.sound);
	    preShakeTime = time;
	}
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	sm.unregisterListener(acceleromererListener);
    }
}

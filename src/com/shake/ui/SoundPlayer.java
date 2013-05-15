package com.shake.ui;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private Context context;
    private SoundPool soundPool;
    private HashMap<Integer, Integer> map;

    public SoundPlayer(Context context) {
	this.context = context;
	map = new HashMap<Integer, Integer>();
	soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 100);
    }

    public void loadSound() {
	addSound(R.raw.sound);
    }

    public void addSound(int resId) {
	int index = soundPool.load(context, resId, 0);
	map.put(resId, index);
    }

    public void playSound(int resId) {
	soundPool.play(map.get(resId), 1, 1, 0, 0, 1);
    }
}

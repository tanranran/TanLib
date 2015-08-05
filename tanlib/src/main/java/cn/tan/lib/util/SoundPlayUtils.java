package cn.tan.lib.util;

/**
 * Created by tan on 2015/8/3.
 */

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundPlayUtils {
    public static AudioAttributes attr = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME) // 设置音效使用场景
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC) // 设置音效的类型
            .build();
    public static SoundPool mSoundPlayer = new SoundPool.Builder()
            .setAudioAttributes(attr) // 设置音效池的属性
            .setMaxStreams(10) // 设置最多可容纳10个音频流
            .build();  // ①
    public static SoundPlayUtils soundPlayUtils;
    // 上下文
    static Context mContext;

    /**
     * 初始化
     *
     * @param context
     */
    public static SoundPlayUtils init(Context context) {
        if (soundPlayUtils == null) {
            soundPlayUtils = new SoundPlayUtils();
        }

        // 初始化声音
        mContext = context;

//        mSoundPlayer.load(mContext, R.raw.beng, 1);// 1
//        mSoundPlayer.load(mContext, R.raw.click, 1);// 2
//        mSoundPlayer.load(mContext, R.raw.diang, 1);// 3
//        mSoundPlayer.load(mContext, R.raw.ding, 1);// 4
//        mSoundPlayer.load(mContext, R.raw.gone, 1);// 5
//        mSoundPlayer.load(mContext, R.raw.popup, 1);// 6
//        mSoundPlayer.load(mContext, R.raw.water, 1);// 7
//        mSoundPlayer.load(mContext, R.raw.ying, 1);// 8

        return soundPlayUtils;
    }

    /**
     * 播放声音
     *
     * @param soundID
     */
    public static void play(int soundID) {
        mSoundPlayer.play(soundID, 1, 1, 0, 0, 1);
    }

}

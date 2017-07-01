package com.inetgoes.kfqbrokers.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by czz on 2015/12/9.
 * 播放订单提示音
 */
public class SoundPoolUtil {
    private static SoundPool pool;
    private static SoundPoolUtil soundPoolUtil;

    private SoundPoolUtil(){
        //指定声音池的最大音频流数目为10，声音品质为5
        pool = new SoundPool(10, AudioManager.STREAM_NOTIFICATION, 5);
    }

    public static SoundPoolUtil getInstance(){
        if(null == soundPoolUtil){
            synchronized (SoundPoolUtil.class){
                if (null == soundPoolUtil){
                    soundPoolUtil = new SoundPoolUtil();
                }
            }
        }
        return soundPoolUtil;
    }

    public void play(Context context,int rawId){
        //载入音频流，返回在池中的id
        final int sourceid = pool.load(context.getApplicationContext(),rawId,0);
        //播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度
        pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sourceid, 2, 2, 0, 0, 1);
            }
        });
    }

    public void play(final Context context,int raw1Id, final int raw2Id){
        //载入音频流，返回在池中的id
        final int sourceid = pool.load(context.getApplicationContext(),raw1Id,0);
        //播放音频，第二个参数为左声道音量;第三个参数为右声道音量;第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;第六个参数为速率，速率最低0.5最高为2，1代表正常速度
        pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sourceid, 2, 2, 0, 0, 1);
                final int twoid = pool.load(context,raw2Id,0);
                pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        soundPool.play(twoid, 2, 2, 0, 0, 1);
                    }
                });
            }
        });
    }

}

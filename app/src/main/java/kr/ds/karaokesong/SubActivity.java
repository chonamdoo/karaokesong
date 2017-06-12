package kr.ds.karaokesong;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kr.ds.handler.ListHandler;
import kr.ds.utils.DsObjectUtils;
import kr.ds.view.VisualizerView;

/**
 * Created by Administrator on 2017-06-12.
 */
public class SubActivity extends BaseActivity{
    private ListHandler mSavedata;
    public static final String API_KEY = "AIzaSyAkfPX3uF_hALFjYOUhwlhewgaqewl08XE";

    private VisualizerView mVisualizerView;
    private VisualizerView mVisualizerView2;
    private TimerTask second;

    private final Handler handler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            mSavedata = (ListHandler) savedInstanceState.getParcelable("data");
        }else{
            mSavedata = (ListHandler) getIntent().getParcelableExtra("data");
        }

        setContentView(R.layout.activty_sub);


        second = new TimerTask() {
            @Override
            public void run() {
                Update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(second, 0, 500);


        mVisualizerView = (VisualizerView) findViewById(R.id.visualizer);

        ViewTreeObserver observer = mVisualizerView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mVisualizerView.setBaseY(mVisualizerView.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mVisualizerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mVisualizerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });


        mVisualizerView2 = (VisualizerView) findViewById(R.id.visualizer2);

        ViewTreeObserver observer2 = mVisualizerView2.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mVisualizerView2.setBaseY(mVisualizerView2.getHeight());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mVisualizerView2.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mVisualizerView2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });





        FragmentManager fm = getFragmentManager();
        String tag = YouTubePlayerFragment.class.getSimpleName();
        YouTubePlayerFragment playerFragment = (YouTubePlayerFragment) fm.findFragmentByTag(tag);
        if (playerFragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            playerFragment = YouTubePlayerFragment.newInstance();
            ft.add(R.id.content_frame, playerFragment, tag);
            ft.commit();
        }

        playerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(!DsObjectUtils.isEmpty(mSavedata.getVideo_id())){
                    youTubePlayer.loadVideo(mSavedata.getVideo_id());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    protected void Update() {
        mVisualizerView.receive(new Random().nextInt(100)+1);
        mVisualizerView2.receive(new Random().nextInt(100)+1);
    }
}

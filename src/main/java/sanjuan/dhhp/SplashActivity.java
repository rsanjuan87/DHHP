package sanjuan.dhhp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private ImageView wave;
    private AnimatorSet animatorSet;
    private ImageView wave1;
    private AnimatorSet animatorSet1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        wave = (ImageView) findViewById(R.id.wave);
        ObjectAnimator maskXAnimator = ObjectAnimator.ofFloat(wave, "x", -1000, 1000);
        maskXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        maskXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        maskXAnimator.setDuration(9000);
        maskXAnimator.setStartDelay(0);

        wave1 = (ImageView) findViewById(R.id.wave1);
        ObjectAnimator maskXAnimator1 = ObjectAnimator.ofFloat(wave1, "x", 1000, -1000);
        maskXAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        maskXAnimator1.setRepeatMode(ValueAnimator.REVERSE);
        maskXAnimator1.setDuration(10000);
        maskXAnimator1.setStartDelay(0);

        // now play both animations together
        animatorSet = new AnimatorSet();
        animatorSet.play(maskXAnimator);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                textView.setSinking(false);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    wave.postInvalidate();
                } else {
                    wave.postInvalidateOnAnimation();
                }

                animatorSet = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet1 = new AnimatorSet();
        animatorSet1.play(maskXAnimator1);
        animatorSet1.setInterpolator(new LinearInterpolator());
        animatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                textView.setSinking(false);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    wave.postInvalidate();
                } else {
                    wave.postInvalidateOnAnimation();
                }

                animatorSet = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animatorSet.start();
        animatorSet1.start();


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finished();
            }
        }, 5000);
        super.onPostCreate(savedInstanceState);
    }

    private void finished() {
        startActivity(new Intent(SplashActivity.this, Setting_Darta.class));
        finish();
    }
}

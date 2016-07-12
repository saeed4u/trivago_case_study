package saeed.trivago.casestudy.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import saeed.trivago.casestudy.R;

/**
 * Created by saeed on 12/07/2016.
 */
public class Utilities {

    public static void slideViewFromRight(final View left, final View right, long duration) {
        ViewGroup parent = (ViewGroup) right.getParent();
        int distance = parent.getWidth() - right.getLeft();
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                ObjectAnimator.ofFloat(right, "alpha", 0, 1),
                ObjectAnimator.ofFloat(right, "translationX", distance, 0),
                ObjectAnimator.ofFloat(left, "alpha", 1, 0),
                ObjectAnimator.ofFloat(left, "translationX", 0, -left.getRight()
                ));
        animatorSet.setDuration(duration);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                right.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                left.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animatorSet.start();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static void updateDisplayForError(final Context context) {
        new MaterialDialog.Builder(context)
                .title("Error")
                .icon(ContextCompat.getDrawable(context, R.drawable.ic_error_white_48dp))
                .content("It seems you have no Internet. Please check your Internet connection and try again.")
                .alwaysCallSingleChoiceCallback()
                .positiveText(R.string.okay)
                .contentGravity(GravityEnum.CENTER)
                .positiveColorRes(R.color.colorAccent)
                .show();
    }

}

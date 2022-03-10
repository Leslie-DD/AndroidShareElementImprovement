/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.activityscenetransitionbasic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = "DetailActivity";

    public static final String EXTRA_PARAM_ID = "detail:_id";
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private Item mItem;
    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        mItem = Item.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));
        mHeaderImageView = findViewById(R.id.imageview_header);
        mHeaderTitle = findViewById(R.id.textview_title);

        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE);
        configEnterExitAnimation(true);
        postponeEnterTransition();

        loadItem();
    }

    private void loadItem() {
        mHeaderTitle.setText(getString(R.string.image_header, mItem.getName(), mItem.getAuthor()));
        if (addTransitionListener()) {
            loadThumbnail();
        } else {
            loadFullSizeImage();
        }
        startPostponedEnterTransition();
    }

    private void loadThumbnail() {
        Picasso.with(mHeaderImageView.getContext())
                .load(mItem.getThumbnailUrl())
                .noFade()
                .into(mHeaderImageView);
    }

    private void loadFullSizeImage() {
        Picasso.with(mHeaderImageView.getContext())
                .load(mItem.getPhotoUrl())
                .noFade()
                .noPlaceholder()
                .into(mHeaderImageView);
    }

    @Override
    public void finishAfterTransition() {
        configEnterExitAnimation(false);
        super.finishAfterTransition();
    }

    private boolean addTransitionListener() {
        final Transition transition = getWindow().getSharedElementEnterTransition();
        if (transition != null) {
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    loadFullSizeImage();
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                }

                @Override
                public void onTransitionResume(Transition transition) {
                }
            });
            return true;
        }

        return false;
    }

    private void configEnterExitAnimation(boolean isEnter) {
        TextviewTransition textviewTransition = new TextviewTransition(isEnter);
        textviewTransition.addTarget(R.id.textview_title);
        ChangeBounds textviewChangeBounds = new ChangeBounds();
        textviewChangeBounds.addTarget(R.id.textview_title);

        ChangeBounds imageChangeBounds = new ChangeBounds();
        imageChangeBounds.addTarget(VIEW_NAME_HEADER_IMAGE);

        TransitionSet set = new TransitionSet()
                .addTransition(textviewTransition)
                .addTransition(textviewChangeBounds)
                .addTransition(imageChangeBounds)
                .setDuration(1500);

        this.getWindow().setSharedElementEnterTransition(set);
    }

    class TextviewTransition extends Transition {
//        private static final String TAG = "TRANSITION";
//
//        private static final String TEXT_SIZE = "TextviewTransition::textSize";
//        private static final String TEXT_COLOR = "TextviewTransition::textColor";
//
        private boolean mIsEnter = true;

        public TextviewTransition(boolean isEnter) {
            mIsEnter = isEnter;
        }

        @Override
        public void captureStartValues(TransitionValues transitionValues) {
//            if (!(transitionValues.view instanceof TextView)) return;
//            Log.e(TAG, "captureStartValues "
//                            + ((TextView) transitionValues.view).getTextSize() + ", "
//                            + Integer.toHexString(((TextView) transitionValues.view).getCurrentTextColor()));
//            transitionValues.values.put(TEXT_SIZE, ((TextView) transitionValues.view).getTextSize());
//            transitionValues.values.put(TEXT_COLOR, ((TextView) transitionValues.view).getCurrentTextColor());
        }

        @Override
        public void captureEndValues(TransitionValues transitionValues) {
//            if (!(transitionValues.view instanceof TextView)) return;
//            Log.e(TAG, "captureEndValues "
//                    + ((TextView) transitionValues.view).getTextSize() + ", "
//                    + Integer.toHexString(((TextView) transitionValues.view).getCurrentTextColor()));
//            transitionValues.values.put(TEXT_SIZE, ((TextView) transitionValues.view).getTextSize());
//            transitionValues.values.put(TEXT_COLOR, ((TextView) transitionValues.view).getCurrentTextColor());
        }

        @Override
        public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
            if (startValues == null || endValues == null) {
                return null;
            }
            return createEnterAnimator(startValues, endValues);
        }

        private Animator createEnterAnimator(TransitionValues startValues, TransitionValues endValues) {
            ObjectAnimator textSizeAnimator = ObjectAnimator.ofFloat((TextView) startValues.view,
                    new Property<TextView, Float>(Float.class, "textSize") {
                        @Override
                        public void set(TextView object, Float value) {
                            object.setTextSize(TypedValue.COMPLEX_UNIT_PX, value);
                        }

                        @Override
                        public Float get(TextView object) {
                            return object.getTextSize();
                        }
                    },
//                    (float) startValues.values.get(TEXT_SIZE),
//                    (float) endValues.values.get(TEXT_SIZE)
                    mIsEnter ? getResources().getDimensionPixelOffset(R.dimen.text_size_start) :
                            getResources().getDimensionPixelOffset(R.dimen.text_size_end),
                    mIsEnter ? getResources().getDimensionPixelOffset(R.dimen.text_size_end) :
                            getResources().getDimensionPixelOffset(R.dimen.text_size_start)

            );

            ObjectAnimator textColorAnimator = ObjectAnimator.ofArgb((TextView) startValues.view,
                    new Property<TextView, Integer>(Integer.class, "textColor") {
                        @Override
                        public void set(TextView object, Integer value) {
                            object.setTextColor(value);
                        }

                        @Override
                        public Integer get(TextView object) {
                            return object.getCurrentTextColor();
                        }
                    },
//                    (int) startValues.values.get(TEXT_COLOR),
//                    (int) endValues.values.get(TEXT_COLOR)
                    mIsEnter ? getResources().getColor(R.color.start_color, null) :
                            getResources().getColor(R.color.end_color, null),
                    mIsEnter ? getResources().getColor(R.color.end_color, null) :
                            getResources().getColor(R.color.start_color, null)
            );

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(textSizeAnimator, textColorAnimator);
            return animatorSet;
        }
    }

}

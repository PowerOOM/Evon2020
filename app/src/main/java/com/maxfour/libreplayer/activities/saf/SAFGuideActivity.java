package com.maxfour.libreplayer.activities.saf;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.maxfour.libreplayer.R;

public class SAFGuideActivity extends IntroActivity {

    public static final int REQUEST_CODE_SAF_GUIDE = 98;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonCtaVisible(false);
        setButtonNextVisible(false);
        setButtonBackVisible(false);

        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        String title = String.format(getString(R.string.saf_guide_slide1_title), getString(R.string.app_name));

        addSlide(new SimpleSlide.Builder()
                .title(title)
                .description(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1
                        ? R.string.saf_guide_slide1_description_before_o : R.string.saf_guide_slide1_description)
                .image(R.drawable.saf_guide_1)
                .background(R.color.md_deep_purple_300)
                .backgroundDark(R.color.md_deep_purple_400)
                .layout(R.layout.fragment_simple_slide_large_image)
                .build());
        addSlide(new Simpl
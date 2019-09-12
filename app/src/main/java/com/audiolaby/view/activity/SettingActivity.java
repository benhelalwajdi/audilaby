package com.audiolaby.view.activity;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.audiolaby.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;

@EActivity(R.layout.activity_settings)
public class SettingActivity extends BasePlayerActivity {


    @ViewById(R.id.allow_notification)
    SwitchCompat allowNotification;

    @ViewById(R.id.picture1)
    CircleImageView picture1;
    @ViewById(R.id.picture2)
    TextView picture2;
    @ViewById(R.id.name)
    TextView name;


    @AfterViews
    void afterViewsInjection() {
        super.afterViewsInjection();

        updateUser();
        this.allowNotification.setChecked(appPref.allowNotification().get());
        this.allowNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                appPref.allowNotification().put(isChecked);
            }
        });

    }


    void updateUser() {
        user = libraryDAO.getUser();

        if (user != null) {

            try {
                Picasso.with(this).load(user.getImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        . placeholder((int) R.drawable.empty)
                        .error((int) R.drawable.empty)
                        .into(this.picture1);
                picture2.setVisibility(View.GONE);
                picture1.setVisibility(View.VISIBLE);
            } catch (Exception e) {

                picture2.setVisibility(View.VISIBLE);
                picture1.setVisibility(View.GONE);
            }

            name.setText(user.getFirstName() + " " + user.getLastName());
        }

    }
}

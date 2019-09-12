package com.audiolaby.view.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.audiolaby.Audiolaby;
import com.audiolaby.R;
import com.audiolaby.controller.AccountController;
import com.audiolaby.controller.enumeration.Gender;
import com.audiolaby.controller.enumeration.RegisterType;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.request.SignUpRequest;
import com.audiolaby.controller.request.SignUpSocialRequest;
import com.audiolaby.controller.response.AccountResponse;
import com.audiolaby.otto.events.UserLoggedInEvent;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.User;
import com.audiolaby.util.ConnectivityUtils;
import com.audiolaby.util.Utils;
import com.audiolaby.view.player.playback.PlaybackControlsFragment;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.audiolaby.util.Utils.getAppVersion;
import static com.audiolaby.util.Utils.getCountry;
import static com.audiolaby.util.Utils.getDeviceVersion;
import static com.audiolaby.util.Utils.getUserCountry;


@EActivity(R.layout.activity_sign_up)
public class SignUpActivity extends BaseActivity implements com.tsongkha.spinnerdatepicker.DatePickerDialog.OnDateSetListener {

    @Bean
    AccountController accountController;
    @App
    Audiolaby audiolaby;
    @Bean
    LibraryDAO libraryDAO;

    Boolean isFirst = true;
    Boolean isRegister = false;

    @ViewById(R.id.flipper)
    ViewFlipper flipper;


    @ViewById(R.id.email)
    EditText email;
    @ViewById(R.id.password_layout)
    TextInputLayout passwordLayout;
    @ViewById(R.id.confirm_layout)
    TextInputLayout confirmLayout;
    @ViewById(R.id.password)
    EditText password;
    @ViewById(R.id.confirm_password)
    EditText confirmPassword;
    @ViewById(R.id.first_name)
    EditText firstName;
    @ViewById(R.id.last_name)
    EditText lastName;
    @ViewById(R.id.checkBox)
    CheckBox checkBox;


    @ViewById(R.id.email_social)
    EditText emailSocial;
    @ViewById(R.id.email_social_layout)
    TextInputLayout emailSocialLayout;
    @ViewById(R.id.birthday)
    TextView birthday;
    @ViewById(R.id.male)
    RadioButton male;
    @ViewById(R.id.female)
    RadioButton female;
    @ViewById(R.id.picture)
    CircleImageView picture;
    @ViewById(R.id.picture_square)
    ImageView pictureSquare;


    @ViewById(R.id.info)
    ScrollView info;


    @ViewById(R.id.user_name)
    TextView userName;
    @ViewById(R.id.user_email)
    TextView userEmail;
    @ViewById(R.id.user_picture)
    CircleImageView userPicture;
    @ViewById(R.id.user_country)
    CircleImageView user_country;
    @ViewById(R.id.confirm)
    Button confirm;


    @ViewById(R.id.register)
    Button register;

    String messageError = "";

    @Extra
    RegisterType registerType;
    @Extra
    String extraEmail;
    @Extra
    String extraFirstName;
    @Extra
    String extraLastName;
    @Extra
    String extraPicture;
    @Extra
    String extraToken;


    @Extra
    Boolean intro;


    private CallbackManager manager;
    private ProgressDialog progressDialog;
    @StringRes(R.string.signup_in)
    String signupIn;

    @StringRes(R.string.error_must_agree)
    String errorMustAgree;
    @StringRes(R.string.error_confirm_password)
    String errorConfirmPassword;
    @StringRes(R.string.error_invalid_password)
    String errorInvalidPassword;
    @StringRes(R.string.error_invalid_email)
    String errorInvalidEmail;
    @StringRes(R.string.error_field_required)
    String errorFieldRequired;
    @StringRes(R.string.error_connection)
    String errorConnection;
    @StringRes(R.string.error_try_again)
    String tryAgain;


    private User user;
    Boolean deny = false;

    @Click({R.id.condition})
    void condition() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://appcdn.media/privacy/?app=Audiolaby")));
    }


    public SignUpActivity() {
        this.manager = Factory.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //fanlive.mustDie(SignUpActivity.this);

    }


    public void onBackPressed() {
        SignInActivity_.intent((Context) SignUpActivity.this).intro(intro).start();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Click({R.id.picture})
    void onPictureClicked() {
        if (isStoragePermissionGranted()) {
            isRegister = false;
            EasyImage.openChooserWithGallery(this, "", 0);
        }
    }

    @Click({R.id.birthday_layout})
    void onDate() {
        Calendar calendar = Calendar.getInstance();
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                SignUpActivity.this,
//                this,
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.show();


        new SpinnerDatePickerDialogBuilder()
                .context(SignUpActivity.this)
                .callback(this)
                .defaultDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .maxDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
                .spinnerTheme(R.style.DatePickerSpinner)
                .build()
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Click({R.id.register})
    void onRegister() {

        Utils.hideKeyboard(SignUpActivity.this);

        if (validator2()) {
            isRegister = true;
            if (deny) {
                isRegister = false;
                executeSignUp();
            } else if (isStoragePermissionGranted()) {
                isRegister = false;
                executeSignUp();
            }

        } else
            showError(messageError);
    }

    @Click({R.id.next})
    void onNext() {

        Utils.hideKeyboard(SignUpActivity.this);

        if (validator()) {
            // emailSocial.setText(email.getText().toString());
            // flipper.showNext();
            executeSignUp();
        } else
            showError(messageError);
    }


    @Click({R.id.confirm})
    void confirm() {

        this.user.save();
        EventBus.getDefault().postSticky(new UserLoggedInEvent(this.user));

        if (intro) {
            MainActivity_.intent((Context) SignUpActivity.this).start();
            finish();
        } else
            finish();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @AfterViews
    void afterViewsInjection() {
        super.afterViewsInjection();
        this.flipper.setInAnimation(SignUpActivity.this, R.anim.transition_in_left);
        EasyImage.configuration(this).setImagesFolderName("audiolaby");

        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        if (registerType != RegisterType.email) {
            firstName.setText(extraFirstName);
            lastName.setText(extraLastName);
            passwordLayout.setVisibility(View.GONE);
            confirmLayout.setVisibility(View.GONE);

            if (extraEmail.isEmpty())
                emailSocialLayout.setVisibility(View.VISIBLE);
            else {
                emailSocialLayout.setVisibility(View.GONE);
                emailSocial.setText(extraEmail);
            }

            if (!extraPicture.equals("empty")) {
                Picasso.with(SignUpActivity.this).load(extraPicture)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder((int) R.drawable.ic_anonymou).error((int) R.drawable.ic_anonymou).into(picture);

                Picasso.with(SignUpActivity.this).load(extraPicture)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder((int) R.drawable.ic_anonymou).error((int) R.drawable.ic_anonymou).into(pictureSquare);

            }


            flipper.showNext();

        } else
            dimissProgress();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Picasso.with(SignUpActivity.this).load(imageFile)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder((int) R.drawable.ic_anonymou).error((int) R.drawable.ic_anonymou).into(picture);
                Picasso.with(SignUpActivity.this).load(imageFile)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder((int) R.drawable.ic_anonymou).error((int) R.drawable.ic_anonymou).into(pictureSquare);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(SignUpActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Background(id = "longtask")
    void executeSignUp() {
        showProgress();
        AccountResponse response = null;
        if (registerType == RegisterType.email) {

            response = this.accountController.signUp(new SignUpRequest(
                    email.getText().toString(),
                    password.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    getUserCountry(this),
                    "android",
                    getDeviceVersion(this),
                    getAppVersion(this),
                    FirebaseInstanceId.getInstance().getToken(),
                    registerType.name(),
                    Gender.male.name(),
                    "2000-20-20"));
        } else {

            response = this.accountController.signUpSocial(new SignUpSocialRequest(
                    emailSocial.getText().toString(),
                    extraToken,
                    extraFirstName,
                    extraLastName,
                    getUserCountry(this),
                    "android",
                    getDeviceVersion(this),
                    getAppVersion(this),
                    FirebaseInstanceId.getInstance().getToken(),
                    registerType.name(),
                    Gender.male.name(),
                    "2000-20-20"));
        }


        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                executeSignUp();
                break;
            case "handler":
                handleResponse(response);
                break;
            default:
                showError(handlerResponse);
//                if (registerType != RegisterType.email) {
//                    SignInActivity_.intent((Context) SignUpActivity.this).intro(intro).start();
//                    finish();
//                }

        }
    }

    @UiThread
    void handleResponse(AccountResponse accountResponse) {

        this.user = accountResponse.getUser();
        this.user.save();
        EventBus.getDefault().postSticky(new UserLoggedInEvent(this.user));
        this.progressDialog.dismiss();
        this.userName.setText(this.user.getFirstName() + " " + this.user.getLastName());
        this.userEmail.setText(this.user.getEmail());
        try {
            Picasso.with(this).load(this.user.getImage())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).placeholder((int) R.drawable.ic_anonymou).error((int) R.drawable.ic_anonymou).into(this.userPicture);
        } catch (Exception e) {
        }

        flipper.showNext();
        flipper.showNext();
    }

    @UiThread
    void showProgress() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage(this.signupIn);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    @UiThread
    void showError(String message) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        Snackbar.make(this.flipper, (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }


    public Boolean validator() {
        Boolean isValid = true;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }
        if (!email.getText().toString().matches(EMAIL_PATTERN)) {
            messageError = errorInvalidEmail;
            return false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }
        if (password.getText().toString().length() < 6) {
            messageError = errorInvalidPassword;
            return false;
        }
        if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
            messageError = errorConfirmPassword;
            return false;
        }
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }
//        if (!checkBox.isChecked()) {
//            messageError = errorMustAgree;
//            return false;
//        }

        return isValid;
    }


    public Boolean validator2() {
        Boolean isValid = true;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(emailSocial.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }
        if (!emailSocial.getText().toString().matches(EMAIL_PATTERN)) {
            messageError = errorInvalidEmail;
            return false;
        }

        if (TextUtils.isEmpty(birthday.getText().toString())) {
            messageError = errorFieldRequired;
            return false;
        }

        return isValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT < 23 ||
                (
                        checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED
                                && checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED
                )) {
            return true;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (isRegister)
                register.performClick();
            else
                picture.performClick();
        } else
            deny = true;
    }


    @Override
    public void onDateSet(com.tsongkha.spinnerdatepicker.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String s_month;
        String s_day;
        if (dayOfMonth < 10)
            s_day = "0" + dayOfMonth;
        else
            s_day = "" + dayOfMonth;
        monthOfYear++;
        if (monthOfYear < 10)
            s_month = "0" + monthOfYear;
        else
            s_month = "" + monthOfYear;

        birthday.setText(year + "-" + s_month + "-" + s_day);
        birthday.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.text_black));
    }
}

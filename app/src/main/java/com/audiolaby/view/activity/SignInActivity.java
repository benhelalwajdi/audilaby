package com.audiolaby.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.audiolaby.Audiolaby;
import com.audiolaby.Constants;
import com.audiolaby.R;
import com.audiolaby.controller.AccountController;
import com.audiolaby.controller.enumeration.RegisterType;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.request.SignInRequest;
import com.audiolaby.controller.request.SignInSocialRequest;
import com.audiolaby.controller.request.VerifRequest;
import com.audiolaby.controller.response.AccountResponse;
import com.audiolaby.controller.response.VerifResponse;
import com.audiolaby.otto.events.UserLoggedInEvent;
import com.audiolaby.persistence.model.User;
import com.audiolaby.util.ConnectivityUtils;
import com.audiolaby.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;

import static com.audiolaby.util.Utils.getAppVersion;
import static com.audiolaby.util.Utils.getDeviceVersion;


@EActivity(R.layout.activity_sign_in)
public class SignInActivity extends BaseActivity implements OnConnectionFailedListener {

    @Bean
    AccountController accountController;
    private GoogleApiClient apiClient;
    @App
    Audiolaby audiolaby;

    @ViewById(R.id.password)
    EditText password;

    @ViewById(R.id.email)
    EditText email;

    @Extra
    Boolean intro;


    @ViewById(R.id.twitter_login)
    TwitterLoginButton twitterLogin;
    @ViewById(R.id.forgot)
    TextView forgot;
    @ViewById(R.id.sign_in)
    Button signIn;

    @StringRes(R.string.error_field_required)
    String fieldRequired;
    @StringRes(R.string.error_invalid_email)
    String invalid_email;
    @StringRes(R.string.error_connection)
    String errorConnection;
    @StringRes(R.string.signing_in)
    String signingIn;
    @StringRes(R.string.error_try_again)
    String tryAgain;


    private CallbackManager manager;
    private ProgressDialog progressDialog;
    private TwitterAuthClient mTwitterAuthClient;
    private User user;

    String messageError = "";


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @AfterViews
    void afterViewsInjection() {
        this.manager = Factory.create();
        super.afterViewsInjection();
        this.apiClient = new Builder(this)
                .enableAutoManage(this, this).addApi(
                        Auth.GOOGLE_SIGN_IN_API,
                        new GoogleSignInOptions
                                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestProfile()
                                .requestEmail()
                                .build())
                .build();

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        twitterLogin.setCallback(new twitterCallback());

        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //  email.setText("admin001@gmail.com");
        // password.setText("azerty");

    }

    public void onBackPressed() {
        if (intro)
        {
            finish();
            IntroActivity_.intent((Context) SignInActivity.this).start();
        }


        else

            finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLogin.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.i("", "" + result.getSignInAccount().toString());
                GoogleSignInAccount acct = result.getSignInAccount();
                String picture = "empty";
                try {
                    picture = acct.getPhotoUrl().toString();
                } catch (Exception e) {
                     picture = "empty";
                }
                executeVerif(RegisterType.google, acct.getId().toString(), acct.getEmail().toString(), acct.getGivenName().toString(), acct.getFamilyName().toString(), picture);

                return;
            }
            return;
        }
        this.manager.onActivityResult(requestCode, resultCode, data);
    }

    @Click({R.id.forgot})
    void onForgetClicked() {
        Utils.hideKeyboard(SignInActivity.this);
        ForgottenPasswordActivity_.intent((Context) this).start();
    }

    @Click({R.id.facebook})
    void facebookSignIn() {
        Utils.hideKeyboard(SignInActivity.this);
        LoginManager.getInstance().registerCallback(this.manager, new facebookCallback());
        LoginManager.getInstance().logInWithReadPermissions((Activity) this,
                Arrays.asList(new String[]{"public_profile", "email"}));
    }

    @Click({R.id.google})
    void googleSignIn() {
        Utils.hideKeyboard(SignInActivity.this);
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(this.apiClient), 0);
    }

    @Click({R.id.twitter})
    void twitterSignIn() {
        Utils.hideKeyboard(SignInActivity.this);
        this.twitterLogin.performClick();

        Call<com.twitter.sdk.android.core.models.User> user = TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(false, false, true);
        user.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> userResult) {
                String name = userResult.data.name;
                String email = userResult.data.email;

                String photoUrlNormalSize = userResult.data.profileImageUrl;
                String photoUrlBiggerSize = userResult.data.profileImageUrl.replace("_normal", "_bigger");
                String photoUrlMiniSize = userResult.data.profileImageUrl.replace("_normal", "_mini");
                String photoUrlOriginalSize = userResult.data.profileImageUrl.replace("_normal", "");
            }

            @Override
            public void failure(TwitterException exc) {
                Log.d("TwitterKit", "Verify Credentials Failure", exc);
            }
        });
    }

    @Click({R.id.sign_in})
    void signIn() {
        Utils.hideKeyboard(SignInActivity.this);
        if (validator())
            executeSignIn("", "email");
        else
            showError(messageError);

    }


    @Click({R.id.signup})
    void signUp() {
        Utils.hideKeyboard(SignInActivity.this);
        SignUpActivity_.intent((Context) SignInActivity.this).intro(intro).registerType(RegisterType.email).start();
        finish();
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Background(id = "longtask")
    void executeSignIn(String id, String type) {
        showProgress();
        AccountResponse response = null;

        switch (type) {

            case "email":
                response = this.accountController.signIn(new SignInRequest(this.email.getText().toString(),
                        this.password.getText().toString(), type,
                        "android", getDeviceVersion(this), getAppVersion(this), FirebaseInstanceId.getInstance().getToken()));
                break;
            case "facebook":
                response = this.accountController.signInSocial(new SignInSocialRequest(id, type,
                        "android", getDeviceVersion(this), getAppVersion(this), FirebaseInstanceId.getInstance().getToken()));
                break;
            case "twitter":
                response = this.accountController.signInSocial(new SignInSocialRequest(id, type,
                        "android", getDeviceVersion(this), getAppVersion(this), FirebaseInstanceId.getInstance().getToken()));
                break;
            case "google":
                response = this.accountController.signInSocial(new SignInSocialRequest(id, type,
                        "android", getDeviceVersion(this), getAppVersion(this), FirebaseInstanceId.getInstance().getToken()));
                break;
        }


        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                executeSignIn(id, type);
                break;
            case "handler":
                handleResponse(response);
                break;
            default:
                showError(handlerResponse);
        }
    }


    @Background(id = "longtask")
    void executeVerif(RegisterType type, String id, String email, String firstName, String lastName, String picture) {
        showProgress();
        VerifResponse response = this.accountController.verif(new VerifRequest(id, type.name()));

        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                executeVerif(type, id, email, firstName, lastName, picture);
                break;
            case "handler": {
                this.progressDialog.dismiss();
                if (response.getExist() == 0) {
                    SignUpActivity_.intent((Context) SignInActivity.this)
                            .registerType(type)
                            .extraToken(id)
                            .extraEmail(email)
                            .extraFirstName(firstName)
                            .extraLastName(lastName)
                            .extraPicture(picture)
                            .intro(intro)
                            .start();
                    Utils.hideKeyboard(SignInActivity.this);
                    finish();
                } else
                    switch (type) {
                        case facebook:
                            executeSignIn(id, "facebook");
                            break;
                        case google:
                            executeSignIn(id, "google");
                            break;
                    }

            }
            break;
            default:
                showError(handlerResponse);
        }
    }


    @UiThread
    void handleResponse(AccountResponse accountResponse) {
        this.user = accountResponse.getUser();
        this.user.save();
        EventBus.getDefault().postSticky(new UserLoggedInEvent(this.user));
        this.progressDialog.dismiss();
        if (intro)
        {
            MainActivity_.intent((Context) SignInActivity.this).start();
            finish();
        }

        else
            finish();

    }

    @UiThread
    void showProgress() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage(this.signingIn);
        this.progressDialog.setCancelable(true);
        this.progressDialog.show();
    }

    @UiThread
    void showError(String message) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        Snackbar.make(this.signIn, (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }

    public Boolean validator() {
        Boolean isValid = true;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email.getText().toString())) {
            messageError = fieldRequired;
            return false;
        }
        if (!email.getText().toString().matches(EMAIL_PATTERN)) {
            messageError = invalid_email;
            return false;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            messageError = fieldRequired;
            return false;
        }

        return isValid;
    }


    class twitterCallback extends Callback<TwitterSession> {

        @Override
        public void success(Result<TwitterSession> result) {

            String id = "" + result.data.getUserId();
            String name = "" + result.data.getUserName();
            Log.i("", "" + id + name);
        }

        @Override
        public void failure(TwitterException exception) {

        }
    }

    class facebookCallback implements FacebookCallback<LoginResult> {
        facebookCallback() {
        }

        public void onSuccess(LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                String email = "";
                                try {
                                    email = object.getString("email");
                                } catch (Exception e) {
                                }

                                executeVerif(RegisterType.facebook, object.getString("id"), email, object.getString("first_name"), object.getString("last_name"), object.getJSONObject("picture").getJSONObject("data").getString("url"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,email,first_name,last_name,birthday,picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        public void onCancel() {
        }

        public void onError(FacebookException exception) {
        }
    }


}

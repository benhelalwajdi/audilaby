package com.audiolaby.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.audiolaby.Audiolaby;
import com.audiolaby.R;
import com.audiolaby.controller.AccountController;
import com.audiolaby.controller.enumeration.ResponseMap;
import com.audiolaby.controller.enumeration.ResponseStatus;
import com.audiolaby.controller.request.ConfirmCodeRequest;
import com.audiolaby.controller.request.ForgetPasswordRequest;
import com.audiolaby.controller.request.ResetPasswordRequest;
import com.audiolaby.controller.response.CommonResponse;
import com.audiolaby.persistence.LibraryDAO;
import com.audiolaby.persistence.model.User;
import com.audiolaby.util.ConnectivityUtils;
import com.audiolaby.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;

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

@EActivity(R.layout.activity_forget)
public class ForgottenPasswordActivity extends BaseActivity {

    @App
    Audiolaby audiolaby;
    @Bean
    AccountController accountController;
    @Bean
    LibraryDAO libraryDAO;

    int counter = 0;
    @ViewById(R.id.flipper)
    ViewFlipper flipper;

    @ViewById(R.id.email)
    EditText email;
    @ViewById(R.id.send)
    Button send;

    @ViewById(R.id.code)
    EditText code;
    @ViewById(R.id.confirm)
    Button confirm;

    @ViewById(R.id.password)
    EditText password;
    @ViewById(R.id.confirm_password)
    EditText confirmPassword;
    @ViewById(R.id.reset)
    Button reset;


    @StringRes(R.string.error_field_required)
    String fieldRequired;
    @StringRes(R.string.error_invalid_email)
    String emailError;
    @StringRes(R.string.error_confirm_password)
    String confirmPasswordError;
    @StringRes(R.string.error_invalid_password)
    String incorrectPassword;

    private CallbackManager manager;
    private ProgressDialog progressDialog;

    String messageError = "";
    String emailText;
    String codeText;


    @StringRes(R.string.error_try_again)
    String unexpectedErrorTryAgain;
    private User user;


    public ForgottenPasswordActivity() {
        this.manager = Factory.create();
    }


    @Click({R.id.send})
    void onNextClicked() {
        Utils.hideKeyboard(ForgottenPasswordActivity.this);
        if (validator1())
        {
            emailText=email.getText().toString();
            sendCode();
        }
        else
            showError(messageError);
    }

    @Click({R.id.confirm})
    void onConfirmClicked() {
        Utils.hideKeyboard(ForgottenPasswordActivity.this);
        if (validator1())
        {
            codeText=code.getText().toString();
            confirmCode();
        }
        else
            showError(messageError);
    }

    @Click({R.id.reset})
    void onResetClicked() {
        Utils.hideKeyboard(ForgottenPasswordActivity.this);
        if (validator1())
        {
            reset();
        }
        else
            showError(messageError);
    }



    @AfterViews
    void afterViewsInjection() {
        super.afterViewsInjection();
        this.flipper.setInAnimation(ForgottenPasswordActivity.this, R.anim.transition_in_left);
        this.flipper.setOutAnimation(ForgottenPasswordActivity.this, R.anim.transition_out_left);

    }



    @Background(id = "longtask")
    void sendCode() {

        CommonResponse response = (CommonResponse) this.accountController.sendCode(new ForgetPasswordRequest(emailText));

        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                sendCode();
                break;
            case "handler":
                update();
                break;
            default:
                showError(handlerResponse);
        }
    }

    @Background(id = "longtask")
    void confirmCode() {

        CommonResponse response = (CommonResponse) this.accountController.confirmCode(new ConfirmCodeRequest(emailText,codeText));
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                confirmCode();
                break;
            case "handler":
                update();
                break;
            default:
                showError(handlerResponse);
        }

    }

    @Background(id = "longtask")
    void reset() {

        CommonResponse response = (CommonResponse) this.accountController.resetPassword(new ResetPasswordRequest(emailText,codeText,password.getText().toString(),confirmPassword.getText().toString()));
        String handlerResponse = responseHandler(response);
        switch (handlerResponse) {
            case "again":
                reset();
                break;
            case "handler":
                update();
                break;
            default:
                showError(handlerResponse);
        }
    }

    @UiThread
    void update() {

        if(counter==2)
            finish();
        else
        {
            flipper.showNext();
            counter++;
        }

    }

    @UiThread
    void showProgress() {
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("....");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
    }

    @UiThread
    void showError(String message) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        Snackbar.make(this.send, (CharSequence) message, Snackbar.LENGTH_LONG).show();
    }



    public Boolean validator1() {
        Boolean isValid = true;
        String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (TextUtils.isEmpty(email.getText().toString())) {
            messageError = fieldRequired;
            return false;
        }
        if (!email.getText().toString().matches(EMAIL_PATTERN)) {
            messageError = emailError;
            return false;
        }
        return isValid;
    }

    public Boolean validator2() {
        Boolean isValid = true;

        if (TextUtils.isEmpty(code.getText().toString())) {
            messageError = fieldRequired;
            return false;
        }
        return isValid;
    }

    public Boolean validator3() {
        Boolean isValid = true;
        if (TextUtils.isEmpty(password.getText().toString())) {
            messageError = fieldRequired;
            return false;
        }
        if (password.getText().toString().length() < 6) {
            messageError = incorrectPassword;
            return false;
        }
        if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
            messageError = confirmPasswordError;
            return false;
        }
        return isValid;
    }


    public String responseHandler(CommonResponse response) {

        if (response != null ) {
            if (response.getThread())
                return "again";
            else {

                if(response.getResponseStatus() == ResponseStatus.SUCCESS )
                    return "handler";
                else
                    return ResponseMap.getError(response.getResponseText(), this);
            }
        }
        else {
            if (!ConnectivityUtils.isConnected(this))  return errorConnection;
            else return tryAgain;
        }

    }

    public void onBackPressed() {
        finish();
    }

}

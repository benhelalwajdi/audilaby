package com.audiolaby.controller.enumeration;

import android.content.Context;

import com.audiolaby.R;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMap {

    public static String getError(ResponseText responseText, Context context) {

        try {
            switch (responseText) {


                case MISSING_PARAMETER:
                    return context.getString(R.string.unexpected_error_try_again);

                case PSEUDO_OR_EMAIL_MUST_BE_UNIQUE:
                    return context.getString(R.string.error_pseudo_email_unique);

                case EMAIL_MUST_BE_UNIQUE:
                    return context.getString(R.string.error_email_unique);

                case SHORT_PASSWORD:
                    return context.getString(R.string.error_short_password);

                case CONFIRM_PASSWORD:
                    return context.getString(R.string.error_confirm_password);

                case INVALID_EMAIL:
                    return context.getString(R.string.error_invalid_email);

                case UPDATE_REQUID:
                    return context.getString(R.string.error_update_requid);

                case INVALID_EMAIL_PASSWORD:
                    return context.getString(R.string.error_invalid_email_password);

                case SOMETHING_WENT_WRONG:
                    return context.getString(R.string.error_something_wrong);

                case USER_NOT_FOUND:
                    return context.getString(R.string.error_user_not_found);

                case INVALID_CODE_PASSWORD:
                    return context.getString(R.string.error_invalid_code_password);

                case INVALID_IMAGE:
                    return context.getString(R.string.error_image);

                case INCORRECT_POST:
                    return context.getString(R.string.error_incorrect_post);

                case INCORRECT_SECURITY_CODE:
                    return context.getString(R.string.error_incorrect_security_code);

                case ACCOUNT_DISABLED:
                    return context.getString(R.string.error_account_disabled);

                case INCORRECT_VERSION:
                    return context.getString(R.string.error_incorrrect_version);

                default:
                    return context.getString(R.string.unexpected_error_try_again);
            }
        } catch (Exception e) {
            return context.getString(R.string.unexpected_error_try_again);
        }

    }


}

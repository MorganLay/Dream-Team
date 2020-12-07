package com.example.alphadrawer.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
public class AccountFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer userNameError;
    private boolean isDataValid;

    public AccountFormState(@Nullable Integer emailError, @Nullable Integer passwordError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    public AccountFormState(@Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer userNameError) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.userNameError = userNameError;
        this.isDataValid = false;
    }

    public AccountFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getUserNameError() {
        return userNameError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
package com.example.alphadrawer.ui.newAccount;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alphadrawer.R;
import com.example.alphadrawer.data.LoginRepository;
import com.example.alphadrawer.data.Result;
import com.example.alphadrawer.data.model.LoggedInUser;
import com.example.alphadrawer.ui.login.AccountFormState;
import com.example.alphadrawer.ui.login.LoggedInUserView;
import com.example.alphadrawer.ui.login.LoginResult;

public class newAccountViewModel extends ViewModel {
    private MutableLiveData<AccountFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    newAccountViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<AccountFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String userEmail, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(userEmail, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String email, String password, String userName) {
        Boolean userNameValid = isUserNameValid(userName);
        Boolean emailValid = isUserEmailValid(email);
        Boolean passwordValid = isPasswordValid(password);
        if(userNameValid && emailValid && passwordValid) {
            loginFormState.setValue(new AccountFormState(true, false));
        }
    }

    public boolean isUserEmailValid(String email) {
        if (email != null && email.contains("@") && email.contains(".com") && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(R.string.invalid_userEmail, null, null));
            return false;
        }
    }

    public boolean isPasswordValid(String password) {
        if(password != null && password.trim().length() > 8) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(null, R.string.invalid_password, null));
            return false;
        }
    }

    public boolean isUserNameValid(String userName) {
        if(userName != null && !userName.isEmpty()) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(null, null, R.string.invalid_userName));
            return false;
        }
    }
}
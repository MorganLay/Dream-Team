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

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String email, String password) {
        if (!isUserNameValid(email)) {
            loginFormState.setValue(new AccountFormState(R.string.invalid_username, null));
        }else if (!isPasswordValid(password)) {
            loginFormState.setValue(new AccountFormState(null, R.string.invalid_password));
        }
        else {
            loginFormState.setValue(new AccountFormState(true));
        }
    }

    // A placeholder email validation check
    private boolean isUserNameValid(String email) {
        if (email == null) {
            return false;
        }
        if (!email.contains("@") || !email.contains(".com")) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 8;
    }
}
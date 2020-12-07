package com.example.alphadrawer.ui.newAccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.alphadrawer.NewAccountActivity;
import com.example.alphadrawer.R;
import com.example.alphadrawer.ui.login.AccountFormState;

public class newAccountFragment extends Fragment {
    private final MutableLiveData<AccountFormState> loginFormState = new MutableLiveData<>();
    private LiveData<AccountFormState> getLoginFormState() {
        return loginFormState;
    }
    private boolean checkAll = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText emailEditText = view.findViewById(R.id.newAccountEmail);
        final EditText nameEditText = view.findViewById(R.id.newAccountName);
        final EditText passwordEditText = view.findViewById(R.id.newAccountPassword);
        final Button accountCreatedButton = view.findViewById(R.id.newAccountButton);

        getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            checkAll = loginFormState.isDataValid();
            if (loginFormState.getEmailError() != null) {
                emailEditText.setError(getString(loginFormState.getEmailError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
            if (loginFormState.getUserNameError() != null) {
                nameEditText.setError(getString(loginFormState.getUserNameError()));
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!nameEditText.getText().toString().isEmpty()) {
                    isUserNameValid(nameEditText.getText().toString());
                }
                if(!emailEditText.getText().toString().isEmpty()) {
                    isUserEmailValid(emailEditText.getText().toString());
                }
                if(!passwordEditText.getText().toString().isEmpty()) {
                    isPasswordValid(passwordEditText.getText().toString());
                }
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nameEditText.addTextChangedListener(afterTextChangedListener);

        accountCreatedButton.setOnClickListener(v -> {
            loginDataChanged(emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), nameEditText.getText().toString());
            if(checkAll) {
                ((NewAccountActivity) getActivity()).newAccount(nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void loginDataChanged(String email, String password, String userName) {
        Boolean userNameValid = isUserNameValid(userName);
        Boolean emailValid = isUserEmailValid(email);
        Boolean passwordValid = isPasswordValid(password);
        if(userNameValid && emailValid && passwordValid) {
            loginFormState.setValue(new AccountFormState(true));
        }
    }

    private boolean isUserEmailValid(String email) {
        if (email != null && email.contains("@") && email.contains(".com") && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(R.string.invalid_userEmail, null, null));
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if(password != null && password.trim().length() > 8) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(null, R.string.invalid_password, null));
            return false;
        }
    }

    private boolean isUserNameValid(String userName) {
        if(userName != null && !userName.isEmpty()) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(null, null, R.string.invalid_userName));
            return false;
        }
    }
}
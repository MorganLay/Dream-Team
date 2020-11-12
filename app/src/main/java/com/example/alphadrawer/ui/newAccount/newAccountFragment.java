package com.example.alphadrawer.ui.newAccount;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alphadrawer.LoginActivity;
import com.example.alphadrawer.MainActivity;
import com.example.alphadrawer.R;
import com.example.alphadrawer.ui.login.AccountFormState;
import com.example.alphadrawer.ui.login.LoginResult;

public class newAccountFragment extends Fragment {

    private newAccountViewModel newAccountViewModel;
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public static newAccountFragment newInstance() {
        return new newAccountFragment();
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView goBackToLogin = view.findViewById(R.id.goBackToLogin);

        newAccountViewModel = new ViewModelProvider(this, new newAccountViewModelFactory())
                .get(newAccountViewModel.class);

        goBackToLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        final EditText emailEditText = view.findViewById(R.id.newAccountEmail);
        final EditText passwordEditText = view.findViewById(R.id.newAccountPassword);
        final Button accountCreatedButton = view.findViewById(R.id.newAccountButton);

        newAccountViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<AccountFormState>() {
            @Override
            public void onChanged(@Nullable AccountFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                accountCreatedButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getEmailError() != null) {
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        newAccountViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
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
                newAccountViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    newAccountViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        accountCreatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAccountViewModel.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
                Intent intent = new Intent(getContext(), MainActivity.class);
                Boolean user = true;
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newAccountViewModel = new ViewModelProvider(this).get(newAccountViewModel.class);
        // TODO: Use the ViewModel
    }

}
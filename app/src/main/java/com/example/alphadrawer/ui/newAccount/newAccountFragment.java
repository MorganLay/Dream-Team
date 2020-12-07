package com.example.alphadrawer.ui.newAccount;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.alphadrawer.NewAccountActivity;
import com.example.alphadrawer.R;
import com.example.alphadrawer.ui.login.AccountFormState;
import com.example.alphadrawer.ui.login.LoginResult;

public class newAccountFragment extends Fragment {

    private newAccountViewModel newAccountViewModel;
    private boolean checkAll = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_account_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newAccountViewModel = new ViewModelProvider(this, new newAccountViewModelFactory())
                .get(newAccountViewModel.class);

        final EditText emailEditText = view.findViewById(R.id.newAccountEmail);
        final EditText nameEditText = view.findViewById(R.id.newAccountName);
        final EditText passwordEditText = view.findViewById(R.id.newAccountPassword);
        final Button accountCreatedButton = view.findViewById(R.id.newAccountButton);

        newAccountViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
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
                    newAccountViewModel.isUserNameValid(nameEditText.getText().toString());
                }
                if(!emailEditText.getText().toString().isEmpty()) {
                    newAccountViewModel.isUserEmailValid(emailEditText.getText().toString());
                }
                if(!passwordEditText.getText().toString().isEmpty()) {
                    newAccountViewModel.isPasswordValid(passwordEditText.getText().toString());
                }
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        nameEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                newAccountViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        accountCreatedButton.setOnClickListener(v -> {
            newAccountViewModel.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
            newAccountViewModel.loginDataChanged(emailEditText.getText().toString(),
                    passwordEditText.getText().toString(), nameEditText.getText().toString());
            if(checkAll) {
                ((NewAccountActivity) getActivity()).newAccount(nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newAccountViewModel = new ViewModelProvider(this).get(newAccountViewModel.class);
        // TODO: Use the ViewModel
    }

}
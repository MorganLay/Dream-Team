package com.example.alphadrawer.ui.profile;

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

import com.example.alphadrawer.MainActivity;
import com.example.alphadrawer.R;
import com.example.alphadrawer.ui.login.AccountFormState;
import com.example.alphadrawer.ui.user.user;

public class profileFragment extends Fragment {

    private final MutableLiveData<AccountFormState> loginFormState = new MutableLiveData<>();
    private LiveData<AccountFormState> getLoginFormState() {
        return loginFormState;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).retrieveProfile();

        final EditText emailEditText = view.findViewById(R.id.profile_email);
        final EditText nameEditText = view.findViewById(R.id.profile_name);
        final EditText ageEditText = view.findViewById(R.id.profile_age);
        final EditText addressEditText = view.findViewById(R.id.profile_address);
        final EditText genderEditText = view.findViewById(R.id.profile_gender);

        final Button resetButton = view.findViewById(R.id.profile_reset_button);
        final Button saveButton = view.findViewById(R.id.profile_submit_button);

        getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            if (loginFormState.getEmailError() != null) {
                emailEditText.setError(getString(loginFormState.getEmailError()));
            }
            if (loginFormState.getUserNameError() != null) {
                nameEditText.setError(getString(loginFormState.getUserNameError()));
            }
        });

        resetButton.setOnClickListener(v -> {
            emailEditText.setError(null);
            nameEditText.setError(null);
            ((MainActivity) getActivity()).retrieveProfile();
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            Integer age = Integer.getInteger(ageEditText.getText().toString());
            String address = addressEditText.getText().toString();
            String gender = genderEditText.getText().toString();

            Boolean isUserNameValid = isUserNameValid(name);
            Boolean isUserEmailValid = isUserEmailValid(email);
            if(isUserNameValid && isUserEmailValid) {
               user userNewInfo = new user(name, email, age, gender, address);
                ((MainActivity) getActivity()).UpdateProfile(userNewInfo);
            }
        });
    }

    private boolean isUserEmailValid(String email) {
        if (email != null && email.contains("@") && email.contains(".com") && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            loginFormState.setValue(new AccountFormState(R.string.invalid_userEmail, null, null));
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
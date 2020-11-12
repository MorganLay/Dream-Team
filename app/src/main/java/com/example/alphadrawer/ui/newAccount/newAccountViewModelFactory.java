package com.example.alphadrawer.ui.newAccount;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.alphadrawer.data.LoginDataSource;
import com.example.alphadrawer.data.LoginRepository;
import com.example.alphadrawer.ui.login.LoginViewModel;

/**
 * ViewModel provider factory to instantiate NewAccountViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class newAccountViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(newAccountViewModel.class)) {
            return (T) new newAccountViewModel(LoginRepository.getInstance(new LoginDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
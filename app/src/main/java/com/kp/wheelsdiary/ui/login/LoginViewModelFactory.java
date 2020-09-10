package com.kp.wheelsdiary.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.kp.wheelsdiary.data.UserHttpClient;
import com.kp.wheelsdiary.service.UserService;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(UserService.getInstance(new UserHttpClient()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}

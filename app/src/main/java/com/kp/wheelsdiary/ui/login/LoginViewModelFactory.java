package com.kp.wheelsdiary.ui.login;

import android.content.Context;

import com.kp.wheelsdiary.service.UserService;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private Context applicationContext;

    public LoginViewModelFactory(Context applicationContext) {

        this.applicationContext = applicationContext;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(UserService.getInstance(), applicationContext);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}

package com.kp.wheelsdiary.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.kp.wheelsdiary.data.VolleyCallBack;
import com.kp.wheelsdiary.service.UserService;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.R;
import com.kp.wheelsdiary.service.WheelService;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserService userService;

    LoginViewModel(UserService userService) {
        this.userService = userService;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<User> result = userService.login(username, password);

        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public void register(String username, String password) {
        Result<User> result = userService.register(username, password);

        if (result instanceof Result.Success) {
            User data = (User) ((Result.Success) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.register_failed));
        }
    }
}

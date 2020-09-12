package com.kp.wheelsdiary.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Patterns;

import com.kp.wheelsdiary.http.LoginCallBack;
import com.kp.wheelsdiary.service.UserService;
import com.kp.wheelsdiary.data.Result;
import com.kp.wheelsdiary.data.model.User;
import com.kp.wheelsdiary.R;
import com.kp.wheelsdiary.service.WheelService;

import java.util.concurrent.ExecutionException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private UserService userService;
    private LoginCallBack callBack;
    private Context context;

    LoginViewModel(UserService userService, Context context) {
        this.userService = userService;
        this.context = context;
        this.callBack = new LoginCallBack() {
            @Override
            public void onSuccess() {
                User data = WheelService.getCurrentUser();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
            }

            @Override
            public void onFailure() {
                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        };
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) throws ExecutionException, InterruptedException {
        // can be launched in a separate asynchronous job
        userService.login(username, password, callBack, context);

    }

    void loginDataChanged(String username, String password) {
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

    void register(String username, String password) throws ExecutionException, InterruptedException {

        userService.register(username, password, context, callBack);

    }
}

package com.kp.wheelsdiary.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kp.wheelsdiary.dto.User;

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    private static final String PREF_USER_ID = "userId";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, User user)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, user.getName());
        editor.putLong(PREF_USER_ID, user.getId());
        editor.apply();
    }

    public static User getUserName(Context ctx)
    {
        User user = new User();
        String name = getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
        Long id = getSharedPreferences(ctx).getLong(PREF_USER_ID, -1);
        user.setId(id);
        user.setName(name);
        return user;
    }
}
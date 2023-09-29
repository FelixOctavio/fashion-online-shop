package com.felix.uas_fashionstore.data;

import android.content.Context;
import android.util.Log;

import com.felix.uas_fashionstore.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static final String TAG = "LoginDataSource";
    private DBController controller;
    private Context context;

    public LoginDataSource (Context context) {
        this.context = context;
    }

    public Result<LoggedInUser> login(String username, String password) {
        try {
            Log.v(TAG, "Check apakah user ada di database atau tidak");
            controller = new DBController(context,"",null,1);
            // TODO: handle loggedInUser authentication
            LoggedInUser check = controller.get_user(username, password);
            if (check != null) {
                LoggedInUser account = check;
                Log.v(TAG, "Berhasil login");
                controller.set_last_login(check.getUserId());
                return new Result.Success<>(account);
            } else {
                Log.v(TAG, "Wrong!");
                return new Result.Error(new IOException("Wrong password or username!"));
            }
        } catch (Exception e) {
            Log.v(TAG, "Ada error" + e);
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
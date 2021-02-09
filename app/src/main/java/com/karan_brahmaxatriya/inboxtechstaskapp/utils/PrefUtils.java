package com.karan_brahmaxatriya.inboxtechstaskapp.utils;

import android.content.Context;

import com.karan_brahmaxatriya.inboxtechstaskapp.modal.login.CheckLoginModel;




public class PrefUtils {
    public static CheckLoginModel getUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        CheckLoginModel currentUser = complexPreferences.getObject("status", CheckLoginModel.class);
        return currentUser;
    }

    public static void setUser(CheckLoginModel currentUser, Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        complexPreferences.putObject("status", currentUser);
        complexPreferences.commit();
    }


    public static void clearCurrentUser(Context ctx) {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        complexPreferences.clearObject();
        complexPreferences.commit();
    }

}

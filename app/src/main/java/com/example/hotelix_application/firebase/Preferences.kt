package com.example.hotelix_application.firebase

import android.content.Context
import android.content.SharedPreferences

class Preferences (context : Context){

    val PREFERENCE_NAME = "SharedPreference"
    val PREFERENCE_LOGIN_ACCESS_LEVEL = "Position"

    val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getAccessLevel() : String? {
        return preferences.getString(PREFERENCE_LOGIN_ACCESS_LEVEL, "0")
    }

    fun setAccessLevel(position : String){
        val editor = preferences.edit()
        editor.putString(PREFERENCE_LOGIN_ACCESS_LEVEL, position)
        editor.apply()
    }

}
package com.whereismybus

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class SharedStorage(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("DATA", Context.MODE_PRIVATE)

    fun set(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()

        // 위젯 업데이트 트리거
        val intent = Intent(context, BusWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids =
                AppWidgetManager.getInstance(context)
                        .getAppWidgetIds(ComponentName(context, BusWidgetProvider::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }

    fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}

class SharedStorageModule(reactContext: ReactApplicationContext) :
        ReactContextBaseJavaModule(reactContext) {
    private val sharedStorage = SharedStorage(reactContext)

    override fun getName(): String {
        return "SharedStorage"
    }

    @ReactMethod
    fun set(key: String, value: String) {
        sharedStorage.set(key, value)
    }
}

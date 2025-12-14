package com.whereismybus

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 📚 Kotlin 강의 #4: AppWidgetProvider
 *
 * 이 클래스는 "위젯의 생명주기를 관리하는 관리자"입니다. 위젯이 추가되거나 업데이트될 때 안드로이드가 이 클래스를 호출합니다.
 */
class BusWidgetProvider : AppWidgetProvider() {

  companion object {

    private const val ACTION_REFRESH = "com.whereismybus.ACTION_REFRESH"

    /** updateAppWidget: 실제로 위젯을 업데이트하는 함수 (JavaScript의 함수와 같습니다) */
    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
      // bus_widget.xml 레이아웃을 불러옵니다
      val views = RemoteViews(context.packageName, R.layout.bus_widget)

      // ListView에 데이터를 공급할 Intent를 만듭니다
      // (BusWidgetService와 연결하는 다리 역할)
      val intent =
              Intent(context, BusWidgetService::class.java).apply {
                // 각 위젯마다 고유한 URI를 부여해야 합니다
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
              }

      // ListView에 어댑터(데이터 공급자)를 연결합니다
      views.setRemoteAdapter(R.id.bus_list, intent)

      // 빈 뷰 설정 (데이터가 없을 때 보여줄 메시지)
      views.setEmptyView(R.id.bus_list, android.R.id.empty)

      // 🔄 새로고침 버튼 클릭 이벤트 설정
      val refreshIntent =
              Intent(context, BusWidgetProvider::class.java).apply {
                action = ACTION_REFRESH
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
              }
      val refreshPendingIntent =
              PendingIntent.getBroadcast(
                      context,
                      appWidgetId,
                      refreshIntent,
                      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
              )
      views.setOnClickPendingIntent(R.id.refresh_button, refreshPendingIntent)

      // ⚙️ 설정 버튼 클릭 이벤트 설정 (앱 실행)
      val settingsIntent =
              Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
              }
      val settingsPendingIntent =
              PendingIntent.getActivity(
                      context,
                      0,
                      settingsIntent,
                      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
              )
      views.setOnClickPendingIntent(R.id.settings_button, settingsPendingIntent)

      // ⏰ 마지막 업데이트 시간 표시
      val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
      views.setTextViewText(R.id.last_update_time, "마지막 업데이트: $currentTime")

      // SharedPreferences에 마지막 업데이트 시간 저장
      val prefs = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
      prefs.edit().putString("lastUpdateTime", currentTime).apply()

      // 위젯 관리자에게 "업데이트 완료!"라고 알립니다
      appWidgetManager.updateAppWidget(appWidgetId, views)

      // ListView 데이터 갱신 신호 보내기
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.bus_list)
    }
  }

  /** onUpdate: 위젯이 업데이트되어야 할 때 호출됩니다. (처음 추가될 때, 또는 데이터가 바뀔 때) */
  override fun onUpdate(
          context: Context,
          appWidgetManager: AppWidgetManager,
          appWidgetIds: IntArray
  ) {
    // 모든 위젯 인스턴스를 업데이트합니다
    // (사용자가 같은 위젯을 여러 개 추가할 수 있기 때문)
    for (appWidgetId in appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId)
    }
  }

  /** onReceive: 브로드캐스트 이벤트를 받을 때 호출됩니다 (새로고침 버튼 클릭 등) */
  override fun onReceive(context: Context, intent: Intent) {
    super.onReceive(context, intent)

    if (intent.action == ACTION_REFRESH) {
      val appWidgetId =
              intent.getIntExtra(
                      AppWidgetManager.EXTRA_APPWIDGET_ID,
                      AppWidgetManager.INVALID_APPWIDGET_ID
              )

      if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        // 위젯 업데이트 (데이터 갱신 + 시간 갱신)
        updateAppWidget(context, appWidgetManager, appWidgetId)
      }
    }
  }
}

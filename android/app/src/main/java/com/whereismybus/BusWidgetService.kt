package com.whereismybus

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.json.JSONArray

/**
 * 📚 Kotlin 강의 #2: RemoteViewsService
 *
 * 이 클래스는 "위젯의 ListView에 데이터를 공급하는 공장"입니다. 안드로이드가 위젯을 그릴 때 이 서비스를 호출해서 데이터를 받아갑니다.
 */
class BusWidgetService : RemoteViewsService() {
    /** onGetViewFactory: 안드로이드가 "데이터 공장 주세요"라고 요청할 때 호출됩니다. 우리는 BusWidgetFactory를 돌려줍니다. */
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory = BusWidgetFactory(this.applicationContext)
}

/**
 * 📚 Kotlin 강의 #3: RemoteViewsFactory
 *
 * 이 클래스는 "실제로 리스트 아이템을 만드는 공장"입니다. JavaScript의 Array.map()과 비슷한 역할을 합니다.
 */
class BusWidgetFactory(
    private val context: Context,
) : RemoteViewsService.RemoteViewsFactory {
    // 버스 정보를 담을 리스트 (JavaScript의 배열과 같음)
    private var busList = mutableListOf<BusInfo>()

    /** onCreate: 공장이 처음 만들어질 때 한 번만 호출됩니다. (초기화 작업을 여기서 합니다) */
    override fun onCreate() {}

    /**
     * onDataSetChanged: "데이터가 바뀌었으니 새로 불러와!"라고 신호가 올 때 호출됩니다. SharedPreferences에서 JSON을 읽어서 파싱합니다.
     */
    override fun onDataSetChanged() {
        // SharedPreferences에서 데이터 읽기
        val prefs = context.getSharedPreferences("DATA", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("busInfo", "[]") ?: "[]"

        // JSON 파싱 (JavaScript의 JSON.parse()와 같음)
        busList.clear() // 기존 데이터 비우기
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                busList.add(
                    BusInfo(
                        busNumber = item.getString("busNumber"),
                        arrivalTime = item.getString("arrivalTime"),
                        remainingStops = item.getString("remainingStops"),
                    ),
                )
            }
        } catch (e: Exception) {
            // JSON 파싱 실패 시 기본 메시지 표시
            busList.add(BusInfo("오류", "데이터를 불러올 수 없습니다", ""))
        }
    }

    /** getCount: "리스트에 아이템이 몇 개 있나요?"라고 물을 때 호출됩니다. */
    override fun getCount(): Int = busList.size

    /**
     * getViewAt: "N번째 아이템을 그려주세요"라고 요청할 때 호출됩니다. (JavaScript의 Array.map((item, index) => ...) 와 비슷)
     */
    override fun getViewAt(position: Int): RemoteViews {
        // bus_list_item.xml 레이아웃을 불러옵니다
        val views = RemoteViews(context.packageName, R.layout.bus_list_item)

        // position번째 버스 정보를 가져옵니다
        val bus = busList[position]

        // 각 TextView에 데이터를 설정합니다
        views.setTextViewText(R.id.bus_number, bus.busNumber)
        views.setTextViewText(R.id.arrival_time, bus.arrivalTime)
        views.setTextViewText(R.id.remaining_stops, bus.remainingStops)

        return views
    }

    /** 아래는 필수로 구현해야 하지만 우리는 안 쓰는 메서드들입니다. */
    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    override fun onDestroy() {
        busList.clear()
    }
}

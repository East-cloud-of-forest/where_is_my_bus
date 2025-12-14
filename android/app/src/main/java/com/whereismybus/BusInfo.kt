package com.whereismybus

/**
 * 📚 Kotlin 강의 #1: 데이터 클래스 (Data Class)
 *
 * `data class`는 데이터를 담는 그릇입니다. (JavaScript의 객체와 비슷)
 *
 * 아래 코드는 "버스 한 대의 정보"를 표현합니다:
 * - busNumber: 버스 번호 (예: "1001번")
 * - arrivalTime: 도착 예정 시간 (예: "3분 후")
 * - remainingStops: 남은 정류장 수 (예: "2정거장")
 */
data class BusInfo(
        val busNumber: String, // val = 변경 불가능한 변수 (JavaScript의 const)
        val arrivalTime: String,
        val remainingStops: String
)

import { BUSAN_BUS_API_KEY } from "@env";
import React, { useEffect, useState } from "react";
import {
  SafeAreaView,
  StatusBar,
  Text,
  View,
  NativeModules,
  TouchableOpacity,
  Alert,
} from "react-native";

const { SharedStorage } = NativeModules;

function HomeView(): React.JSX.Element {
  const [apiKeyStatus, setApiKeyStatus] = useState<string>("로딩 중...");

  useEffect(() => {
    if (BUSAN_BUS_API_KEY) {
      setApiKeyStatus("API 키 로드 완료 ✅");
    } else {
      setApiKeyStatus("API 키 없음 ❌");
    }
  }, []);

  const handleUpdateWidget = () => {
    // 📚 JavaScript 강의: JSON 데이터 만들기
    // Kotlin의 BusInfo 클래스와 같은 구조로 객체를 만듭니다
    const testBusData = [
      {
        busNumber: "1001번",
        arrivalTime: "3분 후",
        remainingStops: "2정거장",
      },
      {
        busNumber: "1003번",
        arrivalTime: "7분 후",
        remainingStops: "5정거장",
      },
      {
        busNumber: "2000번",
        arrivalTime: "12분 후",
        remainingStops: "8정거장",
      },
    ];

    try {
      // JSON.stringify: JavaScript 객체를 문자열로 변환
      // (Kotlin에서 JSONArray로 파싱할 수 있도록)
      const jsonString = JSON.stringify(testBusData);

      SharedStorage.set("busInfo", jsonString);
      Alert.alert(
        "성공",
        "위젯에 버스 데이터를 보냈습니다! 홈 화면을 확인해보세요.",
      );
    } catch (e) {
      console.error(e);
      Alert.alert("오류", "위젯 데이터 전송 실패");
    }
  };

  return (
    <SafeAreaView className="flex-1 bg-gray-100 dark:bg-slate-900">
      <StatusBar barStyle="dark-content" />
      <View className="flex-1 items-center justify-center p-6">
        <Text className="text-3xl font-bold text-blue-600 mb-4">
          Where is My Bus?
        </Text>
        <Text className="text-lg text-gray-700 dark:text-gray-300 mb-2">
          부산시 버스 도착 정보
        </Text>
        <Text className="text-sm text-gray-500 mb-8">{apiKeyStatus}</Text>

        <TouchableOpacity
          onPress={handleUpdateWidget}
          className="bg-blue-500 hover:bg-blue-600 px-6 py-3 rounded-xl shadow-lg active:opacity-90"
        >
          <Text className="text-white font-semibold text-lg">
            위젯에 테스트 데이터 보내기
          </Text>
        </TouchableOpacity>

        <Text className="text-xs text-gray-400 mt-4 text-center">
          버튼을 누르고 홈 화면 위젯을 확인하세요.
        </Text>
      </View>
    </SafeAreaView>
  );
}

export default HomeView;

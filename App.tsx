/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */
// 앱의 메인 진입점 파일입니다.

import React from 'react';
import {
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';

function App(): React.JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
    flex: 1, // 전체 화면을 사용하도록 설정
    justifyContent: 'center' as 'center', // 세로 중앙 정렬
    alignItems: 'center' as 'center', // 가로 중앙 정렬
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <View>
        <Text style={[styles.text, {color: isDarkMode ? Colors.white : Colors.black}]}>
          내 버스 언제 와?
        </Text>
        <Text style={[styles.subText, {color: isDarkMode ? Colors.light : Colors.dark}]}>
             React Native 프로젝트가 설치되었습니다.
        </Text>
        <Text style={[styles.subText, {color: isDarkMode ? Colors.light : Colors.dark}]}>
             (D:\creative\develop\where_is_my_bus)
        </Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  text: {
    fontSize: 24,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 10,
  },
  subText: {
    fontSize: 16,
    textAlign: 'center',
  }
});

export default App;

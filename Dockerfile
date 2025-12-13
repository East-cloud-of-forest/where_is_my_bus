FROM node:20

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 의존성 설치
COPY package*.json ./
RUN npm install

# 소스 코드 복사 (도커 컴포즈 볼륨을 사용할 것이므로 실제로는 덮어씌워짐)
COPY . .

# Metro Bundler 포트 노출
EXPOSE 8081

# Metro Bundler 실행
CMD ["npm", "start"]

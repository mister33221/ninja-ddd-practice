# 使用Node.js作為基礎鏡像
FROM node:19.4.0 as build

# 設置工作的目錄
WORKDIR /app

# 複製package.json和package-lock.json
COPY package*.json ./

# 安裝依賴
RUN npm install --legacy-peer-deps

# 複製所有源代碼
COPY . .

# 構建應用
# RUN npm run build -- --prod # 不太確定，但好像這個指令是舊版的
# 新版的指令
RUN npm run build -- --configuration development

# 使用nginx作為最終鏡像
FROM nginx:stable-alpine

# 從build階段複製構建結果到nginx的html目錄
COPY --from=build /app/dist/ninja-frontend /usr/share/nginx/html/

# 暴露80端口
EXPOSE 80

# 啟動nginx
CMD ["nginx", "-g", "daemon off;"]


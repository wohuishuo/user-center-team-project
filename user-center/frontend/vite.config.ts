import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// 前端开发服务器:把 /api 代理到后端 Spring Boot(8080)
// 对应网页书:实战篇 / 前端架构。
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
      },
    },
  },
});

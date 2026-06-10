import axios from "axios";

// axios 实例:统一 baseURL、自动带 token、统一解包 {code,data,message}
// 对应网页书:技术图鉴 / TanStack Query(底层用 axios)。
const request = axios.create({
  baseURL: "/api",
  timeout: 10000,
});

// 请求拦截:带上 JWT
request.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// 响应拦截:code===0 直接返回 data,否则抛错
request.interceptors.response.use(
  (resp) => {
    const body = resp.data;
    if (body && typeof body === "object" && "code" in body) {
      if (body.code === 0) return body.data;
      return Promise.reject(new Error(body.message || "请求失败"));
    }
    return body;
  },
  (error) => {
    if (error.response?.status === 403) {
      // 未登录 / 无凭证:清掉本地 token
      localStorage.removeItem("token");
    }
    return Promise.reject(error);
  }
);

export default request;

import { create } from "zustand";
import type { UserVO } from "../api/user";

// 全局状态:当前登录用户 + token(对应网页书:实战篇 / 前端架构)。
interface UserState {
  token: string | null;
  currentUser: UserVO | null;
  setAuth: (token: string, user: UserVO) => void;
  setCurrentUser: (user: UserVO | null) => void;
  logout: () => void;
  isAdmin: () => boolean;
}

export const useUserStore = create<UserState>((set, get) => ({
  token: localStorage.getItem("token"),
  currentUser: null,
  setAuth: (token, user) => {
    localStorage.setItem("token", token);
    set({ token, currentUser: user });
  },
  setCurrentUser: (user) => set({ currentUser: user }),
  logout: () => {
    localStorage.removeItem("token");
    set({ token: null, currentUser: null });
  },
  isAdmin: () => get().currentUser?.userRole === 1,
}));

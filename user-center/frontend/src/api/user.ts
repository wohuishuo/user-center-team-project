import request from "./request";

export interface UserVO {
  id: number;
  userAccount: string;
  username?: string;
  avatarUrl?: string;
  gender?: number;
  phone?: string;
  email?: string;
  planetCode?: string;
  userRole: number;
  userStatus: number;
  createTime?: string;
}

export interface LoginResult {
  token: string;
  user: UserVO;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  current: number;
  pageSize: number;
}

export function register(body: {
  userAccount: string;
  userPassword: string;
  checkPassword: string;
  planetCode: string;
}): Promise<number> {
  return request.post("/user/register", body);
}

export function login(body: { userAccount: string; userPassword: string }): Promise<LoginResult> {
  return request.post("/user/login", body);
}

export function getCurrent(): Promise<UserVO> {
  return request.get("/user/current");
}

export function searchUsers(params: {
  username?: string;
  current?: number;
  pageSize?: number;
}): Promise<PageResult<UserVO>> {
  return request.get("/user/search", { params });
}

export function deleteUser(id: number): Promise<boolean> {
  return request.post("/user/delete", id);
}

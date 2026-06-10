import { useEffect, useState } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { Spin, App as AntApp } from "antd";
import { useUserStore } from "./store/user";
import { getCurrent } from "./api/user";
import AppLayout from "./components/AppLayout";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Home from "./pages/Home";
import UserManage from "./pages/UserManage";

/** 需要登录:无 token 跳登录页。 */
function RequireAuth({ children }: { children: React.ReactNode }) {
  const token = useUserStore((s) => s.token);
  if (!token) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

/** 需要管理员:非管理员回首页。 */
function RequireAdmin({ children }: { children: React.ReactNode }) {
  const isAdmin = useUserStore((s) => s.isAdmin)();
  if (!isAdmin) return <Navigate to="/" replace />;
  return <>{children}</>;
}

export default function App() {
  const token = useUserStore((s) => s.token);
  const currentUser = useUserStore((s) => s.currentUser);
  const setCurrentUser = useUserStore((s) => s.setCurrentUser);
  const logout = useUserStore((s) => s.logout);
  const [booting, setBooting] = useState(true);

  // 启动时:若有 token 但还没拉到用户,补一次 /current
  useEffect(() => {
    if (token && !currentUser) {
      getCurrent()
        .then(setCurrentUser)
        .catch(() => logout())
        .finally(() => setBooting(false));
    } else {
      setBooting(false);
    }
  }, []); // eslint-disable-line

  if (booting) {
    return (
      <div style={{ display: "flex", justifyContent: "center", paddingTop: 120 }}>
        <Spin size="large" />
      </div>
    );
  }

  return (
    <AntApp>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
          path="/"
          element={
            <RequireAuth>
              <AppLayout />
            </RequireAuth>
          }
        >
          <Route index element={<Home />} />
          <Route
            path="admin/users"
            element={
              <RequireAdmin>
                <UserManage />
              </RequireAdmin>
            }
          />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AntApp>
  );
}

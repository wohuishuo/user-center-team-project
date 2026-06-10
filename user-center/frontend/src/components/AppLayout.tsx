import { Layout, Menu, Button, Space, Tag } from "antd";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { useUserStore } from "../store/user";

const { Header, Content } = Layout;

export default function AppLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const user = useUserStore((s) => s.currentUser);
  const isAdmin = useUserStore((s) => s.isAdmin)();
  const logout = useUserStore((s) => s.logout);

  const items = [
    { key: "/", label: "首页" },
    ...(isAdmin ? [{ key: "/admin/users", label: "用户管理" }] : []),
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header style={{ display: "flex", alignItems: "center" }}>
        <div style={{ color: "#fff", fontWeight: "bold", marginRight: 24 }}>用户中心</div>
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={items}
          onClick={(e) => navigate(e.key)}
          style={{ flex: 1, minWidth: 0 }}
        />
        <Space>
          <span style={{ color: "#fff" }}>
            {user?.username || user?.userAccount}
            {isAdmin && <Tag color="gold" style={{ marginLeft: 8 }}>管理员</Tag>}
          </span>
          <Button
            size="small"
            onClick={() => {
              logout();
              navigate("/login");
            }}
          >
            退出
          </Button>
        </Space>
      </Header>
      <Content style={{ padding: 24 }}>
        <Outlet />
      </Content>
    </Layout>
  );
}

import { Button, Card, Form, Input, Typography, App as AntApp } from "antd";
import { useNavigate, Link } from "react-router-dom";
import { login, getCurrent } from "../api/user";
import { useUserStore } from "../store/user";

export default function Login() {
  const navigate = useNavigate();
  const setAuth = useUserStore((s) => s.setAuth);
  const setCurrentUser = useUserStore((s) => s.setCurrentUser);
  const { message } = AntApp.useApp();

  const onFinish = async (values: { userAccount: string; userPassword: string }) => {
    try {
      const res = await login(values);
      setAuth(res.token, res.user);
      // 刷新一次当前用户(走后端 /current,顺带验证 token)
      try {
        setCurrentUser(await getCurrent());
      } catch {
        /* ignore */
      }
      message.success("登录成功");
      navigate("/");
    } catch (e: any) {
      message.error(e?.message || "登录失败");
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", paddingTop: 80 }}>
      <Card style={{ width: 380 }}>
        <Typography.Title level={3} style={{ textAlign: "center" }}>
          用户中心 · 登录
        </Typography.Title>
        <Form layout="vertical" onFinish={onFinish}>
          <Form.Item
            label="账号"
            name="userAccount"
            rules={[{ required: true, min: 4, message: "账号至少 4 位" }]}
          >
            <Input placeholder="userAccount" />
          </Form.Item>
          <Form.Item
            label="密码"
            name="userPassword"
            rules={[{ required: true, min: 8, message: "密码至少 8 位" }]}
          >
            <Input.Password placeholder="userPassword" />
          </Form.Item>
          <Button type="primary" htmlType="submit" block>
            登录
          </Button>
        </Form>
        <div style={{ textAlign: "center", marginTop: 12 }}>
          还没有账号?<Link to="/register">去注册</Link>
        </div>
      </Card>
    </div>
  );
}

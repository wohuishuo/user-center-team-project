import { Button, Card, Form, Input, Typography, App as AntApp } from "antd";
import { useNavigate, Link } from "react-router-dom";
import { register } from "../api/user";

export default function Register() {
  const navigate = useNavigate();
  const { message } = AntApp.useApp();

  const onFinish = async (values: any) => {
    try {
      await register(values);
      message.success("注册成功,请登录");
      navigate("/login");
    } catch (e: any) {
      message.error(e?.message || "注册失败");
    }
  };

  return (
    <div style={{ display: "flex", justifyContent: "center", paddingTop: 60 }}>
      <Card style={{ width: 380 }}>
        <Typography.Title level={3} style={{ textAlign: "center" }}>
          用户中心 · 注册
        </Typography.Title>
        <Form layout="vertical" onFinish={onFinish}>
          <Form.Item
            label="账号"
            name="userAccount"
            rules={[
              { required: true, min: 4, message: "账号至少 4 位" },
              { pattern: /^[a-zA-Z0-9_]+$/, message: "账号不能含特殊字符" },
            ]}
          >
            <Input placeholder="字母数字下划线,≥4 位" />
          </Form.Item>
          <Form.Item
            label="密码"
            name="userPassword"
            rules={[{ required: true, min: 8, message: "密码至少 8 位" }]}
          >
            <Input.Password placeholder="≥8 位" />
          </Form.Item>
          <Form.Item
            label="确认密码"
            name="checkPassword"
            dependencies={["userPassword"]}
            rules={[
              { required: true, message: "请再次输入密码" },
              ({ getFieldValue }) => ({
                validator(_, value) {
                  if (!value || getFieldValue("userPassword") === value) return Promise.resolve();
                  return Promise.reject(new Error("两次密码不一致"));
                },
              }),
            ]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item
            label="星球编号"
            name="planetCode"
            rules={[{ required: true, message: "请输入星球编号" }]}
          >
            <Input placeholder="唯一标识" />
          </Form.Item>
          <Button type="primary" htmlType="submit" block>
            注册
          </Button>
        </Form>
        <div style={{ textAlign: "center", marginTop: 12 }}>
          已有账号?<Link to="/login">去登录</Link>
        </div>
      </Card>
    </div>
  );
}

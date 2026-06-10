import { Card, Descriptions, Tag, Typography } from "antd";
import { useUserStore } from "../store/user";

export default function Home() {
  const user = useUserStore((s) => s.currentUser);

  return (
    <Card>
      <Typography.Title level={4}>欢迎,{user?.username || user?.userAccount}</Typography.Title>
      <Descriptions column={1} bordered size="small" style={{ maxWidth: 520 }}>
        <Descriptions.Item label="ID">{user?.id}</Descriptions.Item>
        <Descriptions.Item label="账号">{user?.userAccount}</Descriptions.Item>
        <Descriptions.Item label="星球编号">{user?.planetCode}</Descriptions.Item>
        <Descriptions.Item label="角色">
          {user?.userRole === 1 ? <Tag color="gold">管理员</Tag> : <Tag>普通用户</Tag>}
        </Descriptions.Item>
        <Descriptions.Item label="注册时间">{user?.createTime}</Descriptions.Item>
      </Descriptions>
    </Card>
  );
}

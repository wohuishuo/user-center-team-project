import { useState } from "react";
import { Button, Input, Popconfirm, Space, Table, Tag, App as AntApp } from "antd";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { searchUsers, deleteUser, type UserVO } from "../api/user";

export default function UserManage() {
  const [username, setUsername] = useState("");
  const [page, setPage] = useState({ current: 1, pageSize: 10 });
  const qc = useQueryClient();
  const { message } = AntApp.useApp();

  const { data, isLoading } = useQuery({
    queryKey: ["users", username, page.current, page.pageSize],
    queryFn: () => searchUsers({ username, current: page.current, pageSize: page.pageSize }),
  });

  const del = useMutation({
    mutationFn: (id: number) => deleteUser(id),
    onSuccess: () => {
      message.success("已删除");
      qc.invalidateQueries({ queryKey: ["users"] });
    },
    onError: (e: any) => message.error(e?.message || "删除失败"),
  });

  const columns = [
    { title: "ID", dataIndex: "id", width: 70 },
    { title: "账号", dataIndex: "userAccount" },
    { title: "昵称", dataIndex: "username" },
    { title: "星球编号", dataIndex: "planetCode" },
    {
      title: "角色",
      dataIndex: "userRole",
      render: (r: number) => (r === 1 ? <Tag color="gold">管理员</Tag> : <Tag>普通</Tag>),
    },
    { title: "注册时间", dataIndex: "createTime" },
    {
      title: "操作",
      render: (_: unknown, row: UserVO) => (
        <Popconfirm title="确认删除该用户?" onConfirm={() => del.mutate(row.id)}>
          <Button danger size="small">删除</Button>
        </Popconfirm>
      ),
    },
  ];

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <Input.Search
          placeholder="按用户名搜索"
          allowClear
          onSearch={(v) => {
            setUsername(v);
            setPage({ ...page, current: 1 });
          }}
          style={{ width: 260 }}
        />
      </Space>
      <Table
        rowKey="id"
        loading={isLoading}
        columns={columns as any}
        dataSource={data?.records || []}
        pagination={{
          current: page.current,
          pageSize: page.pageSize,
          total: data?.total || 0,
          onChange: (current, pageSize) => setPage({ current, pageSize }),
        }}
      />
    </div>
  );
}

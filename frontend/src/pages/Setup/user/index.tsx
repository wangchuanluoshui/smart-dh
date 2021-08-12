import React, { useState, useRef, useEffect } from 'react';
import { Button, message, Dropdown, Menu, Input, Upload } from 'antd';
import type { FormInstance } from 'antd';
import { PageContainer } from '@ant-design/pro-layout';
import { EllipsisOutlined, PlusOutlined, SearchOutlined, LoadingOutlined } from '@ant-design/icons';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable, { ActionType } from '@ant-design/pro-table';
import type { TableListItem, TableListPagination, RequestResult } from './data';
import { getUser, addUser, updateUser, removeUser } from './service';
import ProForm, { DrawerForm, ProFormText, ProFormDatePicker, ProFormRadio, } from '@ant-design/pro-form';
import { getRoleList } from '../role/service'
const User: React.FC = () => {

    const actionRef = useRef<ActionType>();
    const formRef = useRef<FormInstance>();
    const [createDrawerVisible, handleDrawerVisible] = useState<boolean>(false);
    const [loading, setLoading] = React.useState(false);
    const [facePath, setFacePath] = React.useState('/boy-face.jpg');
    const [userId, setUserId] = React.useState<string>();
    const [fileList, setFileList] = useState([
        {
            uid: '-1',
            name: 'face.png',
            status: 'done',
            url: '/boy-face.jpg',
        },
    ]);
    const [roleList, setRoleList] = React.useState([]);


    useEffect(() => {
        handleRoleList();
    }, []);



    const setFormValue = (data: TableListItem) => {
        console.log(data)
        formRef?.current?.setFieldsValue(data);
        setUserId(data.id)
        const faceUrl = data.sex === 0 ? '/boy-face.jpg' : '/girl-face.jpg'
        console.log(faceUrl)
        const fileList = [{
            uid: '-1',
            name: 'image.png',
            status: 'done',
            url: faceUrl,
        }]
        setFileList(fileList);
    }
    const columns: ProColumns<TableListItem>[] = [
        {
            title: '排序',
            dataIndex: 'id',
            valueType: 'indexBorder',
            width: 48,
        },
        {
            title: '名称',
            dataIndex: 'userName',
            render: (_) => <a>{_}</a>,
            // 自定义筛选项功能具体实现请参考 https://ant.design/components/table-cn/#components-table-demo-custom-filter-panel
            filterDropdown: () => (
                <div style={{ padding: 8 }}>
                    <Input style={{ width: 188, marginBottom: 8, display: 'block' }} />
                </div>
            ),
            filterIcon: (filtered) => (
                <SearchOutlined style={{ color: filtered ? '#1890ff' : undefined }} />
            ),
        }, {
            title: '生日',
            dataIndex: 'birthday',
            valueType: 'date',
        }, {
            title: '创建时间',
            width: 140,
            dataIndex: 'createdTime',
            valueType: 'date',
        }, {
            title: '邮件',
            dataIndex: 'email',
        },
        // {
        //     title: '头像',
        //     dataIndex: 'facePath',
        // },
        {
            title: '电话',
            dataIndex: 'mobile',
        }, {
            title: '昵称',
            dataIndex: 'nickName',
        }, {
            title: '真实姓名',
            dataIndex: 'realname',
        }, {
            title: '角色',
            dataIndex: 'roleName',
        }, {
            title: '性别',
            dataIndex: 'sex',
            valueEnum: {
                all: { text: '全部', sex: 'Default' },
                0: { text: '男', sex: '0' },
                1: { text: '女', sex: '1' },
            },
        },
        {
            title: '操作',
            width: 180,
            key: 'option',
            valueType: 'option',
            render: (_, recode: TableListItem) => [
                <a key="link" onClick={() => {

                    handleDrawerVisible(true);
                    setFormValue(recode)
                }}>修改</a>,
                <a key="link2" onClick={() => { handleDelete(recode) }}>删除</a>,
                <a key="link3">重置密码</a>,
            ],
        },
    ];

    const menu = (
        <Menu>
            <Menu.Item key="1">1st item</Menu.Item>
            <Menu.Item key="2">2nd item</Menu.Item>
            <Menu.Item key="3">3rd item</Menu.Item>
        </Menu>
    );
    const handleAdd = async (fields: TableListItem) => {
        const hide = message.loading('正在添加');
        let result: RequestResult;
        try {
            console.log(userId)
            if (userId) {
                fields.id = userId;
                result = await updateUser({ ...fields });
            } else {
                result = await addUser({ ...fields });
            }
            hide();
            if (result.code == '0000') {
                message.success(result.msg);
                formRef?.current?.resetFields();
                return true;
            } else {
                message.error(result.msg);
                return false;
            }
        } catch (error) {
            hide();
            message.error('操作失败请重试！');
            return false;
        }
    };

    const handleDelete = async (fields: TableListItem) => {
        const hide = message.loading('正在删除');
        let result: RequestResult;
        try {
            result = await removeUser({
                id: fields.id
            });
            hide();
            if (result.code == '0000') {
                message.success(result.msg);
                actionRef?.current?.reload();
                return true;
            } else {
                message.error(result.msg);
                return false;
            }
        } catch (error) {
            hide();
            message.error('操作失败请重试！');
            return false;
        }
    };

    const handleRoleList = async () => {
        let result: RequestResult;
        try {
            result = await getRoleList({});
            if (result.code == '0000') {
                setRoleList(result.data)
                return true;
            } else {
                return false;
            }
        } catch (error) {
            message.error('查询角色信息失败，请重试！');
            return false;
        }
    };

    const propsUpload: any = {
        name: 'file',                //发到后台的文件参数名
        action: '/api/upload-file',  //上传的地址
        method: 'post',               //上传请求的 http method，默认post
        data: {},          //上传所需额外参数或返回上传额外参数的方法
        maxCount: 1,
        listType: "picture-card",
        className: "avatar-uploader",
        onChange(info: any) {    //上传文件改变时的状态
            const {
                file,     //当前操作的文件对象。
            } = info;
            if (file.status === 'done' && file.response.code === '0000') {
                message.success(`${file.name} 文件上传成功`);
                setFacePath(file.response.data)
                const fileList = [{
                    uid: '-1',
                    name: 'image.png',
                    status: 'done',
                    url: file.response.data,
                }]
                setFileList(fileList);
            }
            if (file.status === 'error') {
                message.error(`${file.name} 文件上传失败`);
            }
        }
    };

    const uploadButton = (
        <div>
            {loading ? <LoadingOutlined /> : <PlusOutlined />}
            <div style={{ marginTop: 8 }}>Upload</div>
        </div>
    );

    return (
        <PageContainer>
            <ProTable<TableListItem, TableListPagination>
                headerTitle="查询表格"
                actionRef={actionRef}
                rowKey="id"
                search={{
                    labelWidth: 120,
                }}
                request={getUser}
                columns={columns}
                pagination={{
                    showQuickJumper: true,
                }}
                dateFormatter="string"
                toolbar={{
                    title: '用户列表',
                    tooltip: '这是一个标题提示',
                }}
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() => {
                            setUserId(undefined)
                            formRef?.current?.resetFields();
                            handleDrawerVisible(true);
                        }}
                    >
                        <PlusOutlined /> 新建用户

                    </Button>,
                    <Dropdown key="menu" overlay={menu}>
                        <Button>
                            <EllipsisOutlined />
                        </Button>
                    </Dropdown>
                ]}
            />
            <DrawerForm
                title="新建用户"
                width={600}
                formRef={formRef}
                onVisibleChange={handleDrawerVisible}
                visible={createDrawerVisible}
                onFinish={async (value) => {
                    value.facePath = facePath;
                    const success = await handleAdd(value as TableListItem);
                    if (success) {
                        handleDrawerVisible(false);
                        if (actionRef.current) {
                            actionRef.current.reload();
                        }
                    }
                }}
                initialValues={{ sex: 0 }}
            >
                <ProForm.Group>
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '用户名为必填项',
                            },
                        ]}
                        label="用户名"
                        name="userName"
                    />
                    <ProFormText.Password
                        rules={[
                            {
                                required: true,
                                message: '密码为必填项',
                            },
                        ]}
                        label="密码"
                        name="password"
                    />
                </ProForm.Group>
                <ProForm.Group>
                    <ProFormDatePicker
                        rules={[
                            {
                                required: true,
                                message: '出生日期为必填项',
                            },
                        ]}
                        label="出生日期"
                        name="birthday"
                    />
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '电话为必填项',
                            },
                        ]}
                        label="电话"
                        name="mobile"
                    />
                </ProForm.Group>
                <ProForm.Group>
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '邮箱为必填项',
                            },
                        ]}
                        label="邮箱"
                        name="email"
                    />

                    <ProFormText
                        label="昵称"
                        name="nickName"
                    />
                </ProForm.Group>
                <ProForm.Group>
                    <ProFormText
                        name="realname"
                        label="真实姓名"
                    />
                    <ProFormRadio.Group
                        name="roleId"
                        label="角色"
                        rules={[
                            {
                                required: true,
                                message: '必须选择一个角色',
                            },
                        ]}
                        options={roleList}
                    />
                </ProForm.Group>
                <ProForm.Group>
                    <ProFormRadio.Group
                        name="sex"
                        label="性别"
                        options={[
                            {
                                label: '男',
                                value: 0,
                            },
                            {
                                label: '女',
                                value: 1,
                            }
                        ]}
                    />
                    <ProForm.Item
                        name="facePath"
                        label="头像"
                    >
                        <Upload
                            fileList={fileList}
                            {...propsUpload}
                        >
                            {uploadButton}
                        </Upload>
                    </ProForm.Item>
                </ProForm.Group>
            </DrawerForm>
        </PageContainer >
    );
};

export default User;

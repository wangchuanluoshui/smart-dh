import React, { useState, useRef, useEffect } from 'react';
import { Button, message, Dropdown, Menu, Input, Tree, Switch } from 'antd';
import type { FormInstance } from 'antd';
import { PageContainer } from '@ant-design/pro-layout';
import { EllipsisOutlined, PlusOutlined, SearchOutlined } from '@ant-design/icons';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable, { ActionType } from '@ant-design/pro-table';
import type { TableListItem, TableListPagination, RequestResult } from './data';
import { getRole, addRole, updateRole, removeRole, updatePermissions, getPermissions } from './service';
import ProForm, { DrawerForm, ProFormText } from '@ant-design/pro-form';
import { getMenuList } from '../menu/service';

const Role: React.FC = () => {

    const actionRef = useRef<ActionType>();
    const formRef = useRef<FormInstance>();
    const treeFormRef = useRef<FormInstance>();
    const [createDrawerVisible, handleDrawerVisible] = useState<boolean>(false);
    const [createTreeDrawerVisible, handleTreeDrawerVisible] = useState<boolean>(false);
    const [roleId, setRoleId] = React.useState<string>();
    const [checkedKeys, setCheckedKeys] = useState<React.Key[]>();
    const [selectedKeys, setSelectedKeys] = useState<React.Key[]>();
    const [autoExpandParent, setAutoExpandParent] = useState<boolean>(true);
    const [expandedKeys, setExpandedKeys] = useState<React.Key[]>();
    const [treeData, setTreeData] = useState([]);

    useEffect(() => {
        handleMenuList();
    }, []);

    const setFormValue = (data: TableListItem) => {
        console.log(data)
        formRef?.current?.setFieldsValue(data);
        setRoleId(data.id)
        console.log(roleId)
    }
    const columns: ProColumns<TableListItem>[] = [
        {
            title: '排序',
            dataIndex: 'id',
            valueType: 'indexBorder',
            width: 48,
        },
        {
            title: '中文名称',
            dataIndex: 'roleChinaName',
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
            title: '角色名称',
            dataIndex: 'roleFullName',
        }, {
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
                <a key="link3" onClick={() => {
                    handleShowMenuList(recode)
                    handleTreeDrawerVisible(true)
                    setRoleId(recode.id)
                }}>修改权限</a>,
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
            console.log(roleId)
            if (roleId) {
                fields.id = roleId;
                result = await updateRole({ ...fields });
            } else {
                result = await addRole({ ...fields });
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

    const handleMenuList = async () => {
        let result: RequestResult;
        try {
            result = await getMenuList();
            setTreeData(result.data);
            return true;
        } catch (error) {
            message.error('查询菜单列表请重试！');
            return false;
        }
    }

    const handleShowMenuList = async (recode: TableListItem) => {
        let result: RequestResult;
        try {
            result = await getPermissions({ id: recode.id });
            setCheckedKeys(result.data)
            return true;
        } catch (error) {
            message.error('查询菜单列表请重试！');
            return false;
        }
    }

    const handleDelete = async (fields: TableListItem) => {
        const hide = message.loading('正在删除');
        let result: RequestResult;
        try {
            result = await removeRole({
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

    const handleUpdatePermissions = async (fields: TableListItem) => {
        const hide = message.loading('正在更新');
        console.log(selectedKeys);
        let result: RequestResult;
        try {
            result = await updatePermissions({ ...fields });
            hide();
            if (result.code == '0000') {
                message.success(result.msg);
                treeFormRef?.current?.resetFields();
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
    const onExpand = (expandedKeysValue: React.Key[]) => {
        console.log('onExpand', expandedKeysValue);
        // if not set autoExpandParent to false, if children expanded, parent can not collapse.
        // or, you can remove all expanded children keys.
        setExpandedKeys(expandedKeysValue);
        setAutoExpandParent(false);
    };

    const onCheck = (checkedKeysValue: React.Key[], halfCheckKeyValue: any) => {
        let checkedKey = [...checkedKeysValue, ...halfCheckKeyValue.halfCheckedKeys]
        console.log('onCheck', checkedKey);
        setCheckedKeys(checkedKeysValue);
    };

    const onSelect = (selectedKeysValue: React.Key[], info: any) => {
        console.log('onSelect', info);
        setSelectedKeys(selectedKeysValue);
    };

    return (
        <PageContainer>
            <ProTable<TableListItem, TableListPagination>
                headerTitle="查询表格"
                actionRef={actionRef}
                rowKey="id"
                search={{
                    labelWidth: 120,
                }}
                request={getRole}
                columns={columns}
                pagination={{
                    showQuickJumper: true,
                }}
                dateFormatter="string"
                toolbar={{
                    title: '角色列表',
                    tooltip: '这是一个标题提示',
                }}
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() => {
                            formRef?.current?.resetFields();
                            handleDrawerVisible(true);
                            setRoleId(undefined);
                        }}
                    >
                        <PlusOutlined /> 新建角色

                    </Button>,
                    <Dropdown key="menu" overlay={menu}>
                        <Button>
                            <EllipsisOutlined />
                        </Button>
                    </Dropdown>
                ]}
            />
            <DrawerForm
                title="新建角色"
                width={500}
                formRef={formRef}
                onVisibleChange={handleDrawerVisible}
                visible={createDrawerVisible}
                onFinish={async (value) => {
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
                                message: '角色名称为必填项',
                            },
                        ]}
                        label="角色名称"
                        name="roleFullName"
                    />
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '中文名称为必填项',
                            },
                        ]}
                        label="中文名称"
                        name="roleChinaName"
                    />
                </ProForm.Group>
            </DrawerForm>

            <DrawerForm
                title="更改权限"
                width={500}
                formRef={treeFormRef}
                onVisibleChange={handleTreeDrawerVisible}
                visible={createTreeDrawerVisible}
                onFinish={async (value) => {
                    value.menuList = checkedKeys;
                    value.roleId = roleId;
                    const success = await handleUpdatePermissions(value as TableListItem);
                    if (success) {
                        handleTreeDrawerVisible(false);
                    }
                }}
            >
                <ProForm.Group>
                    <ProForm.Item valuePropName="checked">
                        <Tree
                            checkable
                            onExpand={onExpand}
                            expandedKeys={expandedKeys}
                            autoExpandParent={autoExpandParent}
                            onCheck={onCheck}
                            checkedKeys={checkedKeys}
                            onSelect={onSelect}
                            selectedKeys={selectedKeys}
                            treeData={treeData}
                        />
                    </ProForm.Item>
                </ProForm.Group>

            </DrawerForm>
        </PageContainer >
    );
};

export default Role;

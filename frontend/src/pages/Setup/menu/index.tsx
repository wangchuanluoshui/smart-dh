import React, { useState, useRef } from 'react';
import { Button, message, Dropdown, Menu, Input } from 'antd';
import type { FormInstance } from 'antd';
import { PageContainer } from '@ant-design/pro-layout';
import { EllipsisOutlined, PlusOutlined, SearchOutlined } from '@ant-design/icons';
import type { ProColumns } from '@ant-design/pro-table';
import ProTable, { ActionType } from '@ant-design/pro-table';
import type { TableListItem, TableListPagination, RequestResult } from './data';
import { getMenu, addMenu, updateMenu, removeMenu } from './service';
import ProForm, { DrawerForm, ProFormText, ProFormSelect } from '@ant-design/pro-form';

const MyMenu: React.FC = () => {

    const actionRef = useRef<ActionType>();
    const formRef = useRef<FormInstance>();
    const [createDrawerVisible, handleDrawerVisible] = useState<boolean>(false);
    const [menuId, setMenuId] = useState<string>();
    const [parentId, setParentId] = useState<string>('-1');

    const setFormValue = (data: TableListItem) => {
        console.log(data)
        formRef?.current?.setFieldsValue(data);
        setMenuId(data.id)
        console.log(menuId)
    }
    const columns: ProColumns<TableListItem>[] = [
        {
            title: '菜单名称',
            dataIndex: 'name',
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
            title: '中文名称',
            dataIndex: 'chineseName',
        }, {
            title: '路径',
            dataIndex: 'path',
        }, {
            title: '所属组件',
            dataIndex: 'component',
        }, {
            title: '图标',
            dataIndex: 'icon',
        }, {
            title: '显示标签',
            dataIndex: 'showTag'
        }, {
            title: '操作',
            width: 180,
            key: 'option',
            valueType: 'option',
            render: (_, recode: TableListItem) => [
                <a key="link" onClick={() => {
                    handleDrawerVisible(true);
                    setFormValue(recode);
                    setParentId(recode.pid)
                }}>修改</a>,
                <a key="link2" onClick={() => { handleDelete(recode) }}>删除</a>,
                <a key="link2" onClick={() => { handleAddChildMenu(recode) }}>添加子菜单</a>,
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
            console.log(menuId)
            if (menuId) {
                fields.id = menuId;
                fields.pid = parentId;
                result = await updateMenu({ ...fields });
            } else {
                result = await addMenu({ ...fields });
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
            result = await removeMenu({
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
    const handleAddChildMenu = (recode: TableListItem) => {
        formRef?.current?.resetFields();
        setParentId(recode.id)
        handleDrawerVisible(true);
    }

    const handleDrawerVisibleAndSetValue = (visible: boolean) => {
        handleDrawerVisible(visible)
    }

    return (
        <PageContainer>
            <ProTable<TableListItem>
                headerTitle="查询表格"
                actionRef={actionRef}
                rowKey="id"
                search={{
                    labelWidth: 120,
                }}
                request={getMenu}
                columns={columns}
                pagination={false}
                dateFormatter="string"
                toolbar={{
                    title: '菜单列表',
                    tooltip: '这是一个标题提示',
                }}
                toolBarRender={() => [
                    <Button
                        type="primary"
                        key="primary"
                        onClick={() => {
                            formRef?.current?.resetFields();
                            handleDrawerVisible(true);
                            setMenuId(undefined);
                            setParentId('-1')
                        }}
                    >
                        <PlusOutlined /> 新建菜单

                    </Button>,
                    <Dropdown key="menu" overlay={menu}>
                        <Button>
                            <EllipsisOutlined />
                        </Button>
                    </Dropdown>
                ]}
            />
            <DrawerForm
                title="新建菜单"
                width={500}
                formRef={formRef}
                onVisibleChange={handleDrawerVisibleAndSetValue}
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
                initialValues={{ redirect: '/' }}
            >
                <ProForm.Group>
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '中文名称为必填项',
                            },
                        ]}
                        label="中文名称"
                        name="chineseName"
                    />
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '菜单名称为必填项',
                            },
                        ]}
                        label="菜单名称"
                        name="name"
                    />
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '菜单路径为必填项',
                            },
                        ]}
                        label="路径"
                        name="path"
                    />

                    <ProFormText
                        label="图标"
                        name="icon"
                    />
                    <ProFormText
                        rules={[
                            {
                                required: true,
                                message: '所属组件为必填项',
                            },
                        ]}
                        label="所属组件"
                        name="component"
                    />
                    <ProFormText
                        label="显示标签"
                        name="showTag"
                    />
                    <ProFormText
                        label="重定向"
                        name="redirect"
                        readonly
                        hidden
                    />
                </ProForm.Group>
            </DrawerForm>
        </PageContainer >
    );
};

export default MyMenu;

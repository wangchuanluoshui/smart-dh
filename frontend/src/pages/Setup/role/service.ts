// @ts-ignore
/* eslint-disable */
import { request } from 'umi';
import { TableListItem, RequestResult } from './data';

export async function getRole(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<{
    data: TableListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  }>('/api/role/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/Role/ */
export async function updateRole(options?: { [key: string]: any }) {
  return request<TableListItem>('/api/role/', {
    method: 'PUT',
    data: (options || {}),
  });
}

export async function updatePermissions(options?: { [key: string]: any }) {
  return request<TableListItem>('/api/role/permissions', {
    method: 'PUT',
    data: (options || {}),
  });
}

export async function getPermissions(
  params: {
    id?: string;
  },
  options?: { [key: string]: any }) {
  return request<RequestResult>('/api/role/permissions', {
    method: 'GET',
    params: {
      ...params,
    }, ...(options || {}),
  });
}

/** 新建规则 POST /api/Role/ */
export async function addRole(options?: { [key: string]: any }) {
  console.log(options)
  return request<TableListItem>('/api/role/', {
    method: 'POST',
    data: (options || {}),
  });
}

/** 删除规则 DELETE /api/Role/ */
export async function removeRole(
  params: {
    id?: string;
  },
  options?: { [key: string]: any },) {
  return request<Record<string, any>>('/api/role/', {
    method: 'DELETE',
    params: {
      ...params,
    }, ...(options || {}),
  });
}

/** 删除规则 DELETE /api/Role/ */
export async function getRoleList(options?: { [key: string]: any },) {
  return request<Record<string, any>>('/api/role/list', {
    method: 'GET',
    params: {
    }, ...(options || {}),
  });
}

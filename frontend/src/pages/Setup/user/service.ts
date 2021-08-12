// @ts-ignore
/* eslint-disable */
import { request } from 'umi';
import { TableListItem } from './data';

export async function getUser(
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
  }>('/api/user/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/user/ */
export async function updateUser(options?: { [key: string]: any }) {
  return request<TableListItem>('/api/user/', {
    method: 'PUT',
    data: (options || {}),
  });
}

/** 新建规则 POST /api/user/ */
export async function addUser(options?: { [key: string]: any }) {
  console.log(options)
  return request<TableListItem>('/api/user/', {
    method: 'POST',
    data: (options || {}),
  });
}

/** 删除规则 DELETE /api/user/ */
export async function removeUser(
  params: {
    id?: string;
  },
  options?: { [key: string]: any },) {
  return request<Record<string, any>>('/api/user/', {
    method: 'DELETE',
    params: {
      ...params,
    }, ...(options || {}),
  });
}


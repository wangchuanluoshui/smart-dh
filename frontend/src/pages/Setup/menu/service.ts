// @ts-ignore
/* eslint-disable */
import { request } from 'umi';
import { TableListItem } from './data';

export async function getMenu(
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
  }>('/api/menu/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/Menu/ */
export async function updateMenu(options?: { [key: string]: any }) {
  return request<TableListItem>('/api/menu/', {
    method: 'PUT',
    data: (options || {}),
  });
}

/** 新建规则 POST /api/Menu/ */
export async function addMenu(options?: { [key: string]: any }) {
  console.log(options)
  return request<TableListItem>('/api/menu/', {
    method: 'POST',
    data: (options || {}),
  });
}

/** 删除规则 DELETE /api/Menu/ */
export async function removeMenu(
  params: {
    id?: string;
  },
  options?: { [key: string]: any },) {
  return request<Record<string, any>>('/api/menu/', {
    method: 'DELETE',
    params: {
      ...params,
    }, ...(options || {}),
  });
}

/** 新建规则 GET /api/menu/page */
export async function getMenuList() {
  return request<TableListItem>('/api/menu/list', {
    method: 'GET',
    params: {
    }
  });
}

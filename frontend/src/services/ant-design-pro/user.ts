// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

/** 获取当前的用户 GET /api/getUserList */
export async function getUserList(options?: { [key: string]: any }) {
    return request<{
      data: API.CurrentUser;
    }>('/api/user/page', {
      method: 'GET',
      ...(options || {}),
    });
  }
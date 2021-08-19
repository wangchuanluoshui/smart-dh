// @ts-ignore
/* eslint-disable */
import { request } from 'umi';

export type RequestResult = {
  code?: string;
  msg?: string;
  data?: any
};

export async function otherSysLogin(
  params: {
    sourcesyscode?: string;
  },
  options?: { [key: string]: any }) {
  return request<RequestResult>('/api/oauth/login', {
    method: 'GET',
    params: {
      ...params,
    }, ...(options || {}),
  });
}


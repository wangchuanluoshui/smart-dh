export type TableListItem = {
  id: string;
  birthday: Date;
  createdTime: Date;
  email: string;
  mobile: string;
  facePath: string;
  roleId: string;
  nickName: string;
  realname: string;
  roleName: string;
  userName: string;
  password: string;
  sex: number;
  code: string;
  msg: string;
};

export type TableListPagination = {
  total: number;
  pageSize: number;
  current: number;
};

export type TableListData = {
  list: TableListItem[];
  pagination: Partial<TableListPagination>;
};

export type TableListParams = {
  userName?: string;
  desc?: string;
  key?: number;
  pageSize?: number;
  currentPage?: number;
  filter?: Record<string, any[]>;
  sorter?: Record<string, any>;
};


export type RequestResult = {
  code?: string;
  msg?: string;
  data?: any
};
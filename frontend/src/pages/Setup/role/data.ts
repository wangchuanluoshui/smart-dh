export type TableListItem = {
  createdTime: Date,
  id: string,
  roleChinaName: string,
  roleFullName: string,
  updatedTime: Date,
  sex: number;
  code: string;
  msg: string;
  data: [];
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
  roleChinaName?: string;
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
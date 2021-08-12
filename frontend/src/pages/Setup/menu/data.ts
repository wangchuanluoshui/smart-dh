export type TableListItem = {
  createdTime: Date,
  id: string,
  name: string,
  chineseName: string,
  updatedTime: Date,
  path: number,
  component: string,
  redirect: string,
  icon: string,
  pid: string,
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
  chineseName?: string;
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
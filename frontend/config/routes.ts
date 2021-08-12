export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user',
        routes: [
          {
            name: 'login',
            path: '/user/login',
            component: './user/Login',
          },
        ],
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    access: 'showWelcome',
    component: './Welcome',
  },
  {
    path: '/admin',
    name: 'admin',
    icon: 'crown',
    access: 'showAdmin',
    component: './Admin',
    routes: [
      {
        path: '/admin/sub-page',
        name: 'sub-page',
        icon: 'smile',
        access: 'showSubPage',
        component: './Admin',
      },
      {
        component: './404',
      },
    ],
  }, {
    path: '/setup',
    name: 'setup',
    icon: 'SettingOutlined',
    access: 'showSetup',
    routes: [
      {
        path: '/setup/user',
        name: 'setup-user',
        component: './Setup/user',
        access: 'showSetupUser',
      }, {
        path: '/setup/role',
        name: 'setup-role',
        component: './Setup/role',
        access: 'showSetupRole',
      }, {
        path: '/setup/menu',
        name: 'setup-menu',
        component: './Setup/menu',
        access: 'showSetupMenu',
      },
      {
        component: './404',
      },
    ],
  },
  {
    name: 'list.table-list',
    icon: 'table',
    path: '/list',
    component: './TableList',
    access: 'showTableList',
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './404',
  },
];

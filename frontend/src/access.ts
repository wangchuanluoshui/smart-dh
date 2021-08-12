/**
import access from './access';
import access from './access';
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState: { currentUser?: API.CurrentUser | undefined }) {
  const { currentUser } = initialState || {};
  console.log(currentUser?.access)
  return {
    showWelcome: currentUser?.access?.includes('showWelcome'),
    showAdmin: currentUser?.access?.includes('showAdmin'),
    showSubPage: currentUser?.access?.includes('showSubPage'),
    showSetup: currentUser?.access?.includes('showSetup'),
    showSetupUser: currentUser?.access?.includes('showSetupUser'),
    showSetupRole: currentUser?.access?.includes('showSetupRole'),
    showSetupMenu: currentUser?.access?.includes('showSetupMenu'),
    showTableList: currentUser?.access?.includes('showTableList'),
  };
}

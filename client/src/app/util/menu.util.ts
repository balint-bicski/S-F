import {Authority} from "../../../target/generated-sources";

export interface Menu {
  label: string;
  route: string;
  authority?: Authority;
}

export const MENU_ITEMS: Menu[] = [
  {
    label: 'Dashboard',
    route: 'dashboard',
  },
  {
    label: 'User List',
    route: 'users',
    authority: Authority.ViewUser
  },
];

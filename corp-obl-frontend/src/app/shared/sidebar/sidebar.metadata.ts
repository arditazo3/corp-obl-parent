// Sidebar route metadata
import {AuthorityEnum} from '../common/api/enum/authority.enum';

export interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
  ddclass: string;
  extralink: boolean;
  submenu: RouteInfo[];
  authorities?: string[];
}

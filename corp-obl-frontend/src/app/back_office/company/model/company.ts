import {User} from '../../../user/model/user';
import {CompanyUser} from './company_user';

export class Company {

  idCompany: number;
  description: string;
  createdBy: string;
  modifiedBy: string;
  usersAssociated: CompanyUser[];
}

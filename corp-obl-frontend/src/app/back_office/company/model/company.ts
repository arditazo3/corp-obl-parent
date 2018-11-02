import {CompanyUser} from './company_user';
import {Office} from '../../office/model/office';

export class Company {

    idCompany: number;
    description: string;
    usersAssociated: CompanyUser[];
    offices: Office[];
}

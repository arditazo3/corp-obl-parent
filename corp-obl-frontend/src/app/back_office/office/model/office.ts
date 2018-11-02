import {Company} from '../../company/model/company';
import {User} from '../../../user/model/user';

export class Office {

    idOffice: number;
    description: string;
    company: Company;
    userProviders: User[];
    userBeneficiaries: User[];
}

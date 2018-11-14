import {Office} from '../../back_office/office/model/office';

export class DateExpirationOfficesHasArchived {

    dateStart: string;
    dateEnd: string;
    offices: Office[];
    showArchived: Boolean;
    userRelationType: number;
}
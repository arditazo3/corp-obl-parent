import {Expiration} from './expiration';
import {ExpirationActivityAttachment} from './expiration-activity-attachment';
import {StatusExpirationEnum} from '../../shared/common/api/enum/status.expiration.enum';

export class ExpirationActivity {

    idExpirationActivity: number;
    expiration: Expiration;
    body: string;
    descriptionLastActivity: string;
    expirationActivityAttachments: ExpirationActivityAttachment[];
}
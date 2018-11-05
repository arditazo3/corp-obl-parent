import {Expiration} from './expiration';
import {ExpirationActivityAttachment} from './expiration-activity-attachment';

export class ExpirationActivity {

    idExpirationActivity: number;
    expiration: Expiration;
    body: string;
    descriptionLastActivity: string;
    expirationActivityAttachments: ExpirationActivityAttachment[];
}
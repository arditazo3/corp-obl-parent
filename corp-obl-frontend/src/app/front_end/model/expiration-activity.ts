import {Expiration} from './expiration';
import {ExpirationActivityAttachment} from './expiration-activity-attachment';
import {DescriptionLang} from '../../shared/common/api/model/description-lang';

export class ExpirationActivity {

    idExpirationActivity: number;
    expiration: Expiration;
    body: string;
    descriptionActivity: DescriptionLang[];
    expirationActivityAttachments: ExpirationActivityAttachment[];
}
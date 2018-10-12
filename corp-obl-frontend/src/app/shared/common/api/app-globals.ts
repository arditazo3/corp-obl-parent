import {IMyDpOptions} from 'mydatepicker';

export class AppGlobals {

    public static emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$';

    public static myDatePickerOptions: IMyDpOptions = {
        // other options...
        dateFormat: 'dd/mm/yyyy',
    };
}

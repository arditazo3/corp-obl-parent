import {Router} from '@angular/router';
import {Injectable} from '@angular/core';
import {DataFilter} from '../api/model/data-filter';
import {PageEnum} from '../api/enum/page.enum';

@Injectable()
export class TransferDataService {

    private _singleParam;
    private _objectParam;
    private _arrayParam;
    private _aloneParam;
    private _dataFilter: DataFilter;

    constructor(
    ) {}


    get singleParam() {
        const _singleParamTemp = this._singleParam;

        this.cleanParams();
        return _singleParamTemp;
    }

    set singleParam(value) {
        this._singleParam = value;
    }

    get objectParam() {
        const _objectParamTemp = this._objectParam;

        this.cleanParams();
        return _objectParamTemp;
    }

    set objectParam(value) {
        this._objectParam = value;
    }

    get arrayParam() {
        const _arrayParamTemp = this._arrayParam;

        this.cleanParams();
        return _arrayParamTemp;
    }

    set arrayParam(value) {
        this._arrayParam = value;
    }

    get aloneParam() {
        const _aloneParamTemp = this._aloneParam;

        this._aloneParam = undefined;
        return _aloneParamTemp;
    }

    set aloneParam(value) {
        this._aloneParam = value;
    }

    get dataFilter(): DataFilter {

        const _dataFilterTemp = this._dataFilter;

        this._dataFilter = new DataFilter(PageEnum.NONE);
        return _dataFilterTemp;
    }

    set dataFilter(value: DataFilter) {
        this._dataFilter = value;
    }

    cleanParams() {
        this._singleParam = undefined;
        this._objectParam = undefined;
        this._arrayParam = undefined;
    }
}

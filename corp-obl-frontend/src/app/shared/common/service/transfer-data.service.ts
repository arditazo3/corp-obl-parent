import {Router} from '@angular/router';
import {Injectable} from '@angular/core';

@Injectable()
export class TransferDataService {

  private _singleParam;
  private _objectParam;
  private _arrayParam;

  constructor(
    private router: Router
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

  cleanParams() {
    this._singleParam = undefined;
    this._objectParam = undefined;
    this._arrayParam = undefined;
  }
}

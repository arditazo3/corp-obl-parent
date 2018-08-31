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
    return this._singleParam;
  }

  set singleParam(value) {
    this._singleParam = value;
  }

  get objectParam() {
    return this._objectParam;
  }

  set objectParam(value) {
    this._objectParam = value;
  }

  get arrayParam() {
    return this._arrayParam;
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

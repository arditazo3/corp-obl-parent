import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
    selector: 'app-office-taks-collapse',
    templateUrl: './office-taks-collapse.component.html',
    styleUrls: ['./office-taks-collapse.component.css']
})
export class OfficeTaksCollapseComponent implements OnInit {

    officeTaskTemplatesArray = [];

    constructor(
        private router: Router
    ) {
    }

    ngOnInit() {
        console.log('OfficeTaksCollapseComponent - ngOnInit');

        const me = this;
        me.getOfficeTaskTemplatesArray(null);
    }

    getOfficeTaskTemplatesArray(officeTaskTemplatessFromParent) {
        console.log('OfficeTaksCollapseComponent - getOfficeTaskTemplatesArray');

        if (!officeTaskTemplatessFromParent) {
            return;
        }
        this.officeTaskTemplatesArray = officeTaskTemplatessFromParent;
    }

}

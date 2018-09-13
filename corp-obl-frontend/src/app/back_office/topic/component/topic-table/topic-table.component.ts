import {Component, OnInit, ViewChild} from '@angular/core';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Topic} from '../../../topic/model/topic';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TopicService} from '../../../topic/service/topic.service';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';

@Component({
  selector: 'app-topic-table',
  templateUrl: './topic-table.component.html',
  styleUrls: ['./topic-table.component.css']
})
export class TopicTableComponent implements OnInit {

    @ViewChild('myTable') table: any;
    @ViewChild('deleteTopicSwal') deleteTopicSwal: SwalComponent;

    columns: any[];
    rows: Topic[];
    data: any;
    temp = [];
    rowSelected: Topic;
    errorDetails: ApiErrorDetails;

    constructor(
        private router: Router,
        private topicService: TopicService,
        private transferService: TransferDataService
    ) {
    }

    async ngOnInit() {
        console.log('TopicTableComponent - ngOnInit');

        const me = this;
        me.getTopics();

        me.columns = [
            {prop: 'description', name: 'Description'}
        ];
    }

    getTopics() {
        console.log('TopicTableComponent - getTopics');

        const me = this;
        this.topicService.getTopics().subscribe(
            (data) => {
                me.rows = data;
                me.temp = [...data];
            }
        );
    }

    updateFilter(event) {
        console.log('TopicTableComponent - updateFilter');

        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.temp.filter(function (d) {
            return d.description.toLowerCase().indexOf(val) !== -1 || !val;
        });

        // update the rows
        this.rows = temp;
        // Whenever the filter changes, always go back to the first page
        this.table = this.data;
    }

    toggleExpandRow(row) {
        console.log('TopicTableComponent - Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
    }

    createNewTopic() {
        console.log('TopicTableComponent - createNewTopic');

        this.router.navigate(['/back-office/topic/create']);
    }

    editTopic(topic: Topic) {
        console.log('TopicTableComponent - editTopic');

        this.transferService.objectParam = topic;

        this.router.navigate(['/back-office/topic/edit']);
    }

    deleteTopicAlert(row) {
        console.log('TopicTableComponent - deleteTopicAlert');

        this.rowSelected = row;

        this.deleteTopicSwal.title = 'Delete: ' + row.description + '?';

        this.deleteTopicSwal.show();
    }

    deleteTopicCofirm() {
        console.log('TopicTableComponent - deleteTopicCofirm' + this.rowSelected.idTopic);

        const topicSelected = this.rowSelected;

        this.topicService.deleteTopic(topicSelected).subscribe(
            (data) => {
                this.errorDetails = undefined;
                this.getTopics();
                console.log('TopicTableComponent - deleteTopicCofirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.log('TopicTableComponent - deleteTopicCofirm - error');
            }
        );
    }

}

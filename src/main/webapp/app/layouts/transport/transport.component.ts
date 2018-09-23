import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-transport',
    templateUrl: './transport.component.html',
    styleUrls: ['transport.component.scss']
})
export class TransportComponent implements OnInit {
    currentAccount: any;
    currentSearch: string;

    constructor() {}

    ngOnInit() {}

    search(searchString) {}

    clear() {}
}

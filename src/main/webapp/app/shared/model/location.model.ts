export interface ILocation {
    id?: number;
    buildingNumber?: number;
    streetName?: string;
    city?: string;
    state?: string;
    zipCode?: number;
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public buildingNumber?: number,
        public streetName?: string,
        public city?: string,
        public state?: string,
        public zipCode?: number
    ) {}
}

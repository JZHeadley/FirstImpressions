import { ILocation } from 'app/shared/model//location.model';

export interface IClothingCompany {
    id?: number;
    companyName?: string;
    companyDesc?: string;
    companyLocation?: ILocation;
}

export class ClothingCompany implements IClothingCompany {
    constructor(public id?: number, public companyName?: string, public companyDesc?: string, public companyLocation?: ILocation) {}
}

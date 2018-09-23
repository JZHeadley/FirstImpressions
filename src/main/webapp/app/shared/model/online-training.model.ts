export interface IOnlineTraining {
    id?: number;
    trainingLink?: string;
    desc?: string;
}

export class OnlineTraining implements IOnlineTraining {
    constructor(public id?: number, public trainingLink?: string, public desc?: string) {}
}

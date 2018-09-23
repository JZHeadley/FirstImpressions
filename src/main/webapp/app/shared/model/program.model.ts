export interface IProgram {
    id?: number;
    programTitle?: string;
    programLink?: string;
    programDesc?: string;
}

export class Program implements IProgram {
    constructor(public id?: number, public programTitle?: string, public programLink?: string, public programDesc?: string) {}
}

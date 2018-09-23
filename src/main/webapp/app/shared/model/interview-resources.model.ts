export interface IInterviewResources {
    id?: number;
    resourceLink?: string;
    desc?: string;
}

export class InterviewResources implements IInterviewResources {
    constructor(public id?: number, public resourceLink?: string, public desc?: string) {}
}

export interface IJob {
    id?: number;
    jobTitle?: string;
    jobLink?: string;
    jobDesc?: string;
    company?: string;
    city?: string;
}

export class Job implements IJob {
    constructor(
        public id?: number,
        public jobTitle?: string,
        public jobLink?: string,
        public jobDesc?: string,
        public company?: string,
        public city?: string
    ) {}
}

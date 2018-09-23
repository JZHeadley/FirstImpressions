import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IInterviewResources } from 'app/shared/model/interview-resources.model';

type EntityResponseType = HttpResponse<IInterviewResources>;
type EntityArrayResponseType = HttpResponse<IInterviewResources[]>;

@Injectable({ providedIn: 'root' })
export class InterviewResourcesService {
    private resourceUrl = SERVER_API_URL + 'api/interview-resources';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/interview-resources';

    constructor(private http: HttpClient) {}

    create(interviewResources: IInterviewResources): Observable<EntityResponseType> {
        return this.http.post<IInterviewResources>(this.resourceUrl, interviewResources, { observe: 'response' });
    }

    update(interviewResources: IInterviewResources): Observable<EntityResponseType> {
        return this.http.put<IInterviewResources>(this.resourceUrl, interviewResources, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IInterviewResources>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInterviewResources[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IInterviewResources[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClothingCompany } from 'app/shared/model/clothing-company.model';

type EntityResponseType = HttpResponse<IClothingCompany>;
type EntityArrayResponseType = HttpResponse<IClothingCompany[]>;

@Injectable({ providedIn: 'root' })
export class ClothingCompanyService {
    private resourceUrl = SERVER_API_URL + 'api/clothing-companies';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/clothing-companies';

    constructor(private http: HttpClient) {}

    create(clothingCompany: IClothingCompany): Observable<EntityResponseType> {
        return this.http.post<IClothingCompany>(this.resourceUrl, clothingCompany, { observe: 'response' });
    }

    update(clothingCompany: IClothingCompany): Observable<EntityResponseType> {
        return this.http.put<IClothingCompany>(this.resourceUrl, clothingCompany, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IClothingCompany>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IClothingCompany[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IClothingCompany[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}

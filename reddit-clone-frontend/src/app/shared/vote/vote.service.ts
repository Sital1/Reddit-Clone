import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { VotePayload } from './vote-payload';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  baseUrl=environment.baseUrl;

  constructor(private httpClient: HttpClient) { }

  vote(votePayLoad:VotePayload):Observable<any>{
    return this.httpClient.post(`${this.baseUrl}api/votes/`, votePayLoad);
  }

}

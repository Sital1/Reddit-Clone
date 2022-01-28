import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { SubredditModel } from './subreddit-response';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {

  baseUrl = environment.baseUrl;
  
  constructor(private http: HttpClient) { }


  getAllSubreddits():Observable<Array<SubredditModel>>{
   return this.http.get<Array<SubredditModel>>(`${this.baseUrl}api/subreddit`);
  }

  createSubReddit(subRedditModel: SubredditModel):Observable<SubredditModel>{
    return this.http.post<SubredditModel>(`${this.baseUrl}api/subreddit`,subRedditModel);
  }

}
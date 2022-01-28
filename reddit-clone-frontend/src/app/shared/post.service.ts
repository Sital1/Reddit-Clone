import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.prod';
import { CreatePostPayload } from '../components/post/create-post/create-post.payload';
import { PostModel } from './post-model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  baseUrl = environment.baseUrl;
 
  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>>{
      return this.http.get<Array<PostModel>>(`${this.baseUrl}api/posts/`);
  }

  createPost(postPayLoad:CreatePostPayload):Observable<any>{
    return this.http.post(`${this.baseUrl}api/posts/`,postPayLoad)
  }

  getPost(postId: number):Observable<PostModel>{
    return this.http.get<PostModel>(`${this.baseUrl}api/posts/${postId}`);
  }

  getAllPostByUser(username:string):Observable<Array<PostModel>>{
    return this.http.get<Array<PostModel>>(`${this.baseUrl}api/posts/by-user/${username}`)
  }

}

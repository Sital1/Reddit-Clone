import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreatePostPayload } from '../components/post/create-post/create-post.payload';
import { PostModel } from './post-model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
 
  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>>{
      return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/');
  }

  createPost(postPayLoad:CreatePostPayload):Observable<any>{
    return this.http.post('http://localhost:8080/api/posts',postPayLoad)
  }

  getPost(postId: number):Observable<PostModel>{
    return this.http.get<PostModel>(`http://localhost:8080/api/posts/${postId}`);
  }

}

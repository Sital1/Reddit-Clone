import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { PostService } from 'src/app/shared/post.service';
import { SubredditModel } from 'src/app/shared/subreddit/subreddit-response';
import { SubredditService } from 'src/app/shared/subreddit/subreddit.service';
import { CreatePostPayload } from './create-post.payload';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  createPostForm: FormGroup
  postPayLoad: CreatePostPayload
  subreddits?: Array<SubredditModel>

  constructor(private router: Router, 
    private subRedditService: SubredditService,
    private postService: PostService) {
      
      this.createPostForm= new FormGroup({
        postName: new FormControl('',Validators.required),
        url:new FormControl('',Validators.required),
        subreddit: new FormControl('',Validators.required),
        description: new FormControl('',Validators.required)
      })
      
      this.postPayLoad={
        postName:'',
        url:'',
        subreddit:'',
        description:''
      }
     }

  ngOnInit(): void {
    this.subRedditService.getAllSubreddits()
    .subscribe({
      next: data => this.subreddits=data,
      error: error => throwError(()=> error)
    })

  }

  createPost(){
    this.postPayLoad.postName=this.createPostForm?.get('postName')?.value;
    this.postPayLoad.description=this.createPostForm?.get('description')?.value;
    this.postPayLoad.url=this.createPostForm?.get('url')?.value;
    this.postPayLoad.subreddit=this.createPostForm?.get('subreddit')?.value;

    this.postService.createPost(this.postPayLoad).subscribe({
      next: ()=>this.router.navigateByUrl('/'),
      error: error=> throwError(()=>error)
    })
  }

  discardPost(){
    this.router.navigateByUrl("/");
  }

}

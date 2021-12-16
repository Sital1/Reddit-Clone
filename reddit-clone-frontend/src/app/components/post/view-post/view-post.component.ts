import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentService } from 'src/app/shared/comment/comment-service.service';
import { CommentPayload } from 'src/app/shared/comment/comment.payload';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

  postId: number;
  post!: PostModel;
  commentForm: FormGroup;
  commentPayload : CommentPayload;
  comments?: CommentPayload[];
  
  constructor( private activatedRoute: ActivatedRoute, private postService: PostService,
    private commentService: CommentService) { 
    this.postId = this.activatedRoute.snapshot.params['id'];
    console.log(this.postId);
    this.postService.getPost(this.postId) 
    .subscribe({
      next: data => this.post=data,
      error: e => throwError(()=> e)
    })

    this.commentForm = new FormGroup({
      text: new FormControl('',Validators.required)
    });

    this.commentPayload={
      text: '',
      postId:this.postId
    }

  }

  ngOnInit(): void {
    this.getPostById();
    this.getCommentsForPost();
  }

  postComment(){
    this.commentPayload.text = this.commentForm.get('text')?.value;
    this.commentService.postComment(this.commentPayload)
    .subscribe({
      next: ()=> {
        this.commentForm.get('text')?.setValue('')
        this.getCommentsForPost();
      },
      error: (e: any) => throwError(()=> e)
    })
  }

  getPostById(){
    this.postService.getPost(this.postId).subscribe({
      next: data => this.post = data,
      error: e => throwError(()=> e)
    })
  }

  getCommentsForPost(){
    this.commentService.getAllCommentsForPost(this.postId).subscribe({
      next: data => this.comments = data,
      error: e => throwError(()=> e)
    })
  }



}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentService } from 'src/app/shared/comment/comment-service.service';
import { CommentPayload } from 'src/app/shared/comment/comment.payload';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  name: string
  posts?: PostModel[]
  comments?: CommentPayload[]
  postLength?: number
  commentLength?: number

  constructor(private activatedRoute: ActivatedRoute,private postService:PostService,
    private commentService:CommentService) {

      this.name = this.activatedRoute.snapshot.params['name'];

      this.postService.getAllPostByUser(this.name)
      .subscribe({
        next: d => {
          this.posts = d;
          this.postLength = d.length;
        },
        error: e => throwError(()=> e)
      })

      this.commentService.getAllCommentsByUser(this.name)
      .subscribe({
        next: d => {
          this.comments = d;
          this.commentLength = d.length;
        },
        error: e => throwError(()=> e)
      })
                
   }

  ngOnInit(): void {
  }

}

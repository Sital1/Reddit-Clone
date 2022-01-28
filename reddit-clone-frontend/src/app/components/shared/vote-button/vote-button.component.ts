import { Component, Input, OnInit } from '@angular/core';
import { faArrowUp, faArrowDown} from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';
import { VotePayload } from 'src/app/shared/vote/vote-payload';
import { VoteType } from 'src/app/shared/vote/vote-type';
import { VoteService } from 'src/app/shared/vote/vote.service';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

  faArrowUp = faArrowUp;
  faArrowDown=faArrowDown;

  upvoteColor?: string;
  downvoteColor?: string;
  votePayload: VotePayload

  @Input()
  post!: PostModel

  constructor(private voteService:VoteService, private postService: PostService,
    private toastr: ToastrService) { 
      this.votePayload={
        voteType : undefined,
        postId : undefined
      }
    }

  ngOnInit(): void {
  }

  downvotePost(){
    this.votePayload.voteType= VoteType.DOWNVOTE;
    this.vote();
  }

  upvotePost(){
    this.votePayload.voteType= VoteType.UPVOTE;
    this.vote();
  }

  vote(){
    this.votePayload.postId = this.post?.postId;
    this.voteService.vote(this.votePayload)
    .subscribe({
      next: d => this.updateVotDetails(),
      error: e => {
        this.toastr.error(e.error.message);
        throwError(()=>e);
      }
    })
  }

  updateVotDetails(){
    this.postService.getPost(this.post.postId)
    .subscribe(post => this.post = post)
  }

}

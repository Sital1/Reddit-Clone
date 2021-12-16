import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { SubredditModel } from 'src/app/shared/subreddit/subreddit-response';
import { SubredditService } from 'src/app/shared/subreddit/subreddit.service';

@Component({
  selector: 'app-create-subreddit',
  templateUrl: './create-subreddit.component.html',
  styleUrls: ['./create-subreddit.component.css']
})
export class CreateSubredditComponent implements OnInit {

  createSubRedditForm: FormGroup;
  subRedditModel: SubredditModel;
  title = new FormControl('');
  description = new FormControl('');


  constructor(private router: Router, private subRedditService: SubredditService) {
    this.createSubRedditForm = new FormGroup({
      title : new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    this.subRedditModel = {
      name: '',
      description: ''
    }

   }

  ngOnInit(): void {
  }

  discard(){
    this.router.navigateByUrl('/');
  }

  createSubReddit(){
    this.subRedditModel.name = this.createSubRedditForm.get('title')?.value;
    this.subRedditModel.description = this.createSubRedditForm.get('description')?.value;

    this.subRedditService.createSubReddit(this.subRedditModel)
    .subscribe(
      {
        next: data => this.router.navigateByUrl('/list-subreddits'),
        error: error => throwError(() => error)
      }
    )
  }


}

import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import { SubredditModel } from 'src/app/shared/subreddit/subreddit-response';
import { SubredditService } from 'src/app/shared/subreddit/subreddit.service';

@Component({
  selector: 'app-list-subreddits',
  templateUrl: './list-subreddits.component.html',
  styleUrls: ['./list-subreddits.component.css']
})
export class ListSubredditsComponent implements OnInit {

  subReddits?: Array<SubredditModel>;

  constructor(private subRedditService: SubredditService) { }

  ngOnInit(): void {
    this.subRedditService.getAllSubreddits()
    .subscribe(
      {
        next: data => this.subReddits = data,
        error: error => throwError(()=> error) 
      }
    )
  }

}

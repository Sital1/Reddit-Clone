import { Component, OnInit } from '@angular/core';
import { SubredditModel } from 'src/app/shared/subreddit/subreddit-response';
import { SubredditService } from 'src/app/shared/subreddit/subreddit.service';

@Component({
  selector: 'app-subreddit-side-bar',
  templateUrl: './subreddit-side-bar.component.html',
  styleUrls: ['./subreddit-side-bar.component.css']
})
export class SubredditSideBarComponent implements OnInit {

  subReddits? : Array<SubredditModel>;

  displayViewAll = false;

  constructor(private subRedditService: SubredditService) { 
    this.subRedditService.getAllSubreddits()
    .subscribe(
      data => {
        if(data.length >=4){
          this.subReddits = data.splice(0,3);
          this.displayViewAll = true;
        }
        else{
          this.subReddits = data;
 
        }
           }
    );
  }

  ngOnInit(): void {
  }

}

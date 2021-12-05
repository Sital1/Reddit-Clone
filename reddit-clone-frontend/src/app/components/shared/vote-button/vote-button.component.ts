import { Component, Input, OnInit } from '@angular/core';
import { faArrowUp, faArrowDown,faComments } from '@fortawesome/free-solid-svg-icons';
import { PostModel } from 'src/app/shared/post-model';

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

  @Input()
  post!: PostModel

  constructor() { }

  ngOnInit(): void {
  }

  downvotePost(){}
  upvotePost(){}

}

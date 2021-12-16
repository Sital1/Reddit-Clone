import { Component, OnInit } from '@angular/core';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';
import {faComments } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';


@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css']
})
export class PostTileComponent implements OnInit {


  faComments=faComments;


  posts$: Array<PostModel> = [];

  constructor(private postService: PostService, private router:Router) { 
    this.postService.getAllPosts()
    .subscribe(
     post => {
       this.posts$ = post;
     }
    )
  }

  ngOnInit(): void {
  }

  goToPost(postId:number){

    this.router.navigateByUrl('/view-post/'+postId);

  }


}

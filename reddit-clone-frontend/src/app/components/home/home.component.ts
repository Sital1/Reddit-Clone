import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';




@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts: Array<PostModel> = [];

  constructor(private postService: PostService) { 
    this.postService.getAllPosts()
    .subscribe({
      next : d => this.posts = d,
      error: e=> throwError(()=> e)
    })

  }

  ngOnInit(): void {
  }


}

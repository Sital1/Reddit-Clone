import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { SignupComponent } from './components/auth/signup/signup.component';
import { UserProfileComponent } from './components/auth/user-profile/user-profile.component';
import { HomeComponent } from './components/home/home.component';
import { CreatePostComponent } from './components/post/create-post/create-post.component';
import { ViewPostComponent } from './components/post/view-post/view-post.component';
import { CreateSubredditComponent } from './components/subreddit/create-subreddit/create-subreddit.component';
import { ListSubredditsComponent } from './components/subreddit/list-subreddits/list-subreddits.component';

const routes: Routes = [
  {path:'', component:HomeComponent},
  {path:'view-post/:id', component:ViewPostComponent},
  {path:'user-profile/:name', component:UserProfileComponent},
  {path:'list-subreddits', component:ListSubredditsComponent},
  {path:'create-post', component:CreatePostComponent},
  {path:'create-subreddit', component: CreateSubredditComponent},
  {path: 'signup', component: SignupComponent},
  {path:'login',component:LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

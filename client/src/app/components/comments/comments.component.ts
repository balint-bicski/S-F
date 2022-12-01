import {Component, Input, OnInit} from "@angular/core";
import {CaffFileService, CommentDto} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html'
})
export class CommentsComponent implements OnInit {
  // The CAFF to retrieve the comments for, input attribute.
  @Input() caffId?: number;
  // The list of previous comments.
  comments: Array<CommentDto>;

  constructor(
    private snackBar: SnackBarService,
    private caffService: CaffFileService
  ) {
    // TODO Temporary until backend is working
    this.comments = [
      {id: 0, creator: "Funky commenter", createdDate: "11:47", content: "Did you ever hear the tragedy of..."},
      {id: 1, creator: "Another guy", createdDate: "11:50", content: "This image doesn't even have a preview!"}
    ];
  }

  ngOnInit() {
    this.loadComments();
  }

  loadComments() {
    this.caffService.getComments(this.caffId).subscribe({
      next: comments => this.comments = comments,
      error: () => this.snackBar.error("Could not load comments! The server probably can't be reached!")
    });
  }

  sendComment(text: string) {
    this.caffService.createComment(this.caffId, text).subscribe({
      next: () => this.loadComments(),
      error: () => this.snackBar.error("Comment could not be added!")
    });
  }

  deleteComment(commentId: number) {
    this.caffService.deleteComment(this.caffId, commentId).subscribe({
      next: () => this.loadComments(),
      error: () => this.snackBar.error("Comment could not be removed!")
    });
  }
}

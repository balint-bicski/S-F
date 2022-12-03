import {Component, Input} from "@angular/core";
import {CaffFileService, CommentDto} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html'
})
export class CommentsComponent {

  noteControl = new FormControl("");
  comments: Array<CommentDto> = [];

  constructor(
    private snackBar: SnackBarService,
    private caffService: CaffFileService
  ) {
  }

  _caffId?: number;

  // The CAFF to retrieve the comments for, input attribute.
  @Input()
  set caffId(id: number) {
    this._caffId = id;
    if (!!id) {
      this.loadComments();
    }
  };

  loadComments() {
    this.caffService.getComments(this._caffId).subscribe({
      next: comments => this.comments = comments,
      error: () => this.snackBar.error("Could not load comments! The server probably can't be reached!")
    });
  }

  sendComment() {
    this.caffService.createComment(this._caffId, this.noteControl.value).subscribe({
      next: () => {
        this.noteControl.reset();
        this.loadComments();
      },
      error: () => this.snackBar.error("Comment could not be added!")
    });
  }

  deleteComment(commentId: number) {
    this.caffService.deleteComment(this._caffId, commentId).subscribe({
      next: () => this.loadComments(),
      error: () => this.snackBar.error("Comment could not be removed!")
    });
  }
}

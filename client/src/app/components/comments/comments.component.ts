import {Component, Input} from "@angular/core";
import {Authority, CaffFileService, CommentDto} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {FormControl, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {timestampPrettyPrint} from "../../util/encoding.util";
import {MatDialog} from "@angular/material/dialog";
import {ConfirmDialogComponent} from "../details-view/confirm-dialog.component";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html'
})
export class CommentsComponent {

  noteControl = new FormControl("", [Validators.required]);
  comments: Array<CommentDto> = [];

  // User permissions for conditional formatting.
  showDeleteButtons: boolean = false;
  showSubmitButton: boolean = false;

  constructor(
    private snackBar: SnackBarService,
    private caffService: CaffFileService,
    private authService: AuthService,
    private dialog: MatDialog,
  ) {
    this.decideUserPermissions();
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
      next: comments => {
        this.comments = comments.map(it => ({...it, createdDate: timestampPrettyPrint(it.createdDate)}))
      },
      error: () => this.snackBar.error("Could not load comments! The server probably can't be reached!")
    });
  }

  sendComment() {
    this.caffService.createComment(this._caffId, this.noteControl.value).subscribe({
      next: () => {
        this.noteControl.reset();
        this.loadComments();
        this.snackBar.success("Comment has been successfully created");
      },
      error: () => this.snackBar.error("Comment could not be added!")
    });
  }

  deleteComment(commentId: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to delete the comment?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.caffService.deleteComment(this._caffId, commentId).subscribe({
          next: () => this.loadComments(),
          error: () => this.snackBar.error("Comment could not be removed!")
        });
      }
    });
  }

  private decideUserPermissions() {
    this.showSubmitButton = this.authService.hasRightToAccess(Authority.WriteNote);
    this.showDeleteButtons = this.authService.hasRightToAccess(Authority.DeleteNote);
  }
}

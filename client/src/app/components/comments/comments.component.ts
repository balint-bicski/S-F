import {Component, Input, OnInit} from "@angular/core";
import {CaffFileService, CommentDto} from "../../../../target/generated-sources";

@Component({
  selector: 'app-comments',
  templateUrl: './comments.component.html',
  styleUrls: ['./comments.component.scss']
})
export class CommentsComponent implements OnInit {
  // The CAFF to retrieve the comments for, input attribute.
  @Input() caffId?: number;
  // The list of previous comments.
  comments: Array<CommentDto>;

  constructor(
    private caffService: CaffFileService
  ) {
    // TODO Temporary until backend is working
    this.comments = [
      {id: 0, creator: "Funky commenter", createdDate: "11:47", content: "Did you ever hear the tragedy of..."},
      {id: 1, creator: "Another guy", createdDate: "11:50", content: "This image doesn't even have a preview!"}
    ];
  }

  ngOnInit() {
    this.caffService.getComments(this.caffId).subscribe(comments => this.comments = comments);
  }

  sendComment(text: string) {
    this.caffService.createComment(this.caffId, text).subscribe(/* TODO Handle received new comment */);
  }

  deleteComment(commentId: number) {
    this.caffService.deleteComment(this.caffId, commentId).subscribe(/* TODO Remove comment by id */);
  }
}

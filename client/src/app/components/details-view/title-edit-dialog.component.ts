import {Component, Inject} from "@angular/core";
import {CaffFileService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-title-edit-dialog',
  templateUrl: './title-edit-dialog.component.html'
})
export class TitleEditDialogComponent {
  constructor(
    private snackBar: SnackBarService,
    private caffService: CaffFileService,
    private dialog: MatDialogRef<TitleEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { caffId: number, originalTitle: string, originalDesc: string,
      originalTime: string, originalWP: string }
  ) {
  }

  saveNameChange(title: string, desc, time, wp) {
    if (!title || !desc || !time || !wp) {
      this.snackBar.error("Data is incomplete!");
      return;
    }

    this.caffService.updateCaffFile(this.data.caffId, title, desc, time, wp).subscribe({
      next: () => this.closeDialog(true),
      error: () => this.snackBar.error("Could not edit submission, make sure you have the proper permissions!")
    })
  }

  closeDialog(titleChanged: boolean) {
    this.dialog.close(titleChanged);
  }
}

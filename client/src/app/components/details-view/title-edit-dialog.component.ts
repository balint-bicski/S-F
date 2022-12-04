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
    @Inject(MAT_DIALOG_DATA) public data: { caffId: number, originalTitle: string }
  ) {
  }

  saveNameChange(title: string) {
    if (!title) {
      this.snackBar.error("No title was given!");
      return;
    }

    this.caffService.updateCaffFile(this.data.caffId, title).subscribe({
      next: () => this.closeDialog(true),
      error: () => this.snackBar.error("Could not rename submission, make sure you have the proper permissions!")
    })
  }

  closeDialog(titleChanged: boolean) {
    this.dialog.close(titleChanged);
  }
}

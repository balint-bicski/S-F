import {Component, Inject} from "@angular/core";
import {EventService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-title-edit-dialog',
  templateUrl: './event-edit-dialog.component.html'
})
export class EventEditDialogComponent {
  constructor(
    private snackBar: SnackBarService,
    private eventService: EventService,
    private dialog: MatDialogRef<EventEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { eventId: number, originalTitle: string, originalDesc: string,
      originalTime: string, originalWP: string }
  ) {
  }

  saveNameChange(title: string, desc, time, wp) {
    if (!title || !desc || !time || !wp) {
      this.snackBar.error("Data is incomplete!");
      return;
    }

    this.eventService.updateEvent(this.data.eventId, title, desc, time, wp).subscribe({
      next: () => this.closeDialog(true),
      error: () => this.snackBar.error("Could not edit submission, make sure you have the proper permissions!")
    })
  }

  closeDialog(titleChanged: boolean) {
    this.dialog.close(titleChanged);
  }
}

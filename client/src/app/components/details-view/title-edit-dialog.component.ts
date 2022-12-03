import {Component} from "@angular/core";
import {CaffFileService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-title-edit-dialog',
  templateUrl: './title-edit-dialog.component.html'
})
export class TitleEditDialogComponent {
  caffId: number;
  originalTitle: string;

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private caffService: CaffFileService
  ) {}

  saveNameChange(title: string) {
    if (!title) {
      this.snackBar.error("No title was given!");
      return;
    }

    this.caffService.updateCaffFile(this.caffId, title).subscribe({
      next: () => this.router.navigate(["/details/" + this.caffId]),
      error: () => this.snackBar.error("Could not rename submission, make sure you have the proper permissions!")
    })
  }
}

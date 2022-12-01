import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {CaffFileService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-upload-dialog',
  templateUrl: './upload-dialog.component.html'
})
export class UploadDialogComponent {
  file?: File;

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private caffService: CaffFileService
  ) {}

  uploadFile(title: string) {
    if (!this.file) {
      this.snackBar.error("No file was selected to upload!");
      return;
    }

    this.caffService.createCaffFile({ file: this.file, title: title }).subscribe({
      next: response => this.router.navigate(['/details/' + response.id]),
      error: () => this.snackBar.error("Could not create new CAFF file, please try again!")
    })
  }

  onFileSelected(event: Event) {
    this.file = (<HTMLInputElement> event.target).files[0];
  }
}

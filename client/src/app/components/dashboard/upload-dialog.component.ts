import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {CaffFileService} from "../../../../target/generated-sources";

@Component({
  selector: 'app-upload-dialog',
  templateUrl: './upload-dialog.component.html'
})
export class UploadDialogComponent {
  file?: File;

  constructor(
    private router: Router,
    private caffService: CaffFileService
  ) {}

  uploadFile(title: string) {
    if (!this.file) {
      // TODO Handle "no file selected" case
    }

    this.caffService.createCaffFile({ file: this.file, title: title }).subscribe({
      next: response => { this.router.navigate(['/details/' + response.id])},
      error: error => { /* TODO Handle error case */ }
    })
  }

  onFileSelected(event: Event) {
    this.file = (<HTMLInputElement> event.target).files[0];
  }
}

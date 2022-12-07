import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {CaffFileService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-upload-dialog',
  templateUrl: './upload-dialog.component.html'
})
export class UploadDialogComponent {

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private caffService: CaffFileService
  ) {
  }

  uploadFile(title: string, desc: string, time: string, wp: string) {
    if (new Date(time) <= new Date()) {
      this.snackBar.error("Invalid date");
      return;
    }
    this.checkWP(wp);

    this.caffService.createCaff(title, desc, time, wp).subscribe({
      next: response => {
        this.router.navigate(['/details/' + response.id]);
        this.snackBar.success("New event has been created");
      },
      error: () => this.snackBar.error("Could not create new event, please try again!")
    })
  }

  checkWP(wp) {
    const req = new XMLHttpRequest();
    // req.addEventListener("load", this.reqListener);
    const url = `https://www.openstreetmap.org/api/0.6/way/${wp}`
    req.open("GET", url);
    // If specified, responseType must be empty string or "document"
    req.responseType = 'document';

    // Force the response to be parsed as XML
    req.overrideMimeType('text/xml');

    req.onload = () => {
      if (req.readyState === req.DONE && req.status === 200) {
        console.log("Valid pitch ID!");
      } else {
        this.snackBar.error("Invalid pitch ID");
      }
    };
    req.send();
  }
}

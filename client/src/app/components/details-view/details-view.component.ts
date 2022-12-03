import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CaffDto, CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html'
})
export class DetailsViewComponent {
  // CAFF data and preview image.
  caff?: CaffDto;
  caffPreview?: SafeUrl;

  // CAFF details table information.
  displayedColumns: string[] = ['key', 'value'];
  detailsData: Array<Object> = [];

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private caffService: CaffFileService
  ) {
    this.loadCaffDetails();
    // TODO Temp values until backend is working
    /*this.caff = {
      ciffCount: 2,
      createdDate: "2022-11-22",
      creator: "Attila",
      id: 0,
      preview: undefined,
      size: 128256,
      title: "The CAFF that doesn't exist",
      uploader: "xXx_attila_xXx"
    };
    this.caffPreview = "https://picsum.photos/300/200";*/
  }

  onBackButtonClicked() {
    this.router.navigate(['/']);
  }

  onPurchaseButtonClicked() {
    this.router.navigate(['/details/' + this.caff.id + '/purchase']);
  }

  // Retrieves the CAFF to display from the server.
  private loadCaffDetails(): void {
    const caffId = Number(this.route.snapshot.paramMap.get('caffId'));
    this.caffService.getCaffFile(caffId).subscribe({
      next: caff => {
        this.caff = caff;
        //this.caffPreview = toSafeUrl(caff.preview, this.sanitizer);
        this.loadDetailsTableContent(caff)
      },
      error: () => this.snackBar.error("Could not load selected CAFF! Either the server can't be reached, or no such CAFF exists!")
    });
  }

  // Populates the details table with the relevant key-value pairs.
  private loadDetailsTableContent(caff: CaffDto): void {
    this.detailsData = [{key: "Original creator:", value: caff.creator},
      {key: "Creation date:", value: caff.createdDate},
      {key: "Number of frames:", value: caff.ciffCount},
      {key: "Uploader:", value: caff.uploader},
      {key: "File size:", value: caff.size}];
  }
}

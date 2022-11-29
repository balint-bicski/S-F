import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CaffDto, CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";

@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html',
  styleUrls: ['./details-view.component.scss']
})
export class DetailsViewComponent implements OnInit {
  // CAFF data and preview image.
  caff: CaffDto | undefined;
  caffPreview: SafeUrl | undefined;

  // CAFF details table information.
  displayedColumns: string[] = ['key', 'value'];
  detailsData: Array<Object> = [];

  constructor(
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private caffService: CaffFileService
  ) {
    // TODO Temp values until backend is working
    this.caff = {
      ciffCount: 2,
      createdDate: "2022-11-22",
      creator: "Attila",
      id: 0,
      preview: undefined,
      size: 128256,
      title: "The CAFF that doesn't exist",
      uploader: "xXx_attila_xXx"
    };
    this.caffPreview = "https://picsum.photos/300/200";
  }

  ngOnInit() {
    this.loadCaffDetails();
    this.loadDetailsTableContent(this.caff);
  }

  // Retrieves the CAFF to display from the server.
  private loadCaffDetails(): void {
    const caffId = Number(this.route.snapshot.paramMap.get('caffId'));

    // TODO Need to get authorization token here
    this.caffService.getCaffFile(caffId, "TODO").subscribe(caff => {
      this.caff = caff;
      this.caffPreview = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(caff.preview));
    });
  }

  // Populates the details table with the relevant key-value pairs.
  private loadDetailsTableContent(caff: CaffDto): void {
    this.detailsData.push({ key: "Original creator:", value: caff.creator });
    this.detailsData.push({ key: "Creation date:", value: caff.createdDate });
    this.detailsData.push({ key: "Number of frames:", value: caff.ciffCount });
    this.detailsData.push({ key: "Uploader:", value: caff.uploader });
    this.detailsData.push({ key: "File size:", value: caff.size });
  }
}

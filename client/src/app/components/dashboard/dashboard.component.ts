import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {CaffFileService, CaffSummaryDto} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UploadDialogComponent} from "./upload-dialog.component";

type SummaryWithPreview = CaffSummaryDto & { previewImage: SafeUrl };

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  // Contains the full list of the caff summaries.
  caffs: Array<SummaryWithPreview>;
  // Contains a filtered array based on the filter text.
  filteredCaffs: Array<SummaryWithPreview>;
  filterText: string;

  constructor(
    private sanitizer: DomSanitizer,
    private uploadDialog: MatDialog,
    private router: Router,
    private authService: AuthService,
    private caffService: CaffFileService
  ) {
    // TODO Temporary until backend is working
    this.caffs = [
      {id: 0, title: "You are", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
      {id: 1, title: "My fire", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
      {id: 2, title: "The one", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
      {id: 3, title: "Desire", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
      {id: 4, title: "Believe when I say", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
      {id: 5, title: "I want it that way", preview: new Blob(), previewImage: "https://picsum.photos/300/200"},
    ];
  }

  ngOnInit() {
    this.caffService.searchCaffFile("").subscribe(caffs => this.caffs = caffs.map(caff => ({
      ...caff, previewImage: this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(caff.preview))
    })));
    this.filteredCaffs = this.caffs;
  }

  // Redirects when a card was clicked.
  onListElementClicked(id: number) {
    this.router.navigate(["/details/" + id]);
  }

  // Refills the filtered list when the filter text changes.
  onFilterChanged() {
    this.filteredCaffs = this.caffs.filter(caff => caff.title.toLowerCase().includes(this.filterText.toLowerCase()));
  }

  onUploadButtonClicked() {
    this.uploadDialog.open(UploadDialogComponent);
  }
}

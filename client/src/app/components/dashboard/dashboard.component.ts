import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Authority, CaffFileService, CaffSummaryDto} from "../../../../target/generated-sources";
import {DomSanitizer} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {UploadDialogComponent} from "./upload-dialog.component";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  // Contains the full list of the caff summaries.
  caffs: Array<CaffSummaryDto>;
  // Contains a filtered array based on the filter text.
  filteredCaffs: Array<CaffSummaryDto>;
  filterText: string;

  // User information for conditional element display.
  showUploadButton: boolean = false;

  constructor(
    private snackBar: SnackBarService,
    private sanitizer: DomSanitizer,
    private uploadDialog: MatDialog,
    private router: Router,
    private authService: AuthService,
    private caffService: CaffFileService
  ) {
  }

  ngOnInit() {
    this.caffService.searchCaffFile("").subscribe({
      next: caffs => {
        this.caffs = caffs;
        this.filteredCaffs = this.caffs;
      },
      error: () => this.snackBar.error("Could not load previews! The server probably can't be reached!")
    });

    this.showUploadButton = this.authService.hasRightToAccess(Authority.UploadCaff);
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

  wpUrl(wp) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `//overpass-turbo.eu/map.html?Q=(%0A%20%20way(${wp})%3B%0A)%3B%20%20%0Aout%20geom%3B`)
  }
}

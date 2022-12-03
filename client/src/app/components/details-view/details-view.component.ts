import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Authority, CaffDto, CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {getItemFromStorage} from "../../util/session-storage.util";
import {TOKEN} from "../../util/session-storage-constant";
import {SnackBarService} from "../../services/snack-bar.service";
import {TitleEditDialogComponent} from "./title-edit-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html'
})
export class DetailsViewComponent implements OnInit {
  // CAFF data and preview image.
  caff: CaffDto | undefined;
  caffPreview: SafeUrl | undefined;

  // CAFF details table information.
  displayedColumns: string[] = ['key', 'value'];
  detailsData: Array<Object> = [];

  // User information for conditional element display.
  showPurchaseButton: boolean = false;
  showTitleEditButton: boolean = false;
  showDeleteButton: boolean = false;

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private caffService: CaffFileService,
    private editDialog: MatDialog,
    private authService: AuthService
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
    this.decideUserPermissions();
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

    this.caffService.getCaffFile(caffId, getItemFromStorage(TOKEN) || "").subscribe({
      next: caff => {
        this.caff = caff;
        this.caffPreview = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(caff.preview));
      },
      error: () => this.snackBar.error("Could not load selected CAFF! Either the server can't be reached, or no such CAFF exists!")
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

  // Decides whether the user is an admin currently or not
  private decideUserPermissions() {
    this.showPurchaseButton = this.authService.isUserLoggedIn && this.authService.hasAuthority(Authority.Payment);
    this.showTitleEditButton = this.authService.isUserLoggedIn && this.authService.hasAuthority(Authority.ModifyCaff);
    this.showDeleteButton = this.authService.isUserLoggedIn && this.authService.hasAuthority(Authority.DeleteCaff);
  }

  onEditTitleButtonClicked() {
    let ref = this.editDialog.open(TitleEditDialogComponent);
    ref.componentInstance.caffId = this.caff.id;
    ref.componentInstance.originalTitle = this.caff.title;
  }

  onDeleteFileButtonClicked() {
    this.caffService.deleteCaffFile(this.caff.id).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => this.snackBar.error("Could not delete file! Make sure you have the proper permissions to do so!")
    });
  }
}

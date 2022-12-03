import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Authority, CaffDto, CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {SnackBarService} from "../../services/snack-bar.service";
import {TitleEditDialogComponent} from "./title-edit-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../services/auth.service";
import {timestampPrettyPrint, toSafeUrl} from "../../util/encoding.util";

@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html'
})
export class DetailsViewComponent {
  // CAFF data and preview image.
  caff?: CaffDto;
  caffPreview?: Promise<SafeUrl>;

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
    this.loadCaffDetails();
    this.decideUserPermissions();
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
        this.caffPreview = toSafeUrl(caff.preview, this.sanitizer);
        this.loadDetailsTableContent(caff);
      },
      error: () => this.snackBar.error("Could not load selected CAFF! Either the server can't be reached, or no such CAFF exists!")
    });
  }

  // Populates the details table with the relevant key-value pairs.
  private loadDetailsTableContent(caff: CaffDto): void {
    this.detailsData = [{key: "Original creator:", value: caff.creator},
      {key: "Creation date:", value: timestampPrettyPrint(caff.createdDate)},
      {key: "Number of frames:", value: caff.ciffCount},
      {key: "Uploader:", value: caff.uploader},
      {key: "File size:", value: caff.size + " bytes"}];
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

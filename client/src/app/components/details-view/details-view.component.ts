import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Authority, CaffDto, CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {SnackBarService} from "../../services/snack-bar.service";
import {TitleEditDialogComponent} from "./title-edit-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../services/auth.service";
import {timestampPrettyPrint, toSafeUrl} from "../../util/encoding.util";
import {ConfirmDialogComponent} from "./confirm-dialog.component";

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
    private dialog: MatDialog,
    private authService: AuthService
  ) {
    this.loadCaffDetails();
    this.decideUserPermissions();
  }

  onBackButtonClicked() {
    this.router.navigate(['/']);
  }

  onPurchaseButtonClicked() {
    this.router.navigate(['/details/' + this.caff.id + '/purchase']);
  }

  onEditTitleButtonClicked() {
    const dialogRef = this.dialog.open(TitleEditDialogComponent, {
      data: {caffId: this.caff.id, originalTitle: this.caff.title},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.loadCaffDetails();
        this.snackBar.success("The CAFF file title has been successfully edited")
      }
    });
  }

  onDeleteFileButtonClicked() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to delete the CAFF?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.caffService.deleteCaffFile(this.caff.id).subscribe({
          next: () => {
            this.router.navigate(['/']);
            this.snackBar.success("CAFF file has been deleted");
          },
          error: () => this.snackBar.error("Could not delete file! Make sure you have the proper permissions to do so!")
        });
      }
    });
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
    this.showPurchaseButton = this.authService.hasRightToAccess(Authority.Payment);
    this.showTitleEditButton = this.authService.hasRightToAccess(Authority.ModifyCaff);
    this.showDeleteButton = this.authService.hasRightToAccess(Authority.DeleteCaff);
  }
}

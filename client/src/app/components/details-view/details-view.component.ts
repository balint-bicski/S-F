import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Authority, CaffDto, CaffFileService, ParticipantDto} from "../../../../target/generated-sources";
import {DomSanitizer} from "@angular/platform-browser";
import {SnackBarService} from "../../services/snack-bar.service";
import {TitleEditDialogComponent} from "./title-edit-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../services/auth.service";
import {ConfirmDialogComponent} from "./confirm-dialog.component";


@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html'
})
export class DetailsViewComponent {
  // CAFF data and preview image.
  caff?: CaffDto;

  // CAFF details table information.
  displayedColumns: string[] = ['key', 'value'];
  detailsData: Array<Object> = [];

  // User information for conditional element display.
  showPurchaseButton: boolean = false;
  showTitleEditButton: boolean = false;
  showDeleteButton: boolean = false;
  showParticipantsDeleteButtons: boolean = false;

  participants: Array<ParticipantDto> = [];

  participantId: number;

  // User permissions for conditional formatting.


  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private caffService: CaffFileService,
    private dialog: MatDialog,
    private authService: AuthService,
  ) {
    this.init();
  }

  onBackButtonClicked() {
    this.router.navigate(['/']);
  }

  onPurchaseButtonClicked() {
    if (this.currentUserJoined()) {
      this.caffService.deleteParticipant(this.caff.id, this.authService.currentUser.id).subscribe({
        next: () => this.loadParticipants(),
        error: () => this.snackBar.error("Participant could not be removed!")
      });
      document.getElementById("joinButton").innerText = "Join";

    } else {
        this.caffService.addParticipant(this.caff.id, this.authService.currentUser.id).subscribe({
          next: () => {
            this.loadParticipants();
            this.snackBar.success("Participation recorded");
          },
          error: () => this.snackBar.error("Participation could not be added!")
        });
        document.getElementById("joinButton").innerText = "Leave";
      }
  }

  onEditTitleButtonClicked() {
    const dialogRef = this.dialog.open(TitleEditDialogComponent, {
      data: {caffId: this.caff.id, originalTitle: this.caff.title,
        originalDesc: this.caff.desc, originalTime: this.caff.time,
        originalWP: this.caff.wp
      },
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.init();
        this.snackBar.success("The event has been successfully edited")
      }
    });
  }

  onDeleteFileButtonClicked() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to delete the event?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.caffService.deleteCaffFile(this.caff.id).subscribe({
          next: () => {
            this.router.navigate(['/']);
            this.snackBar.success("Event has been deleted");
          },
          error: () => this.snackBar.error("Could not delete event! Make sure you have the proper permissions to do so!")
        });
      }
    });
  }


  // Retrieves the CAFF to display from the server.
  private init(): void {
    const caffId = Number(this.route.snapshot.paramMap.get('caffId'));
    this.caffService.getCaffFile(caffId).subscribe({
      next: caff => {
        this.caff = caff;
        this.loadDetailsTableContent(caff);
        this.loadParticipants();
        this.decideUserPermissions();
        if (this.currentUserJoined())
          document.getElementById("joinButton").innerText = "Leave";
      },
      error: () => this.snackBar.error("Could not load selected event! Either the server can't be reached, or no such event exists!")
    });
  }

  // Populates the details table with the relevant key-value pairs.
  private loadDetailsTableContent(caff: CaffDto): void {
    this.detailsData = [
      {key: "Uploader:", value: caff.uploader},
      {key: "Description:", value: caff.desc},
      {key: "Event date:", value: caff.time},
      ];
  }

  // Decides whether the user is an admin currently or not
  private decideUserPermissions() {
    this.showPurchaseButton = this.authService.hasRightToAccess(Authority.Payment);
    this.showTitleEditButton = this.authService.hasRightToModify(this.caff, Authority.ModifyCaff);
    this.showDeleteButton = this.authService.hasRightToModify(this.caff, Authority.DeleteCaff);
    this.showParticipantsDeleteButtons = this.authService.hasRightToModify(this.caff, Authority.DeleteNote);

  }

  wpUrl() {
    if (this.caff == null) {
      return this.sanitizer.bypassSecurityTrustResourceUrl('')
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `//overpass-turbo.eu/map.html?Q=(%0A%20%20way(${this.caff.wp})%3B%0A)%3B%20%20%0Aout%20geom%3B`)
  }

  loadParticipants() {
    this.caffService.getParticipants(this.caff.id).subscribe({
      next: participant => {
        this.participants = participant.map(it => ({...it}))
      },
      error: () => this.snackBar.error("Could not load participants! The server probably can't be reached!")
    });
  }

  deleteParticipant(participantId: number) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to renounce your participation?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.caffService.deleteParticipant(this.caff.id, participantId).subscribe({
          next: () => this.loadParticipants(),
          error: () => this.snackBar.error("Participant could not be removed!")
        });
      }
    });
  }

  currentUserJoined() {
    return this.participants.some(p => p.participant.id == this.authService.currentUser.id)
  }
}

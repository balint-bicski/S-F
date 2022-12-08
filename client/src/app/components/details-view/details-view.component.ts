import {Component} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Authority, EventDto, EventService, ParticipantDto} from "../../../../target/generated-sources";
import {DomSanitizer} from "@angular/platform-browser";
import {SnackBarService} from "../../services/snack-bar.service";
import {EventEditDialogComponent} from "./event-edit-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {AuthService} from "../../services/auth.service";
import {ConfirmDialogComponent} from "./confirm-dialog.component";


@Component({
  selector: 'app-details-view',
  templateUrl: './details-view.component.html'
})
export class DetailsViewComponent {
  // Event data and preview image.
  event?: EventDto;

  // Event details table information.
  displayedColumns: string[] = ['key', 'value'];
  detailsData: Array<Object> = [];

  // User information for conditional element display.
  showJoinLeaveButton: boolean = false;
  showEventEditButton: boolean = false;
  showDeleteButton: boolean = false;
  showParticipantsDeleteButtons: boolean = false;
  participants: Array<ParticipantDto> = [];

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private eventService: EventService,
    private dialog: MatDialog,
    private authService: AuthService,
  ) {
    this.init();
  }

  onBackButtonClicked() {
    this.router.navigate(['/']);
  }

  onJoinLeaveButtonClicked() {
    if (document.getElementById("joinButton").innerText == "Leave") {
      this.eventService.deleteParticipantbyUserId(this.event.id, this.authService.currentUser.id).subscribe({
        next: () => this.loadParticipants(),
        error: () => this.snackBar.error("Participant could not be removed!")
      });
      document.getElementById("joinButton").innerText = "Join";

    } else {
        this.eventService.addParticipant(this.event.id, this.authService.currentUser.id).subscribe({
          next: () => {
            this.loadParticipants();
          },
          error: () => this.snackBar.error("Participation could not be added!")
        });
        document.getElementById("joinButton").innerText = "Leave";
      }
  }

  onEditEventButtonClicked() {
    const dialogRef = this.dialog.open(EventEditDialogComponent, {
      data: {eventId: this.event.id, originalTitle: this.event.title,
        originalDesc: this.event.desc, originalTime: this.event.time,
        originalWP: this.event.wp
      },
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.init();
        this.snackBar.success("The event has been successfully edited")
      }
    });
  }

  onDeleteEventButtonClicked() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {title: "Are you sure you'd like to delete the event?"},
    });
    dialogRef.afterClosed().subscribe(successModification => {
      if (successModification) {
        this.eventService.deleteEvent(this.event.id).subscribe({
          next: () => {
            this.router.navigate(['/']);
            this.snackBar.success("Event has been deleted");
          },
          error: () => this.snackBar.error("Could not delete event! Make sure you have the proper permissions to do so!")
        });
      }
    });
  }


  // Retrieves the Event to display from the server.
  private init(): void {
    const eventId = Number(this.route.snapshot.paramMap.get('eventId'));
    this.eventService.getEvent(eventId).subscribe({
      next: event => {
        this.event = event;
        this.loadDetailsTableContent(event);
        this.loadParticipants();
        this.decideUserPermissions();
      },
      error: () => this.snackBar.error("Could not load selected event! Either the server can't be reached, or no such event exists!")
    });
  }

  // Populates the details table with the relevant key-value pairs.
  private loadDetailsTableContent(event: EventDto): void {
    this.detailsData = [
      {key: "Creator:", value: event.creator},
      {key: "Description:", value: event.desc},
      {key: "Event date:", value: event.time},
      ];
  }

  // Decides whether the user is an admin currently or not
  private decideUserPermissions() {
    this.showJoinLeaveButton = this.authService.isUserLoggedIn;
    this.showEventEditButton = this.authService.hasRightToModify(this.event, Authority.ModifyEvent);
    this.showDeleteButton = this.authService.hasRightToModify(this.event, Authority.DeleteEvent);
    console.log(this.authService.hasRightToModify(this.event, Authority.DeleteNote));
    this.showParticipantsDeleteButtons = this.authService.hasRightToModify(this.event, Authority.DeleteNote);
  }

  wpUrl() {
    if (this.event == null) {
      return this.sanitizer.bypassSecurityTrustResourceUrl('')
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `//overpass-turbo.eu/map.html?Q=(%0A%20%20way(${this.event.wp})%3B%0A)%3B%20%20%0Aout%20geom%3B`)
  }

  loadParticipants() {
    this.eventService.getParticipants(this.event.id).subscribe({
      next: participants => {
        this.participants = participants;
        if (this.currentUserJoined()) {
          document.getElementById("joinButton").innerText = "Leave";
        }
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
        this.eventService.deleteParticipant(participantId).subscribe({
          next: () => this.loadParticipants(),
          error: () => this.snackBar.error("Participant could not be removed!")
        });
        this.loadParticipants();
      }
    });
  }

  currentUserJoined() {
    if (this.participants.length == 0)
      return false;
    return this.participants.some(p => p.userId == this.authService.currentUser.id);
  }
}

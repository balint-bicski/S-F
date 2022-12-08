import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Authority, EventService, EventSummaryDto} from "../../../../target/generated-sources";
import {DomSanitizer} from "@angular/platform-browser";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {CreateDialogComponent} from "./create-dialog.component";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  // Contains the full list of the event summaries.
  events: Array<EventSummaryDto>;
  // Contains a filtered array based on the filter text.
  filteredEvents: Array<EventSummaryDto>;
  filterText: string;
  // User information for conditional element display.
  showCreateButton: boolean = false;

  constructor(
    private snackBar: SnackBarService,
    private sanitizer: DomSanitizer,
    private createDialog: MatDialog,
    private router: Router,
    private authService: AuthService,
    private eventService: EventService
  ) {
  }

  ngOnInit() {
    this.eventService.searchEvent("").subscribe({
      next: events => {
        this.events = events;
        this.filteredEvents = this.events;
      },
      error: () => this.snackBar.error("Could not load previews! The server probably can't be reached!")
    });
    this.showCreateButton = this.authService.hasRightToAccess(Authority.CreateEvent);
  }

  // Redirects when a card was clicked.
  onListElementClicked(id: number) {
    this.router.navigate(["/details/" + id]);
  }

  // Refills the filtered list when the filter text changes.
  onFilterChanged() {
    this.filteredEvents = this.events.filter(event => event.title.toLowerCase().includes(this.filterText.toLowerCase()));
  }

  onCreateButtonClicked() {
    this.createDialog.open(CreateDialogComponent);
  }

  wpUrl(wp) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(
      `//overpass-turbo.eu/map.html?Q=(%0A%20%20way(${wp})%3B%0A)%3B%20%20%0Aout%20geom%3B`)
  }
}

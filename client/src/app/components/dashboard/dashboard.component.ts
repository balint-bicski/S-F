import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {CaffFileService} from "../../../../target/generated-sources";
import {DomSanitizer, SafeUrl} from "@angular/platform-browser";
import {Router} from "@angular/router";

class CaffSummary {
  id: number;
  title: string;
  preview: SafeUrl;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  // Contains the full list of the caff summaries.
  caffs: Array<CaffSummary>;
  // Contains a filtered array based on the filter text.
  filteredCaffs: Array<CaffSummary>;
  filterText: string;

  constructor(
    private router: Router,
    private authService: AuthService,
    private sanitizer: DomSanitizer,
    private caffService: CaffFileService
  ) {
    // TODO Temporary until backend is working
    this.caffs = [
      {id: 0, title: "You are", preview: "https://picsum.photos/300/200"},
      {id: 1, title: "My fire", preview: "https://picsum.photos/300/200"},
      {id: 2, title: "The one", preview: "https://picsum.photos/300/200"},
      {id: 3, title: "Desire", preview: "https://picsum.photos/300/200"},
      {id: 4, title: "Believe when I say", preview: "https://picsum.photos/300/200"},
      {id: 5, title: "I want it that way", preview: "https://picsum.photos/300/200"},
    ];
  }

  ngOnInit() {
    this.caffService.searchCaffFile("").subscribe(caffs => {
      this.caffs = caffs.map(caff => ({
        id: caff.id,
        title: caff.title,
        preview: this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(caff.preview))
      }));
    });

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
}

import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {CaffFileService} from "../../../../target/generated-sources";
import {SnackBarService} from "../../services/snack-bar.service";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html'
})
export class PaymentComponent implements OnInit {
  caffId: number | undefined;

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private caffService: CaffFileService
  ) {}

  ngOnInit() {
    this.caffId = Number(this.route.snapshot.paramMap.get('caffId'));
  }

  onButtonClicked() {
    this.caffService.downloadCaffFile(this.caffId).subscribe({
      next: blob => {
        var url = URL.createObjectURL(blob);
        window.open(url);

        this.router.navigate(['/details/' + this.caffId]);
      },
      error: () => this.snackBar.error("Could not verify payment with server!")
    });
  }
}

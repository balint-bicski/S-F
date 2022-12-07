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
  token: string;

  constructor(
    private snackBar: SnackBarService,
    private router: Router,
    private route: ActivatedRoute,
    private caffService: CaffFileService
  ) {
  }

  ngOnInit() {
    this.caffId = Number(this.route.snapshot.paramMap.get('caffId'));
  }

  onPurchase() {
    this.caffService.purchaseCaffFile(this.caffId).subscribe({
      next: result => {
        this.token = result.token;
        this.snackBar.success("CAFF file has been purchased");
      },
      error: () => {
        this.snackBar.error("Could not purchase CAFF file!");
      }
    });
  }

  onDownload() {
    // this.caffService.downloadCaffFile(this.caffId, this.token).subscribe({
    //   next: data => {
    //     const blob = new Blob([data], {type: 'application/octet-stream'});
    //     const url = URL.createObjectURL(blob);
    //
    //     var link = document.createElement("a");
    //     link.href = url;
    //     link.download = "caff-" + this.caffId + ".caff";
    //     link.click();
    //
    //     this.router.navigate(['/details/' + this.caffId]);
    //     this.snackBar.success("CAFF file is successfully downloaded");
    //   },
    //   error: () => this.snackBar.error("Could not verify payment with server!")
    // });
  }
}

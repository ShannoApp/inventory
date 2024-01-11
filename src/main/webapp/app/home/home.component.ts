import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { take, takeUntil, switchMap } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { BusinessService } from '../entities/business/service/business.service';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [SharedModule],
})
export default class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private businessService: BusinessService,
  ) {}

  ngOnInit(): void {
    // Subscribe to authentication state changes
    this.accountService
      .getAuthenticationState()
      .pipe(take(1))
      .subscribe(account => {
        if (account != null) {
          // Query business data for the authenticated account
          this.businessService.query().subscribe(businessData => {
            if (businessData.body?.length === 0) {
              // If no business data is found, navigate to the new business page
              this.account = account;
              this.router.navigate(['/business/new']);
            }
          });
        } else {
          // If no account is found, navigate to the login page
          this.router.navigate(['/login']);
        }

        // Assign the account (whether it's null or not)
        this.account = account;
      });
  }

  login(): void {
    // Navigate to the login page
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    // Unsubscribe and complete the destroy$ subject
    this.destroy$.next();
    this.destroy$.complete();
  }
}

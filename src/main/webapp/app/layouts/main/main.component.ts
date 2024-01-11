import { Component, OnInit } from '@angular/core';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import { Router } from '@angular/router';
import { BusinessService } from '../../entities/business/service/business.service';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  providers: [AppPageTitleStrategy],
})
export default class MainComponent implements OnInit {
  account: any;
  constructor(
    private router: Router,
    private appPageTitleStrategy: AppPageTitleStrategy,
    private accountService: AccountService,
    private businessService: BusinessService,
  ) {
    // this.businessService.query().subscribe();
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }
}

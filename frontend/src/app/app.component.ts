import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    RouterLink,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatMenuModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'frontend';

  constructor(private readonly router: Router) {}

  get instrumentsActive(): boolean {
    return this.router.url.startsWith('/instruments')
      || this.router.url.startsWith('/fx-spot')
      || this.router.url.startsWith('/currencies');
  }

  get sourcingActive(): boolean {
    return this.router.url.startsWith('/sources')
      || this.router.url.startsWith('/capturers')
      || this.router.url.startsWith('/source-plans');
  }

  get quoteMonitorActive(): boolean {
    return this.router.url.startsWith('/quote-monitor');
  }
}

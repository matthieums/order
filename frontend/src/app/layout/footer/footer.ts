import { Component } from '@angular/core';

@Component({
  imports: [],
  selector: 'app-footer',
  standalone: true,
  styleUrl: './footer.css',
  templateUrl: './footer.html',
})
export class Footer {
  protected readonly year = new Date().getFullYear();
}

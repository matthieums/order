import { Component, signal } from '@angular/core';
import {Layout} from './layout/layout';

@Component({
  imports: [Layout],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
  standalone: true
})
export class App {
  protected readonly title = signal('frontend');
}

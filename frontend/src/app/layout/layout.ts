import { Component } from '@angular/core';
import {Header} from './header/header';
import {Footer} from './footer/footer';
import {RouterOutlet} from '@angular/router';
import {ItemOverview} from '../item-overview/item-overview';

@Component({
  imports: [Footer, Header, RouterOutlet],
  selector: 'app-layout',
  styleUrl: './layout.css',
  templateUrl: './layout.html',
  standalone: true
})
export class Layout {}

import { Routes } from '@angular/router';
import {ItemOverview} from './item-overview/item-overview';
import {ItemDetail} from './item-detail/item-detail';

export const routes: Routes = [
  {path: 'items', component: ItemOverview},
  {path: 'items/:id', component: ItemDetail},
];

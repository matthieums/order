import { Routes } from '@angular/router';
import {ItemOverview} from './item-overview/item-overview';
import {ItemDetail} from './item-detail/item-detail';
import {EditItem} from './edit-item/edit-item';

export const routes: Routes = [
  {path: 'items', component: ItemOverview},
  {path: 'items/:id', component: ItemDetail},
  {path: 'items/:id/edit', component: EditItem},
];

import { Routes } from '@angular/router';
import {ItemOverview} from './item-overview/item-overview';
import {ItemDetail} from './item-detail/item-detail';
import {EditItem} from './edit-item/edit-item';
import {CreateItem} from './create-item/create-item';

export const routes: Routes = [
  {path: '', component: ItemOverview},
  {path: 'items', component: ItemOverview},
  {path: 'items/create', component: CreateItem},
  {path: 'items/:id/edit', component: EditItem},
  {path: 'items/:id', component: ItemDetail},
];

import {Component, inject, ResourceRef} from '@angular/core';
import {ItemService} from '../service/item-service';
import {Item} from '../model/Item';
import {rxResource} from '@angular/core/rxjs-interop';
import {ItemCard} from '../item-card/item-card';

@Component({
  imports: [
    ItemCard
  ],
  selector: 'app-item-overview',
  styleUrl: './item-overview.css',
  templateUrl: './item-overview.html'
})
export class ItemOverview {
  private readonly itemsService = inject(ItemService);


  protected readonly items: ResourceRef<Item[]> = rxResource({
    stream: () => this.itemsService.getItems(),
    defaultValue: [],
  })
}

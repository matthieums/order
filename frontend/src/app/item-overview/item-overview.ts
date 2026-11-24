import {Component, computed, inject, ResourceRef, signal, WritableSignal} from '@angular/core';
import {ItemService} from '../service/item-service';
import {Item} from '../model/Item';
import {rxResource} from '@angular/core/rxjs-interop';
import {ItemCard} from '../item-card/item-card';
import {CreateItem} from '../create-item/create-item';

@Component({
  imports: [
    ItemCard,
  ],
  selector: 'app-item-overview',
  styleUrl: './item-overview.css',
  templateUrl: './item-overview.html'
})
export class ItemOverview {
  private readonly itemsService = inject(ItemService);
  filterTerm: WritableSignal<string> = signal('');

  protected readonly items: ResourceRef<Item[]> = rxResource({
    stream: () => this.itemsService.getItems(),
    defaultValue: [],
  })

  protected readonly filteredItems = computed(() => {
    const search = this.filterTerm().toLowerCase().trim();
    return this.items.value().filter(item =>
        item.name.toLowerCase().startsWith(search)
    );
  })
}

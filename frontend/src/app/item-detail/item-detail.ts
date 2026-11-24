import {Component, effect, inject, input} from '@angular/core';
import {rxResource} from '@angular/core/rxjs-interop';
import {ItemService} from '../service/item-service';
import {RouterLink} from '@angular/router';

@Component({
  imports: [
    RouterLink
  ],
  selector: 'app-item-detail',
  styleUrl: './item-detail.css',
  templateUrl: './item-detail.html',
})
export class ItemDetail {
  private readonly itemService = inject(ItemService);
  protected readonly id = input.required<string>();

  protected readonly item = rxResource({
    params: () => this.id(),
    stream: ({ params }) => this.itemService.getItem(params),
  })
  constructor() {
    effect(() => {
      if (this.item.error()) {
        console.error('Failed to load item:', this.item.error());
      }
    });
  }
}

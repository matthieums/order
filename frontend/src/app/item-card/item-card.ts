import {Component, input, signal, WritableSignal} from '@angular/core';
import {Item} from '../model/Item';

@Component({
  imports: [],
  selector: 'app-item-card',
  styleUrl: './item-card.css',
  templateUrl: './item-card.html',
})
export class ItemCard {
  item = input.required<Item>();
  protected readonly imageSource: string = "img/item-image.png"
}

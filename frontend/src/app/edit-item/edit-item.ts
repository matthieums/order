import {Component, inject, input, signal, WritableSignal} from '@angular/core';
import {ItemService} from '../service/item-service';
import {Router, RouterLink} from '@angular/router';
import {form, FormField, FormRoot, maxLength, min, required, validate} from '@angular/forms/signals';
import {firstValueFrom} from 'rxjs';
import {UpdateItemRequest} from '../model/UpdateItemRequest';
import {Counter} from '../counter/counter';

@Component({
  imports: [
    Counter,
    FormRoot,
    FormField,
    RouterLink
  ],
  selector: 'app-edit-item',
  styleUrl: './edit-item.css',
  templateUrl: './edit-item.html',
})
export class EditItem {
  private itemService: ItemService = inject(ItemService);
  private readonly router = inject(Router);
  readonly id = input.required<string>();
  updateItemModel: WritableSignal<UpdateItemRequest> = signal<UpdateItemRequest>({
    name: '',
    description: '',
    price: 0,
    amountOfStock: 0
  });

  updateItemForm = form(
    this.updateItemModel,
    (item) => {
      required(item.name, {
        message: 'Item needs a valid name'
      });

      required(item.description, {
        message: 'Item needs a valid description'
      });

      maxLength(item.description, 255, {
        message: 'Description may not exceed 255 characters'
      });

      required(item.price, {
        message: 'Item needs a valid price'
      });

      min(item.price, 0.00, {
        message: 'Price must be greater than zero'
      });

      required(item.amountOfStock, {
        message: 'Item needs a valid stock amount'
      });

      min(item.amountOfStock, 0, {
        message: 'Stock amount cannot be negative'
      });

      validate(item.amountOfStock, ({ value }) => {
        if (!Number.isInteger(value())) {
          return {
            kind: 'integer',
            message: 'Stock amount must be a whole number'
          };
        }

        return null;
      });

    },
    {
      submission: {
        action: async (field) => {
          const item = field().value();
          const id = this.id();
          await firstValueFrom(
            this.itemService.updateItem(item, id)
          )
          await this.router.navigate(['/items',id]);
          return undefined;
        }
      }
    }
  );
}

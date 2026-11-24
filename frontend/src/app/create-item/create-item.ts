import {Component, inject, signal, WritableSignal} from '@angular/core';
import {ItemService} from '../service/item-service';
import {CreateItemRequest} from '../model/CreateItemRequest';
import {form, FormField, FormRoot, maxLength, min, required, validate} from '@angular/forms/signals';
import {firstValueFrom} from 'rxjs';
import {Router, RouterLink} from '@angular/router';
import {Counter} from '../counter/counter';

@Component({
  imports: [
    FormField,
    FormRoot,
    Counter,
    RouterLink
  ],
  selector: 'app-create-item',
  styleUrl: './create-item.css',
  templateUrl: './create-item.html',
})
export class CreateItem {
  private itemService: ItemService = inject(ItemService);
  private readonly router = inject(Router);
  createItemModel: WritableSignal<CreateItemRequest> = signal<CreateItemRequest>({
    name: '',
    description: '',
    price: 0,
    amountOfStock: 0
  });

  createItemForm = form(
    this.createItemModel,
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

          const createdItem = await firstValueFrom(
            this.itemService.createItem(item)
          );
          await this.router.navigate(['/']);
          return undefined;
        }
      }
    }
  );


}

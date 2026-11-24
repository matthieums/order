import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {Item} from '../model/Item';
import {CreateItemRequest} from '../model/CreateItemRequest';

@Service()
export class ItemService {
  private readonly http: HttpClient = inject(HttpClient);
  private readonly itemsUrl: string =`${environment.backendUrl}/items`

  getItems(): Observable<Item[]> {
    return this.http.get<Item[]>(this.itemsUrl);
  }

  getItem(id: string): Observable<Item> {
    return this.http.get<Item>(this.itemsUrl + '/' + id);
  }

  createItem(item: CreateItemRequest) {
    return this.http.post<Item>(
      this.itemsUrl,
    item
    );
  }

}

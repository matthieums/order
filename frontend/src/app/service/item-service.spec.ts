import { TestBed } from '@angular/core/testing';
import { ItemService } from './item-service';
import {Item} from '../model/Item';
import {firstValueFrom} from 'rxjs';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {environment} from '../../environments/environment';


describe('ItemService', () => {
  let service: ItemService;
  let httpTesting: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers:[provideHttpClientTesting()]
    });
    service = TestBed.inject(ItemService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve the items from the backend', async () => {
    const itemsResult: Item[] = [
      {
        id: 1,
        name: 'An item',
        description: 'Really just an item',
        price: 1.0,
        amountOfStock: 10
      }
    ]

    const items = firstValueFrom(service.getItems());
    const request = httpTesting.expectOne(`${environment.backendUrl}/items`)
    expect(request.request.method).toBe('GET');
    request.flush(itemsResult);
    expect(await items).toEqual(itemsResult);
  });
});

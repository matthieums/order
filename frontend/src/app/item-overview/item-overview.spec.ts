
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ItemOverview } from './item-overview';
import { Item } from '../model/Item';
import { ItemService } from '../service/item-service';
import { environment } from '../../environments/environment';
import { provideHttpClient } from '@angular/common/http';
import {
  provideHttpClientTesting,
  HttpTestingController
} from '@angular/common/http/testing';

describe('ItemOverview', () => {
  let component: ItemOverview;
  let fixture: ComponentFixture<ItemOverview>;
  let httpTesting: HttpTestingController;
  let testItems: Item[];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemOverview],
      providers: [
        ItemService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    httpTesting = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(ItemOverview);
    component = fixture.componentInstance;

    testItems = [
      {
        id: 1,
        name: 'Angular',
        description: 'Angular framework',
        price: 20,
        amountOfStock: 10,
        stockUrgency: 'LOW'
      },
      {
        id: 2,
        name: 'React',
        description: 'React framework',
        price: 30,
        amountOfStock: 5,
        stockUrgency: 'MEDIUM'
      },
      {
        id: 3,
        name: 'ANGULAR',
        description: 'Angular tutorial',
        price: 15,
        amountOfStock: 8,
        stockUrgency: 'LOW'
      }
    ];

    fixture.detectChanges();

    const request = httpTesting.expectOne(
      environment.backendUrl + '/items'
    );

    request.flush(testItems);

    fixture.detectChanges();
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Filtering feature', () => {

    it('Should filter case-insensitively', () => {
      component.filterTerm.set('ANGULAR');
      fixture.detectChanges();

      const cards = fixture.nativeElement.querySelectorAll('app-item-card');

      expect(cards.length).toBe(2);
    });

    it('Should filter with startswith strategy', () => {
      component.filterTerm.set('Angular');
      fixture.detectChanges();

      const cards = fixture.nativeElement.querySelectorAll('app-item-card');

      expect(cards.length).toBe(2);
      expect(cards[0].textContent).toContain('Angular');
    });

  });
});

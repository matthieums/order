import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ItemCard } from './item-card';

describe('ItemCard', () => {
  let component: ItemCard;
  let fixture: ComponentFixture<ItemCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemCard],
    }).compileComponents();

    fixture = TestBed.createComponent(ItemCard);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('item', {
      id: 1,
      name: 'Angular',
      description: 'Angular framework',
      price: 20,
      amountOfStock: 10,
      stockUrgency: 'LOW'
    });

    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

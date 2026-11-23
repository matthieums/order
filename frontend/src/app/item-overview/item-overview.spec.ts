import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ItemOverview } from './item-overview';

describe('ItemOverview', () => {
  let component: ItemOverview;
  let fixture: ComponentFixture<ItemOverview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItemOverview],
    }).compileComponents();

    fixture = TestBed.createComponent(ItemOverview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

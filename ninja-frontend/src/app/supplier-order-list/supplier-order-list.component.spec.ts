import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplierOrderListComponent } from './supplier-order-list.component';

describe('SupplierOrderListComponent', () => {
  let component: SupplierOrderListComponent;
  let fixture: ComponentFixture<SupplierOrderListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SupplierOrderListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupplierOrderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IProduct } from './../../../product/product.model';
import SharedModule from '../../../../shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'jhi-product-list-popup',
  templateUrl: './product-list-popup.component.html',
  styleUrls: ['./product-list-popup.component.scss'],
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductListPopupComponent implements OnInit {
  @Input()
  open: boolean = false;

  @Input()
  items: { item: any; iSelected: boolean }[] = [];

  @Output()
  productsEvent = new EventEmitter<{ item: any; iSelected: boolean }[]>();

  selectedProducts: IProduct[] = [];
  products: { product: IProduct }[] = [];

  constructor() {}

  ngOnInit(): void {
    this.selectedProducts = [];
    this.products = [];
    this.items.forEach(item => {
      if (item.iSelected) {
        this.selectedProducts.push(item.item);
      }
      this.products.push({ product: item.item });
    });
  }

  selectProduct(i: number) {
    // this.selectedProducts.push(product);
    this.items[i].iSelected = !this.items[i].iSelected;
    return;
  }

  triggerEvent() {
    this.productsEvent.emit(this.items);
    this.open = false;
    return;
  }
}

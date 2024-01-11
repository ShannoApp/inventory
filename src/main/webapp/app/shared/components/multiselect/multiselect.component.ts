import { Component, EventEmitter, Input, OnChanges, Output, SimpleChange, SimpleChanges } from '@angular/core';
import { IProduct } from '../../../entities/product/product.model';
import { FormsModule } from '@angular/forms';
import SharedModule from '../../shared.module';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ICategory } from 'app/entities/category/category.model';

@Component({
  standalone: true,
  selector: 'jhi-multiselect',
  templateUrl: './multiselect.component.html',
  styleUrls: ['./multiselect.component.scss'],
  imports: [SharedModule, FormsModule],
})
export class MultiselectComponent {
  open: boolean = false;
  items: { name: string; discription: string; obj: any }[] | null = null;
  originalItems: { name: string; discription: string; obj: any }[] = [];
  dialogName: string = 'Item';

  selectedItems: Set<any> | null = null;
  products: { product: IProduct }[] = [];

  selectOne: boolean = false;
  categories: ICategory[] | null = [];
  selectedCategory: string | null = null;

  constructor(
    protected activeModal: NgbActiveModal,
    protected categoryService: CategoryService,
  ) {
    this.categoryService.query().subscribe(res => {
      this.categories = res.body;
    });
    this.selectedItems = new Set([...Array.from(this.selectedItems ?? [])]);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  save(id: string): void {
    const obj = {
      selectedItems: this.selectedItems,
      reason: 'after selection',
    };
    this.activeModal.close(obj);
  }

  selectItem(i: number, obj: any) {
    const isUnique = !Array.apply(this.selectedItems).some((s: any) => obj.obj.id === s.obj.id);

    if (isUnique) {
      this.selectedItems?.add(obj);
    }

    if (this.selectOne) {
      this.selectedItems?.clear();
      this.selectedItems?.add(obj);
    }
  }

  removeSelectedItem(i: number, obj: any) {
    this.selectedItems?.delete(obj);
  }

  selectCategory() {
    if (this.selectedCategory !== 'All') {
      this.items = this.originalItems.filter(item => item.obj.category.name === this.selectedCategory);
    } else {
      this.items = this.originalItems;
    }
  }

  searchItems(searchTerm: any) {
    this.items = this.originalItems.filter(item => item.name.toLowerCase().includes(searchTerm.target.value.toLowerCase()));
  }
}

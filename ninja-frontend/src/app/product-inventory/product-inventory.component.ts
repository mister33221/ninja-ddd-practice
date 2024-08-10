import { Component, ElementRef, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { Offcanvas } from 'bootstrap';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

interface Product {
  id: number;
  name: string;
  image: string;
  stockQuantity: number;
  safetyStockLevel: number;
  description: string;
  specifications: string;
}

@Component({
  selector: 'app-product-inventory',
  templateUrl: './product-inventory.component.html',
  styleUrls: ['./product-inventory.component.scss']
})
export class ProductInventoryComponent{
  private offcanvasInstance?: Offcanvas;

  products: Product[] = [
    { id: 1, name: '商品 1', image: 'https://picsum.photos/200/200?random=1', stockQuantity: 100, safetyStockLevel: 20, description: '商品 1 的詳細描述', specifications: '商品 1 的規格' },
    { id: 2, name: '商品 2', image: 'https://picsum.photos/200/200?random=2', stockQuantity: 50, safetyStockLevel: 10, description: '商品 2 的詳細描述', specifications: '商品 2 的規格' },
    { id: 3, name: '商品 3', image: 'https://picsum.photos/200/200?random=3', stockQuantity: 75, safetyStockLevel: 15, description: '商品 3 的詳細描述', specifications: '商品 3 的規格' },
  ];

  selectedProduct: Product | null = null;
  isEditing: boolean = false;

  newImageUrl: SafeUrl | any = null;

  constructor(private sanitizer: DomSanitizer) { }

  openOffcanvas(product: Product): void {
    this.selectedProduct = { ...product };
    this.isEditing = false;
    this.offcanvasInstance?.show();
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
  }

  saveChanges(): void {
    if (this.selectedProduct) {
      const index = this.products.findIndex(p => p.id === this.selectedProduct!.id);
      if (index !== -1) {
        this.products[index] = { ...this.selectedProduct };
      }
      this.isEditing = false;
    }
  }

  closeOffcanvas(): void {
    this.offcanvasInstance?.hide();
    this.selectedProduct = null;
  }

  isLowStock(product: Product): boolean {
    return product.stockQuantity <= product.safetyStockLevel;
  }

  onFileChange(event: Event) {
    const target = event.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      const file = target.files[0];
      const objectUrl = URL.createObjectURL(file);
      this.newImageUrl = this.sanitizer.bypassSecurityTrustUrl(objectUrl);
      if (this.selectedProduct) {
        this.selectedProduct.image = this.newImageUrl;
      }
    }
  }
}

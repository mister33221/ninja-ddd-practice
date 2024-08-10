import { Component, OnInit } from '@angular/core';

interface OrderItem {
  productName: string;
  quantity: number;
  unitPrice: number;
}

interface SupplierOrder {
  id: string;
  date: Date;
  items: OrderItem[];
  totalAmount: number;
  supplierName: string;
  supplierAddress: string;
  status: 'Pending' | 'Confirmed' | 'Shipped' | 'Received' | 'Cancelled';
  paymentTerms: string;
}

@Component({
  selector: 'app-supplier-order-list',
  templateUrl: './supplier-order-list.component.html',
  styleUrls: ['./supplier-order-list.component.scss']
})
export class SupplierOrderListComponent  implements OnInit {
  supplierOrders: SupplierOrder[] = [
    {
      id: 'SO-001',
      date: new Date('2024-08-08'),
      items: [
        { productName: '原料A', quantity: 200, unitPrice: 5 },
        { productName: '零件B', quantity: 100, unitPrice: 15 }
      ],
      totalAmount: 2500,
      supplierName: '台灣電子零件有限公司',
      supplierAddress: '新竹科學園區XX路XX號',
      status: 'Pending',
      paymentTerms: '30天'
    },
    {
      id: 'SO-002',
      date: new Date('2024-08-09'),
      items: [
        { productName: '包裝材料C', quantity: 1000, unitPrice: 0.5 },
        { productName: '標籤D', quantity: 5000, unitPrice: 0.1 }
      ],
      totalAmount: 1000,
      supplierName: '高雄包裝印刷公司',
      supplierAddress: '高雄市前鎮區YY路YY號',
      status: 'Confirmed',
      paymentTerms: '預付款'
    },
    // ... 可以繼續添加更多供應商訂單 ...
  ];

  constructor() { }

  ngOnInit(): void {
  }

  getOrderItemsSummary(items: OrderItem[]): string {
    return items.map(item => `${item.productName} x ${item.quantity}`).join(', ');
  }

  getBadgeClass(status: SupplierOrder['status']): string {
    switch(status) {
      case 'Pending': return 'bg-warning text-dark';
      case 'Confirmed': return 'bg-primary text-white';
      case 'Shipped': return 'bg-info text-white';
      case 'Received': return 'bg-success text-white';
      case 'Cancelled': return 'bg-danger text-white';
      default: return 'bg-secondary text-white';
    }
  }
}

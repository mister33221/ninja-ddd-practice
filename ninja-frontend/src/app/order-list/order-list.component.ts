import { Component, OnInit } from '@angular/core';

interface OrderItem {
  productName: string;
  quantity: number;
  unitPrice: number;
}

interface Order {
  id: string;
  date: Date;
  items: OrderItem[];
  totalAmount: number;
  customerName: string;
  shippingAddress: string;
  status: 'Pending' | 'Shipped' | 'Delivered' | 'Cancelled';
  paymentMethod: string;
}

@Component({
  selector: 'app-order-list',
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})


export class OrderListComponent implements OnInit {
  orders: Order[] = [
    {
      id: 'ORD-001',
      date: new Date('2024-08-08'),
      items: [
        { productName: '商品A', quantity: 2, unitPrice: 100 },
        { productName: '商品B', quantity: 1, unitPrice: 150 }
      ],
      totalAmount: 350,
      customerName: '張三',
      shippingAddress: '台北市大安區XX路XX號',
      status: 'Pending',
      paymentMethod: '信用卡'
    },
    {
      id: 'ORD-002',
      date: new Date('2024-08-09'),
      items: [
        { productName: '商品C', quantity: 3, unitPrice: 80 },
        { productName: '商品D', quantity: 2, unitPrice: 120 }
      ],
      totalAmount: 480,
      customerName: '李四',
      shippingAddress: '新北市板橋區YY路YY號',
      status: 'Shipped',
      paymentMethod: 'PayPal'
    },
    {
      id: 'ORD-003',
      date: new Date('2024-08-10'),
      items: [
        { productName: '商品E', quantity: 1, unitPrice: 200 },
        { productName: '商品F', quantity: 1, unitPrice: 300 }
      ],
      totalAmount: 500,
      customerName: '王五',
      shippingAddress: '台中市北區ZZ路ZZ號',
      status: 'Delivered',
      paymentMethod: 'ATM'
    },
    {
      id: 'ORD-004',
      date: new Date('2024-08-11'),
      items: [
        { productName: '商品G', quantity: 2, unitPrice: 150 },
        { productName: '商品H', quantity: 3, unitPrice: 100 }
      ],
      totalAmount: 550,
      customerName: '趙六',
      shippingAddress: '高雄市三民區WW路WW號',
      status: 'Cancelled',
      paymentMethod: '貨到付款'
    },
    {
      id: 'ORD-005',
      date: new Date('2024-08-12'),
      items: [
        { productName: '商品I', quantity: 1, unitPrice: 250 },
        { productName: '商品J', quantity: 2, unitPrice: 120 }
      ],
      totalAmount: 490,
      customerName: '孫七',
      shippingAddress: '台南市中西區XX路XX號',
      status: 'Pending',
      paymentMethod: '信用卡'
    },
    {
      id: 'ORD-006',
      date: new Date('2024-08-13'),
      items: [
        { productName: '商品K', quantity: 3, unitPrice: 80 },
        { productName: '商品L', quantity: 1, unitPrice: 300 }
      ],
      totalAmount: 540,
      customerName: '周八',
      shippingAddress: '台中市南區YY路YY號',
      status: 'Shipped',
      paymentMethod: 'PayPal'
    },
    {
      id: 'ORD-007',
      date: new Date('2024-08-14'),
      items: [
        { productName: '商品M', quantity: 2, unitPrice: 150 },
        { productName: '商品N', quantity: 1, unitPrice: 200 }
      ],
      totalAmount: 500,
      customerName: '吳九',
      shippingAddress: '新竹市東區ZZ路ZZ號',
      status: 'Delivered',
      paymentMethod: 'ATM'
    },
    {
      id: 'ORD-008',
      date: new Date('2024-08-15'),
      items: [
        { productName: '商品O', quantity: 1, unitPrice: 250 },
        { productName: '商品P', quantity: 2, unitPrice: 120 }
      ],
      totalAmount: 490,
      customerName: '郭十',
      shippingAddress: '桃園市中壢區WW路WW號',
      status: 'Cancelled',
      paymentMethod: '貨到付款'
    }
  ];


  constructor() { }

  ngOnInit(): void {
  }

  getOrderItemsSummary(items: OrderItem[]): string {
    return items.map(item => `${item.productName} x ${item.quantity}`).join(', ');
  }


  getBadgeClass(status: Order['status']): string {
    switch(status) {
      case 'Pending': return 'bg-warning text-dark';
      case 'Shipped': return 'bg-primary text-white';
      case 'Delivered': return 'bg-success text-white';
      case 'Cancelled': return 'bg-danger text-white';
      default: return 'bg-secondary text-white';
    }
  }
}

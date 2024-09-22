export interface CheckoutRequest {
  shoppingCartId: number;
  userId: number;
  cartItems: CartItem[];
}

export interface CartItem {
  id: number;
  cartId: number;
  productId: number;
  productName: string;
  productImageURL: string;
  quantity: number;
  price: number;
  selected?: boolean;
}

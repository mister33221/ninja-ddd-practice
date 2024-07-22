- 今天我們要拿一個實際的案例，來練習使用 DDD (Domain-Driven Design)，設計出一個在 Spring boot 框架上的專案。
- 我盡量把我的思路寫得清楚一點，如果大家看的過程中，有任何覺得可以討論的地方請不吝指教。

## 思路

- 主題是我們要來設計一個在木葉村的忍具店，這個忍具店的主要業務是販售各種忍者用的道具。
- 我們主要的思路流程大致會如下步驟
    1. 需求分析( Requirement Analysis )
    2. 事件風暴（Event Storming）
        1. 領域分析( DDD Domain Analysis )
        2. 定義限界上下文( Bounded Context )
    3. 領域模型設計( DDD Domain Model )
    4. 分層架構設計，按照 DDD 模型的分層架構來設計專案
        1. Interface Layer
        2. Application Layer
        3. Domain Layer
        4. Infrastructure Layer
        5. Anti-corruption Layer 
    5. 實作

## 1. 需求分析

我們的木葉村忍具店系統需要實現以下核心功能：

1. 用戶管理
    用戶註冊和登入
    用戶角色區分（普通忍者、上忍、特殊身份如火影等）
    個人資料管理

2. 商品管理
    忍具分類展示（如武器、防具、卷軸等）
    商品詳細資訊（包括描述、價格、庫存等）
    新品上架和庫存更新

3. 購物功能
    購物車系統
    訂單生成和管理
    支付系統（可能包括現金和特殊的忍者積分）

4. 庫存和供應鏈管理
    實時庫存追踪
    與供應商的接口

5. 多語言支持
    支持不同忍村的語言    

以下是我之後有空再擴展的功能：

1. 搜索和過濾
    按類別、價格、忍者等級等搜索商品
    高級過濾選項

2. 評價和評論系統
    用戶可以對購買的忍具進行評分和評論
    評論管理（例如違規評論的處理）

3. 忍具定制服務
    特殊忍具的定制申請
    定制進度跟踪

4. 優惠活動管理
    季節性折扣
    忠誠客戶獎勵計劃

5. 安全和認證系統
    確保只有合法忍者可以購買某些特殊忍具
    防止敵對忍村的滲透

6. 客戶服務
    在線客服聊天
    常見問題解答（FAQ）

7. 忍具使用指南和教學
    視頻教學
    使用技巧分享

8. 數據分析和報告
    銷售趨勢分析
    用戶行為分析

目前我們先以核心功能為主，以下都會以核心功能為主進行設計。

## 2. 事件風暴

- 事件風暴是一種快速的設計方法，通過參與者的參與，將領域知識快速地抽取出來，並且將這些知識轉換成事件、命令、聚合...等，這樣可以幫助團隊快速地理解業務需求，並且快速地設計出領域模型。
- 事件風暴中的名詞解釋
    - Event: 事件，系統中發生的事情，通常描述為過去式。即當某個事情發生時，我們可以定義為一個事件，讓這個事件被記錄下來，並且通知其他系統或執行其他操作。
    - Command: 命令，用來觸發事件的動作，通常描述為現在或未來式。即當我們要觸發一個事件時，我們可以定義為一個命令，讓這個命令被執行，觸發相應的事件。
    - Aggregate: 聚合，代表一個聚合的根。當業務對象或實體之間有很強的關聯性時，我們可以將這些實體聚合在一起，形成一個聚合，並且指定一個聚合根，負責維護聚合內部的一致性。通過聚合，我們可以將業務邏輯封裝在一個聚合內部，並且對外提供操作的入口。
    - Policy: 策略，定義事件發生後，要自動執行的邏輯或行為。通過策略，我們可以將事件發生後的行為自動化，減少人工干預。例如: 當某忍者註冊成功後，我們可以發送一封郵件給忍者，通知他註冊成功。
- 通常我們會使用不同顏色的便利貼來區分事件、命令、聚合、策略等，這樣可以幫助我們更好的理解事件風暴的結果。
    - Event 事件: 橘色便利貼
    - Command 命令: 藍色便利貼
    - Aggregate 聚合: 粉紅色便利貼
    - Policy 策略: 綠色便利貼
- 事件風暴的流程
    1. 準備階段
        - 邀請業務專家、開發人員、測試人員、產品經理等參與者參與事件風暴。
        - 準備一個大白板或是一個大畫布、便利貼( 用來寫事件、命令、聚合等 )、筆等工具。
        - 本主題中，我們可以邀請木葉村的忍者、木葉村的商人、木葉村的忍者訓練師、忍具店的店主等參與者參與事件風暴。
    2. 執行階段
        1. 識別領域事件
            - 通過參與者的參與，定義出系統中可能會發生的所有事件。
            - 經過整理、補充、討論，最終完善事件列表。
            - 本主題我們以橘色便利貼來標示事件。
            - 用戶管理
                - **用戶註冊完成**：當新用戶成功填寫註冊表單並提交，系統驗證並創建新賬戶後觸發。
                - **用戶登入成功**：用戶輸入正確的憑證並通過系統驗證後觸發。
                - **用戶登出完成**：用戶主動登出或系統自動登出用戶後觸發。
                - **個人資料已更新**：用戶修改其個人資訊（如聯繫方式、忍術屬性等）並保存後觸發。
            - 商品管理
                - **新忍具已添加**：管理員在系統中添加新的忍具產品後觸發。
                - **忍具分類已創建**：管理員創建新的忍具分類（如新型武器類別）後觸發。
                - **忍具分類已更新**：管理員修改現有忍具分類的資訊後觸發。
                - **商品資訊已更新**：管理員修改忍具的詳細資訊（如描述、價格）後觸發。
                - **新品上架完成**：新添加的忍具正式在網站上發布並可供購買時觸發。
                - **庫存數量已更新**：由於銷售、退貨或補貨導致忍具庫存數量變化時觸發。
            - 購物功能
                - **商品已添加到購物車**：用戶選擇一件忍具並將其加入購物車時觸發。
                - **商品已從購物車移除**：用戶從購物車中刪除一件忍具時觸發。
                - **購物車已清空**：用戶選擇清空整個購物車或完成訂單後系統自動清空購物車時觸發。
                - **訂單已創建**：用戶確認購物車中的商品並生成訂單時觸發。
                - **訂單狀態已更新**：訂單狀態發生變化（如待付款、已付款、已發貨等）時觸發。
                - **支付已完成**：用戶成功支付訂單金額後觸發。
            - 庫存和供應鏈管理
                - **庫存水平已達到閾值**：某件忍具的庫存數量降至預設的最低閾值時觸發。
                - **補貨訂單已生成**：系統自動或管理員手動創建向供應商的補貨訂單時觸發。
                - **供應商訂單已確認**：供應商確認接受補貨訂單時觸發。
                - **庫存已補充**：新的忍具庫存到達並更新系統庫存數據時觸發。
            - 多語言支持
                - **用戶語言偏好已更改**：用戶在個人設置中選擇新的界面語言時觸發。
                - **網站語言已切換**：系統根據用戶的語言偏好或選擇切換網站界面語言時觸發。
        2. 添加命令
            - 在事件的基礎上，說明如何觸發這些事件，定義出系統中可能會發生的所有命令。
            - 在定義命令時，需要識別出哪些人或那些系統可以觸發這些命令。
            - 本主題我們以藍色便利貼來標示命令。
            - 用戶管理
                - 註冊用戶
                - 登錄用戶
                - 登出用戶
                - 更新個人資料
            - 商品管理
                - 添加新忍具
                - 創建忍具分類
                - 更新忍具分類
                - 更新商品資訊
                - 上架新品
                - 更新庫存數量
            - 購物功能
                - 添加商品到購物車
                - 從購物車移除商品
                - 清空購物車
                - 創建訂單
                - 更新訂單狀態
                - 處理支付
            - 庫存和供應鏈管理
                - 檢查庫存水平
                - 生成補貨訂單
                - 確認供應商訂單
                - 補充庫存
            - 多語言支持
                - 更改用戶語言偏好
                - 切換網站語言
        3. 識別聚合
            - 定義完 events 和 command，我們先來識別聚合，先將聚合識別出來，有助於定義數據的一致性及把邊界劃分清楚。
            - 聚合分組，根據業務邏輯，將相關的事件和命令分組，形成聚合。
            - 每個聚合都有一個明確的聚合根，這有助於維護數據的一致性和完整性。
            - 聚合的邊界基於業務邏輯和事務一致性需求來劃分，確保相關的實體和值對象在同一個事務邊界內。
            - 這種設計促進了高內聚、低耦合的系統架構，使得每個聚合都能獨立地管理自己的數據和業務規則。
            - 聚合的設計考慮了以上所有的事件和命令，確保每個聚合都能夠處理相應的業務操作。
            - 本主題我們以粉紅色便利貼來標示聚合。
            - 用戶聚合 (User Aggregate)
                - **聚合根**：User
                - **包含的實體/值對象**：UserProfile, UserCredentials
                - **設計思路**：
                - 用戶是系統的核心實體，包含了用戶的基本信息和身份驗證信息。
                - 不包含語言偏好，因為這將通過 HTTP header 動態處理。
                - **支持的命令**：註冊用戶、登錄用戶、登出用戶、更新個人資料。
                    用戶聚合 (User Aggregate)
                - **程式碼**
                    - 聚合根: User
                    ```java
                    public class User {
                        private UserId id;
                        private UserProfile profile;
                        private UserCredentials credentials;
                        
                        public void register(String username, String password, String email) { ... }
                        public void login(String username, String password) { ... }
                        public void logout() { ... }
                        public void updateProfile(UserProfile newProfile) { ... }
                        public void changePassword(String oldPassword, String newPassword) { ... }
                    }
                    ```
                    - 值對象:
                    ```java
                    ypublic class UserProfile {
                        private String fullName;
                        private String email;
                        private String phoneNumber;
                        private LocalDate dateOfBirth;
                    }

                    public class UserCredentials {
                        private String username;
                        private String hashedPassword;
                        private LocalDateTime lastLoginTime;
                    }
                    ```
                    - 設計思路:
                        User 作為聚合根，封裝了用戶的核心信息和行為。
                        UserProfile 和 UserCredentials 作為值對象，分別處理用戶的個人信息和認證信息。
                        方法設計反映了用戶的主要操作，如註冊、登錄、更新資料等。

            - 商品聚合 (Product Aggregate)

                - **聚合根**：Product
                - **包含的實體/值對象**：ProductDetails, ProductCategory, InventoryLevel
                - **設計思路**：
                - 商品是忍具店的核心業務實體，包含了商品的基本信息、分類和庫存水平。
                - 將 ProductDetails、ProductCategory 和 InventoryLevel 作為 Product 的一部分，確保商品相關的所有信息在一個事務邊界內。
                - **支持的命令**：添加新忍具、創建忍具分類、更新忍具分類、更新商品資訊、上架新品、更新庫存數量。
                - **程式碼**：
                    - 聚合根: Product
                    ```java
                    public class Product {
                        private ProductId id;
                        private ProductDetails details;
                        private ProductCategory category;
                        private Money price;
                        private int stockQuantity;
                        
                        public void updateDetails(ProductDetails newDetails) { ... }
                        public void changeCategory(ProductCategory newCategory) { ... }
                        public void updatePrice(Money newPrice) { ... }
                    }
                    ```
                    - 值對象:
                    ```java
                    public class ProductDetails {
                        private String name;
                        private String description;
                        private List<String> images;
                    }

                    public class ProductCategory {
                        private String name;
                        private String description;
                    }
                    ```
                    - 設計思路:
                        Product 作為聚合根，管理商品的所有相關信息。
                        ProductDetails 和 ProductCategory 作為值對象，提供了商品的詳細信息和分類。
                        方法設計允許更新商品的各個方面，包括詳情、分類、價格和庫存。

            -  購物車聚合 (ShoppingCart Aggregate)

                - **聚合根**：ShoppingCart
                - **包含的實體/值對象**：CartItem
                - **設計思路**：
                - 購物車代表了用戶的臨時購買意圖，需要單獨管理。
                - 將 CartItem 作為 ShoppingCart 的一部分，確保購物車操作的原子性。
                - **支持的命令**：添加商品到購物車、從購物車移除商品、清空購物車。
                - **程式碼**：

                    - 聚合根: ShoppingCart
                    ```java
                    public class ShoppingCart {
                        private ShoppingCartId id;
                        private UserId userId;
                        private List<CartItem> items;
                        
                        public void addItem(ProductId productId, int quantity) { ... }
                        public void removeItem(ProductId productId) { ... }
                        public void updateItemQuantity(ProductId productId, int newQuantity) { ... }
                        public void clear() { ... }
                    }
                    ```
                    - 值對象:
                    ```java
                    public class CartItem {
                        private ProductId productId;
                        private int quantity;
                        private Money price;
                    }
                    ```
                    - 設計思路:
                        ShoppingCart 作為聚合根，管理用戶的購物車內容。
                        CartItem 作為值對象，代表購物車中的單個商品項。
                        方法設計支持添加、移除、更新商品，以及清空購物車等操作。

            -  訂單聚合 (Order Aggregate)

                - **聚合根**：Order
                - **包含的實體/值對象**：OrderItem, OrderStatus, PaymentInfo
                - **設計思路**：
                - 訂單是一個重要的業務概念，代表了一次完整的交易。
                - 將 OrderItem、OrderStatus 和 PaymentInfo 作為 Order 的一部分，確保訂單相關的所有信息在一個事務邊界內。
                - **支持的命令**：創建訂單、更新訂單狀態、處理支付。
                - **程式碼**：
                    - 聚合根: Order
                    ```java
                    public class Order {
                        private OrderId id;
                        private UserId userId;
                        private List<OrderItem> items;
                        private OrderStatus status;
                        private Money totalAmount;
                        private PaymentInfo paymentInfo;
                        
                        public void addItem(ProductId productId, int quantity, Money price) { ... }
                        public void updateStatus(OrderStatus newStatus) { ... }
                        public void processPayment(PaymentInfo paymentInfo) { ... }
                    }
                    ```
                    - 值對象:
                    ```java
                    public class OrderItem {
                        private ProductId productId;
                        private int quantity;
                        private Money price;
                    }

                    public class OrderStatus {
                        private String status; // e.g., "PENDING", "PAID", "SHIPPED", "DELIVERED"
                    }

                    public class PaymentInfo {
                        private String paymentMethod;
                        private String transactionId;
                        private LocalDateTime paymentTime;
                    }
                    ```
                    - 設計思路:
                        Order 作為聚合根，封裝了訂單的所有相關信息和狀態。
                        OrderItem、OrderStatus 和 PaymentInfo 作為值對象，分別處理訂單項、訂單狀態和支付信息。
                        方法設計支持添加商品到訂單、更新訂單狀態和處理支付等操作。

            -  庫存聚合 (Inventory Aggregate)

                - **聚合根**：InventoryItem
                - **包含的實體/值對象**：StockLevel, ReorderThreshold
                - **設計思路**：
                - 庫存管理是一個獨立的業務概念，需要單獨追踪和管理。
                - 將 StockLevel 和 ReorderThreshold 作為 InventoryItem 的一部分，確保庫存操作的原子性。
                - **支持的命令**：檢查庫存水平、生成補貨訂單、確認供應商訂單、補充庫存。
                - **程式碼**：
                    - 聚合根: InventoryItem
                    ```java
                    public class InventoryItem {
                        private ProductId productId;
                        private int quantity;
                        private int reorderThreshold;
                        
                        public void addToStock(int amount) { ... }
                        public void removeFromStock(int amount) { ... }
                        public boolean isLowStock() { ... }
                        public void updateReorderThreshold(int newThreshold) { ... }
                    }
                    ```
                    - 值對象:
                    ```java
                    public class StockLevel {
                        private int quantity;
                    }

                    public class ReorderThreshold {
                        private int threshold;
                    }
                    ```
                    - 設計思路:
                        InventoryItem 作為聚合根，管理單個商品的庫存信息。
                        方法設計支持增加和減少庫存、檢查庫存是否不足，以及更新重新訂購閾值。

                - 這種設計的總體思路是：
                    每個聚合都有一個明確的聚合根，負責維護該聚合的一致性和完整性。
                    值對象用於表示那些沒有獨立身份，但包含重要業務數據的概念。
                    方法設計反映了每個聚合的主要業務操作和規則。
                    在每個根聚合的 ID 也使用一個物件包起來，提高可讀性及擴展性。
                    聚合之間通過 ID 引用，而不是直接關聯，以保持鬆耦合。
                    每個聚合都專注於其核心職責，遵循單一職責原則。
                    這樣的設計有助於創建一個清晰、模塊化且易於維護的領域模型，同時也為未來的擴展和變更提供了良好的基礎。

        4. 定義策略
            - 策略是指當特定事件發生時，需要自動執行的邏輯或行為。
            - 本主題我們以綠色便利貼來標示策略。
                - 訂單已創建時，發送短信通知忍者
                - 訂單狀態已更新時，發送短信通知忍者
                - 忍者已註冊時，發送郵件通知忍者

        5. 識別閱讀模型
            - 通過討論，確定系統中可能會發生的所有事件、命令、聚合、策略等。
        6. 劃分限界上下文
            - 根據事件風暴的結果，劃分限界上下文，確定各個聚合的邊界。
        7. 優化
            - 通過討論、補充、整理，最終完善事件風暴的結果。
    3. 總結階段
        - 此時，我們的大白板上應該會呈現出一個完成的流程，包含了事件、命令、聚合、策略等。
        - 將事件風暴的結果整理成文檔，並且與參與者確認。
        - 這份文檔將作為我們設計領域模型的參考。

![alt text](image.png)

- 圖有點小，我把它畫成如下表格，方便大家查看。(老實說我不是很確定這樣畫對不對，還煩請指教。)

| 聚合 | 事件 | 命令 | 策略 |
|------|------|------|------|
| 忍具 | - 忍具已添加<br>- 忍具已更新 | - 添加忍具<br>- 更新忍具資訊 | |
| 訂單 | - 訂單已創建<br>- 訂單狀態已更新 | - 創建訂單<br>- 更新訂單狀態 | - 訂單已創建時，發送短信通知忍者<br>- 訂單狀態已更新時，發送短信通知忍者 |
| 忍者 | - 忍者已註冊<br>- 忍者已登入 | - 註冊忍者<br>- 登入 | - 忍者已註冊時，發送郵件通知忍者 |
| 忍具包 | - 忍具已加入忍具包<br>- 忍具已從忍具包移除 | - 添加忍具到忍具包<br>- 從忍具包移除忍具 | |
| 庫存 | - 庫存已更新 | - 更新庫存 | |

## 3. 領域分析

- 經過事件風暴的討論，我們已經定義出了系統中可能會發生的事件、命令、聚合、策略等，接下來我們要來進行領域分析，將這些事件、命令、聚合、策略等轉換成領域模型。
- 領域分析的目的是將事件、命令、聚合、策略等轉換成領域模型，並且確定領域模型之間的關係，這樣可以幫助我們更好地理解業務需求，並且設計出合適的領域模型。
- 本主題中，我們已經定義了以下聚合
    - 忍具
    - 訂單
    - 忍者
    - 忍具包
    - 庫存
- 接下來我們要來設計這些聚合的領域模型。

## 4. 定義限界上下文

- 甚麼叫做限界上下文?
    - 是 DDD 中的一個概念，用來定義特定領域麼型的邊界，這個邊界內部定義了一個共享的語言與模型，幫助團隊明確劃分和管理系統的邊界。
    - 縣界上下文是要提供特性領域模型與其他領域麼行之間清晰的邊界，在邊界內部，語言、概念、規則等等都是一致的。例如有兩個忍者村屬於兩個不同的領域模型，被限界上下文分開，同時，他們都有一個專有名詞叫做`忍具`，但在兩個限界上下文中，`忍具`的定義可能是不同的。A村的`忍具`可能包含了`科學忍具`、`傳統忍具`等等，而B村的`忍具`可能只包含了`傳統忍具`。這就是所謂的`語言`在同一個模型內部是一致的，但在不同的模型之間可能是不同的。他們被限界上下文分開，就是為了保證這種一致性。
    - 然而在專案中，難免會有被限界上下文分開的兩個領域模型之間需要互相溝通的情況，這時候就需要透過`Anti-corruption Layer 防腐層`或是`Context Map 上下文映射`來解決。
- 在經過事件風暴的討論後，我們已經定義出了系統中可能會發生的事件、命令、聚合、策略等，那麼限界上下文也呼之欲出了。
- 根據事件風暴的結果，我們可以定義以下限界上下文
    - 忍具管理上下文
    - 訂單管理上下文
    - 忍者管理上下文
    - 忍具包管理上下文
    - 庫存管理上下文

## 5. 領域模型設計

- 接下來我們要來設計忍具店的領域模型，這邊我們會使用 UML 來設計我們的領域模型。

```plantuml

```

- 預計我們的資料夾結構會長這樣
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── kai
│   │           └── ninja_ddd_practice
│   │               ├── application
│   │               │   ├── command
│   │               │   │   ├── 
│   │               │   │   └── 
│   │               │   ├── event
│   │               │   │   ├── 
│   │               │   │   └── 
│   │               │   ├── service
│   │               │   │   └── 
│   │               ├── domain
│   │               │   ├── aggregate
│   │               │   │   ├── ninjaTool
|   |               |   |   |   ├── entity
|   |               |   |   |   |   └── NinjaTool.java
|   |               |   |   |   └── valueObject
|   |               |   |   |       ├── ToolCategory.java
|   |               |   |   |       └── ToolSpecification.java
│   │               │   │   ├── order
│   │               │   |   |   ├── entity
|   |               |   |   |   |   └── Order.java
|   |               |   |   |   └── valueObject
|   |               |   |   |       └── 
│   │               │   │   ├── ninja
│   │               │   │   ├── ninjaToolBag
│   │               │   │   └── inventory
│   │               │   ├── repository
│   │               │   │   └── 
│   │               ├── infrastructure
│   │               │   ├── adapter
│   │               │   │   └── out
│   │               │   │       └── event
│   │               │   │           └── 
│   │               │   ├── persistence
│   │               │   │   └── 
│   │               │   └── config
│   │               │       └── 
│   │               └── interface
│   │                   ├── controller
│   │                   │   ├── NinjaToolController.java
│   |                   │   ├── OrderController.java
│   |                   │   ├── NinjaController.java
│   |                   │   ├── NinjaToolBagController.java
│   |                   │   └── InventoryController.java
│   │                   └── dto
│   │                       ├── request
│   │                       └── response
│   └── resources
│       ├── application.properties
│       └── import.sql

```

### 忍具管理上下文

- 忍具管理上下文的核心業務是管理忍具的資訊，這邊我們來看一下忍具管理上下文的領域模型。

- Entity:
    - Ninja Tool (忍具): 代表一種忍具，具有違一個標識符。
- Value Object:
    - Tool Category (忍具類別): 代表忍具的類別，例如: 武器、防具、道具等等。
    - Tool Specification (忍具規格): 代表忍具的規格，例如: 長度、重量、材質等等。
- Aggregate:
    - Ninja Tool Catalog (忍具目錄): 以 Ninja Tool 為聚合根(aggregate root)，管理忍具的資訊。
> 根聚合(aggregate root)是一個特殊的實體。      
> 根聚合的設計原則:     
> 1. 根聚合是一個實體，而不是值對象。
> 2. 根聚合是一個聚合的根，是整個聚合的入口。
> 3. 根聚合負責維護聚合內部的一致性，對外提供操作的入口。
> 4. 聚合內部的實體只能通過根聚合進行操作，外部對聚合內部的實體是透明的。
> 5. 根聚合的設計應該符合業務邏輯，而不是數據庫的設計。
> 6. 盡可能將聚合設計保持小巧，指包含緊密相關的實體，減少聚合根之間的相互依賴。
- Domain Event:
    - Ninja Tool Added Event (忍具新增事件): 當新增一個忍具時，發送一個忍具新增事件。
    - Ninja Tool Updated Event (忍具更新事件): 當更新一個忍具時，發送一個忍具更新事件。
- Repository:
    - Ninja Tool Repository (忍具倉庫): 負責管理忍具的資料存取。

## 6. 分層架構設計

- 接下來我們要來設計專案的分層架構，這邊我們會按照 DDD 的分層架構來設計專案。

### Interface Layer

- Interface Layer 是專案的入口，負責接收外部請求，並轉發給 Application Layer 進行處理。
- Controller(REST API): 提供 RESTful API 服務。
- DTO: Data Transfer Object，用來封裝請求和回應的數據。

### Application Layer

- Application Layer 是專案的應用層，負責處理業務邏輯，並調用 Domain Layer 進行業務處理。
- Service: 服務類，負責處理業務邏輯。
- 命令處理器(Command Handler): 負責處理命令。

### Domain Layer

- Domain Layer 是專案的領域層，負責定義領域模型和業務邏輯。
- Entity: 實體類，代表領域模型。
- Value Object: 值對象，代表領域模型中，不需要是唯一標識符的對象。例如: 忍具類別、忍具規格等等。在不同的忍具，可能會有相同的忍具類別或忍具規格。
- Aggregate: 聚合根，代表一個聚合的根，負責維護聚合內部的一致性。
- Domain Event: 領域事件，例如: 忍具新增事件、忍具更新事件就可以定義為領域事件。
- Domain Service: 領域服務，負責處理領域邏輯。例如我們要處理的業務邏輯，就可以定義是領域服務。

### Infrastructure Layer

- Infrastructure Layer 是專案的基礎設施層，負責專案的基礎設施建設。
- Repository: 資料庫的實踐，負責管理領域模型的資料存取。
- Out bound adapter: 外部適配器，負責對外部系統的適配。例如 publish event、send message、third-party API 等等。

### Anti-corruption Layer

- Anti-corruption Layer 是專案的防腐層，為了防止領域模型之間的相互依賴，我們可以使用防腐層來解決。
- 外部系統適配器: 負責對外部系統的適配。

## 7. 實作







```plantuml
@startuml

skinparam packageStyle rectangle

package "Interface Layer" {
  [NinjaToolController]
  [AddNinjaToolRequest]
  [NinjaToolResponse]
}

package "Application Layer" {
  [NinjaToolService]
  [AddNinjaToolCommand]
  [GetNinjaToolQuery]
}

package "Domain Layer" {
  [NinjaTool]
  [NinjaToolCatalog]
  [ToolCategory]
  [ToolSpecification]
  [NinjaToolRepository]
  [NinjaToolAddedEvent]
  [NinjaToolUpdatedEvent]
  [DomainEventPublisher]
}

package "Infrastructure Layer" {
  [JpaNinjaToolRepository]
  [PersistenceConfig]
}

NinjaToolController --> NinjaToolService: uses
NinjaToolController --> AddNinjaToolRequest: uses
NinjaToolController --> NinjaToolResponse: returns

NinjaToolService --> AddNinjaToolCommand: uses
NinjaToolService --> GetNinjaToolQuery: uses
NinjaToolService --> NinjaToolRepository: uses
NinjaToolService --> NinjaTool: creates/manages

NinjaToolCatalog --> NinjaTool: contains
NinjaToolCatalog --> DomainEventPublisher: publishes events to

NinjaTool --> ToolCategory: has
NinjaTool --> ToolSpecification: has

NinjaToolRepository <|.. JpaNinjaToolRepository: implements

JpaNinjaToolRepository --> PersistenceConfig: uses

@enduml
```

# 🛡️ 防腐層架構指南

## 📋 什麼是防腐層？

**防腐層 (Anti-Corruption Layer, ACL)** 是 Domain-Driven Design (DDD) 中的一個重要模式，主要作用是保護領域模型免受外部系統的"腐蝕"。

### 🎯 防腐層的核心目標

1. **保持領域純淨性** - 確保領域模型不被基礎設施技術污染
2. **隔離外部依賴** - 將外部系統的複雜性隔離在領域邊界之外
3. **提供穩定介面** - 為領域層提供穩定、一致的服務介面
4. **促進技術演進** - 允許底層技術的靈活替換而不影響業務邏輯

### 💀 沒有防腐層的危害

在我們專案的早期版本中，就存在典型的"腐蝕"問題：

```java
// ❌ 錯誤範例：被污染的領域模型 (原本的 Product.java)
@Entity // ← JPA 註解污染了領域模型
@Table(name = "product")
@Getter
@Setter
public class Product {
    @Id // ← 數據庫技術細節洩漏到領域層
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded // ← 持久化技術細節
    private ProductDetails details;

    @ManyToOne // ← 關係映射註解
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    // 業務邏輯與基礎設施技術混合在一起
    public void updateStock(int quantity) {
        if (this.stockQuantity + quantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        this.stockQuantity += quantity;
    }
}
```

**這種寫法的問題：**
- 🚫 領域概念被數據庫技術綁定
- 🚫 無法獨立測試業務邏輯
- 🚫 更換 ORM 框架需要修改領域模型
- 🚫 領域專家無法理解技術註解
- 🚫 業務邏輯與技術實現緊耦合

### ✅ 防腐層解決方案

透過防腐層，我們將領域模型與基礎設施完全分離：

```java
// ✅ 正確範例：純淨的領域模型 (ProductPure.java)
public class ProductPure {
    private final ProductId id;
    private ProductDetails details;
    private Money price;
    private StockQuantity stockQuantity;
    private ProductCategory category;
    private String status;
    private String imageUrl;

    // 純粹的業務邏輯，無任何基礎設施依賴
    public void updateStock(int quantity) {
        this.stockQuantity = this.stockQuantity.adjust(quantity);
    }

    public boolean isAvailable() {
        return "ACTIVE".equals(status) && stockQuantity.isAvailable();
    }

    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        updateStock(-quantity);
    }
}
```

## 🏗️ 本專案的防腐層實作

### 1. 數據庫防腐層 🗄️

#### **問題背景**
在本專案中，我們最初犯了一個常見的錯誤：讓領域模型直接繼承 JPA Repository

```java
// ❌ 錯誤的做法 (原本的 ProductRepositoryImpl.java)
@Repository
public interface ProductRepositoryImpl extends JpaRepository<Product, Long>, ProductRepository {
    // 直接繼承 JpaRepository，將數據庫技術洩漏到領域層
}
```

**這種做法的問題：**
- 🚫 領域層被迫依賴 JPA 技術
- 🚫 Repository 介面包含大量不需要的方法
- 🚫 測試時需要啟動完整的 JPA 環境
- 🚫 無法控制數據訪問的粒度

#### **防腐層解決方案**

我們建立了完整的數據庫防腐層：

**步驟 1：純淨的領域 Repository 介面**
```java
// ✅ 純淨的領域 Repository 介面
public interface ProductRepository {
    Optional<ProductPure> findById(ProductId id);
    List<ProductPure> findAllActive();
    ProductPure save(ProductPure product);
    void deleteById(ProductId id);
    // 只包含業務需要的方法，不包含技術細節
}
```

**步驟 2：數據庫實體（基礎設施層）**
```java
// ✅ 基礎設施層的數據庫實體
@Entity
@Table(name = "products")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    // 所有 JPA 註解都在基礎設施層
}
```

**步驟 3：防腐層映射器**
```java
// ✅ 防腐層映射器，負責領域模型與數據庫實體間的轉換
@Component
public class ProductEntityMapper {
    
    public ProductPure toDomain(ProductEntity entity) {
        if (entity == null) return null;
        
        return ProductPure.builder()
                .id(ProductId.of(entity.getId()))
                .details(ProductDetails.builder()
                    .name(entity.getName())
                    .description(entity.getDescription())
                    .build())
                .price(Money.of(entity.getPrice(), entity.getCurrency()))
                .stockQuantity(StockQuantity.of(entity.getStockQuantity()))
                .build();
    }

    public ProductEntity toEntity(ProductPure domain) {
        // 反向轉換邏輯
    }
}
```

**步驟 4：防腐層 Repository 實作**
```java
// ✅ 防腐層 Repository 實作
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductJpaRepository jpaRepository;
    private final ProductEntityMapper mapper;

    @Override
    public Optional<ProductPure> findById(ProductId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain); // 透過映射器轉換
    }

    @Override
    public ProductPure save(ProductPure product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
```

### 2. 外部服務防腐層 🌐

#### **問題背景**
許多專案直接在應用服務中調用外部 API，導致業務邏輯與外部服務緊耦合：

```java
// ❌ 錯誤的做法：直接在應用服務中調用外部 API
@Service
public class OrderApplicationService {
    
    public void processOrder(Order order) {
        // 直接調用外部支付 API
        PaymentApiClient paymentApi = new PaymentApiClient();
        PaymentApiRequest request = new PaymentApiRequest();
        request.setAmount(order.getTotal().toString());
        request.setMerchantId("12345");
        PaymentApiResponse response = paymentApi.charge(request);
        
        // 業務邏輯與外部 API 格式緊耦合
        if ("SUCCESS".equals(response.getStatus())) {
            order.markAsPaid();
        }
    }
}
```

**這種做法的問題：**
- 🚫 業務邏輯與外部 API 格式緊耦合
- 🚫 外部服務變更會影響業務代碼
- 🚫 難以進行單元測試
- 🚫 無法統一處理外部服務錯誤

#### **防腐層解決方案**

**步驟 1：定義純淨的領域服務介面**
```java
// ✅ 純淨的領域服務介面
public interface PaymentService {
    PaymentResult processPayment(PaymentRequest request);
    PaymentStatus checkPaymentStatus(String transactionId);
}

// ✅ 領域對象，不包含任何外部服務的技術細節
@Value
@Builder
public class PaymentRequest {
    String orderId;
    Money amount;        // 使用領域值對象
    String customerId;
    String paymentMethod;
}
```

**步驟 2：外部服務客戶端（基礎設施層）**
```java
// ✅ 外部服務客戶端，處理具體的 API 調用
@Component
public class ExternalPaymentClient {
    
    public ExternalPaymentResponse processPayment(ExternalPaymentRequest request) {
        // 具體的外部 API 調用邏輯
        // 所有外部服務的技術細節都在這裡
    }
}
```

**步驟 3：防腐層適配器**
```java
// ✅ 防腐層適配器，隔離領域層與外部服務
@Service
public class PaymentServiceAdapter implements PaymentService {
    
    private final ExternalPaymentClient externalPaymentClient;

    @Override
    public PaymentResult processPayment(PaymentRequest request) {
        try {
            // 1. 轉換領域對象為外部 API 格式
            ExternalPaymentRequest externalRequest = convertToExternalRequest(request);
            
            // 2. 調用外部服務
            ExternalPaymentResponse externalResponse = 
                externalPaymentClient.processPayment(externalRequest);
            
            // 3. 轉換外部響應為領域對象
            return convertToDomainResult(externalResponse);
            
        } catch (Exception e) {
            log.error("Payment processing failed", e);
            return PaymentResult.failed(e.getMessage());
        }
    }

    private ExternalPaymentRequest convertToExternalRequest(PaymentRequest request) {
        // 格式轉換邏輯，隔離外部 API 的複雜性
        return ExternalPaymentRequest.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount().getAmount())
                .currency(request.getAmount().getCurrency())
                .build();
    }
}
```

### 3. 消息隊列防腐層 📨

#### **問題背景**
直接在領域服務中使用 Kafka API：

```java
// ❌ 錯誤的做法：領域服務直接依賴 Kafka
@Service 
public class OrderDomainService {
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // 直接依賴 Kafka
    
    public void createOrder(Order order) {
        // 業務邏輯
        order.process();
        
        // 直接使用 Kafka API
        String message = "{\"orderId\":\"" + order.getId() + "\"}";
        kafkaTemplate.send("order-created", message);
    }
}
```

#### **防腐層解決方案**

**步驟 1：定義領域事件**
```java
// ✅ 純淨的領域事件
@Value
@Builder
public class OrderCreatedEvent implements DomainEvent {
    String eventId;
    String orderId;
    String customerId;
    LocalDateTime occurredOn;
    
    public static OrderCreatedEvent create(String orderId, String customerId) {
        return OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .orderId(orderId)
                .customerId(customerId)
                .occurredOn(LocalDateTime.now())
                .build();
    }
}
```

**步驟 2：領域事件發布者介面**
```java
// ✅ 純淨的事件發布者介面
public interface DomainEventPublisher {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
```

**步驟 3：Kafka 防腐層適配器**
```java
// ✅ Kafka 防腐層適配器
@Service
public class DomainEventKafkaAdapter implements DomainEventPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(DomainEvent event) {
        try {
            // 1. 轉換領域事件為 Kafka 消息格式
            KafkaMessage kafkaMessage = convertToKafkaMessage(event);
            
            // 2. 序列化為 JSON
            String messageJson = objectMapper.writeValueAsString(kafkaMessage);
            
            // 3. 發送到對應的 Topic
            String topicName = getTopicName(event.getEventType());
            kafkaTemplate.send(topicName, event.getAggregateId(), messageJson);
            
        } catch (Exception e) {
            log.error("Failed to publish event: {}", event.getEventType(), e);
        }
    }

    private KafkaMessage convertToKafkaMessage(DomainEvent event) {
        return KafkaMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .eventType(event.getEventType())
                .aggregateId(event.getAggregateId())
                .payload(serializeEvent(event))
                .timestamp(event.getOccurredOn())
                .source("ninja-backend")
                .version("1.0")
                .build();
    }
}
```

## ⚠️ 常見的錯誤做法與避免方式

### 1. 純淨領域模型
```
domainLayer/
├── aggregations/
│   └── product/
│       ├── aggregateRoot/
│       │   └── ProductPure.java          # 純淨的產品聚合根
│       └── valueObjects/
│           ├── ProductId.java            # 產品ID值對象
│           ├── Money.java                # 金錢值對象
│           └── StockQuantity.java        # 庫存數量值對象
├── domainEvents/
│   ├── DomainEvent.java                  # 領域事件介面
│   ├── OrderCreatedEvent.java            # 訂單創建事件
│   └── ProductStockChangedEvent.java     # 庫存變更事件
└── domainServices/
    ├── PaymentService.java               # 支付服務介面
    ├── NotificationService.java          # 通知服務介面
    └── DomainEventPublisher.java         # 事件發布者介面
```

### 2. 基礎設施防腐层
```
infrastructureLayer/
├── persistence/
│   ├── entities/
│   │   ├── ProductEntity.java            # 數據庫實體（包含 JPA 註解）
│   │   └── ProductCategoryEntity.java    # 分類實體
│   ├── mappers/
│   │   └── ProductEntityMapper.java      # 領域模型 ↔ 數據庫實體 轉換器
│   └── repositories/
│       └── ProductJpaRepository.java     # JPA Repository（基礎設施）
├── externalServices/
│   ├── payment/
│   │   ├── PaymentServiceAdapter.java    # 支付服務防腐層適配器
│   │   ├── ExternalPaymentClient.java    # 外部支付 API 客戶端
│   │   ├── ExternalPaymentRequest.java   # 外部支付請求格式
│   │   └── ExternalPaymentResponse.java  # 外部支付響應格式
│   └── notification/
│       ├── NotificationServiceAdapter.java # 通知服務防腐層適配器
│       ├── EmailServiceClient.java       # 郵件服務客戶端
│       ├── SmsServiceClient.java         # 簡訊服務客戶端
│       └── PushNotificationClient.java   # 推播服務客戶端
├── messaging/
│   └── kafka/
│       ├── DomainEventKafkaAdapter.java  # Kafka 事件發布防腐層
│       └── KafkaMessage.java             # Kafka 消息格式
└── repositoryImplementations/
    └── ProductRepositoryImpl.java        # Repository 防腐層實作
```

### 3. 防腐层轉換流程

#### **數據庫操作流程**
```
應用服務層
    ↓ 使用領域對象 (ProductPure)
領域 Repository 介面 (ProductRepository)
    ↓ 
防腐層實作 (ProductRepositoryImpl)
    ↓ 轉換：ProductPure → ProductEntity
JPA Repository (ProductJpaRepository)
    ↓ 
數據庫
    ↑ 
JPA Repository 返回 ProductEntity
    ↑ 轉換：ProductEntity → ProductPure
防腐層實作 (ProductRepositoryImpl)
    ↑ 
領域 Repository 介面返回 ProductPure
    ↑ 
應用服務層
```

#### **外部服務調用流程**
```
應用服務層
    ↓ 使用領域對象 (PaymentRequest)
領域服務介面 (PaymentService)
    ↓ 
防腐層適配器 (PaymentServiceAdapter)
    ↓ 轉換：PaymentRequest → ExternalPaymentRequest
外部服務客戶端 (ExternalPaymentClient)
    ↓ 
第三方支付 API
    ↑ 
外部服務客戶端返回 ExternalPaymentResponse
    ↑ 轉換：ExternalPaymentResponse → PaymentResult
防腐層適配器 (PaymentServiceAdapter)
    ↑ 
領域服務介面返回 PaymentResult
    ↑ 
應用服務層
```

## 🔧 最新重構經驗與模式

### 1. 雙重值對象模式 (Dual Value Object Pattern)

在實際重構過程中，我們發現了一個重要模式：**為同一個概念創建純領域模型和 JPA 可嵌入版本**。

#### **問題背景**
當領域模型需要包含複雜值對象時，我們面臨兩個衝突的需求：
- 🎯 **領域純淨性**：希望值對象完全純淨，不包含任何 JPA 註解
- 🗄️ **持久化需求**：JPA 需要 `@Embeddable` 註解才能正確映射

#### **解決方案：雙重值對象模式**

**步驟 1：創建純淨的領域值對象**
```java
// ✅ 純淨的領域值對象
@Value
@Builder
public class PaymentInfoPure {
    String paymentMethod;
    String transactionId;
    Money amount;
    LocalDateTime paymentTime;
    String status;
    String description;

    public boolean isSuccessful() {
        return "SUCCESS".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }
}
```

**步驟 2：創建對應的 JPA 可嵌入版本**
```java
// ✅ JPA 可嵌入版本（僅用於持久化）
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoEmbeddable {
    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Column(name = "payment_currency")
    private String paymentCurrency;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_description")
    private String paymentDescription;

    /**
     * 轉換為純領域模型
     */
    public PaymentInfoPure toDomain() {
        return PaymentInfoPure.builder()
                .paymentMethod(paymentMethod)
                .transactionId(transactionId)
                .amount(new Money(paymentAmount, paymentCurrency))
                .paymentTime(paymentTime)
                .status(paymentStatus)
                .description(paymentDescription)
                .build();
    }

    /**
     * 從純領域模型轉換
     */
    public static PaymentInfoEmbeddable fromDomain(PaymentInfoPure paymentInfo) {
        return PaymentInfoEmbeddable.builder()
                .paymentMethod(paymentInfo.getPaymentMethod())
                .transactionId(paymentInfo.getTransactionId())
                .paymentAmount(paymentInfo.getAmount().getAmount())
                .paymentCurrency(paymentInfo.getAmount().getCurrency())
                .paymentTime(paymentInfo.getPaymentTime())
                .paymentStatus(paymentInfo.getStatus())
                .paymentDescription(paymentInfo.getDescription())
                .build();
    }
}
```

**步驟 3：在聚合根中智能使用**
```java
// ✅ 聚合根同時支持兩種表示
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 用於 JPA 持久化
    @Embedded
    private PaymentInfoEmbeddable paymentInfo;

    /**
     * 業務方法使用純領域模型
     */
    public void processPayment(PaymentInfoPure paymentInfoPure) {
        // 業務邏輯使用純值對象
        if (!paymentInfoPure.isSuccessful()) {
            throw new IllegalArgumentException("Payment must be successful");
        }

        // 轉換後存儲
        this.paymentInfo = PaymentInfoEmbeddable.fromDomain(paymentInfoPure);
        this.status = "PAID";
    }

    /**
     * 對外暴露純領域模型
     */
    public PaymentInfoPure getPaymentInfoDomain() {
        return paymentInfo != null ? paymentInfo.toDomain() : null;
    }
}
```

### 2. ProductCategory 防腐層重構案例

#### **原始問題**
```java
// ❌ 原始錯誤：值對象被 JPA 污染
@Entity
@Table(name = "product_categories")
@Getter
@Setter
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    // 缺少 active 欄位，導致資料庫 NOT NULL 約束錯誤
}
```

#### **重構解決方案**

**步驟 1：純淨的值對象**
```java
// ✅ 純淨的領域值對象
@Value
@Builder
public class ProductCategory {
    Long id;
    String name;
    String description;
    Boolean active;

    public boolean isActive() {
        return Boolean.TRUE.equals(active);
    }

    public static ProductCategory createActive(String name, String description) {
        return ProductCategory.builder()
                .name(name)
                .description(description)
                .active(true)
                .build();
    }
}
```

**步驟 2：基礎設施實體**
```java
// ✅ 基礎設施層實體
@Entity
@Table(name = "product_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private Boolean active;
}
```

**步驟 3：防腐層映射器**
```java
// ✅ 防腐層映射器
@Component
public class ProductCategoryEntityMapper {
    
    public ProductCategory toDomain(ProductCategoryEntity entity) {
        if (entity == null) return null;
        
        return ProductCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.getActive())
                .build();
    }

    public ProductCategoryEntity toEntity(ProductCategory domain) {
        if (domain == null) return null;
        
        return ProductCategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .active(domain.getActive())
                .build();
    }
}
```

**步驟 4：防腐層 Repository**
```java
// ✅ 防腐層 Repository 實作
@Service
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {
    
    private final ProductCategoryJpaRepository jpaRepository;
    private final ProductCategoryEntityMapper mapper;

    @Override
    public Optional<ProductCategory> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ProductCategory> findAllActive() {
        return jpaRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public ProductCategory save(ProductCategory category) {
        ProductCategoryEntity entity = mapper.toEntity(category);
        ProductCategoryEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
```

### 3. Money 值對象設計模式

#### **設計原則**
```java
// ✅ 優秀的 Money 值對象設計
@Value
public class Money {
    BigDecimal amount;
    String currency;
    
    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.amount = amount;
        this.currency = currency;
    }
    
    // 便利構造函數，預設台幣
    public Money(BigDecimal amount) {
        this(amount, "TWD");
    }
    
    // 工廠方法
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
    
    public static Money of(BigDecimal amount, String currency) {
        return new Money(amount, currency);
    }

    // 業務方法
    public Money add(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add different currencies");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(double multiplier) {
        return new Money(amount.multiply(BigDecimal.valueOf(multiplier)), currency);
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
```

### 4. 處理資料庫欄位不一致問題

#### **常見問題：NOT NULL 約束錯誤**

**問題描述：**
```sql
-- ❌ 原始 SQL 缺少必要欄位
INSERT INTO product_categories (name, description) VALUES 
('忍具', '各種忍者工具');
-- 錯誤：Column 'active' cannot be null
```

**解決方案：**
```sql
-- ✅ 修正後的 SQL
INSERT INTO product_categories (name, description, active) VALUES 
('忍具', '各種忍者工具', true),
('武器', '各種忍者武器', true),
('防具', '各種護身裝備', true);
```

**防範措施：**
```java
// ✅ 在映射器中提供預設值
@Component
public class ProductCategoryEntityMapper {
    
    public ProductCategoryEntity toEntity(ProductCategory domain) {
        if (domain == null) return null;
        
        return ProductCategoryEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .active(domain.getActive() != null ? domain.getActive() : true) // 預設值
                .build();
    }
}
```

### 5. 處理常見的重構陷阱

#### **陷阱 1：忘記更新資料庫初始化腳本**
```sql
-- ❌ 錯誤：沒有同步更新 data.sql
INSERT INTO product_categories (name, description) VALUES 
('忍具', '各種忍者工具');
-- 會導致：Column 'active' cannot be null

-- ✅ 正確：同步更新所有欄位
INSERT INTO product_categories (name, description, active) VALUES 
('忍具', '各種忍者工具', true);
```

#### **陷阱 2：複雜值對象的 JPA 嵌入錯誤**
```java
// ❌ 錯誤：直接嵌入複雜值對象
@Entity
public class Order {
    @Embedded
    private PaymentInfoPure paymentInfo; // 包含 Money 等複雜型別
}

// ✅ 正確：使用專用的 Embeddable 版本
@Entity
public class Order {
    @Embedded
    private PaymentInfoEmbeddable paymentInfo;
    
    public void processPayment(PaymentInfoPure domainPaymentInfo) {
        this.paymentInfo = PaymentInfoEmbeddable.fromDomain(domainPaymentInfo);
    }
}
```

#### **陷阱 3：映射器中的空值處理不當**
```java
// ❌ 錯誤：沒有處理空值
public ProductCategory toDomain(ProductCategoryEntity entity) {
    return ProductCategory.builder()
            .id(entity.getId())
            .name(entity.getName())
            .active(entity.getActive()) // 如果為 null 會出問題
            .build();
}

// ✅ 正確：完整的空值檢查
public ProductCategory toDomain(ProductCategoryEntity entity) {
    if (entity == null) return null;
    
    return ProductCategory.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .active(entity.getActive() != null ? entity.getActive() : true)
            .build();
}
```

#### **陷阱 4：Import 混乱導致的編譯錯誤**
```java
// ❌ 錯誤：混用領域模型和實體類型
import com.kai.ninja_ddd_practice.domainLayer.aggregations.product.valueObjects.ProductCategory; // 領域模型
import com.kai.ninja_ddd_practice.infrastructureLayer.persistence.entities.ProductCategoryEntity; // 實體

// 在同一個類中混用會導致型別錯誤

// ✅ 正確：明確區分使用場景
// 在應用服務中：只 import 領域模型
// 在映射器中：明確 import 兩者並正確轉換
// 在 Repository 實作中：主要使用實體，透過映射器轉換
```

### 6. 重構後的驗證策略

#### 編譯階段驗證
```bash
# 確保所有 Java 文件編譯無誤
mvn compile

# 檢查測試編譯
mvn test-compile
```

#### 啟動階段驗證
```java
// ✅ 確保應用能正常啟動，檢查：
// 1. Hibernate 能正確映射所有實體
// 2. 資料庫連接正常
// 3. 初始資料載入成功
@SpringBootTest
public class ApplicationStartupTest {
    @Test
    public void contextLoads() {
        // 如果這個測試通過，說明基本的 Bean 注入和映射都正確
    }
}
```

#### 資料庫結構驗證
```sql
-- 檢查資料表結構
DESCRIBE product_categories;
-- 確保 active 欄位存在且有預設值

-- 檢查資料是否正確載入
SELECT * FROM product_categories WHERE active = true;
```

#### 業務邏輯驗證
```java
// ✅ 確保防腐層不影響業務邏輯
@Test
public void testProductCategoryBusinessLogic() {
    ProductCategory category = ProductCategory.createActive("測試分類", "測試描述");
    assertTrue(category.isActive());
    assertEquals("測試分類", category.getName());
}
```

## 🚀 使用建議

### 1. **始終保持領域層純淨**
```java
// ✅ 好的做法
public class ProductPure {
    private final ProductId id;
    private Money price;
    
    public void updatePrice(Money newPrice) {
        if (newPrice.isNegativeOrZero()) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = newPrice;
    }
}

// ❌ 避免的做法
@Entity  // 不要在領域模型中使用基礎設施註解
public class Product {
    @Id  // 不要讓領域模型依賴 JPA
    private Long id;
}
```

### 2. **依賴抽象而非具體實作**
```java
// ✅ 好的做法 - 依賴領域介面
@Service
public class OrderApplicationService {
    private final PaymentService paymentService; // 抽象介面
    private final ProductRepository productRepository; // 抽象介面
}

// ❌ 避免的做法 - 直接依賴具體實作
@Service
public class OrderApplicationService {
    @Autowired
    private PaymentServiceAdapter paymentAdapter; // 具體實作
    @Autowired
    private ProductJpaRepository jpaRepository; // JPA 具體實作
}
```

### 3. **完整的轉換邏輯**
```java
// ✅ 好的做法 - 完整的空值檢查和錯誤處理
public class ProductEntityMapper {
    public ProductPure toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        
        ProductDetails details = entity.getName() != null ? 
            ProductDetails.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .build() : null;
                
        return ProductPure.builder()
                .id(ProductId.of(entity.getId()))
                .details(details)
                .price(Money.of(entity.getPrice(), entity.getCurrency()))
                .stockQuantity(StockQuantity.of(entity.getStockQuantity()))
                .build();
    }
}
```

### 4. **統一的配置管理**
```java
// ✅ 集中管理防腐層配置
@Configuration
public class AntiCorruptionLayerConfig {
    
    @Bean
    @Primary
    public ObjectMapper antiCorruptionObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

## 📞 總結

通過本專案的防腐層實作，我們實現了：

### ✅ **已解決的問題**
1. **領域污染** - 領域模型不再包含 JPA 註解等基礎設施細節
2. **技術綁定** - 可以輕鬆替換 ORM 框架、消息隊列等底層技術
3. **測試困難** - 領域邏輯可以獨立測試，無需啟動基礎設施
4. **外部依賴** - 外部服務變更不會影響領域邏輯
5. **錯誤傳播** - 外部服務錯誤被統一轉換為領域錯誤
6. **型別不一致** - 透過映射器統一處理領域模型與基礎設施的型別差異
7. **欄位缺失** - 防腐層確保所有必要的資料庫欄位都有對應處理
8. **複雜值對象嵌入** - 使用雙重值對象模式解決 JPA 嵌入限制
9. **NOT NULL 約束** - 映射器提供合理的預設值處理策略

### 🎯 **核心原則**
1. **隔離性** - 領域層與基礎設施層完全隔離
2. **轉換性** - 所有外部交互都經過格式轉換
3. **穩定性** - 為領域層提供穩定的服務介面
4. **可測試性** - 領域邏輯可以獨立測試

### 🔄 **實作模式**
1. **純淨領域模型** + **基礎設施實體** + **映射器**
2. **領域服務介面** + **外部服務適配器** + **格式轉換**
3. **領域事件** + **消息隊列適配器** + **消息轉換**
4. **雙重值對象模式** - 純領域值對象 + JPA 可嵌入版本
5. **映射器預設值策略** - 處理資料庫 NOT NULL 約束
6. **複雜型別拆分** - 避免 Hibernate 無法處理的嵌入型別
7. **防腐層驗證機制** - 確保重構前後功能一致性

### 7. 應用服務層防腐層重構案例 - ProductApplicationService

#### **問題背景：重複的應用服務**

在重構過程中，我們發現了一個常見問題：同一個應用服務存在兩個版本：
- `ProductApplicationService` - 原始版本，功能簡單
- `ProductApplicationServiceV2` - 完整版本，展示正確的防腐層架構

#### **重構決策：合併與升級**

**原始 ProductApplicationService（功能有限）：**
```java
// ❌ 原始版本：功能過於簡單
@Service
public class ProductApplicationService {
    private final ProductRepository productRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Transactional
    public List<GetProductCardsDto> getProductCards() {
        List<ProductPure> products = productRepository.findAvailableProducts();
        return products.stream()
                .map(ProductApplicationLayerMapper::covertProductToGetProductCardsDto)
                .toList();
    }
    // 僅有一個方法，功能不完整
}
```

**ProductApplicationServiceV2（完整實作）：**
```java
// ✅ V2 版本：完整的防腐層架構實作
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductApplicationServiceV2 {
    private final ProductRepository productRepository; // 防腐層 Repository
    private final DomainEventPublisher eventPublisher; // 防腐層事件發布者

    // 包含完整的 CRUD 操作
    public List<ProductPure> getAllActiveProducts() { ... }
    public ProductPure getProductById(Long id) { ... }
    public ProductPure createProduct(...) { ... }
    public void updateProductStock(...) { ... }
    public void updateProductPrice(...) { ... }
    public boolean isProductAvailable(...) { ... }
    public void reserveProductStock(...) { ... }
}
```

#### **重構解決方案：向後相容的升級**

**步驟 1：合併功能到主服務**
```java
// ✅ 重構後：結合兩者優點，保持向後相容
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductApplicationService {
    private final ProductRepository productRepository; // 防腐層 Repository
    private final DomainEventPublisher eventPublisher; // 防腐層事件發布者

    /**
     * 獲取產品卡片列表 (原有功能保持向後相容)
     */
    @Transactional(readOnly = true)
    public List<GetProductCardsDto> getProductCards() {
        log.info("Fetching product cards");
        
        List<ProductPure> products = productRepository.findAvailableProducts();
        
        return products.stream()
                .map(ProductApplicationLayerMapper::covertProductToGetProductCardsDto)
                .toList();
    }

    /**
     * 獲取所有有效產品 (新增的完整功能)
     */
    @Transactional(readOnly = true)
    public List<ProductPure> getAllActiveProducts() {
        log.info("Fetching all active products");
        return productRepository.findAllActive();
    }

    /**
     * 根據ID獲取產品 (展示防腐層模式)
     */
    @Transactional(readOnly = true)
    public ProductPure getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        
        ProductId productId = ProductId.of(id);
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * 創建新產品 (完整的業務邏輯)
     */
    @Transactional
    public ProductPure createProduct(String name, String description, BigDecimal price, 
                                   int stockQuantity, Long categoryId) {
        log.info("Creating new product: {}", name);

        // 1. 創建值對象
        ProductDetails details = ProductDetails.builder()
                .name(name)
                .description(description)
                .build();

        Money productPrice = Money.of(price);
        StockQuantity stock = StockQuantity.of(stockQuantity);

        // 2. 創建產品聚合根（純淨的領域模型）
        ProductPure product = ProductPure.builder()
                .id(null) // 新產品，ID 由數據庫生成
                .details(details)
                .price(productPrice)
                .stockQuantity(stock)
                .status("ACTIVE")
                .imageUrl("https://example.com/default-product.jpg")
                .build();

        // 3. 通過防腐層保存到數據庫
        ProductPure savedProduct = productRepository.save(product);

        log.info("Product created successfully with id: {}", savedProduct.getId().getValue());
        return savedProduct;
    }

    /**
     * 更新產品庫存 (包含事件發布)
     */
    @Transactional
    public void updateProductStock(Long productId, int quantityChange, String reason) {
        log.info("Updating stock for product {} by {} units. Reason: {}", 
                productId, quantityChange, reason);

        // 1. 獲取產品（通過防腐層）
        ProductId id = ProductId.of(productId);
        ProductPure product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. 記錄變更前的庫存
        int oldStock = product.getStockQuantity().getValue();

        // 3. 執行業務邏輯（純粹的領域邏輯）
        product.updateStock(quantityChange);

        // 4. 保存變更（通過防腐層）
        productRepository.save(product);

        // 5. 發布領域事件（通過防腐層）
        ProductStockChangedEvent event = ProductStockChangedEvent.create(
                productId.toString(),
                oldStock,
                product.getStockQuantity().getValue(),
                reason
        );
        eventPublisher.publish(event);

        log.info("Stock updated successfully. Old: {}, New: {}", 
                oldStock, product.getStockQuantity().getValue());
    }

    // ... 其他業務方法
}
```

**步驟 2：移除重複的服務類別**
```bash
# 刪除重複的 V2 版本
rm ProductApplicationServiceV2.java
```

**步驟 3：驗證重構結果**
```java
// ✅ 確保 Controller 仍然正常工作
@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductApplicationService productApplicationService; // 仍然注入主服務

    @GetMapping("/get-product-cards")
    public List<GetProductsResponse> getProductList() {
        // 原有的 API 仍然正常工作
        List<GetProductCardsDto> productCardsDtos = productApplicationService.getProductCards();
        return productCardsDtos.stream()
                .map(product -> objectMapper.convertValue(product, GetProductsResponse.class))
                .toList();
    }
}
```

#### **重構效益**

**1. 向後相容性**
- ✅ 原有的 `getProductCards()` 方法保持不變
- ✅ 現有的 Controller 和測試不需要修改
- ✅ API 端點繼續正常工作

**2. 功能完整性**
- ✅ 新增了完整的產品管理功能
- ✅ 展示了正確的防腐層使用模式
- ✅ 包含了領域事件發布機制

**3. 架構一致性**
- ✅ 使用統一的防腐層模式
- ✅ 遵循 DDD 最佳實踐
- ✅ 保持領域模型純淨性

**4. 可維護性**
- ✅ 消除了重複代碼
- ✅ 提供了完整的業務操作
- ✅ 便於後續功能擴展

#### **重構教訓**

**避免功能分散**
```java
// ❌ 錯誤：為同一個聚合創建多個應用服務
ProductApplicationService        // 基本功能
ProductApplicationServiceV2      // 完整功能
ProductManagementService         // 管理功能
// 導致功能分散，維護困難

// ✅ 正確：統一在一個應用服務中
ProductApplicationService {
    // 所有與產品相關的應用層邏輯
    getProductCards()           // 原有功能
    getAllActiveProducts()      // 查詢功能  
    createProduct()            // 創建功能
    updateProductStock()       // 更新功能
    // 功能集中，易於維護
}
```

**保持向後相容**
```java
// ✅ 重構時保持原有 API 不變
@Transactional(readOnly = true)
public List<GetProductCardsDto> getProductCards() {
    log.info("Fetching product cards"); // 新增日誌
    
    // 原邏輯保持不變，確保向後相容
    List<ProductPure> products = productRepository.findAvailableProducts();
    return products.stream()
            .map(ProductApplicationLayerMapper::covertProductToGetProductCardsDto)
            .toList();
}
```

**漸進式功能升級**
```java
// ✅ 在保持原功能的基礎上逐步添加新功能
public class ProductApplicationService {
    // 步驟 1：保留原有方法
    public List<GetProductCardsDto> getProductCards() { ... }
    
    // 步驟 2：添加新的查詢方法
    public List<ProductPure> getAllActiveProducts() { ... }
    
    // 步驟 3：添加創建方法
    public ProductPure createProduct(...) { ... }
    
    // 步驟 4：添加更新方法（包含事件發布）
    public void updateProductStock(...) { ... }
}
```

### 📋 **重構經驗教訓**

## 🎯 新增聚合根重構案例

### 4. ShoppingCart 聚合根重構

#### **原始問題**
```java
// ❌ 原始錯誤：購物車聚合根被 JPA 污染
@Entity
@Table(name = "shopping_carts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

    // 業務邏輯與基礎設施技術混合
    public void addProduct(Product product, int quantity) {
        // 直接操作基礎設施層的 Product Entity
        items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.incrementQuantity(quantity, product.getPrice()),
                        () -> items.add(new CartItem(this.id, product, quantity, product.getPrice()))
                );
    }
}
```

#### **重構解決方案**

**1. 純領域模型**
```java
// ✅ 純淨的購物車聚合根
@Value
@Builder(toBuilder = true)
@Jacksonized
public class ShoppingCartPure {
    
    ShoppingCartId id;
    @NonNull UserId userId;
    @NonNull @Builder.Default List<CartItemPure> items = List.of();

    /**
     * 新增產品到購物車 - 純業務邏輯
     */
    public ShoppingCartPure addProduct(ProductPure product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        Optional<CartItemPure> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(product.getId()))
                .findFirst();
                
        if (existingItem.isPresent()) {
            // 更新現有商品數量
            List<CartItemPure> updatedItems = items.stream()
                    .map(item -> item.getProductId().equals(product.getId()) 
                            ? item.incrementQuantity(quantity) 
                            : item)
                    .toList();
            return this.toBuilder().items(updatedItems).build();
        } else {
            // 新增商品項目
            CartItemPure newItem = CartItemPure.builder()
                    .productId(product.getId())
                    .productName(product.getDetails().getName())
                    .quantity(quantity)
                    .unitPrice(product.getPrice().getAmount())
                    .build();
                    
            List<CartItemPure> updatedItems = new java.util.ArrayList<>(items);
            updatedItems.add(newItem);
                    
            return this.toBuilder().items(updatedItems).build();
        }
    }

    /**
     * 計算總金額
     */
    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(CartItemPure::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

**2. JPA Entity（基礎設施層）**
```java
// ✅ 基礎設施層實體
@Entity
@Table(name = "shopping_carts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShoppingCartEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "cartId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();
}
```

**3. 防腐層映射器**
```java
// ✅ 領域模型與基礎設施層的轉換
@Component
public class ShoppingCartEntityMapper {

    public ShoppingCartPure toDomain(ShoppingCartEntity entity) {
        if (entity == null) return null;

        List<CartItemPure> items = entity.getItems().stream()
                .map(this::cartItemToDomain)
                .toList();

        return ShoppingCartPure.builder()
                .id(entity.getId() != null ? ShoppingCartId.of(entity.getId()) : null)
                .userId(UserId.of(entity.getUserId()))
                .items(items)
                .build();
    }

    public ShoppingCartEntity toEntity(ShoppingCartPure domain) {
        // 領域模型轉換為實體的邏輯
    }
}
```

### 5. Order 聚合根重構

#### **原始問題**
```java
// ❌ 原始錯誤：訂單聚合根被 JPA 污染
@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private String status; // 使用字符串而非枚舉

    @Embedded
    private PaymentInfoEmbeddable paymentInfo;

    // 空的業務方法
    public void addItem(Long productId, int quantity, BigDecimal price) { }
    public void updateStatus(OrderStatus newStatus) { }
}
```

#### **重構解決方案**

**1. 純領域模型**
```java
// ✅ 純淨的訂單聚合根
@Value
@Builder(toBuilder = true)
@Jacksonized
public class OrderPure {
    
    OrderId id;
    @NonNull UserId userId;
    @NonNull @Builder.Default List<OrderItemPure> items = List.of();
    @NonNull @Builder.Default OrderStatus status = OrderStatus.PENDING;
    @NonNull BigDecimal totalAmount;
    PaymentInfoPure paymentInfo;
    @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt;

    /**
     * 新增訂單項目 - 完整的業務邏輯
     */
    public OrderPure addItem(ProductId productId, String productName, int quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        
        // 檢查是否已存在相同產品
        Optional<OrderItemPure> existingItem = items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();
                
        if (existingItem.isPresent()) {
            // 更新現有項目數量
            List<OrderItemPure> updatedItems = items.stream()
                    .map(item -> item.getProductId().equals(productId) 
                            ? item.toBuilder().quantity(item.getQuantity() + quantity).build()
                            : item)
                    .toList();
                    
            return this.toBuilder()
                    .items(updatedItems)
                    .totalAmount(calculateTotalAmount(updatedItems))
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // 新增項目
            OrderItemPure newItem = OrderItemPure.builder()
                    .productId(productId)
                    .productName(productName)
                    .quantity(quantity)
                    .unitPrice(unitPrice)
                    .build();
                    
            List<OrderItemPure> updatedItems = new java.util.ArrayList<>(items);
            updatedItems.add(newItem);
            
            return this.toBuilder()
                    .items(updatedItems)
                    .totalAmount(calculateTotalAmount(updatedItems))
                    .updatedAt(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * 更新訂單狀態 - 包含狀態轉換驗證
     */
    public OrderPure updateStatus(OrderStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        // 檢查狀態轉換是否合法
        if (!isValidStatusTransition(this.status, newStatus)) {
            throw new IllegalStateException(
                String.format("Invalid status transition from %s to %s", this.status, newStatus)
            );
        }
        
        return this.toBuilder()
                .status(newStatus)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 處理付款
     */
    public OrderPure processPayment(PaymentInfoPure paymentInfo) {
        if (paymentInfo == null) {
            throw new IllegalArgumentException("Payment info cannot be null");
        }
        
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only process payment for pending orders");
        }
        
        return this.toBuilder()
                .paymentInfo(paymentInfo)
                .status(OrderStatus.PAID)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * 檢查狀態轉換是否合法
     */
    private boolean isValidStatusTransition(OrderStatus from, OrderStatus to) {
        return switch (from) {
            case PENDING -> to == OrderStatus.PAID || to == OrderStatus.CANCELLED;
            case PAID -> to == OrderStatus.PROCESSING || to == OrderStatus.CANCELLED;
            case PROCESSING -> to == OrderStatus.SHIPPED || to == OrderStatus.CANCELLED;
            case SHIPPED -> to == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false; // 終態，不可轉換
        };
    }

    private BigDecimal calculateTotalAmount(List<OrderItemPure> items) {
        return items.stream()
                .map(OrderItemPure::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

**2. 強型別訂單狀態**
```java
// ✅ 擴展的訂單狀態枚舉
public enum OrderStatus {
    PENDING("PENDING", "待處理"),
    PAID("PAID", "已支付"),
    PROCESSING("PROCESSING", "處理中"),
    SHIPPED("SHIPPED", "已發貨"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");

    private final String status;
    private final String statusDescription;

    OrderStatus(String status, String statusDescription) {
        this.status = status;
        this.statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public boolean canCancel() {
        return this == PENDING || this == PAID;
    }
}
```

**3. JPA Entity 與映射器**
```java
// ✅ JPA Entity（基礎設施層）
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // 付款資訊分解為基本欄位
    @Column(name = "payment_method")
    private String paymentMethod;
    
    @Column(name = "payment_amount", precision = 10, scale = 2)
    private BigDecimal paymentAmount;
    
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum OrderStatusEnum {
        PENDING, PAID, PROCESSING, SHIPPED, COMPLETED, CANCELLED
    }
}
```

## 🎯 **完整聚合根重構效果**

經過完整的防腐層重構，現在所有聚合根都遵循正確的 DDD 模式：

### **✅ 已重構的聚合根**
1. **UserPure** - 用戶聚合根，包含 UserId、UserProfilePure、UserCredentialsPure
2. **ProductPure** - 產品聚合根，包含 ProductId、ProductDetails、Money、StockQuantity
3. **ShoppingCartPure** - 購物車聚合根，包含 CartItemPure、ShoppingCartId
4. **OrderPure** - 訂單聚合根，包含 OrderItemPure、OrderStatus、PaymentInfoPure

### **🏗️ 統一的防腐層架構**
每個聚合根都包含完整的防腐層組件：
- **純領域模型** - 不含任何基礎設施依賴
- **JPA Entity** - 僅用於基礎設施層
- **EntityMapper** - 負責領域模型與實體轉換
- **Repository Interface** - 純領域的 Repository 契約
- **JpaRepository** - Spring Data JPA Repository
- **Repository Implementation** - 防腐層 Repository 實作

### **📊 重構統計**
- ✅ **4 個主要聚合根**完成重構
- ✅ **12+ 個值對象**創建（各種 ID、Pure 類型）
- ✅ **8 個 JPA Entity**（分離基礎設施關切）
- ✅ **4 個 EntityMapper**（領域-基礎設施轉換）
- ✅ **8 個 Repository**（介面 + 實作）
- ✅ **所有編譯錯誤**已解決
- ✅ **應用程式**可正常啟動

### 📋 **重構經驗教訓**
1. **領域純淨性是第一原則** - 絕不允許基礎設施註解污染領域模型
2. **資料庫約束要與程式碼同步** - 每個 NOT NULL 欄位都要有對應處理
3. **複雜值對象需要特殊處理** - 使用雙重表示模式解決 JPA 限制
4. **映射器是防腐層的核心** - 負責所有格式轉換和預設值處理
5. **漸進式重構策略** - 一次重構一個聚合，確保每步都能驗證
6. **完整的測試驗證** - 編譯、啟動、資料載入、業務邏輯都要測試通過
7. **避免重複的應用服務** - 同一聚合的功能應集中在單一應用服務中
8. **保持向後相容性** - 重構時保留原有 API，逐步添加新功能
9. **統一使用防腐層模式** - 所有外部交互都應通過防腐層進行

這個防腐層架構確保了您的 DDD 專案具有真正的技術無關性和高度的可維護性，是企業級 DDD 實作的最佳實踐。


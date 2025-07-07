# Ninja DDD Practice

這是一個使用領域驅動設計 (DDD) 方法實現的 Ninja 工具項目。

## 開發規範

### Conventional Commits 提交規範

本專案使用 [Conventional Commits](https://www.conventionalcommits.org/) 規範來標準化提交訊息。這有助於自動生成變更日誌和語義化版本號。

提交訊息的基本格式：

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

#### 常用的提交類型

- `feat`: 添加新功能
- `fix`: 修復錯誤
- `docs`: 僅文檔更改
- `style`: 不影響代碼含義的更改 (空白、格式、缺少分號等)
- `refactor`: 既不修復錯誤也不添加功能的代碼更改
- `perf`: 改進性能的代碼更改
- `test`: 添加缺失的測試或修改現有的測試
- `build`: 影響構建系統或外部依賴的更改 (例如: npm, maven)
- `ci`: CI 配置文件和腳本的更改
- `chore`: 其他不修改 src 或測試文件的更改
- `revert`: 恢復先前的提交

#### 示例

```
feat(shopping-cart): 添加購物車結帳功能

添加了購物車結帳頁面和支付整合。

BREAKING CHANGE: 更改了結帳 API 的請求格式
```

## 自動化流程

- **語義化版本控制**: 基於 Conventional Commits 自動生成版本號
- **自動變更日誌**: 發布時自動生成詳細的變更日誌
- **自動 Git Tag**: 每次發布時自動創建標籤並推送
- **自動 Release**: 將構建產物和變更日誌作為發布資源上傳

## 構建與部署

請參考項目根目錄中的 `.github/workflows/main.yml` 文件了解完整的 CI/CD 流程。

# todoApp backend

# directory
```
.
├── main/java/com/example/spring_project/
│   ├── common // 共通メソッド用。(それ以外のものもあるが廃止予定。)
│   ├── config // 設定ファイル
│   ├── domain
│   │   ├── entity // ドメインモデル
│   │   └── repository // interface
│   ├── infrastructure
│   │   ├── googleApi // google api系のメソッド
│   │   ├── rdb // MySQL操作用のMapperのJavaファイル
│   │   └── redis // redis系のメソッド
│   ├── presentation
│   │   ├── controller // コントローラ
│   │   └── model // リクエスト・レスポンス用
│   └── service // ユースケース
└── resources/java/com/example/spring_project/
    ├── config // 設定ファイル
    └── infrastructure // mybatisのxmlファイル

```

# 実行方法
- dev containerで`spring`のコンテナに入る。
- vscodeのサイドバー`実行とデバッグ`から`SpringProjectApplication`を実行。
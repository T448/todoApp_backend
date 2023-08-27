# todoApp backend

# directory
```
.
├── bin
│   ├── default
│   ├── generated-sources
│   ├── generated-test-sources
│   ├── main
│   └── test
├── gradle
└── src
    ├── main
    │   ├── java
    │   └── resources
    └── test
```

# directory 案
```
.
├── java/x/x/x/...
│   ├── domain
│   │   ├── entity // classの定義。プロパティ(名前と型)、メソッド(外部サービス(DB等)とやり取りして値を取得するものはここには書かない。)
│   │   └── repository // interfaceの定義。メソッドについて書くが、中身の具体的な処理は書かない。メソッド名、引数、返り値。
│   ├── infrastructure
│   │   ├── ormapper // mybatisのxmlとのマッピング(対応付け)用。interface
│   │   └── repository //  実装クラス。domain.repositoryではinterfaceのみを定義しているため、中身の具体的な処理はここで行う。(といっても、実際のDB操作はxmlで行い、interfaceから値を取り出せるので、それを使用して、加工等して返す感じ。)
│   ├── presentation // ユーザーがフロント操作して最初に指示が到達するのがこのpresentation層。
│   │   ├── controller // GetMapperなど。
│   │   ├── model // リクエスト、レスポンスの型定義
│   │   └── scheduled_task // 名前の通り。
│   ├── service // ユースケースを実現するロジックを書く。
│   └── config // 環境変数等
└──resources // mybatisのxml
```
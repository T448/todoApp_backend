# todoApp backend

# directory
```
.
├── README.md
├── build.gradle
├── eclipse-formatter.xml
├── gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── spring_project
    │   │               ├── SpringProjectApplication.java
    │   │               ├── common // 共通メソッド用。(それ以外のものもあるが廃止予定。)
    │   │               ├── config // 設定用
    │   │               │   └── ApplicationProperty.java 
    │   │               ├── domain
    │   │               │   ├── entity // ドメインモデル
    │   │               │   └── repository // interface
    │   │               ├── infrastructure
    │   │               │   ├── googleApi // google api系のメソッド
    │   │               │   ├── rdb // MySQL操作用のMapperのJavaファイル
    │   │               │   └── redis // redis系のメソッド
    │   │               ├── presentation
    │   │               │   ├── controller // コントローラ
    │   │               │   └── model // リクエスト・レスポンス用
    │   │               └── service // ユースケース
    │   └── resources
    │       ├── application.yml
    │       └── com
    │           └── example
    │               └── spring_project
    │                   ├── config
    │                   │   └── application.properties // 設定ファイル
    │                   └── infrastructure
    │                       └── rdb // mybatisのxmlファイル
    └── test
```

# 実行方法
- dev containerで`spring`のコンテナに入る。
- vscodeのサイドバー`実行とデバッグ`から`SpringProjectApplication`を実行。

または以下のコマンドを実行

```
./gradlew bootrun
```

## build
```
./gradlew build
java -jar ./build/libs/spring_project-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev // 本番環境の場合はprodを指定する
```

### src/main/resources/com/example/spring_project/config/application.properties
```
spring.request_url=https://oauth2.googleapis.com/token
spring.grant_type=refresh_token
spring.login_url=http://localhost:8080/api/login
spring.redis_host=redis
spring.redis_port=6379

spring.oauth2_request_url=https://oauth2.googleapis.com/token
spring.get_user_info_request_url=https://www.googleapis.com/oauth2/v1/userinfo
spring.client_secret={GCPで発行したClient Secret}
spring.client_id={GCPで発行したClient ID}
spring.redirect_uri=http://localhost:5173/app
```
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
    │       ├── application-prod.yml
    │       └── com
    │           └── example
    │               └── spring_project
    │                   ├── config
    │                   │   ├── application.properties // 設定ファイル(main)
    │                   │   ├── application-dev.properties // 設定ファイル(開発環境用)
    │                   │   └── application-prod.properties // 設定ファイル(本番環境用)
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

# 設定ファイル
### src/main/resources/com/example/spring_project/config/application.properties
```
spring.profiles.default=dev
spring.profiles.active=dev
# spring.profiles.default=prod
# spring.profiles.active=prod
```
### src/main/resources/com/example/spring_project/config/application-dev.properties
```
server.port=8080
spring.request_url=https://oauth2.googleapis.com/token
spring.grant_type=refresh_token
spring.login_url=http://localhost:8080/api/login
spring.redis_host=redis-dev
spring.redis_port=6379

spring.oauth2_request_url=https://oauth2.googleapis.com/token
spring.get_user_info_request_url=https://www.googleapis.com/oauth2/v1/userinfo
spring.client_secret={GCPで発行したClient Secret}
spring.client_id={GCPで発行したClient ID}
spring.redirect_uri=http://localhost:5173/app
```

### src/main/resources/com/example/spring_project/config/application-prod.properties
```
server.port=8080
spring.request_url=https://oauth2.googleapis.com/token
spring.grant_type=refresh_token
spring.login_url=http://localhost:8080/api/login
spring.redis_host=redis-prod
spring.redis_port=6379

spring.oauth2_request_url=https://oauth2.googleapis.com/token
spring.get_user_info_request_url=https://www.googleapis.com/oauth2/v1/userinfo
spring.client_secret={GCPで発行したClient Secret}
spring.client_id={GCPで発行したClient ID}
spring.redirect_uri=http://localhost:4173/app
```

### src/main/resources/application.yml
```
spring:
  datasource:
    url: jdbc:mysql://mysql-prod:3306/mysqldb
    username: user
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

```

### src/main/resources/application-prod.yml
```
spring:
  datasource:
    url: jdbc:mysql://mysql-prod:3306/mysqldb
    username: user
    password: password
    driver-class-name: com.mysql.cj.jdbc.Driver

```
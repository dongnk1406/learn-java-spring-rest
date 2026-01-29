# Identity service

This microservice is responsible for:

* Onboarding users
* Roles and permissions
* Authentication

## Tech stack

* Build tool: maven >= 3.9.5
* Java: 21
* Framework: Spring boot 3.2.x
* DBMS: MySQL

## Prerequisites

* Java SDK 21
* A MySQL server

## Start application

`docker compose up -d`

`mvn install`

`mvn spring-boot:run`

## Lombok

* là một thư viện Java giúp sinh các mã getter & setter tự động. Bên cạnh đó còn hỗ trợ sinh các hàm khởi tạo (constructor) với tham số, hoặc không có tham số.

* với dto, ta nên dùng anotation @Data của lombok => nó giúp gen @Settet, @Getter, @ToString, @EqualAndHashCode, @RequiredArgConstructor
* @Builder: giúp tạo một builder class

30/05/2025: Mã hóa mật khẩu an toàn và matching nhanh chóng với Bcrypt

PreAuthorize: kiểm tra thoả mãn điều kiện rồi mới vào method để thực hiện -> được dùng nhiều hơn
PostAuthorize: thực hiện method, rồi mới kiểm tra, nếu thoả mãn thì trả về kết quả

## Logout flow

* lưu token đã logout

* ở bước verifyToken, nêú token đó đã tồn tại trong bảng invalidate-token => throw Error

## Isolation trong UnitTest, best practice trong Unit test

Isolation là unit test có thể chạy trên bất cứ môi trường nào, mà không cần service bên ngoài
=> solution: sử dụng h2 database

* Hiện tại đang có logic sẽ khoẻ tạo tài khoản admin khi init. Nhưng có lỗi phần đó
=> solution: sử dụng ConditionalOnProperty để tạo điều kiện chi khởi tạo @Bean
VD: hiện tại đang kết nối với CSDL bên ngoài -> vói isolation, ta có thể chạy unit test mà k cần kết nối

## Format code using Spotless

`mvn spotless:check`

`mvn spotless:apply`

## Code coverage using JaCoCo

for some reason, we need to open db first. Let's fix it later in unit test using Isolation
`./mvnw test jacoco:report`

go to target/site/index.html to see the test report

## Code quality with SonarLint & SonarQube

`docker run --name sonar-qube -p 9000:9000 -d sonarqube:lts-community`

username:admin
password: root

* access localhost:9000, generate token and cmd
* run that sonarqube cmd in the command prompt

## Swagger

Reference: <https://www.youtube.com/watch?v=VYvqF-J2JFc>
<http://localhost:8080/identity/swagger-ui/index.html>

## Concurrency

## Bean

The @Bean annotation tells Spring that the method produces a Spring-managed bean.

When the application starts, Spring calls this method, executes the configuration, and registers the returned SecurityFilterChain in the ApplicationContext.

That bean is then available for dependency injection anywhere in your application.

## Unique field trong JPA và bài toán concurrent request

## Race condition

## ORM

* Spring JPA

* Spring JPA and Hibernate are closely related but not the same: Hibernate is an ORM implementation, while Spring JPA (often Spring Data JPA) is a higher-level abstraction built on top of JPA that can use Hibernate under the hood

* Hibernate is a JPA implementation, while Spring Data JPA is a JPA data access abstraction. Spring Data JPA cannot work without a JPA provider.

## Spring security

![alt text](assets/aop.png)

* Spring Security filter chain is a series of standard servlet filters that work in concert to apply security logic to every incoming HttpServletRequest. Each filter has a specific responsibility (e.g., authentication, authorization), and they are executed in a defined order before the request reaches the application's main logic (like a controller).

### KeyCloak

## AOP

<https://www.youtube.com/watch?v=GAKppXwPelw&list=PL2xsxmVse9IYN20XBnf7dXUNnErtzP1ov>

### OAuth

<https://www.youtube.com/watch?v=SViY_TMa5sg&list=PL2xsxmVse9IbweCh6QKqZhousfEWabSeq>

## Migration

## Middleware

Middleware functions can perform the following tasks:

* execute any code.
* make changes to the request and the response objects.
* end the request-response cycle.
* call the next middleware function in the stack.
* if the current middleware function does not end the request-response cycle, it must call next() to pass control to the next middleware function.
Otherwise, the request will be left hanging.

## Validation

* Schema based validation

* Object schema validation

* Class validator

## Guard

Guards are executed after all middleware, but before any interceptor or pipe.

## Authentication

### Role-based authentication

### Refresh token

## Authorization

## Interceptors

Interceptors have a set of useful capabilities which are inspired by the Aspect Oriented Programming (AOP) technique. They make it possible to:

bind extra logic before / after method execution
transform the result returned from a function
transform the exception thrown from a function
extend the basic function behavior
completely override a function depending on specific conditions (e.g., for caching purposes)

## File upload

## Hashing

## Microservices

## CI/CD

## Docker

## Redis

## Kafka

## AWS

## Monitoring

## Profiles

<https://docs.spring.io/spring-boot/reference/features/profiles.html>

generate key using <https://generate-random.org/>

With Spring Boot Maven Plugin

<https://docs.spring.io/spring-boot/maven-plugin/using.html#using.overriding-command-line>

`mvn spring-boot:run -Dspring-boot.run.profiles=production`

`mvn spring-boot:run -Dspring-boot.run.profiles=staging`

## Environment variables

<https://docs.spring.io/spring-boot/reference/features/external-config.html>

<https://www.youtube.com/watch?v=v2aX-gJACRw&list=PL2xsxmVse9IaxzE8Mght4CFltGOqcG6FC&index=29>

## Build and Deploy

### Build

#### Option 1

`./mvnw clean`

`./mvnw package`

if you wanna ignore the unit test
`./mvnw package -DskipTests`

After the build is completed, you can the the archive in target folder
Now you can run the .jar file
`java -jar directoryToFile`

#### Option 2 (more popular)

install java, maven on your machine first

`mvn clean`

`mvn package -DskipTests`

#### Option 3

`docker build -t archiveName .`

eg: `docker build -t spring-boot-identity:0.0.1 .`

then we can run the image

`docker run archiveName`

eg: `docker run spring-boot-identity:0.0.1`

## Send email

<https://www.geeksforgeeks.org/springboot/spring-boot-sending-email-via-smtp/>

## Architecture (Domain driven design)

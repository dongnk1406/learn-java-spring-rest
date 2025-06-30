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
`mvn spring-boot:run`

## Build application
`mvn clean package`

## Lombok
- là một thư viện Java giúp sinh các mã getter & setter tự động. Bên cạnh đó còn hỗ trợ sinh các hàm khởi tạo (constructor) với tham số, hoặc không có tham số.
- với dto, ta nên dùng anotation @Data của lombok => nó giúp gen @Settet, @Getter, @ToString, @EqualAndHashCode, @RequiredArgConstructor
- @Builder: giúp tạo một builder class

30/05/2025: Mã hóa mật khẩu an toàn và matching nhanh chóng với Bcrypt

PreAuthorize: kiểm tra thoả mãn điều kiện rồi mới vào method để thực hiện -> được dùng nhiều hơn
PostAuthorize: thực hiện method, rồi mới kiểm tra, nếu thoả mãn thì trả về kết quả

## Logout flow
- lưu token đã logout

## Isolation trong UnitTest, best practice trong Unit test
Isolation là unit test có thể chạy trên bất cứ môi trường nào, mà không cần service bên ngoài
=> solution: sử dụng h2 database
- Hiện tại đang có logic sẽ khoẻ tạo tài khoản admin khi init. Nhưng có lỗi phần đó
=> solution: sử dụng ConditionalOnProperty để tạo điều kiện chi khởi tạo @Bean
VD: hiện tại đang kết nối với CSDL bên ngoài -> vói isolation, ta có thể chạy unit test mà k cần kết nối

## Format code using Spotless
mvn spotless:check
mvn spotless:apply

## Code coverage using JaCoCo
for some reason, we need to open db first. Let's fix it later in unit test using Isolation
./mvnw test jacoco:report

go to target/site/index.html to see the test report

## Code quality with SonarLint & SonarQube
`docker run --name sonar-qube -p 9000:9000 -d sonarqube:lts-community`

username:admin
password: root

- access localhost:9000, generate token and cmd
- run that sonarqube cmd in the command prompt
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

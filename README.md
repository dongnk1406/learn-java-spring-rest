*** Lombok 
- là một thư viện Java giúp sinh các mã getter & setter tự động. Bên cạnh đó còn hỗ trợ sinh các hàm khởi tạo (constructor) với tham số, hoặc không có tham số.
- với dto, ta nên dùng anotation @Data của lombok => nó giúp gen @Settet, @Getter, @ToString, @EqualAndHashCode, @RequiredArgConstructor
- @Builder: giúp tạo một builder class

Khóa học Java spring boot 3 By Devteria

docker compose up -d

30/05/2025: Mã hóa mật khẩu an toàn và matching nhanh chóng với Bcrypt

PreAuthorize: kiểm tra thoả mãn điều kiện rồi mới vào method để thực hiện -> được dùng nhiều hơn
PostAuthorize: thực hiện method, rồi mới kiểm tra, nếu thoả mãn thì trả về kết quả

*** Logout flow
- lưu token đã logout

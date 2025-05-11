# 🛠️ Book Store - Backend

Bu proje, kitap satılan bir e-ticaret sitesi örneğidir (Case).
Spring Boot ile geliştirilmiş olup, JWT kimlik doğrulama sistemiyle güvenli giriş/çıkış işlemleri gerçekleştirilir.

## 🚀 Başlarken

### Gereksinimler

- Java 17
- Visual Codse (veya benzeri bir editör)
- MySQL (veya bağlantılı bir veritabanı altyapısı)  
- Postman (API testleri için önerilir)

### Kurulum

1. Bu repoyu klonlayın:

```bash
git clone https://github.com/Mudo06/Book-Store.git
```

2. `application.properties` dosyasını düzenleyin:

Veritabanı bağlantısı ayarını yapılandırın (veri tabanı projeyi başlattığınızda otomatik olarak oluşacaktır).

```json
# Database configuration
spring.datasource.url=jdbc:mysql://your-connection/book_store?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

4. Uygulamayı başlatın:

```bash
mvn spring-boot:run
```

5. Eğer editörünüz Otomatik tarayıcı açma desteklenmiyor ise link ile herhangi bir tarayıcıda Swagger UI'a bağlanabilirsiniz:

```bash
http://localhost:8080/swagger-ui/index.html?continue#/
```
  6.Eğer Postman ile test etmek isterseniz
`Bookstore_API_Collection.json` dosyasını Postman de import ederek test edebilirsiniz.


## 📌 Özellikler

- JWT ile kimlik doğrulama  
- Kullanıcı kaydı ve girişi  
- Kitap Yazar Kullanıcı Admin Sepet Sepet ürünü Sipariş  ekleme, güncelleme, silme  
- Çeşitli Get API ler (kitabı ismine göre bulma v.b.).   
- RESTful API mimarisi  

## 📁 Proje Yapısı

```bash
bookstore/
├── config/
│   ├── JwtService.java
│   ├── SecurityConfig.java
│   ├── SecurityUtil.java
│   └── SwaggerConfig.java
│
├── controller/
│   ├── AuthorController.java
│   ├── BookController.java
│   ├── OrderController.java
│   ├── ShoppingCartController.java
│   └── UserController.java
│
├── dto/
│   ├── Cart/
│   │   ├── CartItemDTO.java
│   │   └── ShoppingCartDTO.java
│   ├── Jwt/
│   │   ├── AuthResponseDTO.java
│   │   └── SignInRequest.java
│   ├── Order/
│   │   ├── CreateOrderRequest.java
│   │   ├── OrderDTO.java
│   │   └── OrderItemDTO.java
│   ├── AuthorDTO.java
│   ├── BookDTO.java
│   └── UserDTO.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── UnauthorizedException.java
│
├── model/
│   ├── Cart/
│   │   ├── CartItem.java
│   │   └── ShoppingCart.java
│   ├── Order/
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── OrderStatus.java
│   ├── Author.java
│   ├── Book.java
│   └── User.java
│
├── repository/
│   ├── Cart/
│   │   ├── CartItemRepository.java
│   │   └── ShoppingCartRepository.java
│   ├── Order/
│   │   ├── OrderItemRepository.java
│   │   ├── OrderRepository.java
│   │   ├── AuthorRepository.java
│   │   ├── BookRepository.java
│   │   └── UserRepository.java
│
├── service/
│   ├── AuthorService.java
│   ├── BookService.java
│   ├── OrderService.java
│   ├── ShoppingCartService.java
│   └── UserService.java
│
└── BookstoreApplication.java

```

##🖼️ Swagger Tüm API'ler
![Image](https://github.com/user-attachments/assets/78bce7e3-82d4-4ec2-b76b-401949c4e859)

##🛢️ Veritabanı Şeması (Entity-Relationship Diagram)
![Image](https://github.com/user-attachments/assets/94b0701f-1457-41eb-b24e-2195a4d503f6)

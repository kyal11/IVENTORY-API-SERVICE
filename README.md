# 📦 Inventory API

Inventory API adalah RESTful service untuk mengelola barang (item), pengguna (user), owner, serta transaksi item seperti assign, transfer, borrow, dan return.  
API ini dirancang untuk mendukung tracking stok dan riwayat transaksi

Dokumentasi lengkap seluruh endpoint API dapat diakses melalui Postman Collection berikut:

👉 [Lihat Dokumentasi Postman](https://documenter.getpostman.com/view/38192115/2sBXVcjsVM)
---

## 🚀 Fitur Utama

- Manajemen User (Admin, Staff, Auditor, dll)
- Manajemen Item dan status ketersediaan
- Manajemen Owner (Division, Person, Location, Vendor)
- Transaksi Item:
    - Assign
    - Transfer
    - Borrow
    - Return
- Tracking item:
    - Riwayat peminjaman
    - Item yang sedang dipinjam (aktif)
    - Riwayat seluruh transaksi
    - Transfer Kepemilikan barang
    - Assign barang ke sistem
- Audit log transaksi item
- Autentikasi menggunakan JWT

---

## 🛠️ Teknologi

- **Java 21**
- **Spring Boot 4.0.1**
- **PostgreSQL**
---

## ⚙️ Instalasi & Menjalankan Aplikasi

### 1️⃣ Konfigurasi Application Properties
1. Salin file:
   ```
   application.properties.example
   ```
2. Ubah menjadi:
   ```
   application.properties
   ```
3. Isi konfigurasi sesuai environment
4. Pindahkan ke:
   ```
   src/main/resources
   ```

---

### 2️⃣ Menjalankan Database (Docker Compose – Opsional)
Jika menggunakan Docker Compose:

1. Salin file:
   ```
   .env.example
   ```
2. Ubah menjadi:
   ```
   .env
   ```
3. Sesuaikan konfigurasi database
4. Jalankan:
   ```bash
   docker compose up -d
   ```

---

### 3️⃣ Setup Database
1. Jalankan file SQL:
   ```
   src/main/resources/db/migration/init_db_v1.sql
   ```
2. Jalankan seeder:
   ```
   init_seeder_v1.sql
   ```

---

### 4️⃣ Menjalankan Project
Jalankan perintah berikut di root project:

```bash
mvn clean
mvn install
mvn spring-boot:run
```

Atau jalankan langsung:
```
InventoryApplication.java
```

---

### 5️⃣ Dokumentasi API
Dokumentasi API lengkap tersedia di:

- 📁 Local:
  ```
  src/main/resources/documentation
  ```

- 🌐 **Online Documentation (Postman):**
  [Lihat Dokumentasi Postman](https://documenter.getpostman.com/view/38192115/2sBXVcjsVM)
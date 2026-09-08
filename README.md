# Lab 8 - Product Shop

รหัสนักศึกษา **673380062-2**, **SEC 1**  
ชื่อ Repository สำหรับส่ง: **lab8-673380062-2-sec1**

ระบบ Spring Boot + Thymeleaf + JPA + PostgreSQL ตามโจทย์
[PANANG6425/LAB08_Table_Relationships](https://github.com/PANANG6425/LAB08_Table_Relationships)
ใช้ HTML และ CSS จากต้นฉบับ แล้วเพิ่ม Java, validation, หน้าจัดการรีวิว และชุดทดสอบ

## สิ่งที่ทำได้

- CRUD สินค้าพร้อมข้อมูลเสริม ProductDetail แบบ 1:1
- เพิ่ม/แสดง/ลบรีวิวหลายรายการต่อสินค้าแบบ 1:N
- ส่วนลด NONE, MEMBER 10%, SEASONAL 20% ด้วย Strategy Pattern
- แก้ไขสินค้าโดยรักษา detail ID และรีวิวเดิม
- ลบสินค้าพร้อมรายละเอียดและรีวิวผ่าน JPA cascade
- ตรวจข้อมูลฝั่งเซิร์ฟเวอร์ รวมคะแนน 1-5 และแสดงข้อผิดพลาดในฟอร์ม
- ไม่อนุญาตให้ bind Entity ID หรือ FK จากฟอร์ม

## วิธีรัน (Windows / PowerShell)

ต้องมี JDK 17 ขึ้นไป และ PostgreSQL เปิดทำงาน
โปรเจกต์มี Maven Wrapper ไม่ต้องติดตั้ง Maven แยก

1. สร้างฐานข้อมูลด้วย pgAdmin หรือคำสั่ง:

```powershell
psql -U postgres -c "CREATE DATABASE lab8shop;"
```

2. เปิด PowerShell ในโฟลเดอร์โปรเจกต์ แล้วตั้งรหัสผ่าน PostgreSQL ของตนเอง:

```powershell
$env:DB_PASSWORD="รหัสผ่าน PostgreSQL ของคุณ"
.\mvnw.cmd spring-boot:run
```

3. เปิด <http://localhost:8080/products>

ถ้า `psql` ไม่อยู่ใน PATH ให้ใช้ SQL ใน Query Tool ของ pgAdmin:

```sql
CREATE DATABASE lab8shop;
```

หากพอร์ตหรือผู้ใช้ต่างจากค่าเริ่มต้น กำหนดก่อนรัน:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/lab8shop"
$env:DB_USERNAME="postgres"
$env:PORT="8080"
```

JPA สร้างตารางและ Foreign Key อัตโนมัติด้วย `ddl-auto=update`
อย่าใส่รหัสผ่านจริงใน Git หรือรายงาน

บน macOS/Linux ใช้ `sh mvnw spring-boot:run` และตั้ง environment variables ตาม shell ที่ใช้

## ทดสอบและ build

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
java -jar target/lab8-673380062-2-sec1-0.0.1-SNAPSHOT.jar
```

ชุดทดสอบใช้ H2 ใน PostgreSQL mode จึงไม่ต้องเปิด PostgreSQL เพื่อรัน tests
แอปตามปกติใช้ PostgreSQL ตามโจทย์
ผลจริงและข้อจำกัดของสภาพแวดล้อมที่ใช้ทดสอบอยู่ใน `docs/test-results.txt`

## โครงสร้าง

```text
src/main/java/com/example/demo/
  DemoApplication.java
  model/        Product, ProductDetail, Review
  repository/   JpaRepository ทั้งสาม Entity
  strategy/     interface + strategies + DiscountContext
  form/         DTO และ validation
  service/      ProductService และ transactions
  controller/   ProductController และ HTTP mappings
src/main/resources/
  application.properties
  templates/products/  list, add, edit, delete, reviews
  static/css/style.css
src/test/              ชุดทดสอบ 6 กรณี
docs/                  รายงาน PDF, ภาพหน้าจอ, SQL และผลทดสอบ
```

## ความสัมพันธ์

- `products.detail_id` -> `product_details.id` มี UNIQUE เพื่อบังคับ 1:1
- `reviews.product_id` -> `products.id` รองรับหลายรีวิวต่อสินค้า
- ฝั่ง owning ของ 1:N คือ Review ส่วน Product.reviews เป็น inverse ด้วย `mappedBy`
- `cascade=ALL` + `orphanRemoval=true` จัดการการลบข้อมูลลูกจาก JPA
- ฐานข้อมูลไม่ได้กำหนด ON DELETE CASCADE; การลบผ่านแอปทำภายใน transaction

## ไฟล์ส่งและ GitHub

- รายงาน: `docs/Lab08-Report-673380062-2-sec1.pdf`
- ภาพจริง: `docs/screenshots/`
- SQL snapshot: `docs/database-snapshot.sql` เป็นข้อมูลตัวอย่างก่อนทดสอบลบ ใช้ศึกษาหรือ restore ลงฐานข้อมูลใหม่ว่างเท่านั้น
- `docs/verify-database.sql` ใช้ดูตารางและ FK ใน pgAdmin
- `ASSIGNMENT.md` คือโจทย์ต้นฉบับ

ยังไม่ได้สร้างหรืออัปโหลด Repository ในบัญชี GitHub ของนักศึกษา
สร้าง repo ชื่อ `lab8-673380062-2-sec1` โดยเว้นว่าง แล้ว push จากโฟลเดอร์นี้:

```powershell
git init -b main
git add .
git commit -m "Complete Lab 8 table relationships"
git remote add origin https://github.com/YOUR_USERNAME/lab8-673380062-2-sec1.git
git push -u origin main
```

เปลี่ยน YOUR_USERNAME เป็นบัญชีของตนเอง และตั้ง Git name/email ก่อน commit หากเครื่องยังไม่เคยตั้งค่า
จากนั้นส่งลิงก์ repository และ PDF ตามโจทย์

## ตัวอย่างข้อมูลสำหรับอธิบายงาน

ใช้ชื่อ `iPhone 15 Pro (673380062-2 SEC 1)` ราคา 40,000 บาท MEMBER จะได้ 36,000 บาท
เพิ่มรีวิวอีกหนึ่งรายการ แล้วแก้ราคาเป็น 39,000 บาท SEASONAL จะได้ 31,200 บาท
รีวิวเดิมยังอยู่ครบ หลังลบสินค้าให้ตรวจทั้งสามตารางว่าไม่มีแถวลูกค้าง

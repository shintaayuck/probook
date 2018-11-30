# Tugas 2 IF3110 Pengembangan Aplikasi Berbasis Web 

Melakukan *upgrade* Website toko buku online pada Tugas 1 dengan mengaplikasikan **arsitektur web service REST dan SOAP**.

## Format penamaan 
arkavTalks-13516017-M. Nuraihan Naufal

### Tujuan Pembuatan Tugas

Diharapkan dengan tugas ini anda dapat mengerti:
* Produce dan Consume REST API
* Produce dan Consume Web Services dengan protokol SOAP
* Membuat web application yang akan memanggil web service secara REST dan SOAP.
* Memanfaatkan web service eksternal (API)

## Anggota Tim

1. 13516017 - M. Nurraihan Naufal
2. 13516029 - Shinta Ayu Chandra Kemala
3. 13516152 - Deborah Aprilia Josephine

### Outline ReadMe

1. Deskripsi tugas
2. Struktur aplikasi dan penggunaan
3. Penjelasan terkait :
- Basis data pada aplikasi
- Konsep *shared session* dengan menggunakan REST
- Mekanisme pembangkitan token dan expiry time pada aplikasi
- Kelebihan dan kelemahan dari arsitektur aplikasi tugas ini, dibandingkan dengan aplikasi monolitik (login, CRUD DB, dll jadi dalam satu aplikasi)
4. Pembagian tugas

### Deskripsi Tugas

Pada tugas 2, kami diminta untuk mengembangkan aplikasi toko buku online sederhana yang sudah dibuat pada tugas 1. Arsitektur aplikasi diubah agar memanfaatkan 2 buah webservice, yaitu webservice bank dan webservice buku. Baik aplikasi maupun kedua webservice, masing-masing memiliki database sendiri. Kami juga perlu mengubah beberapa hal pada aplikasi pro-book yang sudah kami buat.

### Struktur Aplikasi dan Penggunaan

#### 1. Webservice bank

Aplikasi ini menyediakan sebuah webservice bank yang menyediakan service untuk melakukan validasi kartu dan transfer. Webservice ini diimplementasikan menggunakan NodeJS dengan protokol REST. Pada webservice bank terdapat sebuah database bank yang memiliki tabel customers dan tabel transaksi. 
  
#### 2. Webservice buku

Aplikasi ini menyediakan sebuah webservice buku yang menyediakan service untuk melakukan pencarian buku, mendapatkan detail buku, menangani proses pembelian, dan memberikan rekomendasi buku sederhana. Webservice ini diimplementasikan menggunakan JAX-WS dengan protokol SOAP. Webservice ini memanfaatkan Google Books API melalui HttpURLConnection. Pada webservice ini terdapat sebuah database book yang memiliki tabel books dan tabel book_category. 
  
#### 3. Aplikasi pro-book

Pada aplikasi pro-book ini dilakukan beberapa pengembangan dari tugas sebelumnya. Berikut beberapa perubahan yang dilakukan pada aplikasi ini:

**Memanfaatkan webservice bank untuk validasi nomor kartu** - Setiap user menyimpan informasi nomor kartu yang divalidasi menggunakan webservice bank. Validasi dilakukan ketika melakukan registrasi atau mengubah informasi nomor kartu. Jika nomor kartu tidak valid, registrasi atau update profile gagal dan data tidak berubah.

**Memanfaatkan webservice book untuk mendapatkan deskripsi buku** - Data buku diambil dari webservice buku,sehingga aplikasi tidak menyimpan data buku secara lokal. Setiap kali aplikasi membutuhkan informasi buku, aplikasi akan melakukan request kepada webservice buku. Hal ini termasuk proses search dan melihat detail  buku.

**Memanfaatkan webservice book untuk melakukan pembelian buku** - Proses pembelian buku pada aplikasi ditangani oleh webservice buku. Status pembelian (berhasil/gagal dan alasannya) dilaporkan kepada user dalam bentuk notifikasi. Untuk kemudahan, tidak perlu ada proses validasi dalam melakukan transfer

**Memanfaatkan webservice book untuk menerima rekomendasi buku**- Pada halaman detail buku, terdapat rekomendasi buku yang didapatkan dari webservice buku. Asumsikan sendiri tampilan yang sesuai.

**Melakukan penggabungan search-book dan search-result dengan menggunakan AJAX** - Halaman search-book dan search-result pada tugas 1 digabung menjadi satu halaman search yang menggunakan AngularJS. Proses pencarian buku diambil dari webservice buku menggunakan **AJAX**. Hasil pencarian akan ditampilkan pada halaman search menggunakan AngularJS, setelah mendapatkan respon dari webservice. Ubah juga tampilan saat melakukan pencarian untuk memberitahu jika aplikasi sedang melakukan pencarian atau tidak ditemukan hasil.

**Menggunakan mekanisme access token** - Aplikasi Anda menggunakan `access token` untuk menentukan active user. Mekanisme pembentukan dan validasi access token adalah sebagai berikut. `Access token` berupa string random. Ketika user melakukan login yang valid, sebuah access token di-generate, disimpan dalam database server, dan diberikan kepada browser. Satu `access token` memiliki `expiry time` token (berbeda dengan expiry time cookie) dan hanya dapat digunakan pada 1 *browser/agent* dari 1 *ip address* tempat melakukan login. Sebuah access token mewakilkan tepat 1 user. Sebuah access token dianggap valid jika:
- Access token terdapat pada database server dan dipasangkan dengan seorang user.
- Access token belum expired, yaitu expiry time access token masih lebih besar dari waktu sekarang.
- Access token digunakan oleh browser yang sesuai.
- Access token digunakan dari ip address yang sesuai.

**Mengimplementasikan TOTP untuk token bank** - Mekanisme token menggunakan algoritma TOTP. Token berupa 6 digit angka. Shared secret key disimpan di database webserver beruba 32 digit huruf. Implementasi token bank ini memanfaatkan OTPLibrary.  
    
**Login menggunakan akun google** - Aplikasi memiliki pilihan untuk login menggunakan akun google, seperti yang sering ditemui pada aplikasi web atau game. Informasi yang ditampilkan untuk user yang login dengan akun google diambil dari informasi akun google tersebut.

### Penjelasan
1. Basis data pada aplikasi ini

Pada **basis data probook** terdapat tabel book_vr, orders, review, session, dan user. Pada tabel *book_vr* terdiri atas kolom id yang merupakan id book sesuai dengan GoogleBooks, kolom vote dan kolom rating. Pada tabel *orders* terdiri atas kolom id order, book_id, user_id, review_id, amount yang merepresentasikan jumlah order, dan created_at yang merepresentasikan waktu dibuatnya review. Pada tabel *session* terdiri atas kolom id session, session_id yang merepresentasikan access token, user_id, expire dengan tipe timestamp, browser, dan ip. Pada tabel *review* terdiri atas tabel id review, comment dan star. Pada tabel *user* terdiri atas tabel id user, name, username, email, password, address, phone, avatar, dan cardnumber.

Pada **basis data book** terdapat tabel books dan tabel book_category. Tabel *books* terdiri atas kolom bookid, price dan boughtqty yang merepresentasikan jumlah buku yang telah dibeli. Tabel *book_category* terdiri atas kolom bookid dan category. 

Pada **basis data bank** terdapat tabel customers dan tabel transaksi. Tabel *customers* terdiri atas kolom. Tabel *transaksi* terdiri atas kolom. 

2. Konsep shared session pada REST

3. Mekanisme pembangkitan token dan expiry time

Token dibangkitkan pada saat pengguna melakukan login dengan menggunakan browser atau ip yang belum ada di tabel session. Expiry time di set pada saat token dibangkitkan. Pada aplikasi ini terdapat fungsi inSession yang akan melakukan validasi jika waktu sudah melebih expiry time pada tabel session. 

4. Kelebihan dan kelemahan dari arsitektur aplikasi tugas ini, dibandingkan dengan aplikasi monolitik (login, CRUD DB, dll jadi dalam satu aplikasi)

### Pembagian Tugas

REST :
1. Validasi nomor kartu : 13516029
2. Transfer : 13516017
3. Menerima token : 13516152
4. Verifikasi token (bonus) : 13516152

SOAP :
1. Utilities : 13516029 
2. BookService : 13516029
3. BuyBookService : 13516017
4. RecommenderService : 13516152

Perubahan Web app :
1. Halaman search : 13516029
2. Halaman details book : 13516029
3. Mekanisme access token : 13516152
4. Halaman details book - Recommendation : 13516152
5. Halaman edit profile : 13516152
6. Halaman profile : 13516152
7. Halaman order : 13516017
8. Halaman history : 13516017
9. Halaman review : 13516017

Bonus :
1. Pembangkitan token HTOP/TOTP : 13516152
2. Validasi token : 13516152
3. Google login : 13516029

## About

Author

Shinta | Raihan | Deborah 

IF3110 Pengembangan Aplikasi Berbasis Web 
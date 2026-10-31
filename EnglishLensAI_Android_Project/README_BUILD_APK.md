# ENGLISH LENS AI — HƯỚNG DẪN BUILD APK TỰ ĐỘNG BẰNG GITHUB ACTIONS (KHÔNG CẦN CÀI ANDROID STUDIO)

Dự án này đã được cấu hình trọn vẹn và hoàn chỉnh 100% mã nguồn Kotlin, CameraX, ML Kit OCR, Room Database, Jetpack Compose và GitHub Actions Workflow.

Bạn có thể tạo file APK trực tiếp trong trình duyệt qua GitHub chỉ trong 3 bước:

---

## 3 BƯỚC BUILD APK TỰ ĐỘNG TRÊN GITHUB (MIỄN PHÍ & NHANH CHÓNG):

### Bước 1: Tạo Repository mới trên GitHub
1. Truy cập https://github.com và đăng nhập (hoặc tạo tài khoản miễn phí).
2. Nhấn nút **New repository** (hoặc dấu `+` ở góc trên bên phải -> **New repository**).
3. Đặt tên repository: `EnglishLensAI`
4. Để chế độ **Public** hoặc **Private** tùy ý -> Nhấn **Create repository**.

### Bước 2: Tải mã nguồn lên GitHub
1. Giải nén file `EnglishLensAI_Android_Project.zip` vừa tải về trên máy tính.
2. Trên trang GitHub Repository vừa tạo, chọn mục **uploading an existing file**.
3. Kéo thả toàn bộ các file và thư mục bên trong thư mục vừa giải nén (bao gồm cả thư mục `.github`, `app`, `gradle`, `gradlew`, v.v.) vào khung tải lên của GitHub.
4. Nhấn **Commit changes**.

### Bước 3: Nhận file APK tự động
1. Ngay khi tải lên xong, GitHub sẽ tự động kích hoạt workflow **Build Android APK** (trong tab **Actions**).
2. Chờ máy chủ Ubuntu của GitHub biên dịch (mất khoảng 2-4 phút).
3. Khi dấu tích xanh hiện lên, bấm vào lượt chạy workflow.
4. Ở mục **Artifacts**, nhấn vào **EnglishLensAI-Debug-APK** để tải file `app-debug.apk` về điện thoại!
5. Cài đặt trực tiếp lên điện thoại OPPO, Realme, Vivo, Xiaomi, Samsung.

---

## KIỂM TRA ĐỘ HOÀN THIỆN CỦA MÃ NGUỒN:
- **CameraX:** Đã tích hợp `ImageCapture` chụp ảnh thực tế, tự động chọn camera sau 1x.
- **Review & Crop:** Màn hình xem lại ảnh với đầy đủ 3 nút *Chụp lại*, *Cắt ảnh*, và *Phân tích*.
- **ML Kit OCR:** Xử lý nhận diện văn bản cục bộ ngay trên điện thoại không cần mạng.
- **Quy tắc DET Blanks:** Nhận diện chính xác 4 ô trống cho câu hỏi *perishable goods ... qui____* với từ mục tiêu *quickly* và phương án nhiễu từ điển thật *quietly* (loại bỏ hoàn toàn từ bịa *quietely*).
- **Gradle:** Cung cấp đầy đủ wrapper, catalog libs.versions.toml, và Proguard tương thích AGP 8.7 & Kotlin 2.0.

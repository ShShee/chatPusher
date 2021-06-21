# chatPusher
<img src=https://github.com/ShShee/chatPusher/blob/main/screenshots/src1.png width=300 height=580 /> <img src=https://github.com/ShShee/chatPusher/blob/main/screenshots/src2.png width=300 height=580 /> <img src=https://github.com/ShShee/chatPusher/blob/main/screenshots/src3.png width=300 height=580 /> <img src=https://github.com/ShShee/chatPusher/blob/main/screenshots/src4.png width=300 height=580 /> <img src=https://github.com/ShShee/chatPusher/blob/main/screenshots/database.PNG width=910/> 

## Hướng dẫn sử dụng 

- Sử dụng PUSHER riêng (Optional): 
  - Clone [Backend Node.js](https://github.com/ShShee/chatPusher_backend) về thay PUSHER KEY rồi tạo repos của bản thân up lên heroku 
  - Vào Constants.java đổi API thành link heroku
  - Thay PUSHER ID trong hàm onCreate của ChatRoomActivity.java
- Sử dụng FIREBASE riêng:
  -  Đổi tên package (cho chắc)
  -  Nhớ thêm mail support + SHA1 vào firebase project
  -  Mở auth qua gmail
  -  Đổi file google-services.json
  -  Đổi Instance Link của Database trong FirebaseHelper.java
- Đăng nhập vào play store của AVD trước rồi mới sử dụng được

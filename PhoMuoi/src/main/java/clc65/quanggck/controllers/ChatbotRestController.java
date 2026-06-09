package clc65.quanggck.controllers;

import clc65.quanggck.models.MonAn;
import clc65.quanggck.services.MonAnService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotRestController {

    private final MonAnService monAnService;

    // Inject Service quản lý món ăn vào để tương tác với Database
    public ChatbotRestController(MonAnService monAnService) {
        this.monAnService = monAnService;
    }

    @PostMapping("/ask")
    public Map<String, String> xuLyCauHoiChatbot(@RequestBody Map<String, String> payload) {
        String messageCuaKhach = payload.get("message");
        Map<String, String> response = new HashMap<>();
        
        if (messageCuaKhach == null || messageCuaKhach.trim().isEmpty()) {
            response.put("reply", "Dạ, bạn cần mình hỗ trợ thông tin gì ạ?");
            return response;
        }

        String msgLower = messageCuaKhach.toLowerCase();
        String phanHoiCuaBot = "";

        // 1. Lấy toàn bộ danh sách món ăn hiện có trong Database ra để so khớp từ khóa
        List<MonAn> dsMonAn = monAnService.getAllMonAn(); 
        // (Nếu Service của bạn đang đặt tên hàm lấy tất cả món là hàm khác, hãy đổi tên cho đúng nha)

        boolean timThayMon = false;

        for (MonAn mon : dsMonAn) {
            String tenMonLower = mon.getTenMon().toLowerCase();

            // Nếu câu hỏi của khách có chứa tên món ăn tồn tại trong DB
            if (msgLower.contains(tenMonLower)) {
                timThayMon = true;
                
                // Tình huống A: Khách hỏi về Nguyên liệu / Thành phần / Có gì
                if (msgLower.contains("nguyên liệu") || msgLower.contains("thành phần") || msgLower.contains("gồm có") || msgLower.contains("có gì")) {
                    String nguyenLieu = (mon.getNguyenLieu() != null && !mon.getNguyenLieu().isEmpty()) 
                                        ? mon.getNguyenLieu() 
                                        : "Nước dùng hầm xương ống, bánh phở tươi thớ mỏng, gia vị gia truyền Phở Mười.";
                    
                    phanHoiCuaBot = "Dạ! Món <b>" + mon.getTenMon() + "</b> bên mình được chế biến từ các nguyên liệu chính: " + nguyenLieu;
                } 
                // Tình huống B: Khách hỏi về Giá cả
                else if (msgLower.contains("giá") || msgLower.contains("bao nhiêu") || msgLower.contains("tiền")) {
                    phanHoiCuaBot = "Món <b>" + mon.getTenMon() + "</b> hiện tại có giá bán là <b>" + String.format("%,.0f", mon.getGiaTien()) + " đ</b> một bát ạ. Bạn có muốn thêm vào giỏ hàng luôn không?";
                } 
                // Tình huống C: Khách chỉ nhắc tên món chung chung
                else {
                    phanHoiCuaBot = "Món <b>" + mon.getTenMon() + "</b> hiện đang bán rất chạy tại quán với giá <b>" + String.format("%,.0f", mon.getGiaTien()) + " đ</b>. Món này đậm đà chuẩn vị Nha Trang luôn đó ạ!";
                }
                break; // Tìm thấy món rồi thì dừng vòng lặp
            }
        }

        // 2. Nếu khách không hỏi đích danh món nào trong Database, xử lý theo từ khóa hệ thống mặc định
        if (!timThayMon) {
            if (msgLower.contains("chào") || msgLower.contains("hello") || msgLower.contains("hi")) {
                phanHoiCuaBot = "Xin chào! Chúc bạn một ngày tốt lành. Mình là Trợ lý ảo kết nối trực tiếp với bếp nhà Phở Mười. Bạn muốn tìm hiểu nguyên liệu hay giá cả của món nào ạ?";
            } 
            else if (msgLower.contains("địa chỉ") || msgLower.contains("đâu") || msgLower.contains("ở đâu")) {
                phanHoiCuaBot = "Dạ! Quán Phở Mười tọa lạc tại <b>Tổ 9, thôn Vĩnh Châu, Tây Nha Trang, Khánh Hòa</b>. Rất hân hạnh được đón tiếp bạn ghé quán thưởng thức ạ! 📍";
            } 
            else if (msgLower.contains("giờ") || msgLower.contains("mở cửa") || msgLower.contains("thời gian")) {
                phanHoiCuaBot = "Dạ! Quán Phở Mười mở cửa phục vụ vào 2 khung giờ: từ <b>6h đến 10h trưa</b> và từ <b>17h đến 20h tối</b> hàng ngày ạ. Bạn lưu ý thời gian ghé quán nhé! ⏰";
            } 
            else if (msgLower.contains("thực đơn") || msgLower.contains("menu") || msgLower.contains("danh sách món")) {
                phanHoiCuaBot = "Hiện tại thực đơn hệ thống đang có các món ăn nóng hổi: ";
                for (MonAn m : dsMonAn) {
                    phanHoiCuaBot += "<b>" + m.getTenMon() + "</b>, ";
                }
                phanHoiCuaBot = phanHoiCuaBot.substring(0, phanHoiCuaBot.length() - 2) + ". Bạn muốn xem chi tiết thành phần món nào cứ tìm theo danh mục!";
            } 
            else {
                phanHoiCuaBot = "Dạ, câu hỏi này tạm thời nằm ngoài danh mục trả lời tự động của mình. Bạn có thể hỏi cụ thể tên món (Ví dụ: <i>'Nguyên liệu phở bò tái lăn'</i>) hoặc gọi hotline <b>0357 007 831</b> để bên mình hỗ trợ ngay ạ! 📞";
            }
        }

        response.put("reply", phanHoiCuaBot);
        return response;
    }
}
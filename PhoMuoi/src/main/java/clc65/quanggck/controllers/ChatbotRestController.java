package clc65.quanggck.controllers;

import clc65.quanggck.models.MonAn;
import clc65.quanggck.services.MonAnService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotRestController {

    private final MonAnService monAnService;

    public ChatbotRestController(MonAnService monAnService) {
        this.monAnService = monAnService;
    }

    @PostMapping("/ask")
    public Map<String, String> xuLyCauHoiChatbot(@RequestBody Map<String, String> payload) {
        String messageCuaKhach = payload.get("message");
        Map<String, String> response = new HashMap<>();

        if (messageCuaKhach == null || messageCuaKhach.trim().isEmpty()) {
            response.put("reply", "Dạ, bạn cần mình hỗ trợ thông tin gì ạ? 😊");
            return response;
        }

        String msgLower = messageCuaKhach.toLowerCase().trim();
        String phanHoiCuaBot;

        // Lấy toàn bộ danh sách món ăn từ Database
        List<MonAn> dsMonAn = monAnService.getAllMonAn();

        // ============================================================
        // XỬ LÝ CÁC TỪ KHÓA HỆ THỐNG TRƯỚC (chào, địa chỉ, giờ...)
        // ============================================================
        if (msgLower.contains("chào") || msgLower.contains("hello") || msgLower.contains("hi")
                || msgLower.equals("xin chào") || msgLower.contains("alo")) {
            phanHoiCuaBot = "Xin chào bạn! 👋 Mình là Trợ lý ảo của quán <b>Phở Mười</b>.<br>"
                    + "Bạn có thể hỏi mình về:<br>"
                    + "🍜 <b>Tên món ăn</b> – ví dụ: <i>\"coca\"</i>, <i>\"súp\"</i>, <i>\"phở\"</i><br>"
                    + "💰 <b>Giá món</b> – ví dụ: <i>\"giá coca\"</i>, <i>\"phở bò bao nhiêu\"</i><br>"
                    + "🧾 <b>Nguyên liệu</b> – ví dụ: <i>\"nguyên liệu phở\"</i><br>"
                    + "📋 <b>Thực đơn</b> – gõ <i>\"menu\"</i> hoặc <i>\"thực đơn\"</i>";
            response.put("reply", phanHoiCuaBot);
            return response;
        }

        if (msgLower.contains("địa chỉ") || msgLower.contains("ở đâu") || msgLower.contains("chỗ nào")) {
            phanHoiCuaBot = "📍 Quán <b>Phở Mười</b> tọa lạc tại:<br>"
                    + "<b>Tổ 9, thôn Vĩnh Châu, Tây Nha Trang, Khánh Hòa</b><br>"
                    + "Rất hân hạnh được đón tiếp bạn ghé quán thưởng thức!";
            response.put("reply", phanHoiCuaBot);
            return response;
        }

        if (msgLower.contains("giờ mở cửa") || msgLower.contains("mở cửa") || msgLower.contains("giờ bán")
                || msgLower.contains("thời gian")) {
            phanHoiCuaBot = "⏰ Quán <b>Phở Mười</b> phục vụ 2 ca:<br>"
                    + "🌅 Sáng: <b>6h00 – 10h00</b><br>"
                    + "🌆 Chiều: <b>17h00 – 20h00</b><br>"
                    + "Bạn lưu ý giờ ghé quán nhé!";
            response.put("reply", phanHoiCuaBot);
            return response;
        }

        if (msgLower.contains("thực đơn") || msgLower.contains("menu") || msgLower.contains("danh sách món")
                || msgLower.contains("có gì") || msgLower.contains("bán gì")) {
            StringBuilder sb = new StringBuilder();
            sb.append("📋 <b>Thực đơn hiện tại của quán:</b><br>");
            // Nhóm các món thành danh sách đẹp
            int stt = 1;
            for (MonAn m : dsMonAn) {
                if (Boolean.TRUE.equals(m.getTrangThai())) {
                    sb.append(stt).append(". <b>").append(m.getTenMon()).append("</b> – ")
                      .append(String.format("%,.0f", m.getGiaTien())).append(" đ<br>");
                    stt++;
                }
            }
            sb.append("<br>Bạn muốn biết thêm chi tiết món nào cứ gõ tên món nhé! 😊");
            response.put("reply", sb.toString());
            return response;
        }

        // ============================================================
        // TÌM KIẾM MÓN ĂN THEO TỪ KHÓA (logic chính)
        // Đảo ngược: tenMon.contains(keyword) → tìm tất cả món khớp
        // ============================================================

        // Tách từ khóa tìm kiếm: bỏ các từ phụ như "giá", "nguyên liệu", "bao nhiêu"...
        String keyword = msgLower
                .replace("giá", "").replace("bao nhiêu", "").replace("tiền", "")
                .replace("nguyên liệu", "").replace("thành phần", "").replace("gồm có", "")
                .replace("cho mình biết", "").replace("cho tôi biết", "")
                .replace("hỏi về", "").replace("tìm", "").replace("search", "")
                .replace("xem", "").replace("là gì", "").replace("có không", "")
                .trim();

        // Phát hiện loại câu hỏi
        boolean hoiGia = msgLower.contains("giá") || msgLower.contains("bao nhiêu") || msgLower.contains("tiền");
        boolean hoiNguyenLieu = msgLower.contains("nguyên liệu") || msgLower.contains("thành phần")
                || msgLower.contains("gồm có") || msgLower.contains("làm từ");

        // Thu thập tất cả món có tên chứa từ khóa
        List<MonAn> dsMonKhop = new ArrayList<>();
        if (!keyword.isEmpty()) {
            for (MonAn mon : dsMonAn) {
                String tenMonLower = mon.getTenMon().toLowerCase();
                // Kiểm tra tên món có chứa từ khóa (hoặc từ khóa chứa tên món)
                if (tenMonLower.contains(keyword) || keyword.contains(tenMonLower)) {
                    dsMonKhop.add(mon);
                }
            }
        }

        // ---- Trả lời theo kết quả tìm kiếm ----

        if (dsMonKhop.isEmpty()) {
            // Không tìm thấy món nào
            phanHoiCuaBot = "😅 Dạ, mình chưa tìm thấy món nào có tên <b>\"" + messageCuaKhach.trim() + "\"</b> trong thực đơn.<br>"
                    + "Bạn thử gõ lại tên khác hoặc gõ <b>\"menu\"</b> để xem toàn bộ thực đơn nhé!<br>"
                    + "Hoặc gọi hotline <b>0357 007 831</b> để được tư vấn trực tiếp 📞";

        } else if (dsMonKhop.size() == 1) {
            // Tìm thấy đúng 1 món → trả lời chi tiết
            MonAn mon = dsMonKhop.get(0);
            if (hoiNguyenLieu) {
                String nl = (mon.getNguyenLieu() != null && !mon.getNguyenLieu().isEmpty())
                        ? mon.getNguyenLieu()
                        : "Nước dùng hầm xương ống, bánh phở tươi thớ mỏng, gia vị gia truyền Phở Mười.";
                phanHoiCuaBot = "🧾 Món <b>" + mon.getTenMon() + "</b> được chế biến từ:<br><i>" + nl + "</i>";
            } else if (hoiGia) {
                phanHoiCuaBot = "💰 Món <b>" + mon.getTenMon() + "</b> hiện có giá: "
                        + "<b>" + String.format("%,.0f", mon.getGiaTien()) + " đ</b><br>"
                        + "Bạn có muốn thêm vào giỏ hàng không? 🛒";
            } else {
                phanHoiCuaBot = "🍜 Món <b>" + mon.getTenMon() + "</b> đang bán với giá "
                        + "<b>" + String.format("%,.0f", mon.getGiaTien()) + " đ</b>.<br>"
                        + "Món này đậm đà chuẩn vị Nha Trang luôn đó ạ! Bạn thử dùng xem nhé 😋";
            }

        } else {
            // Tìm thấy NHIỀU món → hiển thị danh sách tất cả
            StringBuilder sb = new StringBuilder();
            sb.append("🔍 Mình tìm thấy <b>").append(dsMonKhop.size())
              .append(" món</b> có từ khóa <b>\"").append(messageCuaKhach.trim()).append("\"</b>:<br><br>");

            for (int i = 0; i < dsMonKhop.size(); i++) {
                MonAn mon = dsMonKhop.get(i);
                sb.append("▶ <b>").append(mon.getTenMon()).append("</b>");
                sb.append(" – <span style='color:#ef4444;font-weight:600;'>")
                  .append(String.format("%,.0f", mon.getGiaTien())).append(" đ</span>");
                if (hoiNguyenLieu && mon.getNguyenLieu() != null && !mon.getNguyenLieu().isEmpty()) {
                    sb.append("<br><small style='color:#64748b;'>📝 ").append(mon.getNguyenLieu()).append("</small>");
                }
                sb.append("<br>");
            }

            sb.append("<br>Bạn muốn biết thêm về món nào thì gõ tên cụ thể hơn nhé! 😊");
            phanHoiCuaBot = sb.toString();
        }

        response.put("reply", phanHoiCuaBot);
        return response;
    }
}
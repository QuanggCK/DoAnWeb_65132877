package clc65.quanggck.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối đến thư mục gốc của dự án
        String rootPath = System.getProperty("user.dir");
        
        // Tạo đường dẫn tuyệt đối trỏ thẳng vào src/main/resources/static/images/
        String imagesLocalPath = Paths.get(rootPath, "src", "main", "resources", "static", "images")
                                      .toAbsolutePath().toString();
        
        // Ánh xạ toàn bộ đường dẫn web /images/** vào thư mục vật lý trên ổ cứng
        // Thêm dấu gạch chéo "/" ở cuối để Spring hiểu là một thư mục
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + imagesLocalPath + "/");
    }
}
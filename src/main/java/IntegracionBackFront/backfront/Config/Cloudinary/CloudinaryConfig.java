package IntegracionBackFront.backfront.Config.Cloudinary;
//las clases de confugracion se les tiene que agregar la anotacion Configuration

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    //variables para almacenar las credenciales de Cloudinary
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary(){
        //Creara un objeto de tipo Dotenv
        Dotenv dotenv = Dotenv.load();

        //Crear un mapa para guardar la clave valor del archivo .env
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", dotenv.get("CLOUDINARY_API_KEY"));
        config.put("api_secret", dotenv.get("CLOUDINARY_API_SECRET"));

        //retorna una nueva instancia de cloudinary con la configuracion cargada
        return new Cloudinary(config);
    }

}

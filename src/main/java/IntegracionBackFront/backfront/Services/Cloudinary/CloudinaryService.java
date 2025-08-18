package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    //constante que define el tamaño maximo permitido por los archivos maxima de (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    //constante para definir los tipos de archivos definidos
    private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".jpeg", ".png"};

    //cliente de Cloudinary inyectado como dependencia
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     *metodo para subir imagenes a la raiz de cloudinary
     * @param file
     * @return URL de la imagen
     * @throws IOException
     */
    public String uploadImage(MultipartFile file) throws IOException {
        //validamos el archivo
        validateImage(file);

        //sube el archivo a cloudinary con configuraciones basicas
        //tipo de recurso auto-detectado
        //calidad automatica con nivel "good"
        Map<?, ?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", "auto",
                        "quality", "auto:good"
                ));
        //retorna la URL segura de la imagen
        return (String) uploadResult.get("secure_url");
    }

    /**
     *
     * @param file
     * @param folder
     * @return
     * @throws IOException
     */
    //metodo para subir imagen a una carpeta en especifico
    public String uploadImage(MultipartFile file, String folder) throws IOException{
        validateImage(file);
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFilename = "ing_" + UUID.randomUUID() + fileExtension;

        //configuracion para subir la imagen
        Map<String, Object> options = ObjectUtils.asMap(
                "folder", folder,       //carpeta de destino
                "public_id", uniqueFilename,    //nombre unico para el archivo
                "use_filename", false,          //no usar el nombre original
                "unique_filename", false,       //no generar nombre unico (poceso hecho anteriormente
                "overwrite", false,             //no sobreescribir archivos
                "resource_type", "auto",        //auto-detectar tipo de recurso
                "quality", "auto:good"          //optimizacion fr calidad automatica
        );

        //subir el archivo
        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        //Retornamos la URL segura
        return (String) uploadResult.get("secure_url");
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public void validateImage(MultipartFile file) throws IOException {
        //verificar si el archivo esta vacio
        if(file.isEmpty()){
            throw new IllegalArgumentException(("El archivo no puede estar vacio"));
        }
        //verificar el tamaño de la imagen
        if(file.getSize() > MAX_FILE_SIZE){
            throw new IllegalArgumentException("El archivo no puede ser mayor a 5MB");
        }
        //obtener y validar el nombre original del archivo
        String OriginalFileName = file.getOriginalFilename();
        if(OriginalFileName == null){
            throw new IllegalArgumentException("Nombre del archivo invalido");
        }
        //extraer y validar la extencion
        String extencion = OriginalFileName.substring(OriginalFileName.lastIndexOf(".")).toLowerCase();
        if(!Arrays.asList(ALLOWED_EXTENSIONS).contains(extencion)){
            throw new IllegalArgumentException("Solo de permiten archivos JPG, JPEG Y PNG");
        }
        //Verifica el tipo de MIME sea una imagen
        if(!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen valida");
        }
    }

}

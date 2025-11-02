package com.neoeval.backend.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

// Interfaz para el servicio de almacenamiento de archivos
public interface FileStorageService {

    /**
     * Guarda el archivo y devuelve la ruta donde se almacenó.
     */
    String saveFile(MultipartFile file) throws IOException;

    /**
     * Elimina el archivo de la ruta de almacenamiento.
     */
    void deleteFile(String storagePath);

    // ===================================
    // ✅ Clase de Información de Descarga
    // ===================================
    /**
     * Clase interna para contener la información necesaria para la descarga.
     */
    class FileDownloadInfo {
        private final byte[] content;
        private final String contentType;
        private final String fileName;

        public FileDownloadInfo(byte[] content, String contentType, String fileName) {
            this.content = content;
            this.contentType = contentType;
            this.fileName = fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getContentType() {
            return contentType;
        }

        public String getFileName() {
            return fileName;
        }
    }

    // ===================================
    // ✅ Método para cargar archivo como bytes
    // ===================================
    /**
     * Carga el archivo como un array de bytes para ser enviado en la respuesta HTTP.
     * @param storagePath La ruta de almacenamiento del archivo.
     * @return Un objeto FileDownloadInfo con el contenido, tipo y nombre del archivo.
     * @throws IOException Si ocurre un error de E/S.
     * @throws com.neoeval.backend.exception.ResourceNotFoundException Si el archivo no existe.
     */
    FileDownloadInfo loadFileAsBytes(String storagePath) throws IOException;
}
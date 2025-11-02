package com.neoeval.backend.service.impl;

import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.service.FileStorageService;
import com.neoeval.backend.service.FileStorageService.FileDownloadInfo; // Importar la clase interna FileDownloadInfo
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    // NOTA IMPORTANTE: Esta implementación es una SIMULACIÓN.

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("El archivo subido está vacío.");
        }

        // --- Lógica de Simulación Simplificada ---
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.lastIndexOf('.') > 0) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        }
        String baseName = UUID.randomUUID().toString();

        // La ruta simulada, incluyendo el nombre de archivo para la descarga
        String storagePath = "/uploads/" + baseName + "/" + (originalFileName != null ? originalFileName : "default" + extension);

        // En un caso real, aquí iría file.transferTo(new File(storagePath))

        System.out.println("Archivo SIMULADAMENTE guardado en: " + storagePath);

        return storagePath;
    }

    @Override
    public void deleteFile(String storagePath) {
        if (storagePath != null && !storagePath.isEmpty()) {
            System.out.println("Archivo SIMULADAMENTE eliminado: " + storagePath);
        }
    }

    @Override
    public FileDownloadInfo loadFileAsBytes(String storagePath) throws IOException {

        if (storagePath == null || storagePath.isEmpty() || !storagePath.startsWith("/uploads")) {
            throw new ResourceNotFoundException("File", "path", storagePath, "Ruta de archivo no válida o simulada como no existente.");
        }

        String fileName;
        String contentType;

        int lastSlashIndex = storagePath.lastIndexOf('/');
        if (lastSlashIndex != -1 && lastSlashIndex < storagePath.length() - 1) {
            fileName = storagePath.substring(lastSlashIndex + 1);
        } else {
            fileName = "downloaded_file";
        }

        if (fileName.toLowerCase().endsWith(".pdf")) {
            contentType = MediaType.APPLICATION_PDF_VALUE;
        } else if (fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else if (fileName.toLowerCase().endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (fileName.toLowerCase().endsWith(".mp4")) {
            contentType = "video/mp4";
        } else {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // 🎯 CORRECCIÓN: Usar bytes vacíos para simular contenido binario y evitar error visual
        byte[] contentBytes;

        if (contentType.equals(MediaType.APPLICATION_PDF_VALUE) ||
                contentType.startsWith("image/") ||
                contentType.startsWith("video/")) {

            // Para archivos binarios (PDF, imágenes, videos), enviar bytes nulos.
            contentBytes = new byte[1024]; // 1KB de datos binarios nulos
        } else {
            // Para otros tipos o genéricos, enviar una cadena simple (opcional)
            String simulatedContent = "Simulación OK: " + fileName;
            contentBytes = simulatedContent.getBytes(StandardCharsets.UTF_8);
        }

        System.out.println("Archivo SIMULADAMENTE cargado: " + fileName + " con tipo: " + contentType);

        return new FileDownloadInfo(contentBytes, contentType, fileName);
    }
}
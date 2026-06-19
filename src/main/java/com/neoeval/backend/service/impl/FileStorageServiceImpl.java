package com.neoeval.backend.service.impl;

import com.neoeval.backend.entity.StoredFile;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.repository.StoredFileRepository;
import com.neoeval.backend.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final StoredFileRepository storedFileRepository;

    public FileStorageServiceImpl(StoredFileRepository storedFileRepository) {
        this.storedFileRepository = storedFileRepository;
    }

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("El archivo subido está vacío.");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null) originalFileName = "unnamed_file";
        
        String extension = "";
        if (originalFileName.lastIndexOf('.') > 0) {
            extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        }

        String contentType = file.getContentType();
        if (contentType == null) contentType = "application/octet-stream";

        StoredFile storedFile = StoredFile.builder()
                .fileName(originalFileName)
                .contentType(contentType)
                .data(file.getBytes())
                .build();

        storedFile = storedFileRepository.save(storedFile);

        System.out.println("Archivo REAL guardado en la BD con ID: " + storedFile.getId());

        // Retornamos el ID como storagePath para que sea compatible con el resto del sistema
        return storedFile.getId();
    }

    @Override
    public void deleteFile(String storagePath) {
        if (storagePath != null && !storagePath.isEmpty()) {
            try {
                storedFileRepository.deleteById(storagePath);
                System.out.println("Archivo REAL eliminado de la BD con ID: " + storagePath);
            } catch (Exception e) {
                System.err.println("No se pudo eliminar el archivo de la BD: " + e.getMessage());
            }
        }
    }

    @Override
    public FileDownloadInfo loadFileAsBytes(String storagePath) throws IOException {

        if (storagePath == null || storagePath.isEmpty()) {
            throw new ResourceNotFoundException("File", "path", "null", "Ruta de archivo no proporcionada.");
        }

        StoredFile storedFile = storedFileRepository.findById(storagePath)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", storagePath, "El archivo no existe en la base de datos."));

        String contentType = storedFile.getContentType();
        String fileName = storedFile.getFileName();
        byte[] contentBytes = storedFile.getData();

        System.out.println("Archivo REAL cargado de BD: " + fileName + " con tipo: " + contentType + " (" + contentBytes.length + " bytes)");

        return new FileDownloadInfo(contentBytes, contentType, fileName);
    }
}
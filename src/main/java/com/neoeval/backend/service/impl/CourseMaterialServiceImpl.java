package com.neoeval.backend.service.impl;

import com.neoeval.backend.dto.request.ClassSessionRequest;
import com.neoeval.backend.dto.request.CourseModuleRequest;
import com.neoeval.backend.dto.request.MaterialResourceRequest;
import com.neoeval.backend.dto.response.ClassSessionResponse;
import com.neoeval.backend.dto.response.CourseModuleResponse;
import com.neoeval.backend.dto.response.MaterialResourceResponse;
import com.neoeval.backend.entity.ClassGroup;
import com.neoeval.backend.entity.ClassSession;
import com.neoeval.backend.entity.CourseModule;
import com.neoeval.backend.entity.MaterialResource;
import com.neoeval.backend.entity.Subject;
import com.neoeval.backend.entity.Teacher;
import com.neoeval.backend.exception.ResourceNotFoundException;
import com.neoeval.backend.exception.AccessDeniedException;
import com.neoeval.backend.repository.ClassSessionRepository;
import com.neoeval.backend.repository.ClassGroupRepository;
import com.neoeval.backend.repository.CourseModuleRepository;
import com.neoeval.backend.repository.MaterialResourceRepository;
import com.neoeval.backend.repository.SubjectRepository;
import com.neoeval.backend.repository.TeacherRepository;
import com.neoeval.backend.service.CourseMaterialService;
import com.neoeval.backend.service.FileStorageService;
import com.neoeval.backend.service.FileStorageService.FileDownloadInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseMaterialServiceImpl implements CourseMaterialService {

    private final CourseModuleRepository moduleRepository;
    private final ClassSessionRepository sessionRepository;
    private final MaterialResourceRepository resourceRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final ClassGroupRepository classGroupRepository; // ✅ INYECCIÓN DE ClassGroupRepository
    private final FileStorageService fileStorageService;

    public CourseMaterialServiceImpl(CourseModuleRepository moduleRepository,
                                     ClassSessionRepository sessionRepository,
                                     MaterialResourceRepository resourceRepository,
                                     SubjectRepository subjectRepository,
                                     TeacherRepository teacherRepository,
                                     ClassGroupRepository classGroupRepository, // ✅ NUEVO PARÁMETRO
                                     FileStorageService fileStorageService) {
        this.moduleRepository = moduleRepository;
        this.sessionRepository = sessionRepository;
        this.resourceRepository = resourceRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.classGroupRepository = classGroupRepository; // ✅ ASIGNACIÓN
        this.fileStorageService = fileStorageService;
    }

    // ===================================
    // MÉTODOS DE MAPEO (Manual Mappers)
    // ===================================
    private MaterialResourceResponse mapResourceToResponse(MaterialResource resource) {
        MaterialResourceResponse response = new MaterialResourceResponse();
        response.setId(resource.getId());
        response.setResourceName(resource.getResourceName());
        response.setResourceType(resource.getResourceType());
        response.setStoragePath(resource.getStoragePath());
        response.setFileSizeKB(resource.getFileSizeKB());
        response.setUploadedAt(resource.getUploadedAt());
        return response;
    }

    private ClassSessionResponse mapSessionToResponse(ClassSession session) {
        ClassSessionResponse response = new ClassSessionResponse();
        response.setId(session.getId());
        response.setTitle(session.getTitle());
        response.setOrderIndex(session.getOrderIndex());
        response.setLearningObjective(session.getLearningObjective());
        response.setCreatedAt(session.getCreatedAt());

        List<MaterialResourceResponse> resources = session.getResources().stream()
                .map(this::mapResourceToResponse)
                .collect(Collectors.toList());
        response.setResources(resources);

        return response;
    }

    private CourseModuleResponse mapToResponse(CourseModule module) {
        CourseModuleResponse response = new CourseModuleResponse();
        response.setId(module.getId());
        response.setTitle(module.getTitle());
        response.setDescription(module.getDescription());
        response.setCreatedAt(module.getCreatedAt());

        response.setSubjectId(module.getSubject().getId());
        response.setSubjectName(module.getSubject().getName());
        response.setTeacherId(module.getTeacher().getId());
        response.setTeacherName(module.getTeacher().getName());

        // ✅ NUEVO MAPEO: Incluir la información del ClassGroup
        if (module.getClassGroup() != null) {
            response.setClassGroupId(module.getClassGroup().getId());
            response.setClassGroupName(module.getClassGroup().getName());
        }

        return response;
    }

    // ===================================
    // VALIDACIONES
    // ===================================
    private void checkModuleTeacher(CourseModule module, Long currentTeacherId) {
        if (!module.getTeacher().getId().equals(currentTeacherId)) {
            throw new AccessDeniedException("Acceso Denegado. El módulo no pertenece al profesor actual.");
        }
    }

    private void checkSessionTeacher(ClassSession session, Long currentTeacherId) {
        if (!session.getCourseModule().getTeacher().getId().equals(currentTeacherId)) {
            throw new AccessDeniedException("Acceso Denegado. La sesión no pertenece al profesor actual.");
        }
    }

    // ===================================
    // IMPLEMENTACIONES DE CourseModule
    // ===================================
    @Override
    @Transactional
    public CourseModuleResponse createModule(CourseModuleRequest request, Long currentTeacherId) {

        // 1. Obtener Subject
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));

        // 2. Obtener Teacher
        Teacher teacher = teacherRepository.findById(currentTeacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", currentTeacherId));

        // 3. ✅ OBTENER CLASSGROUP (Nuevo: Obligatorio según el DTO)
        ClassGroup classGroup = classGroupRepository.findById(request.getClassGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("ClassGroup", "id", request.getClassGroupId()));


        // 4. Crear Módulo y asignar las entidades
        CourseModule module = new CourseModule();
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setSubject(subject);
        module.setTeacher(teacher);
        module.setClassGroup(classGroup); // ✅ ASIGNACIÓN CLAVE AL GRUPO

        CourseModule savedModule = moduleRepository.save(module);

        // Forzar inicialización de proxies antes de mapear (buena práctica)
        savedModule.getSubject().getName();
        savedModule.getTeacher().getName();

        return mapToResponse(savedModule);
    }

    @Override
    @Transactional
    public CourseModuleResponse updateModule(Long moduleId, CourseModuleRequest request, Long currentTeacherId) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseModule", "id", moduleId));

        checkModuleTeacher(module, currentTeacherId);

        // Actualizar Subject si cambia
        if (!module.getSubject().getId().equals(request.getSubjectId())) {
            Subject newSubject = subjectRepository.findById(request.getSubjectId())
                    .orElseThrow(() -> new ResourceNotFoundException("Subject", "id", request.getSubjectId()));
            module.setSubject(newSubject);
        }

        // ✅ Actualizar ClassGroup si cambia (asumiendo que request.getClassGroupId() no es nulo)
        if (module.getClassGroup() == null || !module.getClassGroup().getId().equals(request.getClassGroupId())) {
            ClassGroup newGroup = classGroupRepository.findById(request.getClassGroupId())
                    .orElseThrow(() -> new ResourceNotFoundException("ClassGroup", "id", request.getClassGroupId()));
            module.setClassGroup(newGroup);
        }

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());

        return mapToResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseModuleResponse getModuleWithDetails(Long moduleId) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseModule", "id", moduleId));

        CourseModuleResponse response = mapToResponse(module);

        List<ClassSessionResponse> sessions = module.getSessions().stream()
                .map(this::mapSessionToResponse)
                .collect(Collectors.toList());

        response.setSessions(sessions);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseModuleResponse> getModulesByTeacher(Long teacherId) {
        return moduleRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteModule(Long moduleId, Long currentTeacherId) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseModule", "id", moduleId));

        checkModuleTeacher(module, currentTeacherId);

        moduleRepository.delete(module);
    }

    // ===================================
    // IMPLEMENTACIONES DE ClassSession
    // ===================================
    @Override
    @Transactional
    public ClassSessionResponse createSession(Long moduleId, ClassSessionRequest request, Long currentTeacherId) {
        CourseModule module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("CourseModule", "id", moduleId));

        checkModuleTeacher(module, currentTeacherId);

        Integer orderIndex = Optional.ofNullable(request.getOrderIndex())
                .orElseGet(() -> sessionRepository.findMaxOrderIndexByCourseModuleId(moduleId)
                        .map(maxIndex -> maxIndex + 1)
                        .orElse(1));

        ClassSession session = new ClassSession();
        session.setTitle(request.getTitle());
        session.setLearningObjective(request.getLearningObjective());
        session.setOrderIndex(orderIndex);
        session.setCourseModule(module);

        return mapSessionToResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional
    public ClassSessionResponse updateSession(Long moduleId, Long sessionId, ClassSessionRequest request, Long currentTeacherId) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", sessionId));

        if (!session.getCourseModule().getId().equals(moduleId)) {
            throw new ResourceNotFoundException("ClassSession", "id", sessionId, "No existe en el módulo " + moduleId);
        }
        checkSessionTeacher(session, currentTeacherId);

        session.setTitle(request.getTitle());
        session.setLearningObjective(request.getLearningObjective());
        session.setOrderIndex(request.getOrderIndex());

        return mapSessionToResponse(sessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public ClassSessionResponse getSession(Long sessionId) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", sessionId));

        return mapSessionToResponse(session);
    }

    @Override
    @Transactional
    public void deleteSession(Long moduleId, Long sessionId, Long currentTeacherId) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", sessionId));

        if (!session.getCourseModule().getId().equals(moduleId)) {
            throw new ResourceNotFoundException("ClassSession", "id", sessionId, "No existe en el módulo " + moduleId);
        }
        checkSessionTeacher(session, currentTeacherId);

        sessionRepository.delete(session);
    }

    // ===================================
    // IMPLEMENTACIONES DE MaterialResource
    // ===================================
    @Override
    @Transactional
    public MaterialResourceResponse createResource(Long sessionId, MaterialResourceRequest request, Long currentTeacherId, MultipartFile file) {
        ClassSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("ClassSession", "id", sessionId));

        checkSessionTeacher(session, currentTeacherId);

        String finalStoragePath;
        Integer finalFileSizeKB = null;

        if (file != null && !file.isEmpty()) {
            try {
                finalStoragePath = fileStorageService.saveFile(file);

                Long sizeInBytes = file.getSize();
                Long sizeInKB = sizeInBytes / 1024;

                if (sizeInKB > Integer.MAX_VALUE) {
                    throw new IllegalArgumentException("El tamaño del archivo (" + sizeInKB + " KB) excede el límite de almacenamiento permitido.");
                }
                finalFileSizeKB = sizeInKB.intValue();

                if (request.getResourceName() == null || request.getResourceName().isEmpty()) {
                    request.setResourceName(file.getOriginalFilename());
                }

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo: " + e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Error de archivo: " + e.getMessage(), e);
            }
        }
        else {
            finalStoragePath = request.getStoragePath();

            if (request.getFileSizeKB() != null) {
                if (request.getFileSizeKB() > Integer.MAX_VALUE) {
                    throw new RuntimeException("El tamaño de recurso especificado excede el límite de almacenamiento permitido.");
                }
                finalFileSizeKB = request.getFileSizeKB().intValue();
            }
        }

        MaterialResource resource = new MaterialResource();
        resource.setResourceName(request.getResourceName());
        resource.setResourceType(request.getResourceType());
        resource.setStoragePath(finalStoragePath);
        resource.setFileSizeKB(finalFileSizeKB);
        resource.setClassSession(session);

        return mapResourceToResponse(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public MaterialResourceResponse updateResource(Long sessionId, Long resourceId, MaterialResourceRequest request, Long currentTeacherId) {
        MaterialResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialResource", "id", resourceId));

        if (!resource.getClassSession().getId().equals(sessionId)) {
            throw new ResourceNotFoundException("MaterialResource", "id", resourceId, "No existe en la sesión " + sessionId);
        }
        checkSessionTeacher(resource.getClassSession(), currentTeacherId);

        resource.setResourceName(request.getResourceName());
        resource.setResourceType(request.getResourceType());
        resource.setStoragePath(request.getStoragePath());

        if (request.getFileSizeKB() != null) {
            if (request.getFileSizeKB() > Integer.MAX_VALUE) {
                throw new RuntimeException("El tamaño de recurso especificado excede el límite de almacenamiento permitido.");
            }
            resource.setFileSizeKB(request.getFileSizeKB().intValue());
        } else {
            resource.setFileSizeKB(null);
        }

        return mapResourceToResponse(resourceRepository.save(resource));
    }

    // ===================================
    // IMPLEMENTACIONES DE DESCARGA
    // ===================================

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadFile(String storagePath) {
        try {
            FileDownloadInfo info = fileStorageService.loadFileAsBytes(storagePath);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + info.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(info.getContentType()))
                    .body(info.getContent());

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("File", "path", storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + storagePath, e);
        }
    }

    // ===================================
    // IMPLEMENTACIÓN PARA ESTUDIANTES (CORREGIDO)
    // ===================================
    @Override
    @Transactional(readOnly = true)
    public List<CourseModuleResponse> getModulesForStudent(Long studentId) {
        return moduleRepository.findModulesByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===================================
    // IMPLEMENTACIONES DE ELIMINACIÓN
    // ===================================
    @Override
    @Transactional
    public void deleteResource(Long sessionId, Long resourceId, Long currentTeacherId) {
        MaterialResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialResource", "id", resourceId));

        if (!resource.getClassSession().getId().equals(sessionId)) {
            throw new ResourceNotFoundException("MaterialResource", "id", resourceId, "No existe en la sesión " + sessionId);
        }
        checkSessionTeacher(resource.getClassSession(), currentTeacherId);

        if (resource.getStoragePath() != null) {
            fileStorageService.deleteFile(resource.getStoragePath());
        }

        resourceRepository.delete(resource);
    }
}
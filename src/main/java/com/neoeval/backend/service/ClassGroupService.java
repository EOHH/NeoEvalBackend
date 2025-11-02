package com.neoeval.backend.service;

import com.neoeval.backend.dto.request.CreateGroupRequest;
import com.neoeval.backend.dto.response.ClassGroupResponse;

import java.util.List;

public interface ClassGroupService {
    ClassGroupResponse createGroup(CreateGroupRequest groupRequest);
    ClassGroupResponse getGroupById(Long id);
    List<ClassGroupResponse> getAllGroups();
    List<ClassGroupResponse> getGroupsByTeacherId(Long teacherId);

    // ✅ NUEVO: Método para agregar una lista de estudiantes
    ClassGroupResponse addStudentsToGroup(Long groupId, List<Long> studentIds);

    ClassGroupResponse addStudentToGroup(Long groupId, Long studentId);
    ClassGroupResponse removeStudentFromGroup(Long groupId, Long studentId);
    ClassGroupResponse updateGroup(Long id, CreateGroupRequest groupRequest);
    void deleteGroup(Long id);
}
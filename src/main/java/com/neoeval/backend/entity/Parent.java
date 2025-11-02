package com.neoeval.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "user_id") // Correcto para herencia JOINED
public class Parent extends User {

    @Column(length = 50)
    private String relationship;

    // Relación OneToOne con Student: Un padre tiene UN estudiante.
    // Este es el lado "dueño" de la relación, por lo que tiene @JoinColumn.
    // 'unique = true' es crucial para OneToOne si no se maneja en la base de datos de otra forma.
    // 'nullable = false' si un padre DEBE tener un estudiante (depende de tu lógica de negocio).
    // Si 'student_id' en la tabla 'parents' es la FK a la tabla 'students' (que a su vez usa 'user_id' como PK),
    // entonces 'referencedColumnName' debería apuntar al ID de la tabla 'students', que es 'user_id'.
    // Esto es correcto si 'Student' también extiende 'User' y su PK es 'user_id'.
    @OneToOne(fetch = FetchType.LAZY) // Lazy loading es una buena práctica
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", unique = true)
    private Student student;

    // Constructors
    public Parent() {
        super();
        setUserType("PARENT");
    }

    public Parent(String name, String email, String password) {
        super(name, email, password, "PARENT");
    }

    // Getters and Setters
    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Student getStudent() {
        return student;
    }

    // Ajuste en setStudent para manejar la bidireccionalidad de forma segura
    public void setStudent(Student student) {
        // Establece la relación en este objeto Parent
        this.student = student;

        // Asegura la bidireccionalidad en el objeto Student
        // Esto previene un bucle infinito si Student.setParent() también llamara a Parent.setStudent()
        // La condición 'student.getParent() != this' es importante.
        // Usamos Object.equals() para comparar si son el mismo objeto, o simplemente chequeamos si es nulo.
        if (student != null && (student.getParent() == null || !student.getParent().equals(this))) {
            student.setParent(this);
        }
    }
}
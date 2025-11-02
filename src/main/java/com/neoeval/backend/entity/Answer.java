package com.neoeval.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Renombrado de 'content' a 'text' para mayor claridad.
    // Almacena el texto de la opción (A, B, C...) o la respuesta modelo (para V/F o Abiertas).
    @Column(name = "answer_text", columnDefinition = "TEXT")
    private String text;

    // Indica si esta es la respuesta correcta para la pregunta.
    @Column(name = "is_correct")
    private Boolean isCorrect = false;

    // Puntos que otorga esta respuesta (útil para ponderación o preguntas dinámicas)
    @Column(name = "points_value")
    private Integer pointsValue;

    // Retroalimentación específica para esta opción de respuesta.
    @Column(columnDefinition = "TEXT")
    private String feedback;

    // Relación Muchos a Uno con Question
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // -------------------------------------------------------------------------
    // Constructors

    public Answer() {
    }

    /**
     * Constructor principal para la creación de respuestas.
     */
    public Answer(Question question, String text, Boolean isCorrect, Integer pointsValue) {
        this.question = question;
        this.text = text;
        this.isCorrect = isCorrect;
        this.pointsValue = pointsValue;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Renombré getContent() a getText()
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * CRÍTICO: Método requerido por la convención de Spring/JPA para evaluar el campo booleano
     * y por tu lógica de calificación en StudentResultServiceImpl.
     */
    public boolean isCorrect() {
        return isCorrect != null && isCorrect;
    }

    // Mantengo el getter original de tu código para obtener el objeto Boolean
    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public Integer getPointsValue() {
        return pointsValue;
    }

    public void setPointsValue(Integer pointsValue) {
        this.pointsValue = pointsValue;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
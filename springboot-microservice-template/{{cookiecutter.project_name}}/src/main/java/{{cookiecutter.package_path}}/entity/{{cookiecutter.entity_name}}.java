package {{ cookiecutter.package_name }}.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
{% if cookiecutter.use_lombok == 'true' -%}
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
{% endif -%}
{% if cookiecutter.use_flyway == 'true' -%}
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
{% endif %}

import java.time.LocalDateTime;
{% if cookiecutter.use_lombok == 'false' -%}
import java.util.Objects;
{% endif %}

@Entity
@Table(name = "{{ cookiecutter.entity_name_plural_lower }}")
{% if cookiecutter.use_lombok == 'true' -%}
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
{% endif -%}
{% if cookiecutter.use_flyway == 'true' -%}
@EntityListeners(AuditingEntityListener.class)
{% endif -%}
public class {{ cookiecutter.entity_name }} {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    
    {% if cookiecutter.use_lombok == 'true' -%}
    {% endif -%}
    @Column(nullable = false)
    private Boolean active{% if cookiecutter.use_lombok == 'false' %} = true{% endif %};
    
    {% if cookiecutter.use_flyway == 'true' -%}
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    {% endif -%}
    
    {% if cookiecutter.use_lombok == 'false' -%}
    // Constructors
    public {{ cookiecutter.entity_name }}() {
        this.active = true;
    }
    
    public {{ cookiecutter.entity_name }}(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
    
    {% if cookiecutter.use_flyway == 'true' -%}
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    {% endif -%}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        {{ cookiecutter.entity_name }} that = ({{ cookiecutter.entity_name }}) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "{{ cookiecutter.entity_name }}{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                {% if cookiecutter.use_flyway == 'true' -%}
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                {% endif -%}
                '}';
    }
    {% endif -%}
}
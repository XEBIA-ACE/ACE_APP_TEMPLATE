// BaseDto.java
package {{ cookiecutter.package_name }}.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@SuperBuilder
public abstract class BaseDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer version;
}
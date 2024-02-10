package com.bricool.security.service_pack;



import com.bricool.security.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue
    private Integer id;

    private String serviceName;
    private String description;
    private LocalDate date;
    private BigDecimal price;

    @Getter
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    @JsonIgnore
    private User serviceProvider;

    @Getter
    @Transient // Ignore this field for database persistence
    private String imageBase64;

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}


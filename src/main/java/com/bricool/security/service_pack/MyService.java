package com.bricool.security.service_pack;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service")
public class MyService {

    @Id
    @GeneratedValue
    private Integer id;

    private String serviceName;
    private String description;
    private LocalDate date;
    private BigDecimal price;
    private String category;
    private String location;
    @Column(name = "service_provider_id")
    private Integer serviceProvider;
    @Getter
    private String imagePath;

//    @ManyToOne
//    @JoinColumn(name = "service_provider_id")
//    @JsonIgnore
//    public User serviceProvider;


    @Getter
    @Transient // Ignore this field for database persistence
    private String imageBase64;

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }
}


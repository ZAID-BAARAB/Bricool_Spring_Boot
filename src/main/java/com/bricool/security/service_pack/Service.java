package com.bricool.security.service_pack;



import com.bricool.security.user.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "service_provider_id")
    private User serviceProvider;
}


package com.kurly.kurlyview.domain.product;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
public class Rate {
    private LocalDate date;
    private double rate;
}

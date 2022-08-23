package com.kurly.kurlyview.domain.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Data
public class Rate {
    private LocalDateTime date;
    private double rate;
}

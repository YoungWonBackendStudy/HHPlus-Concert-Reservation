package io.hhplus.concert.domain.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Concert {
    Long id;
    String name;
    String description;
}

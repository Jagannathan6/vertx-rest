package com.vertx.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private String id;

    private String name;

    private String city;

}

package com.microcompany.accountsservice.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "No puede quedar en blanco")
    @NotNull
    private String name;

    @NotBlank(message = "No puede quedar en blanco")
    @NotNull
    @Email
    private String email;

    public void isValid() throws Exception {
        if (name == null || email == null)
            throw new Exception("Cuenta no válida");
    }
}

package com.mzalcmanis.transfer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class Client {

    private UUID id;

    private String firstName;

    private String lastName;


}

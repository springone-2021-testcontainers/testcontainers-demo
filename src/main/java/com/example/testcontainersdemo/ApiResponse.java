package com.example.testcontainersdemo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private String parameter;

    private String status;

    private String operation;

}

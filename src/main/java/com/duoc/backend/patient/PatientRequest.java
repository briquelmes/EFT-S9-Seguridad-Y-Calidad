package com.duoc.backend.patient;

public record PatientRequest(String name, String species, String breed, int age, String owner) {
}

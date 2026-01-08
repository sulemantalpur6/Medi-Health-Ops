package com.mh.auth.service.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PermissionEnum {

    PATIENT_READ("PATIENT_READ", "View patient information", "PATIENT", "READ"),
    PATIENT_WRITE("PATIENT_WRITE", "Create and update patient records", "PATIENT", "WRITE"),
    PATIENT_DELETE("PATIENT_DELETE", "Delete patient records", "PATIENT", "DELETE"),
    PATIENT_MANAGE("PATIENT_MANAGE", "Full patient management access", "PATIENT", "MANAGE"),

    DOCTOR_READ("DOCTOR_READ", "View doctor information", "DOCTOR", "READ"),
    DOCTOR_WRITE("DOCTOR_WRITE", "Create and update doctor profiles", "DOCTOR", "WRITE"),
    DOCTOR_SCHEDULE_READ("DOCTOR_SCHEDULE_READ", "View doctor schedules", "DOCTOR", "READ_SCHEDULE"),
    DOCTOR_SCHEDULE_WRITE("DOCTOR_SCHEDULE_WRITE", "Manage doctor schedules", "DOCTOR", "WRITE_SCHEDULE"),

    APPOINTMENT_READ("APPOINTMENT_READ", "View appointments", "APPOINTMENT", "READ"),
    APPOINTMENT_WRITE("APPOINTMENT_WRITE", "Create and update appointments", "APPOINTMENT", "WRITE"),
    APPOINTMENT_DELETE("APPOINTMENT_DELETE", "Cancel appointments", "APPOINTMENT", "DELETE"),

    PRESCRIPTION_READ("PRESCRIPTION_READ", "View prescriptions", "PRESCRIPTION", "READ"),
    PRESCRIPTION_WRITE("PRESCRIPTION_WRITE", "Create and update prescriptions", "PRESCRIPTION", "WRITE"),
    PRESCRIPTION_APPROVE("PRESCRIPTION_APPROVE", "Approve prescriptions", "PRESCRIPTION", "APPROVE"),

    LAB_TEST_READ("LAB_TEST_READ", "View lab test results", "LAB_TEST", "READ"),
    LAB_TEST_WRITE("LAB_TEST_WRITE", "Create and update lab tests", "LAB_TEST", "WRITE"),
    LAB_TEST_ORDER("LAB_TEST_ORDER", "Order lab tests", "LAB_TEST", "ORDER"),
    LAB_TEST_APPROVE("LAB_TEST_APPROVE", "Approve lab test results", "LAB_TEST", "APPROVE"),

    PHARMACY_READ("PHARMACY_READ", "View pharmacy inventory", "PHARMACY", "READ"),
    PHARMACY_WRITE("PHARMACY_WRITE", "Manage pharmacy inventory", "PHARMACY", "WRITE"),
    PHARMACY_DISPENSE("PHARMACY_DISPENSE", "Dispense medications", "PHARMACY", "DISPENSE"),

    BILLING_READ("BILLING_READ", "View billing information", "BILLING", "READ"),
    BILLING_WRITE("BILLING_WRITE", "Create and update bills", "BILLING", "WRITE"),
    BILLING_APPROVE("BILLING_APPROVE", "Approve invoices", "BILLING", "APPROVE"),

    MEDICAL_RECORD_READ("MEDICAL_RECORD_READ", "View medical records", "MEDICAL_RECORD", "READ"),
    MEDICAL_RECORD_WRITE("MEDICAL_RECORD_WRITE", "Create and update medical records", "MEDICAL_RECORD", "WRITE"),
    MEDICAL_RECORD_EXPORT("MEDICAL_RECORD_EXPORT", "Export medical records", "MEDICAL_RECORD", "EXPORT"),

    REPORT_VIEW("REPORT_VIEW", "View reports", "REPORT", "VIEW"),
    REPORT_GENERATE("REPORT_GENERATE", "Generate reports", "REPORT", "GENERATE"),

    USER_READ("USER_READ", "View user information", "USER", "READ"),
    USER_WRITE("USER_WRITE", "Create and update users", "USER", "WRITE"),
    USER_DELETE("USER_DELETE", "Delete users", "USER", "DELETE"),
    ROLE_READ("ROLE_READ", "View roles and permissions", "ROLE", "READ"),
    ROLE_WRITE("ROLE_WRITE", "Create and update roles", "ROLE", "WRITE"),
    ROLE_MANAGE("ROLE_MANAGE", "Full role management", "ROLE", "MANAGE");

    private final String name;
    private final String description;
    private final String resource;
    private final String action;
}
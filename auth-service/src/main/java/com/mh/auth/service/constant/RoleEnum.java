package com.mh.auth.service.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import static com.mh.auth.service.constant.PermissionEnum.*;


@Getter
@RequiredArgsConstructor
public enum RoleEnum {

    ROLE_SUPER_ADMIN(
            "ROLE_SUPER_ADMIN",
            "Super Administrator with complete system access",
            Set.of(
                    USER_READ, USER_WRITE, USER_DELETE,
                    ROLE_READ, ROLE_WRITE, ROLE_MANAGE,
                    PATIENT_READ, PATIENT_WRITE, PATIENT_DELETE, PATIENT_MANAGE,
                    DOCTOR_READ, DOCTOR_WRITE, DOCTOR_SCHEDULE_READ, DOCTOR_SCHEDULE_WRITE,
                    APPOINTMENT_READ, APPOINTMENT_WRITE, APPOINTMENT_DELETE,
                    PRESCRIPTION_READ, PRESCRIPTION_WRITE, PRESCRIPTION_APPROVE,
                    LAB_TEST_READ, LAB_TEST_WRITE, LAB_TEST_ORDER, LAB_TEST_APPROVE,
                    PHARMACY_READ, PHARMACY_WRITE, PHARMACY_DISPENSE,
                    BILLING_READ, BILLING_WRITE, BILLING_APPROVE,
                    MEDICAL_RECORD_READ, MEDICAL_RECORD_WRITE, MEDICAL_RECORD_EXPORT,
                    REPORT_VIEW, REPORT_GENERATE
            )
    ),

    ROLE_HOSPITAL_ADMIN(
            "ROLE_HOSPITAL_ADMIN",
            "Hospital Administrator managing operations",
            Set.of(
                    USER_READ, USER_WRITE,
                    PATIENT_READ, PATIENT_WRITE, PATIENT_MANAGE,
                    DOCTOR_READ, DOCTOR_SCHEDULE_READ,
                    APPOINTMENT_READ, APPOINTMENT_WRITE,
                    BILLING_READ, BILLING_WRITE, BILLING_APPROVE,
                    REPORT_VIEW, REPORT_GENERATE
            )
    ),

    ROLE_DOCTOR(
            "ROLE_DOCTOR",
            "Medical Doctor with clinical privileges",
            Set.of(
                    PATIENT_READ, PATIENT_WRITE,
                    APPOINTMENT_READ, APPOINTMENT_WRITE,
                    PRESCRIPTION_READ, PRESCRIPTION_WRITE, PRESCRIPTION_APPROVE,
                    LAB_TEST_READ, LAB_TEST_ORDER,
                    MEDICAL_RECORD_READ, MEDICAL_RECORD_WRITE,
                    DOCTOR_SCHEDULE_READ, DOCTOR_SCHEDULE_WRITE,
                    REPORT_VIEW
            )
    ),

    ROLE_NURSE(
            "ROLE_NURSE",
            "Registered Nurse with patient care access",
            Set.of(
                    PATIENT_READ, PATIENT_WRITE,
                    APPOINTMENT_READ,
                    PRESCRIPTION_READ,
                    LAB_TEST_READ,
                    MEDICAL_RECORD_READ
            )
    ),

    ROLE_LAB_TECHNICIAN(
            "ROLE_LAB_TECHNICIAN",
            "Laboratory Technician",
            Set.of(
                    PATIENT_READ,
                    LAB_TEST_READ, LAB_TEST_WRITE,
                    MEDICAL_RECORD_READ
            )
    ),

    ROLE_PHARMACIST(
            "ROLE_PHARMACIST",
            "Licensed Pharmacist",
            Set.of(
                    PATIENT_READ,
                    PRESCRIPTION_READ,
                    PHARMACY_READ, PHARMACY_WRITE, PHARMACY_DISPENSE
            )
    ),

    ROLE_RECEPTIONIST(
            "ROLE_RECEPTIONIST",
            "Front Desk Receptionist",
            Set.of(
                    PATIENT_READ, PATIENT_WRITE,
                    APPOINTMENT_READ, APPOINTMENT_WRITE,
                    DOCTOR_SCHEDULE_READ,
                    BILLING_READ
            )
    ),

    ROLE_BILLING_CLERK(
            "ROLE_BILLING_CLERK",
            "Billing and Accounts Clerk",
            Set.of(
                    PATIENT_READ,
                    BILLING_READ, BILLING_WRITE,
                    REPORT_VIEW
            )
    ),

    ROLE_PATIENT(
            "ROLE_PATIENT",
            "Registered Patient with limited access",
            Set.of(
                    PATIENT_READ,
                    APPOINTMENT_READ, APPOINTMENT_WRITE,
                    PRESCRIPTION_READ,
                    LAB_TEST_READ,
                    MEDICAL_RECORD_READ,
                    BILLING_READ
            )
    );

    private final String name;
    private final String description;
    private final Set<PermissionEnum> permissions;
}

package com.duoc.backend;

import com.duoc.backend.patient.PatientController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectedXssEscapingTest {

    private static final String XSS_PAYLOAD = "<script>alert(1)</script>";
    private static final String ESCAPED_PAYLOAD = "&lt;script&gt;alert(1)&lt;/script&gt;";

    @Test
    void patientRegisterEscapesHtmlInName() {
        PatientController controller = new PatientController(null);

        assertEquals("Hello {" + ESCAPED_PAYLOAD + "}", controller.greetings(XSS_PAYLOAD));
    }

    @Test
    void greetingsEscapesHtmlInName() {
        SecuredController controller = new SecuredController();

        assertEquals("Hello {" + ESCAPED_PAYLOAD + "}", controller.greetings(XSS_PAYLOAD));
    }

    @Test
    void greetingsPreservesPlainTextName() {
        SecuredController controller = new SecuredController();

        assertEquals("Hello {ZAP}", controller.greetings("ZAP"));
    }
}

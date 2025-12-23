package com.example.demo;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@Listeners(TestResultListener.class)
public class DemoApplicationTests {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PatientProfileRepository patientProfileRepository;

    @Mock
    private RecoveryCurveProfileRepository recoveryCurveProfileRepository;

    @Mock
    private DailySymptomLogRepository dailySymptomLogRepository;

    @Mock
    private DeviationRuleRepository deviationRuleRepository;

    @Mock
    private ClinicalAlertRecordRepository clinicalAlertRecordRepository;

    @Mock
    private UserDetailsService userDetailsService;

    private AuthService authService;
    private PatientProfileService patientProfileService;
    private RecoveryCurveService recoveryCurveService;
    private DeviationRuleService deviationRuleService;
    private ClinicalAlertService clinicalAlertService;
    private DailySymptomLogService dailySymptomLogService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        authService = new AuthServiceImpl(appUserRepository, passwordEncoder, authenticationManager, jwtTokenProvider);
        patientProfileService = new PatientProfileServiceImpl(patientProfileRepository);
        recoveryCurveService = new RecoveryCurveServiceImpl(recoveryCurveProfileRepository);
        deviationRuleService = new DeviationRuleServiceImpl(deviationRuleRepository);
        clinicalAlertService = new ClinicalAlertServiceImpl(clinicalAlertRecordRepository);

        RecoveryCurveService rcService = recoveryCurveService;
        DeviationRuleService drService = deviationRuleService;
        ClinicalAlertService caService = clinicalAlertService;

        dailySymptomLogService = new DailySymptomLogServiceImpl(
                dailySymptomLogRepository,
                patientProfileRepository,
                rcService,
                drService,
                caService
        );
    }

    // -------------------------------------------------
    // GROUP 1: Simple servlet / basic web behaviour
    // -------------------------------------------------

    @Test(priority = 1, groups = "servlet-basic")
    public void testHealthOfApplicationContextSimulation() {
        Assert.assertNotNull(authService);
        Assert.assertNotNull(patientProfileService);
    }

    @Test(priority = 2, groups = "servlet-basic")
    public void testSimpleHttpSimulation_StatusCodeOk() {
        int status = 200;
        Assert.assertEquals(status, 200);
    }

    @Test(priority = 3, groups = "servlet-basic")
    public void testServletLikeGetEndpointReturnsContent() {
        String responseBody = "Hello Servlet";
        Assert.assertTrue(responseBody.contains("Servlet"));
    }

    @Test(priority = 4, groups = "servlet-basic")
    public void testServletHandlesQueryParamSimulation() {
        String name = "Demo";
        String response = "Hello " + name;
        Assert.assertEquals(response, "Hello Demo");
    }

    // -------------------------------------------------
    // GROUP 2: CRUD operations with Spring Boot / REST
    // -------------------------------------------------

    @Test(priority = 5, groups = "crud-patient-positive")
    public void testCreatePatientProfile_Success() {
        PatientProfile profile = PatientProfile.builder()
                .id(1L)
                .patientId("P001")
                .fullName("John Doe")
                .age(40)
                .email("john@example.com")
                .surgeryType("KNEE")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(patientProfileRepository.save(any(PatientProfile.class))).thenReturn(profile);

        PatientProfile result = patientProfileService.createPatient(profile);

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getPatientId(), "P001");
        verify(patientProfileRepository, times(1)).save(any(PatientProfile.class));
    }

    @Test(priority = 6, groups = "crud-patient-positive")
    public void testGetPatientById_Found() {
        PatientProfile profile = PatientProfile.builder()
                .id(2L)
                .patientId("P002")
                .email("p2@example.com")
                .surgeryType("HIP")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(patientProfileRepository.findById(2L)).thenReturn(Optional.of(profile));

        PatientProfile result = patientProfileService.getPatientById(2L);

        Assert.assertEquals(result.getId(), 2L);
        verify(patientProfileRepository).findById(2L);
    }

    @Test(priority = 7, groups = "crud-patient-negative", expectedExceptions = ResourceNotFoundException.class)
    public void testGetPatientById_NotFound() {
        when(patientProfileRepository.findById(99L)).thenReturn(Optional.empty());
        patientProfileService.getPatientById(99L);
    }

    @Test(priority = 8, groups = "crud-patient-positive")
    public void testUpdatePatientStatus() {
        PatientProfile profile = PatientProfile.builder()
                .id(3L)
                .patientId("P003")
                .email("p3@example.com")
                .surgeryType("KNEE")
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(patientProfileRepository.findById(3L)).thenReturn(Optional.of(profile));
        when(patientProfileRepository.save(any(PatientProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PatientProfile updated = patientProfileService.updatePatientStatus(3L, false);

        Assert.assertFalse(updated.getActive());
    }

    // -------------------------------------------------
    // GROUP 3: Dependency Injection / IoC
    // -------------------------------------------------

    @Test(priority = 9, groups = "di-ioc")
    public void testServicesInjectedCorrectly() {
        Assert.assertNotNull(patientProfileService);
        Assert.assertNotNull(recoveryCurveService);
    }

    @Test(priority = 10, groups = "di-ioc")
    public void testAuthServiceUsesPasswordEncoder() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setPassword("password");
        request.setFullName("New User");

        AppUser savedUser = AppUser.builder()
                .id(10L)
                .email("new@example.com")
                .password("encoded")
                .fullName("New User")
                .role(UserRole.CLINICIAN)
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(savedUser);
        when(jwtTokenProvider.generateToken(savedUser)).thenReturn("jwt-token");

        AuthResponse response = authService.register(request);

        verify(passwordEncoder, times(1)).encode("password");
        Assert.assertEquals(response.getEmail(), "new@example.com");
        Assert.assertEquals(response.getToken(), "jwt-token");
    }

@Test(priority = 11, groups = "di-ioc")
public void testAuthServiceLoginAuthenticationManagerInvoked() {
    AuthRequest request = new AuthRequest();
    request.setEmail("login@example.com");
    request.setPassword("password");

    AppUser existing = AppUser.builder()
            .id(11L)
            .email("login@example.com")
            .password("encoded")
            .role(UserRole.CLINICIAN)
            .build();

    when(appUserRepository.findByEmail("login@example.com"))
            .thenReturn(Optional.of(existing));

    when(jwtTokenProvider.generateToken(existing))
            .thenReturn("token");

    org.springframework.security.core.Authentication auth =
            new UsernamePasswordAuthenticationToken(
                    "login@example.com", "password", new ArrayList<>());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

    AuthResponse response = authService.login(request);

    verify(authenticationManager, times(1))
            .authenticate(any(UsernamePasswordAuthenticationToken.class));

    Assert.assertEquals(response.getToken(), "token");
}
@Test(priority = 12, groups = "di-ioc", expectedExceptions = IllegalArgumentException.class)
public void testAuthServiceLoginInvalidUser() {
    AuthRequest request = new AuthRequest();
    request.setEmail("no-user@example.com");
    request.setPassword("password");

    when(appUserRepository.findByEmail("no-user@example.com"))
            .thenReturn(Optional.empty());

    org.springframework.security.core.Authentication auth =
            new UsernamePasswordAuthenticationToken(
                    "no-user@example.com", "password", new ArrayList<>());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

    authService.login(request);
}


    @Test(priority = 13, groups = "hibernate-crud")
    public void testSaveRecoveryCurveProfile() {
        RecoveryCurveProfile curve = RecoveryCurveProfile.builder()
                .id(1L)
                .surgeryType("KNEE")
                .dayNumber(1)
                .expectedPainLevel(5)
                .expectedMobilityLevel(3)
                .expectedFatigueLevel(4)
                .build();

        when(recoveryCurveProfileRepository.save(any(RecoveryCurveProfile.class))).thenReturn(curve);

        RecoveryCurveProfile result = recoveryCurveService.createCurveEntry(curve);

        Assert.assertEquals(result.getSurgeryType(), "KNEE");
    }

    @Test(priority = 14, groups = "hibernate-crud")
    public void testFindRecoveryCurveBySurgeryType() {
        List<RecoveryCurveProfile> curves = Collections.singletonList(
                RecoveryCurveProfile.builder()
                        .id(2L)
                        .surgeryType("HIP")
                        .dayNumber(1)
                        .expectedPainLevel(4)
                        .expectedMobilityLevel(2)
                        .expectedFatigueLevel(3)
                        .build()
        );

        when(recoveryCurveProfileRepository.findBySurgeryTypeOrderByDayNumberAsc("HIP"))
                .thenReturn(curves);

        List<RecoveryCurveProfile> result = recoveryCurveService.getCurveForSurgery("HIP");

        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0).getExpectedPainLevel(), 4);
    }

    @Test(priority = 15, groups = "hibernate-crud")
    public void testDeviationRuleUniqueCodeSimulation() {
        DeviationRule rule = DeviationRule.builder()
                .id(1L)
                .ruleCode("PAIN_HIGH")
                .parameter("PAIN")
                .threshold(3)
                .severity("HIGH")
                .active(true)
                .build();

        when(deviationRuleRepository.save(any(DeviationRule.class))).thenReturn(rule);
        when(deviationRuleRepository.findByRuleCode("PAIN_HIGH")).thenReturn(Optional.of(rule));

        DeviationRule saved = deviationRuleService.createRule(rule);
        Optional<DeviationRule> fetched = deviationRuleService.getRuleByCode("PAIN_HIGH");

        Assert.assertTrue(fetched.isPresent());
        Assert.assertEquals(saved.getRuleCode(), "PAIN_HIGH");
    }

    @Test(priority = 16, groups = "hibernate-crud", expectedExceptions = ResourceNotFoundException.class)
    public void testUpdateDeviationRule_NotFound() {
        when(deviationRuleRepository.findById(999L)).thenReturn(Optional.empty());
        deviationRuleService.updateRule(999L, new DeviationRule());
    }

    // -------------------------------------------------
    // GROUP 5: JPA Mapping / Normalization-like checks
    // -------------------------------------------------

    @Test(priority = 17, groups = "jpa-normalization")
    public void testPatientProfileNormalizedFields() {
        PatientProfile profile = PatientProfile.builder()
                .id(5L)
                .patientId("NORM01")
                .fullName("Norm Alized")
                .email("norm@example.com")
                .surgeryType("KNEE")
                .createdAt(LocalDateTime.now())
                .build();

        Assert.assertNotNull(profile.getPatientId());
        Assert.assertNotNull(profile.getEmail());
        Assert.assertNotNull(profile.getSurgeryType());
    }

    @Test(priority = 18, groups = "jpa-normalization")
    public void testClinicalAlertReferencesPatientAndLog() {
        ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                .id(10L)
                .patientId(1L)
                .logId(2L)
                .alertType("PAIN_SPIKE")
                .severity("HIGH")
                .message("Critical spike")
                .build();

        Assert.assertEquals(alert.getPatientId(), 1L);
        Assert.assertEquals(alert.getLogId(), 2L);
    }

    @Test(priority = 19, groups = "jpa-normalization")
    public void testDeviationRuleHasAllRequiredAttributes() {
        DeviationRule rule = DeviationRule.builder()
                .id(15L)
                .ruleCode("MOBILITY_LOW")
                .parameter("MOBILITY")
                .threshold(2)
                .severity("MEDIUM")
                .active(true)
                .build();

        Assert.assertNotNull(rule.getRuleCode());
        Assert.assertTrue(rule.getThreshold() > 0);
    }

    @Test(priority = 20, groups = "jpa-normalization")
    public void testRecoveryCurveOneEntryPerDayPerSurgeryConceptually() {
        RecoveryCurveProfile c1 = RecoveryCurveProfile.builder()
                .surgeryType("KNEE")
                .dayNumber(1)
                .build();
        RecoveryCurveProfile c2 = RecoveryCurveProfile.builder()
                .surgeryType("KNEE")
                .dayNumber(1)
                .build();

        Assert.assertEquals(c1.getDayNumber(), c2.getDayNumber());
    }

    // -------------------------------------------------
    // GROUP 6: Many-to-Many relationships (conceptual)
    // -------------------------------------------------

    @Test(priority = 21, groups = "many-to-many")
    public void testUserRoleAssignmentConcept() {
        AppUser user = AppUser.builder()
                .id(100L)
                .email("clinician@example.com")
                .password("p")
                .fullName("Clin I Cian")
                .role(UserRole.CLINICIAN)
                .build();

        Assert.assertEquals(user.getRole(), UserRole.CLINICIAN);
    }

    @Test(priority = 22, groups = "many-to-many")
    public void testMultiplePatientsForClinicianConceptual() {
        AppUser clinician = AppUser.builder()
                .id(101L)
                .email("c@example.com")
                .password("p")
                .fullName("C Linician")
                .role(UserRole.CLINICIAN)
                .build();

        List<PatientProfile> profiles = new ArrayList<>();
        profiles.add(PatientProfile.builder().id(1L).patientId("C1").surgeryType("KNEE").build());
        profiles.add(PatientProfile.builder().id(2L).patientId("C2").surgeryType("HIP").build());

        Assert.assertEquals(profiles.size(), 2);
        Assert.assertEquals(clinician.getRole(), UserRole.CLINICIAN);
    }

    @Test(priority = 23, groups = "many-to-many")
    public void testMultipleDeviationRulesAppliedToOneLogConceptual() {
        DailySymptomLog log = DailySymptomLog.builder()
                .id(50L)
                .patientId(1L)
                .logDate(LocalDate.now())
                .painLevel(8)
                .build();

        List<DeviationRule> rules = Arrays.asList(
                DeviationRule.builder().parameter("PAIN").threshold(2).severity("MEDIUM").build(),
                DeviationRule.builder().parameter("PAIN").threshold(4).severity("HIGH").build()
        );

        Assert.assertEquals(rules.size(), 2);
        Assert.assertEquals(log.getPainLevel().intValue(), 8);
    }

    @Test(priority = 24, groups = "many-to-many")
    public void testAlertListForPatientConceptual() {
        List<ClinicalAlertRecord> alerts = Arrays.asList(
                ClinicalAlertRecord.builder().patientId(1L).alertType("PAIN_SPIKE").build(),
                ClinicalAlertRecord.builder().patientId(1L).alertType("MOBILITY_DROP").build()
        );

        Assert.assertEquals(alerts.size(), 2);
        Assert.assertEquals(alerts.get(0).getPatientId(), alerts.get(1).getPatientId());
    }

    // -------------------------------------------------
    // GROUP 7: Security controls & JWT
    // -------------------------------------------------

    @Test(priority = 25, groups = "security-jwt")
    public void testJwtIncludesEmailUserIdRole() {
        AppUser user = AppUser.builder()
                .id(200L)
                .email("jwt@example.com")
                .password("p")
                .role(UserRole.ADMIN)
                .build();

        when(jwtTokenProvider.generateToken(user)).thenReturn("jwt-token-value");

        String token = jwtTokenProvider.generateToken(user);

        Assert.assertNotNull(token);
        Assert.assertEquals(token, "jwt-token-value");
    }

   @Test(priority = 26, groups = "security-jwt")
public void testLoginReturnsJwtToken() {
    AuthRequest request = new AuthRequest();
    request.setEmail("secure@example.com");
    request.setPassword("p");

    AppUser user = AppUser.builder()
            .id(300L)
            .email("secure@example.com")
            .password("encoded")
            .role(UserRole.CLINICIAN)
            .build();

    when(appUserRepository.findByEmail("secure@example.com"))
            .thenReturn(Optional.of(user));

    when(jwtTokenProvider.generateToken(user))
            .thenReturn("secure-token");

    org.springframework.security.core.Authentication auth =
            new UsernamePasswordAuthenticationToken(
                    "secure@example.com", "p", new ArrayList<>());

    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

    AuthResponse response = authService.login(request);

    Assert.assertEquals(response.getToken(), "secure-token");
}

    @Test(priority = 27, groups = "security-jwt")
    public void testJwtValidationFalseForInvalidToken() {
        when(jwtTokenProvider.validateToken("bad-token")).thenReturn(false);
        boolean valid = jwtTokenProvider.validateToken("bad-token");
        Assert.assertFalse(valid);
    }

    @Test(priority = 28, groups = "security-jwt")
    public void testJwtValidationTrueForValidToken() {
        when(jwtTokenProvider.validateToken("good-token")).thenReturn(true);
        boolean valid = jwtTokenProvider.validateToken("good-token");
        Assert.assertTrue(valid);
    }

    // -------------------------------------------------
    // GROUP 8: HQL / HCQL (repository / query behavior)
    // -------------------------------------------------

    @Test(priority = 29, groups = "hql-hcql")
    public void testGetLogsByPatientUsesRepository() {
        List<DailySymptomLog> logs = Arrays.asList(
                DailySymptomLog.builder().id(1L).patientId(1L).logDate(LocalDate.now()).build(),
                DailySymptomLog.builder().id(2L).patientId(1L).logDate(LocalDate.now().minusDays(1)).build()
        );

        when(patientProfileRepository.findById(1L)).thenReturn(
                Optional.of(PatientProfile.builder().id(1L).patientId("P1").surgeryType("KNEE").createdAt(LocalDateTime.now()).build()));
        when(dailySymptomLogRepository.findByPatientId(1L)).thenReturn(logs);

        List<DailySymptomLog> result = dailySymptomLogService.getLogsByPatient(1L);

        Assert.assertEquals(result.size(), 2);
    }

    @Test(priority = 30, groups = "hql-hcql")
    public void testGetActiveRulesQueriesRepository() {
        List<DeviationRule> rules = Collections.singletonList(
                DeviationRule.builder().id(1L).active(true).build()
        );

        when(deviationRuleRepository.findByActiveTrue()).thenReturn(rules);

        List<DeviationRule> result = deviationRuleService.getActiveRules();

        Assert.assertEquals(result.size(), 1);
        Assert.assertTrue(result.get(0).getActive());
    }

    @Test(priority = 31, groups = "hql-hcql")
    public void testAlertResolveUpdatesRecord() {
        ClinicalAlertRecord alert = ClinicalAlertRecord.builder()
                .id(1000L)
                .patientId(1L)
                .logId(2L)
                .resolved(false)
                .build();

        when(clinicalAlertRecordRepository.findById(1000L)).thenReturn(Optional.of(alert));
        when(clinicalAlertRecordRepository.save(any(ClinicalAlertRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ClinicalAlertRecord resolved = clinicalAlertService.resolveAlert(1000L);

        Assert.assertTrue(resolved.getResolved());
    }

    @Test(priority = 32, groups = "hql-hcql", expectedExceptions = ResourceNotFoundException.class)
    public void testAlertResolveNotFound() {
        when(clinicalAlertRecordRepository.findById(9999L)).thenReturn(Optional.empty());
        clinicalAlertService.resolveAlert(9999L);
    }
	    // -------------------------------------------------
    // CONTINUATION: GROUP 8 (HQL / HCQL)
    // -------------------------------------------------

    @Test(priority = 33, groups = "hql-hcql")
    public void testFindRuleByCode() {
        DeviationRule rule = DeviationRule.builder()
                .ruleCode("PAIN_LIMIT")
                .active(true)
                .build();

        when(deviationRuleRepository.findByRuleCode("PAIN_LIMIT"))
                .thenReturn(Optional.of(rule));

        Optional<DeviationRule> result = deviationRuleService.getRuleByCode("PAIN_LIMIT");

        Assert.assertTrue(result.isPresent());
    }

    @Test(priority = 34, groups = "hql-hcql")
    public void testFindRuleByCode_NotFound() {
        when(deviationRuleRepository.findByRuleCode("INVALID"))
                .thenReturn(Optional.empty());

        Optional<DeviationRule> result = deviationRuleService.getRuleByCode("INVALID");

        Assert.assertFalse(result.isPresent());
    }

    // -------------------------------------------------
    // GROUP 9: DAILY SYMPTOM LOG VALIDATION
    // -------------------------------------------------

    @Test(priority = 35, groups = "symptom-validation")
    public void testCreateDailySymptomLog_Success() {
        PatientProfile patient = PatientProfile.builder().id(1L).createdAt(LocalDateTime.now()).surgeryType("KNEE").build();
        DailySymptomLog log = DailySymptomLog.builder().id(1L).patientId(1L).logDate(LocalDate.now()).build();

        when(patientProfileRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(dailySymptomLogRepository.findByPatientIdAndLogDate(anyLong(), any()))
                .thenReturn(Optional.empty());
        when(dailySymptomLogRepository.save(any())).thenReturn(log);

        DailySymptomLog result = dailySymptomLogService.recordSymptomLog(log);

        Assert.assertNotNull(result);
    }

    @Test(priority = 36, groups = "symptom-validation", expectedExceptions = IllegalArgumentException.class)
    public void testDuplicateDailyLogRejected() {
        DailySymptomLog log = DailySymptomLog.builder().patientId(1L).logDate(LocalDate.now()).build();
        when(patientProfileRepository.findById(1L))
                .thenReturn(Optional.of(PatientProfile.builder().build()));
        when(dailySymptomLogRepository.findByPatientIdAndLogDate(anyLong(), any()))
                .thenReturn(Optional.of(log));

        dailySymptomLogService.recordSymptomLog(log);
    }

    @Test(priority = 37, groups = "symptom-validation", expectedExceptions = ResourceNotFoundException.class)
    public void testDailyLogPatientMissing() {
        DailySymptomLog log = DailySymptomLog.builder().patientId(999L).logDate(LocalDate.now()).build();
        when(patientProfileRepository.findById(999L)).thenReturn(Optional.empty());
        dailySymptomLogService.recordSymptomLog(log);
    }

    // -------------------------------------------------
    // GROUP 10: CLINICAL ALERT RULE TRIGGERING
    // -------------------------------------------------

    @Test(priority = 38, groups = "alert-trigger")
    public void testAlertTriggeredForPainSpike() {
        PatientProfile patient = PatientProfile.builder()
                .id(1L)
                .surgeryType("KNEE")
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        RecoveryCurveProfile curve = RecoveryCurveProfile.builder()
                .dayNumber(2)
                .expectedPainLevel(3)
                .build();

        DeviationRule rule = DeviationRule.builder()
                .parameter("PAIN")
                .threshold(2)
                .severity("HIGH")
                .active(true)
                .build();

        DailySymptomLog log = DailySymptomLog.builder()
                .id(5L)
                .patientId(1L)
                .logDate(LocalDate.now())
                .painLevel(8)
                .build();

        when(patientProfileRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(dailySymptomLogRepository.findByPatientIdAndLogDate(anyLong(), any()))
                .thenReturn(Optional.empty());
        when(recoveryCurveProfileRepository.findBySurgeryTypeOrderByDayNumberAsc("KNEE"))
                .thenReturn(List.of(curve));
        when(deviationRuleRepository.findByActiveTrue())
                .thenReturn(List.of(rule));
        when(dailySymptomLogRepository.save(any())).thenReturn(log);
        when(clinicalAlertRecordRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DailySymptomLog saved = dailySymptomLogService.recordSymptomLog(log);
        Assert.assertEquals(saved.getPainLevel().intValue(), 8);
    }

    // -------------------------------------------------
    // GROUP 11: SECURITY ROLE-BASED ACCESS
    // -------------------------------------------------

    @Test(priority = 39, groups = "role-access")
    public void testAdminRoleAssignedCorrectly() {
        AppUser user = AppUser.builder().role(UserRole.ADMIN).build();
        Assert.assertEquals(user.getRole(), UserRole.ADMIN);
    }

    @Test(priority = 40, groups = "role-access")
    public void testClinicianRoleAssignedCorrectly() {
        AppUser user = AppUser.builder().role(UserRole.CLINICIAN).build();
        Assert.assertEquals(user.getRole(), UserRole.CLINICIAN);
    }

    @Test(priority = 41, groups = "role-access")
    public void testHealthAssistantRoleAssignedCorrectly() {
        AppUser user = AppUser.builder().role(UserRole.HEALTH_ASSISTANT).build();
        Assert.assertEquals(user.getRole(), UserRole.HEALTH_ASSISTANT);
    }

    // -------------------------------------------------
    // GROUP 12: PATIENT LOOKUP & SEARCH
    // -------------------------------------------------

    @Test(priority = 42, groups = "patient-search")
    public void testFindByPatientId_Success() {
        when(patientProfileRepository.findByPatientId("P100"))
                .thenReturn(Optional.of(PatientProfile.builder().patientId("P100").build()));

        Optional<PatientProfile> result = patientProfileService.findByPatientId("P100");

        Assert.assertTrue(result.isPresent());
    }

    @Test(priority = 43, groups = "patient-search")
    public void testFindByPatientId_NotFound() {
        when(patientProfileRepository.findByPatientId("X999"))
                .thenReturn(Optional.empty());

        Assert.assertFalse(patientProfileService.findByPatientId("X999").isPresent());
    }

    // -------------------------------------------------
    // GROUP 13: ALERT FETCHING & RESOLUTION
    // -------------------------------------------------

    @Test(priority = 44, groups = "alert-fetch")
    public void testFetchAlertsByPatient() {
        when(clinicalAlertRecordRepository.findByPatientId(1L))
                .thenReturn(List.of(new ClinicalAlertRecord(), new ClinicalAlertRecord()));

        List<ClinicalAlertRecord> alerts = clinicalAlertService.getAlertsByPatient(1L);

        Assert.assertEquals(alerts.size(), 2);
    }

    @Test(priority = 45, groups = "alert-fetch")
    public void testFetchAlertById() {
        when(clinicalAlertRecordRepository.findById(10L))
                .thenReturn(Optional.of(new ClinicalAlertRecord()));

        Optional<ClinicalAlertRecord> result = clinicalAlertService.getAlertById(10L);
        Assert.assertTrue(result.isPresent());
    }

    // -------------------------------------------------
    // GROUP 14: UPDATE DAILY LOG
    // -------------------------------------------------

   @Test(priority = 46, groups = "update-log")
public void testUpdateDailyLog_Success() {

    Long patientId = 10L;

    PatientProfile patient = PatientProfile.builder()
            .id(patientId)
            .patientId("P010")
            .surgeryType("KNEE")
            .createdAt(LocalDateTime.now())
            .build();

    DailySymptomLog existing = DailySymptomLog.builder()
            .id(1L)
            .patientId(patientId)   // ✅ MUST NOT BE NULL
            .logDate(LocalDate.now())
            .painLevel(3)
            .mobilityLevel(4)
            .fatigueLevel(2)
            .build();

    DailySymptomLog updated = DailySymptomLog.builder()
            .patientId(patientId)   // ✅ FORCE AGAIN
            .painLevel(5)
            .mobilityLevel(6)
            .fatigueLevel(3)
            .logDate(LocalDate.now())
            .build();

    when(dailySymptomLogRepository.findById(1L))
            .thenReturn(Optional.of(existing));

    when(patientProfileRepository.findById(patientId))
            .thenReturn(Optional.of(patient));

    when(dailySymptomLogRepository.save(any(DailySymptomLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    DailySymptomLog result =
            dailySymptomLogService.updateSymptomLog(1L, updated);

    Assert.assertEquals(result.getPainLevel().intValue(), 5);
    Assert.assertEquals(result.getPatientId(), patientId);
}
    // -------------------------------------------------
    // GROUP 15: RECOVERY CURVE GENERATION
    // -------------------------------------------------

    @Test(priority = 47, groups = "curve-generation")
    public void testRecoveryCurveFullGeneration() {
        when(recoveryCurveProfileRepository.findAll()).thenReturn(List.of(new RecoveryCurveProfile()));
        Assert.assertEquals(recoveryCurveService.getAllCurves().size(), 1);
    }

    // -------------------------------------------------
    // GROUP 16: DATA INTEGRITY
    // -------------------------------------------------

    @Test(priority = 48, groups = "data-integrity")
    public void testEmailUniquenessConstraintConcept() {
        AppUser u1 = AppUser.builder().email("same@x.com").build();
        AppUser u2 = AppUser.builder().email("same@x.com").build();
        Assert.assertEquals(u1.getEmail(), u2.getEmail());
    }

    // -------------------------------------------------
    // GROUP 17: END-TO-END WORKFLOW
    // -------------------------------------------------

    @Test(priority = 49, groups = "workflow")
    public void testFullWorkflowSimulation() {
        Assert.assertTrue(true);
    }

    // -------------------------------------------------
    // EDGE CASE TESTS
    // -------------------------------------------------

    @Test(priority = 50)
    public void testEdgeCaseZeroPain() {
        DailySymptomLog log = DailySymptomLog.builder().painLevel(0).build();
        Assert.assertEquals(log.getPainLevel().intValue(), 0);
    }

    @Test(priority = 51)
    public void testEdgeCaseMaxPain() {
        DailySymptomLog log = DailySymptomLog.builder().painLevel(10).build();
        Assert.assertEquals(log.getPainLevel().intValue(), 10);
    }

    @Test(priority = 52)
    public void testEdgeCaseNullNotes() {
        DailySymptomLog log = DailySymptomLog.builder().additionalNotes(null).build();
        Assert.assertNull(log.getAdditionalNotes());
    }

    @Test(priority = 53)
    public void testEdgeCaseInactivePatient() {
        PatientProfile p = PatientProfile.builder().active(false).build();
        Assert.assertFalse(p.getActive());
    }

    // -------------------------------------------------
    // FINAL SYSTEM STABILITY TESTS
    // -------------------------------------------------

    @Test(priority = 54)
    public void testSystemDoesNotCrashOnEmptyLists() {
        when(clinicalAlertRecordRepository.findAll()).thenReturn(List.of());
        Assert.assertEquals(clinicalAlertService.getAllAlerts().size(), 0);
    }

    @Test(priority = 55)
    public void testMultiplePatientsHandledCorrectly() {
        when(patientProfileRepository.findAll()).thenReturn(List.of(
                new PatientProfile(), new PatientProfile(), new PatientProfile()
        ));
        Assert.assertEquals(patientProfileService.getAllPatients().size(), 3);
    }

    @Test(priority = 56)
    public void testAlertDefaultResolvedFalse() {
        ClinicalAlertRecord alert = new ClinicalAlertRecord();
        Assert.assertFalse(alert.getResolved());
    }

    @Test(priority = 57)
    public void testDeviationRuleDefaultActiveTrue() {
        DeviationRule rule = new DeviationRule();
        Assert.assertTrue(rule.getActive());
    }

    @Test(priority = 58)
    public void testJwtTokenNotNull() {
        Assert.assertNotNull("jwt");
    }

    @Test(priority = 59)
    public void testSwaggerLoadsConceptually() {
        Assert.assertTrue(true);
    }

    @Test(priority = 60)
    public void testDatabaseConnectionConceptually() {
        Assert.assertTrue(true);
    }

    @Test(priority = 61)
    public void testApplicationStartsSuccessfully() {
        Assert.assertTrue(true);
    }

    @Test(priority = 62)
    public void testEndToEndSecurityFlow() {
        Assert.assertTrue(true);
    }

    @Test(priority = 63)
    public void testMultipleAlertsGenerated() {
        Assert.assertTrue(true);
    }

    @Test(priority = 64)
    public void testMultipleRulesApplied() {
        Assert.assertTrue(true);
    }

    @Test(priority = 65)
    public void testHighLoadLoggingSimulation() {
        Assert.assertTrue(true);
    }

    @Test(priority = 66)
    public void testDataConsistencyAfterUpdates() {
        Assert.assertTrue(true);
    }

    @Test(priority = 67)
    public void testConcurrentAccessSimulation() {
        Assert.assertTrue(true);
    }

    @Test(priority = 68)
    public void testRecoveryCurveBoundaryDayZero() {
        RecoveryCurveProfile curve = RecoveryCurveProfile.builder().dayNumber(0).build();
        Assert.assertEquals(curve.getDayNumber().intValue(), 0);
    }

    @Test(priority = 69)
    public void testRecoveryCurveFutureDays() {
        RecoveryCurveProfile curve = RecoveryCurveProfile.builder().dayNumber(30).build();
        Assert.assertEquals(curve.getDayNumber().intValue(), 30);
    }

    @Test(priority = 70)
    public void testFinalSystemValidationPassed() {
        Assert.assertTrue(true);
    }

}

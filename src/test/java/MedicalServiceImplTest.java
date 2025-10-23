import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.of;

public class MedicalServiceImplTest {

    static Stream<Arguments> getMessageById() {

        return Stream.of(
                of("1", new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))), "Warning, patient with id: 1, need help"),
                of("2", new PatientInfo("2", "Семен", "Михайлов", LocalDate.of(1982, 1, 16),
                        new HealthInfo(new BigDecimal("36.6"), new BloodPressure(120, 80))), "Warning, patient with id: 2, need help")

        );
    }

    @ParameterizedTest(name = "Id:{0},{1} -> {2}")
    @MethodSource("getMessageById")
    @DisplayName("Получение сообщения по id пациента, если давление не совпадает")
    void testSendMessage_WhenPatientBloodPressure_equalsBadPressure(String id, PatientInfo patientInfo, String expectedMessage) {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.anyString());

        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);

        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalServiceImpl.checkBloodPressure(id, currentPressure);

        Mockito.verify(alertService).send(
                String.format("Warning, patient with id: %s, need help", patientInfo.getId())
        );

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest(name = "Id:{0},{1} -> {2}")
    @MethodSource("getMessageById")
    @DisplayName("Получение сообщения по id пациента, если давление не совпадает")
    void testSendMessage_WhenPatientTemperature_equalsBadTemperature(String id, PatientInfo patientInfo, String expectedMessage) {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.anyString());

        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);

        BigDecimal currentTemperature = new BigDecimal("38.5");
        medicalServiceImpl.checkTemperature(id, currentTemperature);

        Mockito.verify(alertService).send(
                String.format("Warning, patient with id: %s, need help", patientInfo.getId())
        );

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(alertService).send(messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest(name = "Id:{0},{1} -> {2}")
    @MethodSource("getMessageById")
    @DisplayName("Получение сообщения по id пациента, если давление не совпадает")
    void testNotSendMessage_WhenPatientBloodPressure_equalsGoodPressure(String id, PatientInfo patientInfo, String expectedMessage) {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.anyString());

        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);

        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalServiceImpl.checkBloodPressure(id, currentPressure);

        Mockito.verify(alertService, Mockito.never()).send(Mockito.anyString());
    }

    @ParameterizedTest(name = "Id:{0},{1} -> {2}")
    @MethodSource("getMessageById")
    @DisplayName("Получение сообщения по id пациента, если давление не совпадает")
    void testNotSendMessage_WhenPatientTemperature_equalsGoodTemperature(String id, PatientInfo patientInfo, String expectedMessage) {
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.anyString());

        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);

        BigDecimal currentTemperature = new BigDecimal("36.8");
        medicalServiceImpl.checkTemperature(id, currentTemperature);

        Mockito.verify(alertService, Mockito.never()).send(Mockito.anyString());
    }


    //    @Test
//    void testSendMessage_WhenPatientBloodPressure_equalsBadPressure(){
//        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
//        PatientInfo patientInfo = new PatientInfo("1","Иван", "Петров", LocalDate.of(1980, 11, 26),
//                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
//        Mockito.when(patientInfoRepository.getById("1"))
//                .thenReturn(patientInfo);
//
//        SendAlertService alertService = Mockito.mock(SendAlertService.class);
//        Mockito.doNothing().when(alertService).send(Mockito.anyString());
//        //alertService.send(String.format("Warning, patient with id: %s, need help", patientInfo.getId()));
//
//        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);
//
//        BloodPressure currentPressure = new BloodPressure(60, 120);
//        medicalServiceImpl.checkBloodPressure("1",currentPressure);
//
//        Mockito.verify(alertService).send(
//                String.format("Warning, patient with id: %s, need help", patientInfo.getId())
//        );
//    }


}

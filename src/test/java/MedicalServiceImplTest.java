import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
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

    @Test
    void testSendMessage_WhenPatientBloodPressure_equalsBadPressure(){
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        PatientInfo patientInfo = new PatientInfo("1","Иван", "Петров", LocalDate.of(1980, 11, 26),
                new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)));
        Mockito.when(patientInfoRepository.getById("1"))
                .thenReturn(patientInfo);

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.anyString());
        //alertService.send(String.format("Warning, patient with id: %s, need help", patientInfo.getId()));

        MedicalServiceImpl medicalServiceImpl = new MedicalServiceImpl(patientInfoRepository, alertService);

        BloodPressure currentPressure = new BloodPressure(60, 120);
        medicalServiceImpl.checkBloodPressure("1",currentPressure);

        Mockito.verify(alertService).send(
                String.format("Warning, patient with id: %s, need help", patientInfo.getId())
        );
    }

//    @Test
//    void testSendMessage_WhenCountryIsRussia_ReturnsRussianMessage() {
//        GeoService geoService = Mockito.mock(GeoService.class);
//        Mockito.when(geoService.byIp("172.0.32.11"))
//                .thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
//
//        LocalizationService localizationService = Mockito.mock(LocalizationService.class);
//        Mockito.when(localizationService.locale(Country.RUSSIA))
//                .thenReturn("Добро пожаловать");
//
//        MessageSenderImpl messageSenderImpl = new MessageSenderImpl(geoService, localizationService);
//        Map<String, String> headers = new HashMap<>();
//        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");
//
//        String message = messageSenderImpl.send(headers);
//        //String expectedMessage = localizationService.locale(Country.RUSSIA);
//
//        Assertions.assertEquals("Добро пожаловать", message);
//
////        Mockito.verify(geoService, Mockito.times(1)).byIp("172.0.32.11");
////        Mockito.verify(localizationService,Mockito.times(3)).locale(Country.RUSSIA);
//
//        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(geoService).byIp(argumentCaptor.capture());
//        Assertions.assertEquals("172.0.32.11", argumentCaptor.getValue());
//
//        ArgumentCaptor<Country> countryCaptor = ArgumentCaptor.forClass(Country.class);
//        Mockito.verify(localizationService, Mockito.times(2)).locale(countryCaptor.capture());
//        Assertions.assertEquals(Country.RUSSIA, countryCaptor.getValue());
//    }

//    static Stream<Arguments> getMessageByCountry() {
//
//        return Stream.of(
//                of(Country.RUSSIA, "Добро пожаловать"),
//                of(Country.USA, "Welcome"),
//                of(Country.BRAZIL, "Welcome"),
//                of(Country.GERMANY, "Welcome")
//        );
//    }
}

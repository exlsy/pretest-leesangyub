package com.pretest.leesangyub.config;


import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.when;

// @SpringBootTest
// @ActiveProfiles("local")

public class JasyptConfigTest {
    @Mock
    private Environment environment;  // Environment를 Mock으로 생성

    @InjectMocks
    private JasyptConfig jasyptConfig;

    // @Autowired
    // @Qualifier( "jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
        when(environment.getProperty("secret")).thenReturn("easytask");  // Mock 환경변수 설정

        // 실제 JasyptConfig의 stringEncryptor 메서드를 호출하여 encryptor 생성
        stringEncryptor = jasyptConfig.stringEncryptor();
    }


    @Test
    // JASYPT 암호화 복호화 단위테스트
    public void testEncryptionDecryption() {
        // Given: 원본 문자열이 주어졌을 때
        // String originalText = "user1357!";
        // String originalText = "redis1357!";
        String originalText = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        // When: 원본 문자열을 암호화하고
        String encryptedText = stringEncryptor.encrypt(originalText);
        System.out.println("Encrypted text: " + encryptedText);

        // When: 암호화된 문자열을 복호화하면
        String decryptedText = stringEncryptor.decrypt(encryptedText);
        System.out.println("Decrypted text: " + decryptedText);

        // Then: 복호화된 문자열이 원본 문자열과 동일해야 한다
        Assertions.assertEquals(originalText, decryptedText);
    }
}

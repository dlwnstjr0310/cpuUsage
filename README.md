# 사용한 기술 및 툴의 버전 정보
- `Java 17`
- `SpringBoot 3.2.5`
- `IntelliJ 2024.1`
### DB
- `MariaDB`
- `H2`
  

### 프로젝트 실행 방법
- 배포를 하지 않았기 때문에, 로컬 환경에 H2 Database 와 MariaDB 가 모두 설치되어 있어야 합니다.
- MariaDB 의 경우 기존에 사용중인 MySQL 과 포트 번호가 겹쳐, 3307 포트로 설정해두었습니다. ( application-dev.yml 파일에서 변경하시면 됩니다. )
  
### 프로젝트 구조 설명
- 엔티티는 CpuUsage (분 단위), CpuUsageSummary (시, 일 단위) 두 가지만 사용합니다.
- CpuUsage 엔티티는 id 와 usage 로 이루어져 있으며, 모든 엔티티는 제공 기한 설정을 위하여 BaseTimeEntity 를 상속받습니다.
- 시/일 단위 데이터의 경우, 삭제 기간을 제외하면 모든 컬럼에 저장되어야하는 데이터가 동일하므로, 열거 타입을 이용해 구분 하도록 설정해두었습니다.
- 단순한 조회만 이루어지는 API 는 CpuUsageController 에, 삽입과 삭제가 이루어지는 경우 DataAutoManagementController 에 있습니다.
- 예외처리는 exception 패키지 내의 ExceptionHandler 가 담당하고 있으며, 자세한 정보를 제공하기 위하여 Error를 열거 타입으로 만들어 사용하고 있습니다.

# 요구사항과 해결 방법
<details>
  <summary>CPU 사용률 데이터 분 단위 수집 및 DB 저장</summary>
  <br>
  1. @Scheduled 어노테이션을 사용해 매 분마다 DB 에 저장하도록 설정해두었습니다. <br>
  2. OperatingSystemMXBean 클래스의 getCpuLoad() 메소드를 이용하여 현재 시스템의 CPU 사용률을 가져와 저장하였습니다. <br>
</details>
<details>
  <summary>H2 DB를 개발 및 테스트에 사용, MariaDB 를 운용 DB 로 사용</summary>
  <br>
  1. spring.profiles.active 설정을 통하여 프로파일을 분리하였습니다. <br>
  2. 테스트 환경에서는 @ActiveProfiles("test") 어노테이션을 사용해 applictaion-test.yml 파일의 설정을 읽어 H2 DB 를 사용합니다. <br>
  3. 실 사용 및 Swagger 에서는 기본적으로 활성화되어있는 application-dev.yml 파일의 설정을 읽어 MariaDB 를 사용합니다.
</details>
<details>
  <summary>Swagger 를 사용하여 API 문서화</summary>
  <br>
  1. 클래스에 @Tag 어노테이션을 사용하여 큰 범주로 API 를 분리했습니다. <br>
  2. 메소드마다 @Operation 과 @ApiResponses 어노테이션을 사용하여 메소드의 자세한 설명과 응답의 형태를 확인할 수 있습니다.<br>
  3. 파라미터가 존재하는 경우 @Schema 어노테이션을 사용해 필요한 데이터의 형태를 알 수 있게 작성해두었습니다.
</details>
<details>
  <summary>분, 시, 일 단위마다 데이터 제공 기한 설정</summary>
  <br>
  1. 잦은 삭제가 이루어지지 않도록 조회의 범위를 설정했고, 설정 범위를 초과한 경우 예외가 발생하도록 설정해두었습니다. <br>
  2. 데이터의 삭제는 createdAt 필드를 기준으로 조회하여 매일 자정에 오래된 데이터는 삭제하도록 설정해두었습니다. <br>
</details>
<details>
  <summary>데이터 수집 및 API 요청 실패 시 예외 처리</summary>
  <br>
  1. 분 단위 CPU 사용 데이터 수집 실패 시 발생하는 Exception 에 대해 로그 작성 및 예외 처리 해두었습니다.<br>
  2. API 요청 실패 시 발생하는 Excception 에 대하여 Exception 패키지에 핸들링 해두었습니다.
</details>
<details>
  <summary>유닛 테스트, 통합 테스트 작성</summary>
  <br>
  1. Controller 에 대한 통합 테스트는 @SpringBootTest, @AutoConfigureMockMvc 를 이용하여 작성하였습니다. <br>
  2. Service, Repository 에 대한 유닛 테스트는 Mock 객체를 이용하여 작성해두었습니다.
</details>

# 주요 API 설명
### CPU 사용량 조회 API
<details>
  <summary>분 단위 사용량 조회</summary>
  'yyyy-MM-ddTHH:mm:ss' 의 형태로 기간의 시작일과 종료일을 입력한 후 조회합니다. swagger 에서 다음과 같이 실행해보실 수 있습니다.
  
  ![image](https://github.com/dlwnstjr0310/cpuUsage/assets/126157268/52a37013-f5de-43d5-b074-6d4d18fd15af)

  반환되는 데이터는 성공시 List<UsageResponse.Base> 의 형태로 되어있으며, 실패 시 HttpStatusCode, ErrorMessage 가 리턴됩니다.
  전달값의 형식이 올바르지 않은 경우나 7일이 경과한 데이터를 조회한 경우 예외가 발생합니다.
</details>
<details>
  <summary>시간 단위 사용량 조회</summary>
  'yyyy-MM-ddTHH:mm:ss' 의 형태로 기간의 시작일과 종료일을 입력한 후 조회합니다. swagger 에서 다음과 같이 실행해보실 수 있습니다.

![image](https://github.com/dlwnstjr0310/cpuUsage/assets/126157268/e5ef62b5-ad62-4b40-9df4-844af5a61de0)

  반환되는 데이터는 성공시 List<UsageResponse.Target> 의 형태로 되어있으며, 실패 시 HttpStatusCode, ErrorMessage 가 리턴됩니다.
  전달값의 형식이 올바르지 않은 경우나 3개월이 경과한 데이터를 조회한 경우 예외가 발생합니다.
</details>
<details>
  <summary>일 단위 사용량 조회</summary>
  'yyyy-MM-dd' 의 형태로 기간의 시작일과 종료일을 입력한 후 조회합니다. swagger 에서 다음과 같이 실행해보실 수 있습니다.

  ![image](https://github.com/dlwnstjr0310/cpuUsage/assets/126157268/55cf949f-fa45-4253-b3ed-ebee90b7412d)

  반환되는 데이터는 성공시 List<UsageResponse.Target> 의 형태로 되어있으며, 실패 시 HttpStatusCode, ErrorMessage 가 리턴됩니다.
  전달값의 형식이 올바르지 않은 경우나 1년 이상 경과한 데이터를 조회한 경우 예외가 발생합니다.

</details>

### CPU 사용량 저장 API
<details>
  <summary>분 단위 사용량 저장</summary>
  매 분마다 자동으로 실행되어, 현재 CPU 사용량을 DB 에 저장합니다.
  swagger 에서 수동으로 실행하면 현재의 CPU 사용량을 DB 에 저장합니다.

  반환 데이터는 HttpsStatusCode : 200 , 실패 시 HttpStatusCode : 500 , ErrorMessage 가 리턴되며 로그가 저장됩니다.
</details>
<details>
  <summary>시간 단위 사용량 저장</summary>
  매 시간마다 자동으로 실행되어, 해당 시간의 0분 ~ 59분 까지의 CPU 사용량의 최대/평균/최소값을 계산한 후 저장합니다.
  swagger 에서 수동으로 실행하면 한시간 전의 0분 ~ 59분 까지의 CPU 사용량을 계산 후 저장합니다.

</details>
<details>
  <summary>일 단위 사용량 저장</summary>
  자정마다 자동으로 실행되어, 그날의 분 단위 CPU 사용량의 최대/평균/최소값을 계산한 후 저장합니다.
  swagger 에서 수동으로 실행하면 어제의 분 단위 CPU 사용량을 계산 후 저장합니다.
  
</details>

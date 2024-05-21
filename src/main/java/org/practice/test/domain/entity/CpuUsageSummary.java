package org.practice.test.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.practice.test.domain.common.BaseTimeEntity;
import org.practice.test.domain.common.Status;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CpuUsageSummary extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	Double maxUsage;

	Double avgUsage;

	Double minUsage;

	@NotNull
	@Enumerated(EnumType.STRING)
	Status.Type type;

}

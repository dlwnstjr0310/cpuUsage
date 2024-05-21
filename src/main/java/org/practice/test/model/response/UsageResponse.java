package org.practice.test.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.practice.test.domain.entity.CpuUsage;
import org.practice.test.domain.entity.CpuUsageSummary;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@UtilityClass
public class UsageResponse {

	private static String formatUsage(Double cpuUsage) {
		return String.format("%.2f", cpuUsage * 100) + "%";
	}

	private static LocalDateTime truncateDateTime(LocalDateTime recordTime) {
		return recordTime.truncatedTo(ChronoUnit.SECONDS);
	}

	@Getter
	@Builder
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Base {

		LocalDateTime recordTime;

		String usage;

		public static Base of(CpuUsage cpuUsage) {

			return Base.builder()
					.recordTime(truncateDateTime(cpuUsage.getCreatedAt()))
					.usage(formatUsage(cpuUsage.getUsage()))
					.build();
		}

		public static List<Base> of(List<CpuUsage> cpuUsageList) {

			return cpuUsageList.stream()
					.map(UsageResponse.Base::of)
					.sorted(Comparator.comparing(Base::getRecordTime))
					.toList();
		}
	}

	@Getter
	@Setter
	@Builder
	@FieldDefaults(level = AccessLevel.PRIVATE)
	public static class Target {

		LocalDateTime recordTime;

		String maxUsage;

		String avgUsage;

		String minUsage;

		public static Target of(CpuUsageSummary cpuUsageSummary) {

			return Target.builder()
					.recordTime(truncateDateTime(cpuUsageSummary.getCreatedAt()))
					.maxUsage(formatUsage(cpuUsageSummary.getMaxUsage()))
					.avgUsage(formatUsage(cpuUsageSummary.getAvgUsage()))
					.minUsage(formatUsage(cpuUsageSummary.getMinUsage()))
					.build();
		}

		public static List<Target> of(List<CpuUsageSummary> cpuUsageList) {

			return cpuUsageList.stream()
					.map(UsageResponse.Target::of)
					.sorted(Comparator.comparing(Target::getRecordTime))
					.toList();
		}
	}
}

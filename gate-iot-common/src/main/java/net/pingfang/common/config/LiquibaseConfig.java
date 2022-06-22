package net.pingfang.common.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResourceLoader;

import com.google.common.io.Files;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.MigrationsUtils;

@Configuration
@ConditionalOnBean(value = LiquibaseProperties.class)
@Slf4j
public class LiquibaseConfig {
	@Resource
	LiquibaseProperties properties;

	@Bean("liquibase")
	public SpringLiquibase liquibase(DataSource dataSource) throws IOException {
		List<String> paths = properties.getChangelog();
		MigrationsUtils migrationsUtils = new MigrationsUtils(com.google.common.io.Files.createTempDir());
		List<String> files = paths.stream().map(migrationsUtils::writeResourceToMigrationDirectory)
				.flatMap(Collection::stream).collect(Collectors.toList());
		String str = MigrationsUtils.generate(files);

		ClassPathResource resource = new ClassPathResource("database/migrations.xml");
		// 获取文件
		File file = resource.getFile();
		Files.write(str.getBytes(StandardCharsets.UTF_8), file);
		log.info("Dynamic Migration File: {}", file.getAbsolutePath());

		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:database/migrations.xml");
		liquibase.setResourceLoader(new FileSystemResourceLoader());
		liquibase.setShouldRun(true);

		// cleanup directory on exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try (Stream<Path> dir = java.nio.file.Files.walk(file.toPath())) {
					dir.sorted(Comparator.reverseOrder()) //
							.map(x -> {
								try {
									return new FileWriter(x.toFile());
								} catch (IOException e) {
									e.printStackTrace();
									return null;
								}
							}).filter(Objects::nonNull) //
							.forEach(x -> {
								try {
									x.write("");
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
				} catch (IOException e) {
					log.error("Shutdown Cleanup Error", e);
				}
			}
		});

		return liquibase;
	}
}

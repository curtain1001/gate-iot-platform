package net.pingfang.common.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 17:30
 */
@Component
@ConfigurationProperties(prefix = "liquibase")
public class LiquibaseProperties {
	private List<String> changelog;

	public List<String> getChangelog() {
		return changelog;
	}

	public void setChangelog(List<String> changelog) {
		this.changelog = changelog;
	}
}

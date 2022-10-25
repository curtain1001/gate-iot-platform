package net.pingfang.device.plc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import net.pingfang.device.testconfig.CertificateService;
import net.pingfang.device.testconfig.DefaultNetworkConfigManager;
import net.pingfang.network.DefaultNetworkManager;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@ComponentScan(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE), basePackageClasses = {
		DefaultNetworkConfigManager.class, DefaultNetworkManager.class, CertificateService.class })
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class TestApplication {
	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(TestApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  服务启动成功   ლ(´ڡ`ლ)ﾞ  \n" + " .-------.       ____     __        \n"
				+ " |  _ _   \\      \\   \\   /  /    \n" + " | ( ' )  |       \\  _. /  '       \n"
				+ " |(_ o _) /        _( )_ .'         \n" + " | (_,_).' __  ___(_ o _)'          \n"
				+ " |  |\\ \\  |  ||   |(_,_)'         \n" + " |  | \\ `'   /|   `-'  /           \n"
				+ " |  |  \\    /  \\      /           \n" + " ''-'   `'-'    `-..-'              ");
	}
}

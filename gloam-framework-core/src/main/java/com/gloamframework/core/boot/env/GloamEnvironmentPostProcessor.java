package com.gloamframework.core.boot.env;

import com.gloamframework.core.boot.banner.GloamBanner;
import com.gloamframework.core.boot.scanner.ResourceScanner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Gloam环境前置处理器
 *
 * @author 晓龙
 */
public class GloamEnvironmentPostProcessor implements EnvironmentPostProcessor {

	/**
	 * gloam banner
	 */
	private static final Banner GLOAM_BANNER = new GloamBanner();

	/**
	 * gloam scanner
	 */
	private static final ResourceScanner resourceScanner = ResourceScanner.getDefault();

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// 设置banner
		application.setBanner(GLOAM_BANNER);
		// 进行包扫描，扫描需要被重写的配置资源
	}

}

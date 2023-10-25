package com.gloamframework.core.boot.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

/**
 * gloam启动banner
 *
 * @author 晓龙
 */
public class GloamBanner implements Banner {
	private static final String BANNER = "  ____ _     ___    _    __  __ \n" +
			" / ___| |   / _ \\  / \\  |  \\/  |\n" +
			"| |  _| |  | | | |/ _ \\ | |\\/| |\n" +
			"| |_| | |__| |_| / ___ \\| |  | |\n" +
			" \\____|_____\\___/_/   \\_\\_|  |_|\n";

	private static final String AUTHOR = "Author          ::";
	private static final String SPRING_BOOT = "Spring Boot     :: ";
	private static final String SPRING_CLOUD = "Spring Cloud    ::";
	private static final String SPRING_ALIBABA = "Spring Alibaba  ::";

	private static final int STRAP_LINE_SIZE = 30;

	@Override
	public void printBanner(Environment environment, Class<?> sourceClass, PrintStream printStream) {
		// 打印基础banner
		printStream.println(AnsiOutput.toString(AnsiColor.CYAN, BANNER));
		String version = SpringBootVersion.getVersion();
		version = (version != null) ? " v-" + version : "";
		StringBuilder padding = new StringBuilder();
		while (padding.length() < STRAP_LINE_SIZE - (version.length() + SPRING_BOOT.length())) {
			padding.append(" ");
		}
		String author = "  Moxy";
		String springCloudVersion = "  v-Hoxton.SR9";
		String springAlibabaVersion = "  v-2.2.6.RELEASE";
		// 打印版本
		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, AUTHOR, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, author));
		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_BOOT, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, version));
		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_CLOUD, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, springCloudVersion));
		printStream.println(AnsiOutput.toString(AnsiColor.GREEN, SPRING_ALIBABA, AnsiColor.DEFAULT, padding.toString(), AnsiStyle.FAINT, springAlibabaVersion));
		printStream.println();
	}
}

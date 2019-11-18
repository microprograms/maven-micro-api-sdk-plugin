package com.github.microprograms.maven_micro_api_sdk_plugin.utils;

public class Fn {
	public static String parseJavaPackageName(String javaPackageName) {
		return javaPackageName.replaceAll("-", "_");
	}
}

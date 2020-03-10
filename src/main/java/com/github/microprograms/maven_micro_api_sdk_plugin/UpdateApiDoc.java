package com.github.microprograms.maven_micro_api_sdk_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.github.microprograms.micro_api_sdk.model.ModuleDefinition;
import com.github.microprograms.micro_api_sdk.model.ShowdocDefinition;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "update-api-doc", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateApiDoc extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/api.json")
	private String apiConfigFilePath;
	@Parameter(defaultValue = "src/main/resources")
	private String apiDocDir;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-api-doc");
		getLog().info("------------------------------------------------------------------------");
		try {
			if (!new File(apiConfigFilePath).exists()) {
				getLog().info(String.format("%s not exists, nothing to update.", apiConfigFilePath));
				return;
			}

			ModuleDefinition moduleDefinition = ApiSdk.build(apiConfigFilePath);
			ApiSdk.Markdown.writeToFile(moduleDefinition, new ArrayList<>(), new File(apiDocDir));
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

}

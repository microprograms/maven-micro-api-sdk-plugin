package com.github.microprograms.maven_micro_api_sdk_plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.microprograms.micro_api_sdk.model.PlainModelerDefinition;
import com.github.microprograms.micro_api_sdk.utils.ModelSdk;

@Mojo(name = "update-all-models", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateAllModels extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/model.json")
	private String configFilePath;
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	private String srcFolder;
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.model")
	private String javaPackageName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-all-models");
		getLog().info("------------------------------------------------------------------------");
		try {
			PlainModelerDefinition modelerDefinition = ModelSdk.build(configFilePath);
			ModelSdk.UpdateJavaSourceFile.updateAll(modelerDefinition, srcFolder, _getJavaPackageName());
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

	private String _getJavaPackageName() {
		return javaPackageName.replaceAll("-", "_");
	}

}

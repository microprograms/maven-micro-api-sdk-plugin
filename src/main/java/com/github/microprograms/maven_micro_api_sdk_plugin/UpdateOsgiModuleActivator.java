package com.github.microprograms.maven_micro_api_sdk_plugin;

import java.io.File;

import com.github.microprograms.maven_micro_api_sdk_plugin.utils.Fn;
import com.github.microprograms.micro_api_sdk.model.ModuleDefinition;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk;
import com.github.microprograms.micro_api_sdk.utils.OsgiModule;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "update-osgi-module-activator", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateOsgiModuleActivator extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/api.json")
	private String apiConfigFilePath;
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	private String srcFolder;
	@Parameter(defaultValue = "${project.artifactId}")
	private String osgiJavaPackageName;
	@Parameter(defaultValue = "${project.artifactId}.api")
	private String apiJavaPackageName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-osgi-module-activator");
		getLog().info("------------------------------------------------------------------------");
		try {
			if (!new File(apiConfigFilePath).exists()) {
				getLog().info(String.format("%s not exists, nothing to update.", apiConfigFilePath));
				return;
			}

			ModuleDefinition moduleDefinition = ApiSdk.build(apiConfigFilePath);
			String parsedApiJavaPackageName = Fn.parseJavaPackageName(apiJavaPackageName);
			String parsedOsgiJavaPackageName = Fn.parseJavaPackageName(osgiJavaPackageName);
			OsgiModule.UpdateActivatorJavaSourceFile.update(moduleDefinition, srcFolder, parsedOsgiJavaPackageName,
					parsedApiJavaPackageName);
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

}

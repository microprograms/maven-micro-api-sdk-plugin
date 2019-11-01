package com.github.microprograms.maven_micro_api_sdk_plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.microprograms.micro_api_sdk.model.ModuleDefinition;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk.UpdateJavaSourceFile.UpdateStrategy;

@Mojo(name = "update-all-apis", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateAllApis extends AbstractMojo {

	@Parameter(defaultValue = "${project.build.sourceDirectory}api.json")
	private String configFilePath;
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	private String srcFolder;
	@Parameter(defaultValue = "${project.groupId}.${project.artifactId}.api")
	private String javaPackageName;
	@Parameter
	private String updateStrategyClassName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-all-apis");
		getLog().info("------------------------------------------------------------------------");
		try {
			ModuleDefinition moduleDefinition = ApiSdk.build(configFilePath);
			ApiSdk.UpdateJavaSourceFile.updateAllApis(moduleDefinition, srcFolder, _getJavaPackageName(),
					_getUpdateStrategy());
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

	private String _getJavaPackageName() {
		return javaPackageName.replaceAll("-", "_");
	}

	private UpdateStrategy _getUpdateStrategy() throws Exception {
		if (StringUtils.isBlank(updateStrategyClassName)) {
			return new ApiSdk.UpdateJavaSourceFile.DefaultUpdateStrategy();
		}
		return (UpdateStrategy) Class.forName(updateStrategyClassName).newInstance();
	}

}

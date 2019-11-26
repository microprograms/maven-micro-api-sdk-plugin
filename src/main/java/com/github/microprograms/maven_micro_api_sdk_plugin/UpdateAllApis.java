package com.github.microprograms.maven_micro_api_sdk_plugin;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.microprograms.maven_micro_api_sdk_plugin.utils.Fn;
import com.github.microprograms.micro_api_sdk.model.ModuleDefinition;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk;
import com.github.microprograms.micro_api_sdk.utils.ApiSdk.UpdateJavaSourceFile.UpdateStrategy;

@Mojo(name = "update-all-apis", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateAllApis extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/api.json")
	private String apiConfigFilePath;
	@Parameter(defaultValue = "${project.build.sourceDirectory}")
	private String srcFolder;
	@Parameter(defaultValue = "${project.artifactId}")
	private String apiJavaPackageName;
	@Parameter(defaultValue = "${project.artifactId}.model")
	private String modelJavaPackageName;
	@Parameter
	private String apiUpdateStrategyClassName;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-all-apis");
		getLog().info("------------------------------------------------------------------------");
		try {
			ModuleDefinition moduleDefinition = ApiSdk.build(apiConfigFilePath);
			String parsedApiJavaPackageName = Fn.parseJavaPackageName(apiJavaPackageName);
			String parsedModelJavaPackageName = Fn.parseJavaPackageName(modelJavaPackageName);
			ApiSdk.UpdateJavaSourceFile.updateAllApis(moduleDefinition, srcFolder, parsedApiJavaPackageName,
					parsedModelJavaPackageName, _getUpdateStrategy());
			ApiSdk.UpdateJavaSourceFile.updateErrorCode(moduleDefinition, srcFolder, parsedApiJavaPackageName);
			ApiSdk.UpdateJavaSourceFile.updateAllInnerModels(moduleDefinition, srcFolder, parsedApiJavaPackageName);
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

	private UpdateStrategy _getUpdateStrategy() throws Exception {
		if (StringUtils.isBlank(apiUpdateStrategyClassName)) {
			return new ApiSdk.UpdateJavaSourceFile.DefaultUpdateStrategy();
		}
		return (UpdateStrategy) Class.forName(apiUpdateStrategyClassName).newInstance();
	}

}

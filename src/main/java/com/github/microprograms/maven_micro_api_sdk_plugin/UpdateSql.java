package com.github.microprograms.maven_micro_api_sdk_plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.microprograms.micro_api_sdk.model.PlainModelerDefinition;
import com.github.microprograms.micro_api_sdk.utils.ModelSdk;

@Mojo(name = "update-sql", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateSql extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/model.json")
	private String configFilePath;
	@Parameter
	private String excludeModelNames;
	@Parameter(defaultValue = "")
	private String tablePrefix;
	@Parameter(defaultValue = "src/main/resources")
	private String dir;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-all-sql");
		getLog().info("------------------------------------------------------------------------");
		try {
			PlainModelerDefinition modelerDefinition = ModelSdk.build(configFilePath);
			ModelSdk.Sql.writeToFile(modelerDefinition, _getExcludeModelNames(), tablePrefix, new File(dir));
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

	private List<String> _getExcludeModelNames() {
		if (StringUtils.isBlank(excludeModelNames)) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<>();
		for (String x : excludeModelNames.split("\\s*,\\s*")) {
			list.add(x);
		}
		return list;
	}

}

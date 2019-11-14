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

import com.github.microprograms.micro_api_sdk.model.PlainModelDefinition;
import com.github.microprograms.micro_api_sdk.utils.ModelSdk;

@Mojo(name = "update-sql", defaultPhase = LifecyclePhase.COMPILE)
public class UpdateSql extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/model.json")
	private String modelConfigFilePath;
	@Parameter
	private String sqlExcludeModelNames;
	@Parameter
	private String sqlTablePrefix;
	@Parameter(defaultValue = "src/main/resources")
	private String sqlDir;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-sql");
		getLog().info("------------------------------------------------------------------------");
		try {
			PlainModelDefinition modelDefinition = ModelSdk.build(modelConfigFilePath);
			ModelSdk.Sql.writeToFile(modelDefinition, _getExcludeModelNames(), _getTablePrefix(), new File(sqlDir));
		} catch (Exception e) {
			throw new MojoFailureException("", e);
		}
	}

	private List<String> _getExcludeModelNames() {
		if (StringUtils.isBlank(sqlExcludeModelNames)) {
			return Collections.emptyList();
		}
		List<String> list = new ArrayList<>();
		for (String x : sqlExcludeModelNames.split("\\s*,\\s*")) {
			list.add(x);
		}
		return list;
	}

	private String _getTablePrefix() {
		if (StringUtils.isBlank(sqlTablePrefix)) {
			return "";
		}
		return sqlTablePrefix;
	}

}

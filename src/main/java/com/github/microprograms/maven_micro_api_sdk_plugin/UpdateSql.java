package com.github.microprograms.maven_micro_api_sdk_plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.microprograms.maven_micro_api_sdk_plugin.utils.Fn;
import com.github.microprograms.micro_api_sdk.model.PlainModelDefinition;
import com.github.microprograms.micro_api_sdk.utils.ModelSdk;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(name = "update-sql", defaultPhase = LifecyclePhase.COMPILE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class UpdateSql extends AbstractMojo {

	@Parameter(defaultValue = "src/main/resources/model.json")
	private String modelConfigFilePath;
	@Parameter
	private String sqlExcludeModelNames;
	@Parameter
	private String sqlTablePrefix;
	@Parameter(defaultValue = "${project.artifactId}.model")
	private String modelJavaPackageName;
	@Parameter(defaultValue = "src/main/resources")
	private String sqlDir;
	@Parameter(defaultValue = "${project}")
	private MavenProject project;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("------------------------------------------------------------------------");
		getLog().info("micro-api-sdk: update-sql");
		getLog().info("------------------------------------------------------------------------");
		try {
			if (!new File(modelConfigFilePath).exists()) {
				getLog().info(String.format("%s not exists, nothing to update.", modelConfigFilePath));
				return;
			}

			ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(_getClassLoader());

			PlainModelDefinition modelDefinition = ModelSdk.build(modelConfigFilePath);
			ModelSdk.Sql.writeToFile(modelDefinition, _getExcludeModelNames(), _getTablePrefix(),
					Fn.parseJavaPackageName(modelJavaPackageName), new File(sqlDir));

			Thread.currentThread().setContextClassLoader(oldClassLoader);
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

	private ClassLoader _getClassLoader() throws MojoExecutionException {
		try {
			List<String> classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			URL urls[] = new URL[classpathElements.size()];
			for (int i = 0; i < classpathElements.size(); ++i) {
				urls[i] = new File((String) classpathElements.get(i)).toURI().toURL();
			}
			return new URLClassLoader(urls, getClass().getClassLoader());
		} catch (Exception e) {
			throw new MojoExecutionException("Couldn't create a classloader.", e);
		}
	}

}

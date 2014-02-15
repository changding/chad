package org.myresarch.tomcat.maven;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.path.PathTranslator;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.util.StringUtils;

/**
 * @author chad
 * @description 支持自动下载远程tomcat7的mojo插件
 *
 */
public abstract class AbstractTomcatMojo extends AbstractMojo {
	// maven env
	@Component
	protected MavenProject project;
	@Component
	protected MavenSession session;
	@Component
	protected MojoExecution mojoExecution;
	@Component
	protected ArtifactMetadataSource source;

	/**
	 * Contains the full list of projects in the reactor.
	 */
	@Parameter(defaultValue = "${reactorProjects}", readonly = true)
	protected List<MavenProject> reactorProjects;

	// util
	@Component
	protected ArchiverManager archiverManager;

	/**
	 * Used to look up Artifacts in the remote repository.
	 */
	@Component
	protected ArtifactFactory factory;

	/**
	 * Used to look up Artifacts in the remote repository.
	 */
	@Component
	protected ArtifactResolver resolver;

	// deprecated util
	@Component
	protected PathTranslator pathTranslator;
	@Component
	protected LoggerRetriever loggerRetriever;

	// user util
	protected PluginParameterExpressionEvaluator evaluator;

	//
	// configuration
	//

	/**
	 * Skip execution
	 */
	@Parameter(property = "maven.tomcat.skip", defaultValue = "false")
	protected boolean skip;
	
	
	@Parameter(property = "maven.tomcat.ignorePackaging",defaultValue = "false")
	protected boolean ignorePackaging;
	

	/**
	 * The packaging of the Maven project that this goal operates upon.
	 */
	@Parameter(defaultValue = "${project.packaging}", required = true, readonly = true)
	private String packaging;

	/**
	 * tomcat archive download URL.
	 */
	@Parameter(property = "tomcat.url", defaultValue = "http://mirrors.cnnic.cn/apache/tomcat/tomcat-7/v7.0.50/bin/apache-tomcat-7.0.50-deployer.tar.gz")
	protected URL tomcatArchiveUrl;

	@Parameter(defaultValue = "${project.build.directory}/tomcat")
	protected File catalinaHome;

	/**
	 * 设置 <a href="http://tomcat.apache.org/tomcat-7.0-doc/introduction.html">catalina.base</a>. 
	 * <p>
	 * TODO: 当前实现 catalinaBase 即 catalinaHome. 
	 */
	@Parameter(defaultValue = "${project.build.directory}/tomcat")
	protected File catalinaBase;

	//
	// properties
	//

	protected PluginParameterExpressionEvaluator getEvaluator() {
		if (evaluator == null) {
			evaluator = new PluginParameterExpressionEvaluator(session, mojoExecution, pathTranslator, //
					loggerRetriever.getLogger(), project, session.getExecutionProperties());
		}
		return evaluator;
	}

	//
	// methods
	//

	protected void addArtifact(LinkedHashSet<String> classpath, Artifact artifact, String destName) {
		classpath.add(destName);
	}

	protected void addArtifact(Archiver archiver, LinkedHashSet<String> classpath, Artifact artifact,
			boolean artifactIdConflict) {
		String destName = artifact.getFile().getName();
		if (artifactIdConflict) {
			destName = artifact.getGroupId() + "-" + destName;
		}
		addArtifact(archiver, classpath, artifact, destName);
	}

	protected void addArtifact(Archiver archiver, LinkedHashSet<String> classpath, Artifact artifact, String destName) {
		addArtifact(classpath, artifact, destName);
		String destFileName = "lib/" + destName;
		getLog().debug("  " + artifact + " => " + destFileName);
		archiver.addFile(artifact.getFile(), destFileName);
	}

	/*
	 * below methods are reorganized from maven-dependency-plugin:2.8 
	 * {@link org.apache.maven.plugin.dependency.fromConfiguration.AbstractFromConfigurationMojo AbstractFromConfigurationMojo}.
	 */

	protected MavenProject getProjectFromReactor(Artifact artifact) {
		// check reactor projects
		for (MavenProject p : reactorProjects == null ? Collections.<MavenProject> emptyList() : reactorProjects) {
			// check the main artifact
			if (equals(artifact, p.getArtifact())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Checks to see if the specified artifact is available from the reactor.
	 *
	 * @param artifact The artifact we are looking for.
	 * @return The resolved artifact that is the same as the one we were looking for or <code>null</code> if one could
	 *         not be found.
	 */
	private Artifact getArtifactFromReactor(Artifact artifact) {
		// check project dependencies first off
		for (Artifact a : (Set<Artifact>) project.getArtifacts()) {
			if (equals(artifact, a) && hasFile(a)) {
				return a;
			}
		}

		// check reactor projects
		for (MavenProject p : reactorProjects == null ? Collections.<MavenProject> emptyList() : reactorProjects) {
			// check the main artifact
			if (equals(artifact, p.getArtifact()) && hasFile(p.getArtifact())) {
				return p.getArtifact();
			}

			// check any side artifacts
			for (Artifact a : (List<Artifact>) p.getAttachedArtifacts()) {
				if (equals(artifact, a) && hasFile(a)) {
					return a;
				}
			}
		}

		// not available
		return null;
	}

	/**
	 * Returns <code>true</code> if the artifact has a file.
	 *
	 * @param artifact the artifact (may be null)
	 * @return <code>true</code> if and only if the artifact is non-null and has a file.
	 */
	private static boolean hasFile(Artifact artifact) {
		return artifact != null && artifact.getFile() != null && artifact.getFile().isFile();
	}

	/**
	 * Null-safe compare of two artifacts based on groupId, artifactId, version, type and classifier.
	 *
	 * @param a the first artifact.
	 * @param b the second artifact.
	 * @return <code>true</code> if and only if the two artifacts have the same groupId, artifactId, version,
	 *         type and classifier.
	 */
	protected static boolean equals(Artifact a, Artifact b) {
		return a == b || !(a == null || b == null) && StringUtils.equals(a.getGroupId(), b.getGroupId())
				&& StringUtils.equals(a.getArtifactId(), b.getArtifactId())
				&& StringUtils.equals(a.getVersion(), b.getVersion()) && StringUtils.equals(a.getType(), b.getType())
				&& StringUtils.equals(a.getClassifier(), b.getClassifier());
	}

	@Override
	public final void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			getLog().info("skip execution");
			return;
		}
		if (!"war".equals(packaging) || ignorePackaging) {
			getLog().info("skip non-war project");
			return;
		}
		doExecute();
	}

	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

}

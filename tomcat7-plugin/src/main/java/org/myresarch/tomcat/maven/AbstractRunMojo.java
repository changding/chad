package org.myresarch.tomcat.maven;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.client.fluent.Request;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

public abstract class AbstractRunMojo extends AbstractTomcatMojo {

	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}")
	protected File webappDirectory;

	/**
	 * 配置 hsf 的 "project.name".
	 * 
	 * <p>
	 * maven 项目也有一个同名的 "project.name" 属性, 
	 * maven2 默认为 "Unnamed - xxx:xxx:war:0.0.1-SNAPSHOT", maven3 默认为 ${artifactId}, 
	 * 为了避免混淆, hsf 的 "project.name" 不支持使用属性配置. 
	 */
	@Parameter(defaultValue = "${project.build.finalName}")
	protected String projectName;

	public File download(URL url, File directory) throws MojoExecutionException {
		URI uri;
		try {
			uri = url.toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		String name = new File(uri.getPath()).getName();
		File file = new File(directory, name);
		if (file.exists()) {
			if (!file.delete()) {
				throw new RuntimeException("delete file failed: " + file.toString());
			}
		}

		try {
			getLog().info("download file: " + url + " => " + file);
			Request.Get(uri).execute().saveContent(file);
		} catch (IOException e) {
			throw new MojoExecutionException("download file failed: " + uri, e);
		}

		return file;
	}

	public static String getBasename(String name) {
		int pos = name.lastIndexOf('.');
		if (pos < 0) {
			name = "";
		} else {
			name = name.substring(0, pos);
			final String TAR = ".tar";
			if (name.endsWith(TAR)) {
				name = name.substring(0, name.length() - TAR.length());
			}
		}
		return name;
	}

	public void prepareCatalinaBase() throws MojoExecutionException {
		if (catalinaBase.isDirectory()) {
			return;
		}
		if (catalinaBase.exists()) {
			if (!catalinaBase.delete()) {
				throw new RuntimeException("delete catalina.base failed: " + catalinaBase);
			}
		}

		File buildDirectory = new File(project.getBuild().getDirectory());
		if (!buildDirectory.isDirectory()) {
			if (buildDirectory.mkdirs()) {
				throw new MojoExecutionException("mkdirs failed: " + buildDirectory);
			}
		}
		File tomcatArchiveFile = download(tomcatArchiveUrl, buildDirectory);
		//File pandoraArchiveFile = download(pandoraArchiveUrl, buildDirectory);
		File deploy = new File(catalinaBase, "deploy");

		if (!catalinaBase.mkdirs()) {
			throw new RuntimeException("mkdirs catalina.base failed: " + catalinaBase);
		}

		try {
			{
				UnArchiver unArchiver = archiverManager.getUnArchiver(tomcatArchiveFile);
				unArchiver.setSourceFile(tomcatArchiveFile);
				// 解压前确认删除旧文件夹
				File parent = catalinaBase.getParentFile();
				String name = getBasename(tomcatArchiveFile.getName());
				File tmp = new File(catalinaBase.getParent(), name);
				try {
					FileUtils.forceDelete(tmp);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				unArchiver.setDestDirectory(parent);
				unArchiver.extract();
				if (!tmp.renameTo(catalinaBase)) {
					throw new RuntimeException("rename catalina.base failed: " + tmp);
				}
			}
		} catch (ArchiverException e) {
			throw new RuntimeException(e);
		} catch (NoSuchArchiverException e) {
			throw new RuntimeException(e);
		}
	}

}

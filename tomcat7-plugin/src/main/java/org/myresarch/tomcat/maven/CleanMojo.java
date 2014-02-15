package org.myresarch.tomcat.maven;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.FileUtils;

/**
 * 清除 tomcat 相关文件.
 * 
 */
@Mojo(name = "clean")
public class CleanMojo extends AbstractTomcatMojo {

	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		if (catalinaBase.exists()) {
			try {
				FileUtils.forceDelete(catalinaBase);
			} catch (IOException e) {
				throw new MojoExecutionException("delete catalina.base failed: " + catalinaBase);
			}
		}
	}

}

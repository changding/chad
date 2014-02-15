package org.myresarch.tomcat.maven;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;
import org.myresearch.tomcat.launch.TomcatLauncher;


/**
 * @author chad
 * @description 执行tomcat容器启动的mojo
 *
 */
@Mojo(name = "run", requiresDependencyResolution = ResolutionScope.TEST)
@Execute(phase = LifecyclePhase.PACKAGE)
public class RunMojo extends AbstractTomcatMojo {
	
	@Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}")
	protected File webappDirectory;
	
	@Parameter(defaultValue = "${project.build.finalName}")
	protected String projectName;

	@Override
	protected void doExecute() throws MojoExecutionException, MojoFailureException {
		getLog().info("catalina.home: " + catalinaHome);
		getLog().info("catalina.base: " + catalinaBase);
		getLog().info("project.name: " + projectName);

		deployWebapp();//设置deploy的目录

		TomcatLauncher launcher = new TomcatLauncher();
		launcher.setCatalinaHome(catalinaHome);
		launcher.setCatalinaBase(catalinaBase);
		launcher.setProjectName(projectName);
		try {
			launcher.start();
		} catch (Exception e) {
			throw new MojoExecutionException("run tomcat error", e);
		}
		//加上一个钩子
		Runtime.getRuntime().addShutdownHook(new Thread(new CleanThread(catalinaBase)));
	}

	protected void deployWebapp() {
		File deploy = new File(catalinaBase, "webapp");
		String warName = projectName + ".war";
		File deployWar = new File(deploy, warName);
		try {
			FileUtils.forceDelete(deployWar);
			Archiver archiver = archiverManager.getArchiver("dir");
			archiver.addDirectory(webappDirectory);
			archiver.setDestFile(deployWar);
			archiver.createArchive();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchArchiverException e) {
			throw new RuntimeException(e);
		}
	}

}

class CleanThread implements Runnable{
	private File catalinaBase;
	CleanThread(File catalinaBase){
		this.catalinaBase = catalinaBase;
	}

	@Override
	public void run() {
		if (catalinaBase.exists()) {
			try {
				FileUtils.forceDelete(catalinaBase);
			} catch (IOException e) {
				try {
					throw new MojoExecutionException("delete catalina.base failed: " + catalinaBase);
				} catch (MojoExecutionException e1) {
					//ignore
				}
			}
		}
		
	}
	
}

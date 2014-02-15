package org.myresearch.tomcat.launch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.lang.StringUtils;

/**
 * @author chad
 * @description tomcat的launcher
 *
 */
public class TomcatLauncher {

	private File javaHome;
	private File catalinaHome;
	private File catalinaBase;
	private String projectName;

	//
	// properties
	//
	
	public File getJavaHome() {
		return javaHome;
	}

	public void setJavaHome(File javaHome) {
		this.javaHome = javaHome;
	}

	public File getCatalinaHome() {
		return catalinaHome;
	}

	public void setCatalinaHome(File catalinaHome) {
		this.catalinaHome = catalinaHome;
	}

	public File getCatalinaBase() {
		return catalinaBase;
	}

	public void setCatalinaBase(File catalinaBase) {
		this.catalinaBase = catalinaBase;
	}

	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	//
	// method
	//
	
	/**
	 * <pre>
    eval "\"$_RUNJAVA\"" "\"$LOGGING_CONFIG\"" $LOGGING_MANAGER $JAVA_OPTS $CATALINA_OPTS \
      -Djava.endorsed.dirs="\"$JAVA_ENDORSED_DIRS\"" -classpath "\"$CLASSPATH\"" \
      -Dcatalina.logs=\"$CATALINA_LOGS\" \
      -Dcatalina.base="\"$CATALINA_BASE\"" \
      -Dcatalina.home="\"$CATALINA_HOME\"" \
      -Djava.io.tmpdir="\"$CATALINA_TMPDIR\"" \
      org.apache.catalina.startup.Bootstrap "$@" start \
      >> "$CATALINA_OUT" 2>&1 "&"
	 * <pre>
	 * @throws ExecuteException
	 * @throws IOException
	 */
	public void start() throws ExecuteException, IOException {
		ArrayList<String> argv = new ArrayList<String>();
		fillArgv(argv);

		DefaultExecutor executor = new DefaultExecutor();
		Iterator<String> arg = argv.iterator();
		CommandLine command = new CommandLine(arg.next());
		while (arg.hasNext()) {
			command.addArgument(arg.next(), false);
		}
		executor.execute(command);
	}

	private void fillArgv(List<String> argv) {
		String javaCommand = getJavaCommand();
		argv.add(javaCommand);
		
		File conf = new File(catalinaBase, "conf");
		File loggingConfig = new File(conf, "logging.properties");
		argv.add("-Djava.util.logging.config.file=" + loggingConfig.toString());
		
		argv.add("-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager");
		
		/*
		if [[ "$JAVA_OPTS" != *-Dlog4j.defaultInitOverride* ]]; then
		    JAVA_OPTS="$JAVA_OPTS -Dlog4j.defaultInitOverride=true"
		fi
		JAVA_OPTS="$JAVA_OPTS -Dorg.apache.tomcat.util.http.ServerCookie.ALLOW_EQUALS_IN_VALUE=true"
		JAVA_OPTS="$JAVA_OPTS -Dorg.apache.tomcat.util.http.ServerCookie.ALLOW_HTTP_SEPARATORS_IN_V0=true"
		 */
		argv.add("-Dlog4j.defaultInitOverride");
		argv.add("-Dorg.apache.tomcat.util.http.ServerCookie.ALLOW_EQUALS_IN_VALUE=true");
		argv.add("-Dorg.apache.tomcat.util.http.ServerCookie.ALLOW_HTTP_SEPARATORS_IN_V0=true");
		
		File endorsed = new File(catalinaHome, "endorsed");
		argv.add("-Djava.endorsed.dirs=" + endorsed.toString());

		String classpath = getClassPath();
		argv.add("-classpath");
		argv.add(classpath);

		argv.add("-Dcatalina.home=" + catalinaHome.toString());
		argv.add("-Dcatalina.base=" + catalinaBase.toString());
		
		File logs = new File(catalinaBase, "logs");
		argv.add("-Dcatalina.logs=" + logs.toString());
		
		File temp = new File(catalinaBase, "temp");
		argv.add("-Djava.io.tmpdir=" + temp.toString());
		
		argv.add("-Dproject.name=" + projectName);

		argv.add("org.apache.catalina.startup.Bootstrap");
		argv.add("start");
		
		System.out.println("argv: " + argv);
	}

	private String getJavaCommand() {
		// TODO prefer "${javaHome}/bin/java"
		return "java";
	}

	private String getClassPath() {
		ArrayList<String> pathList = new ArrayList<String>();
		File bin = new File(catalinaHome, "bin");
		String jars[] = { "bootstrap.jar", "tomcat-juli.jar", };
		for (String jar : jars) {
			File path = new File(bin, jar);
			pathList.add(path.toString());
		}
		String pathString = StringUtils.join(pathList.iterator(), File.pathSeparator);
		return pathString;
	}

}

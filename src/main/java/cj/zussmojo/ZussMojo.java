package cj.zussmojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zuss.Resolver;
import org.zkoss.zuss.Zuss;
import org.zkoss.zuss.impl.out.BuiltinResolver;
import org.zkoss.zuss.metainfo.ZussDefinition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mojo(name="compile-css", defaultPhase= LifecyclePhase.COMPILE)
public class ZussMojo extends AbstractMojo {

    @Parameter(property = "includes")
    private List includes;
    @Parameter(property = "inputDirectory")
    private File inputDirectory;
    @Parameter(property = "outputDirectory")
    private File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (includes.size() == 0 ) {
            getLog().info("zuss-mojo has nothing to do!");
            return;
        }
        for (String includeFileName : (ArrayList<String>) includes) {
            getLog().info("compiling file: " + includeFileName + " ......");
            File zussFile = new File(inputDirectory + "/" + includeFileName);
            File cssFile = initializeOutputFile(includeFileName);
            if (cssFile != null) {
                ZussDefinition zussDefinition = zussParse(zussFile);
                if (zussDefinition != null) {
                    zussTranslate(zussDefinition, cssFile);
                }
            }
        }
    }

    private void zussTranslate(ZussDefinition zussDefinition, File cssFile) {
        Resolver resolver = new BuiltinResolver();
        try {
            Zuss.translate(zussDefinition, cssFile, "UTF-8", resolver);
        } catch (Exception e) {
            getLog().error("zuss translate to css file failed for: " + cssFile);
            e.printStackTrace();
        }
        getLog().info("css file: " + cssFile.toString() + " generated!");
    }

    private ZussDefinition zussParse(File zussFile) {
        ZussDefinition zussDefinition;
        try {
            zussDefinition = Zuss.parse(zussFile, "UTF-8");
        } catch (Exception e) {
            getLog().error("parse failed for file: " + zussFile.toString());
            e.printStackTrace();
            return null;
        }
        return zussDefinition;
    }

    private File initializeOutputFile(String includeFileName) {
        File cssFile =  new File(outputDirectory + "/" + StringUtils.replace(includeFileName,".zuss",".css"));
        if (!cssFile.exists()) {
            cssFile.getParentFile().mkdirs();
            try {
                cssFile.createNewFile();
            } catch (IOException e) {
                getLog().error("failed to create output file: " + cssFile.toString());
                e.printStackTrace();
                return null;
            }
        }
        return cssFile;
    }

    protected List getIncludes() {
        return includes;
    }

    protected void setIncludes(List includes) {
        this.includes = includes;
    }

    protected File getInputDirectory() {
        return inputDirectory;
    }

    protected void setInputDirectory(File inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    protected File getOutputDirectory() {
        return outputDirectory;
    }

    protected void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
